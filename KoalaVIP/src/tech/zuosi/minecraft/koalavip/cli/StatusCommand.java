package tech.zuosi.minecraft.koalavip.cli;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.koalavip.Core;
import tech.zuosi.minecraft.koalavip.view.User;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by luckykoala on 18-3-24.
 */
public class StatusCommand implements SubCommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            User user = Core.getInstance().getDatabaseManager().get(sender.getName());
            int downgradeTimes = user.getDowngradeTimes();
            sender.sendMessage(new String[]{
                    ChatColor.BLUE+"==="+ChatColor.GOLD+"[KVIP]当前状态"+ChatColor.BLUE+"===",
                    String.format(ChatColor.BLUE+"VIP组: "+ChatColor.GREEN+"%s\n"+
                            ChatColor.BLUE+"历史总消费: "+ChatColor.GREEN+"%d", user.getGroup().getGroupName(), user.getMoneySpentTotal()),
                    ChatColor.BLUE+"当前状态: " + (downgradeTimes > 0 ?
                            ChatColor.RED+String.format(
                                    "最近降级过（恢复期消费 %d/%d）", user.getMoneySpentLocked(),
                                    Core.getInstance().getConfig().getInt("Money.restore", 100)) :
                            ChatColor.GREEN+"正常"),
                    ChatColor.BLUE+"最近一次消费发生于: "+
                            ChatColor.GREEN+ DateFormat.getDateInstance().format(new Date(user.getLastTimePurchasing()))
            });
            return true;
        } else {
            sender.sendMessage(ChatColor.RED+"本命令不支持在控制台使用");
            return true;
        }
    }
}
