package tech.zuosi.minecraft.koalavip.cli;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import tech.zuosi.minecraft.koalavip.Core;

import java.util.concurrent.TimeUnit;

/**
 * Created by luckykoala on 18-3-24.
 */
public class AdminCommand implements SubCommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if(!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "本命令仅限OP执行");
            return true;
        }

        if(args.length == 1) {
            if("debug".equalsIgnoreCase(args[0])) {
                if(Core.getInstance().isDebugOn()) {
                    Core.getInstance().setDebugOn(false);
                    sender.sendMessage(ChatColor.RED + "已关闭debug模式");
                } else {
                    Core.getInstance().setDebugOn(true);
                    sender.sendMessage(ChatColor.RED + "已开启debug模式");
                }
            }
        } else if(args.length == 2) {
            String subCmd = args[0];
            if("inc".equalsIgnoreCase(subCmd)) {
                try {
                    long minutes = Long.parseLong(args[1]);
                    Core.getInstance().getTick()
                            .intercept(TimeUnit.MINUTES.toMillis(minutes));
                    sender.sendMessage(ChatColor.BLUE + "已将时间偏移向后推进"+
                            ChatColor.GOLD+Long.toString(minutes)+ChatColor.BLUE+"分钟");
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "时间参数格式错误");
                }
            } else if("set".equalsIgnoreCase(subCmd)) {
                try {
                    long minutes = Long.parseLong(args[1]);
                    Core.getInstance().getTick()
                            .intercept(TimeUnit.MINUTES.toMillis(minutes),true);
                    sender.sendMessage(ChatColor.BLUE + "已将时间偏移设置为"+
                            ChatColor.GOLD+Long.toString(minutes)+ChatColor.BLUE+"分钟");
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "时间参数格式错误");
                }
            }
        } else {
            sender.sendMessage(ChatColor.GRAY + "... admin inc/set/debug [10], inc 向后推进n分钟, debug 开关debug模式");
        }

        return true;
    }
}
