package tech.zuosi.minecraft.parkour.selection;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.GameMap;

import java.util.HashMap;
import java.util.Map;

import static tech.zuosi.minecraft.parkour.util.FormatUtil.millisecondsFrom;

/**
 * Created by LuckyKoala on 18-9-19.
 */
public class SelectionManager {
    public final Map<String, Selection> selectionMap;
    public static final String[] SELECTION_USAGE = new String[]{
            "============ 地点选取 ============",
            ChatColor.GREEN + "1. 手持石斧对方块左键进行选取",
            ChatColor.GREEN + "2. 手持石斧对任意方块右键将反选上一个地点",
            ChatColor.GREEN + "3. 手持石斧对空气左键返回上一阶段",
            ChatColor.GREEN + "4. 手持石斧对空气右键进入下一阶段",
            ChatColor.BLUE + ">> 请选取起点 <左键铁制或金制压力板方块>",
    };

    public SelectionManager() {
        this.selectionMap = new HashMap<>();
    }

    public Conversation buildConversation(Player player, Selection selection) {
        ValidatingPrompt validatingPrompt = new ValidatingPrompt() {
            @Override
            protected boolean isInputValid(ConversationContext conversationContext, String s) {
                String data[] = s.split("\\s");
                return data.length==3 &&
                        0L!=(millisecondsFrom(data[0])+millisecondsFrom(data[1])+millisecondsFrom(data[2]));
            }

            @Override
            protected Prompt acceptValidatedInput(ConversationContext context, String s) {
                String data[] = s.split("\\s");
                context.setSessionData("data",
                        new long[]{millisecondsFrom(data[0]),millisecondsFrom(data[1]),millisecondsFrom(data[2])});
                return END_OF_CONVERSATION;
            }

            @Override
            public String getPromptText(ConversationContext context) {
                return ">> 请输入分别对应1星 2星 3星所需时间，格式为 分钟:秒:毫秒，多个时间由空格分割";
            }
        };
        Conversation conversation = Core.getInstance().conversationFactory
                .withFirstPrompt(validatingPrompt)
                .buildConversation(player);
        conversation.addConversationAbandonedListener(event -> {
            if(event.gracefulExit()) {
                long[] data = (long[]) event.getContext().getSessionData("data");
                selection.getBuilder().timeRequiredForOneStar(data[0])
                        .timeRequiredForTwoStars(data[1])
                        .timeRequiredForThreeStars(data[2]);
                player.sendMessage("已设置最佳时间");

                GameMap gameMap = selection.getBuilder().path(selection.getMapPath()).build();
                boolean success = Core.getInstance().gameManager
                        .addGameMap(selection.getMapPath(), gameMap,
                                Selection.Purpose.EDIT == selection.getPurpose());
                if(success) {
                    player.sendMessage(ChatColor.GREEN + "成功创建/更新地图");
                    Core.getInstance().selectionManager.selectionMap.remove(player.getName());
                } else {
                    player.sendMessage(ChatColor.RED + "指定地图路径已存在，请使用命令重新指定地图路径，选区将会保留");
                    player.sendMessage(ChatColor.RED + "或是通过返回来退出选取程序");
                }
            }
        });
        return conversation;
    }
}
