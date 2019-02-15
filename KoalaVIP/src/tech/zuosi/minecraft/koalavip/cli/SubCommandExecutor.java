package tech.zuosi.minecraft.koalavip.cli;

import org.bukkit.command.CommandSender;

/**
 * Created by luckykoala on 18-3-27.
 */
public interface SubCommandExecutor {
    boolean onCommand(CommandSender sender, String[] args);
}
