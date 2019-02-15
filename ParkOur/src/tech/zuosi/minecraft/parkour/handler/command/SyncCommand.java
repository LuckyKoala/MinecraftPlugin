package tech.zuosi.minecraft.parkour.handler.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.MapPath;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by LuckyKoala on 18-9-22.
 */
public class SyncCommand implements CommandDispatcher {
    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if("sync".equals(label)) {
            if(args.length == 1) {
                if(!sender.isOp()) {
                    sender.sendMessage(ChatColor.RED + "只有管理员才能执行这个命令");
                    return;
                }
                MapPath mapPath = MapPath.fromString(args[0]);
                if(mapPath != MapPath.EMPTY && Core.getInstance().gameManager.getPathToMap().containsKey(mapPath)) {
                    Core.getInstance().gameManager.syncFormatToConfig(mapPath);
                    sender.sendMessage(ChatColor.GREEN + "同步中!");
                } else {
                    sender.sendMessage(ChatColor.RED + "指定路径不合法或不存在对应的地图，请检查后重试!");
                }
            } else {
                displayHelpInfo(sender);
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
        sender.sendMessage(ChatColor.RED + "Usage: " + TOP_COMMAND_LABEL + " sync");
    }
}
