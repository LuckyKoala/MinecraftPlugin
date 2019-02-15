package tech.zuosi.minecraft.parkour.selection;

import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.util.Vector;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.GameMap;
import tech.zuosi.minecraft.parkour.game.GameRegion;
import tech.zuosi.minecraft.parkour.game.MapPath;

import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by LuckyKoala on 18-9-19.
 */
@Data
public class Selection {
    private String creator;
    private MapPath mapPath;
    private GameMap.GameMapBuilder builder;
    private Stage stage;
    private Purpose purpose;
    private Stack<Vector> vectorStack;

    public enum Purpose {
        CREATE, EDIT
    }

    public enum Stage {
        START_POINT(0, ChatColor.GREEN + ">> 请选取起点 <左键石制或金制压力板方块>"),
        GAME_REGION(1, ChatColor.BLUE + ">> 请以最低点-最高点这个顺序选择地点以确认活动平台,单点选取直接双击目标地点即可"),
        END_POINT(2, ChatColor.GREEN + ">> 请选取终点 <左键石制或金制压力板方块>"),
        TIME_REQUIRED(3, ChatColor.BLUE + ">> 请设置各等级所需时间"),
        FINISH(4, ChatColor.GOLD + ">> 已选取完所有地点，右键空气以确认完成本次选取"); //Should never reach here

        private final int index;
        private final String description;

        Stage(int index, String description) {
            this.index = index;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        static Stage fromIndex(int index) {
            Stage[] stages = values();
            if(index >= stages.length) return FINISH;
            else if(index < 0) return START_POINT;

            for(Stage stage : stages) {
                if(stage.index == index) return stage;
            }
            return START_POINT;
        }
    }

    public Selection(String creator, MapPath mapPath, Purpose purpose) {
        this.creator = creator;
        this.mapPath = mapPath;
        this.purpose = purpose;
        this.builder = GameMap.builder();
        this.stage = Stage.START_POINT;
        this.vectorStack = new Stack<>();
    }

    public boolean pushVector(Vector vector) {
        if(isMaxVectorWillPresent()) {
            Vector min = vectorStack.pop().clone();
            Vector max = vector.clone();
            Core.getInstance().getLogger().info(String.format("Before transform, now min is %s and max is %s",
                    min, max));

            boolean transformHappened = false;
            double minX, minY, minZ, maxX, maxY, maxZ;
            minX = min.getX();
            minY = min.getY();
            minZ = min.getZ();
            maxX = max.getX();
            maxY = max.getY();
            maxZ = max.getZ();

            if(minX > maxX) {
                min.setX(maxX);
                max.setX(minX);
                transformHappened = true;
            }
            if(minY > maxY) {
                min.setY(maxY);
                max.setY(minY);
                transformHappened = true;
            }
            if(minZ > maxZ) {
                min.setZ(maxZ);
                max.setZ(minZ);
                transformHappened = true;
            }

            //We need one unit of height for player to stand in region!
            min.add(new Vector(-1,1,-1));
            max.add(new Vector(1,1,1));
            vectorStack.push(min);
            vectorStack.push(max);
            if(transformHappened) {
                Core.getInstance().getLogger().info(String.format("Transform happened, now min is %s and max is %s",
                        min, max));
            }
            return transformHappened;
        } else {
            vectorStack.push(vector);
            return false;
        }
    }

    public boolean isMaxVectorWillPresent() {
        return vectorStack.size() % 2 == 1;
    }

    public String vectorStage() {
        return isMaxVectorWillPresent() ? "最高点" : "最低点";
    }

    public boolean loadVectorStack() {
        if(vectorStack.empty()) return false;
        int size = vectorStack.size();
        if(size % 2 == 0) {

            builder.regions(IntStream.range(0, size/2)
                    .mapToObj(i -> GameRegion.builder()
                            .max(vectorStack.pop())
                            .min(vectorStack.pop())
                            .build())
                    .collect(Collectors.toSet()));
            return true;
        } else {
            return false;
        }
    }

    public Stage previousStage() {
        this.stage = Stage.fromIndex(stage.index-1);
        return this.stage;
    }

    public Stage nextStage() {
        this.stage = Stage.fromIndex(stage.index+1);
        return this.stage;
    }

    public boolean isFullSelection() {
        return stage == Stage.FINISH;
    }

    public boolean isEmptySelection() {
        return stage == Stage.START_POINT;
    }
}
