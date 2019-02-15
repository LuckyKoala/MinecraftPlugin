package tech.zuosi.minecraft.koalavip.cli;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tech.zuosi.minecraft.koalavip.Core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by luckykoala on 18-3-27.
 */
public class BasicCommand implements CommandExecutor {
    private static final Map<String, SubCommandExecutor> subCommandMap;

    static {
        subCommandMap = new HashMap<>();
        subCommandMap.put("admin", new AdminCommand());
        subCommandMap.put("buff", new BuffCommand());
        subCommandMap.put("status", new StatusCommand());
        subCommandMap.put("setup", new SetupCommand());
        subCommandMap.put("reward", new RewardCommand());
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(new String[]{
                ChatColor.BLUE+"==="+ChatColor.WHITE+"KoalaVIP帮助菜单"+ChatColor.BLUE+"===",
                ChatColor.GOLD+"/kvip:"+ChatColor.WHITE+" 查看帮助信息",
                ChatColor.GOLD+"/kvip reward:"+ChatColor.WHITE+" 领取奖励",
                ChatColor.GOLD+"/kvip buff:"+ChatColor.WHITE+" BuffCard相关",
                ChatColor.GOLD+"/kvip status"+ChatColor.WHITE+" 查看当前状态"
        });
        if(sender.isOp()) {
            sender.sendMessage(new String[]{
                    ChatColor.GOLD+"/kvip admin:"+ChatColor.WHITE+" 管理命令",
                    ChatColor.GOLD+"/kvip setup:"+ChatColor.WHITE+" 配置命令",
                    ChatColor.DARK_GRAY+"当前插件版本号: "+ChatColor.GREEN+ Core.getInstance().getDescription().getVersion(),
            });
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean handled = false;
        if (cmd.getName().equalsIgnoreCase("kvip")) {
            if(args.length >= 1) {
                String subCmd = args[0];
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                handled = Optional.ofNullable(subCommandMap.get(subCmd))
                        .map(e -> e.onCommand(sender, subArgs))
                        .orElse(false);
            }
            if(!handled) sendHelpMessage(sender);
            return true; //永远返回true，覆盖默认的错误提示
        }
        return false;
    }
}
