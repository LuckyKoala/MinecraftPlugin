package tech.zuosi.minecraft.parkour.handler.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.parkour.Core;

/**
 * Created by LuckyKoala on 18-9-18.
 */
public class MenuCommand implements CommandDispatcher {
    @Override
    public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if("menu".equals(label)) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                Core.getInstance().paneManager.showMainPaneTo(player);
            } else {
                commandNeedExecuteByPlayer(sender);
            }

        }
    }
}
