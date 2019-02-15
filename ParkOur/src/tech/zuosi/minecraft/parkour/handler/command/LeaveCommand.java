package tech.zuosi.minecraft.parkour.handler.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.GameManager;

/**
 * Created by LuckyKoala on 18-9-18.
 */
public class LeaveCommand implements CommandDispatcher {
    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if("leave".equals(label)) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                GameManager gameManager = Core.getInstance().gameManager;
                if (gameManager.gamePlayerFor(player).getMapPath() == null) {
                    player.sendMessage(ChatColor.RED + "您已经在游戏区域外。");
                } else {
                    gameManager.removePlayerFromGame(player);
                }
            } else {
                commandNeedExecuteByPlayer(sender);
            }
        }
    }
}
