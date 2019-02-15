package tech.zuosi.minecraft.parkour.handler.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.GameManager;
import tech.zuosi.minecraft.parkour.game.GamePlayer;
import tech.zuosi.minecraft.parkour.game.MapPath;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by LuckyKoala on 18-9-19.
 */
public class OptionCommand implements CommandDispatcher {

    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if("option".equals(label)) {
            if(args.length == 0) {
                displayHelpInfo(sender);
            } else {
                if(sender instanceof Player) {
                    Player player = (Player) sender;
                    GameManager gameManager = Core.getInstance().gameManager;
                    GamePlayer gamePlayer = gameManager.gamePlayerFor(player);

                    final MapPath mapPath = gamePlayer.getMapPath();
                    if(mapPath == null) {
                        player.sendMessage(ChatColor.RED + "您已经在地图外，无法继续");
                        return;
                    }

                    if(args.length == 1) {
                        int optionValue = Integer.valueOf(args[0]);
                        switch (optionValue) {
                            case 0:
                                //Rejoin
                                boolean successRejoin = gameManager.addPlayerToGame(player, mapPath, true);
                                if(!successRejoin) {
                                    player.sendMessage(ChatColor.RED + "找不到指定地图");
                                }
                                break;
                            case 1:
                                //Next level
                                MapPath next = mapPath.nextLevel();
                                if(gameManager.isPathExist(next)){
                                    boolean success = gameManager.addPlayerToGame(player, next);
                                    if(!success) {
                                        player.sendMessage(ChatColor.RED + "未解锁该地图，获得上一级别的二星以上评价即可解锁");
                                    }
                                } else {
                                    Core.getInstance().getLogger().warning(mapPath +"没有下一等级地图，请检查！");
                                    player.sendMessage(ChatColor.RED + "不存在下一等级");
                                }

                                break;
                            case 2:
                                //Back to lobby
                                gamePlayer.leave();
                                break;
                            case 3:
                                //Hide or show other players
                                boolean hiding = gamePlayer.hideOrShowPlayers();
                                if(hiding) {
                                    player.sendMessage(ChatColor.GREEN + "已隐藏同地图其他玩家");
                                } else {
                                    player.sendMessage(ChatColor.GREEN + "已显示同地图其他玩家");
                                }
                                break;
                            case 4:
                                //Open menu
                                player.performCommand("po menu");
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
            if(args.length == 2) {
                String partialPath = args[1];
                return Core.getInstance().gameManager.getPathToMap().keySet()
                        .stream()
                        .map(MapPath::toString)
                        .filter(str -> str.startsWith(partialPath))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }
}