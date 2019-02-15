package tech.zuosi.minecraft.parkour.handler.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.GameManager;
import tech.zuosi.minecraft.parkour.game.GameMap;
import tech.zuosi.minecraft.parkour.game.MapPath;
import tech.zuosi.minecraft.parkour.selection.Selection;
import tech.zuosi.minecraft.parkour.selection.SelectionManager;

import java.util.List;
import java.util.stream.Collectors;

import static tech.zuosi.minecraft.parkour.game.MapPath.PATH_FORMAT;

/**
 * Created by LuckyKoala on 18-9-18.
 */
public class EditCommand implements CommandDispatcher {
    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if("edit".equals(label)) {
            if(args.length == 0) {
                displayHelpInfo(sender);
            } else {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    if(!player.isOp()) {
                        player.sendMessage(ChatColor.RED + "只有管理员才能执行这个命令");
                        return;
                    }
                    GameManager gameManager = Core.getInstance().gameManager;
                    if(args.length == 1) {
                        MapPath mapPath = MapPath.fromString(args[0]);
                        if(MapPath.EMPTY == mapPath) {
                            player.sendMessage(ChatColor.RED + "地图路径格式有误，格式为 "+PATH_FORMAT);
                        } else {
                            GameMap gameMap = gameManager.fromMapPath(mapPath);
                            if (gameMap == null) {
                                player.sendMessage(ChatColor.RED + "指定地图不存在");
                            } else {
                                Core.getInstance().selectionManager.selectionMap
                                        .putIfAbsent(player.getName(),
                                                new Selection(player.getName(), mapPath, Selection.Purpose.EDIT));
                                player.sendMessage(ChatColor.GREEN + "进入选取程序,接下来你需要重新设置地图的各个位置");
                                player.sendMessage(SelectionManager.SELECTION_USAGE);
                                player.teleport(gameMap.getStartPoint());
                            }
                        }
                    } else {
                        displayHelpInfo(sender);
                    }
                } else {
                    commandNeedExecuteByPlayer(sender);
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if("po".equals(cmd.getLabel())) {
            if(args.length == 1) {
                String partialPath = args[0];
                return Core.getInstance().gameManager.getPathToMap().keySet()
                        .stream()
                        .map(MapPath::toString)
                        .filter(str -> str.startsWith(partialPath))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public void displayHelpInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage: " + TOP_COMMAND_LABEL + " edit <"+PATH_FORMAT +">");
    }
}
