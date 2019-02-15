package tech.zuosi.minecraft.koalavip.cli;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.koalavip.Core;
import tech.zuosi.minecraft.koalavip.view.User;

public class RewardCommand implements SubCommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            User user = Core.getInstance().getDatabaseManager().get(sender.getName());
            if (args.length == 0) {
                long now = Core.getInstance().getTick().getCurrentMs();
                int daysRemain = user.getGroup().getRewardFromAllCommand(user.getUsername(), now);
                if(daysRemain > 0)
                    sender.sendMessage(ChatColor.DARK_GRAY + String.format("距离下次可领取奖励 %d 天", daysRemain));
            } else {
                showHelpInfo(sender);
            }
        } else {
            sender.sendMessage(ChatColor.RED+"本命令不支持在控制台使用");
        }

        return true;
    }

    private void showHelpInfo(CommandSender sender) {
        sender.sendMessage(new String[]{
                ChatColor.BLUE+"==="+ChatColor.WHITE+"奖励菜单"+ChatColor.BLUE+"===",
                ChatColor.WHITE+"/kvip reward"+ChatColor.GOLD+"领取奖励"
        });
    }
}
