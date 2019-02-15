package tech.zuosi.minecraft.parkour.handler.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by LuckyKoala on 18-9-18.
 */
interface CommandDispatcher {
    String TOP_COMMAND_LABEL = "/po";

    void onCommand(CommandSender sender, Command cmd, String label, String[] args);
    default List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return null;
    }
    default void displayHelpInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Please use " + TOP_COMMAND_LABEL + " to see help info");
    }
    default void commandNeedExecuteByPlayer(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "This command can't be run within Console");
    }
}
