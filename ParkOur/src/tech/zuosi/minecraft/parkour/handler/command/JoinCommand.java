package tech.zuosi.minecraft.parkour.handler.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.GameManager;
import tech.zuosi.minecraft.parkour.game.MapPath;

import java.util.List;
import java.util.stream.Collectors;

import static tech.zuosi.minecraft.parkour.game.MapPath.PATH_FORMAT;

/**
 * Created by LuckyKoala on 18-9-18.
 */
public class JoinCommand implements CommandDispatcher {
    public static final String JOIN_COMMAND_LABEL = "join";

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if("join".equals(label)) {
            if(args.length == 0) {
                displayHelpInfo(sender);
            } else {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    GameManager gameManager = Core.getInstance().gameManager;
                    if (gameManager.gamePlayerFor(player).getMapPath() == null) {
                        if(args.length == 1) {
                            //Exact match
                            boolean success = gameManager.addPlayerToGame(player, MapPath.fromString(args[0]));
                            if (!success) {
                                player.sendMessage(ChatColor.RED + "找不到指定地图或未解锁该地图，获得上一级别的二星以上评价即可解锁");
                            }
                        } else {
                            displayHelpInfo(sender);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "您已经在游戏区域内，请先离开游戏区域。");
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
        sender.sendMessage(ChatColor.RED + "Usage: " + TOP_COMMAND_LABEL + " join <"+PATH_FORMAT +">");
    }
}
