package tech.zuosi.minecraft.parkour.handler.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.GameMap;
import tech.zuosi.minecraft.parkour.game.MapPath;

import static tech.zuosi.minecraft.parkour.handler.command.JoinCommand.JOIN_COMMAND_LABEL;

/**
 * Created by LuckyKoala on 18-9-18.
 */
public class ListCommand implements CommandDispatcher {
    public static final String LIST_COMMAND_LABEL = "list";

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(LIST_COMMAND_LABEL.equals(label)) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                switch (args.length) {
                    case 0:
                        MapEditorHelper.showDifficulty(player.getName());
                        break;
                    case 1:
                        MapEditorHelper.showScene(player.getName(), args[0]);
                        break;
                    case 2:
                        MapEditorHelper.showMap(player.getName(), args[0], args[1]);
                        break;
                    case 3:
                        MapPath mapPath = MapPath.builder().difficulty(args[0])
                                .scene(args[1]).level(args[2]).build();
                        GameMap gameMap = Core.getInstance().gameManager.fromMapPath(mapPath);
                        if(gameMap != null) {
                            player.sendMessage(
                                    ChatColor.GREEN + "该地图存在，可使用"+TOP_COMMAND_LABEL+" "+JOIN_COMMAND_LABEL
                                            +" 来进入该地图");
                        } else {
                            player.sendMessage(ChatColor.RED + "找不到指定的地图");
                        }
                }
            } else {
                commandNeedExecuteByPlayer(sender);
            }

        }
    }
}
