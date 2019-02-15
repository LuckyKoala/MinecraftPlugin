package tech.zuosi.minecraft.koalavip.cli;

import com.greatmancode.craftconomy3.Cause;
import com.greatmancode.craftconomy3.account.Account;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.koalavip.Core;
import tech.zuosi.minecraft.koalavip.view.BuffCard;
import tech.zuosi.minecraft.koalavip.view.User;

/**
 * Created by luckykoala on 18-3-24.
 */
public class BuffCommand implements SubCommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            User user = Core.getInstance().getDatabaseManager().get(sender.getName());
            if (args.length == 3) {
                String subCmd = args[0];
                if ("give".equalsIgnoreCase(subCmd)) {
                    handleGive(sender, args);
                }
            } else if (args.length == 2) {
                String subCmd = args[0];
                if ("buy".equalsIgnoreCase(subCmd)) {
                    String cardName = args[1];
                    handleBuy(sender, cardName, user);
                }
            } else if (args.length == 1) {
                if ("show".equalsIgnoreCase(args[0])) {
                    handleShow(sender, user);
                } else if ("get".equalsIgnoreCase(args[0])) {
                    handleGet(sender, user);
                }
            } else {
                showHelpInfo(sender);
            }
        } else {
            sender.sendMessage(ChatColor.RED+"本命令不支持在控制台使用");
        }

        return true;
    }

    private void handleGive(CommandSender sender, String[] args) {
        if(sender.isOp()) {
            String playerName = args[1];
            String cardName = args[2];
            BuffCard buffCard = Core.getInstance().getTemplateManager().newBuffCard(cardName);
            if(buffCard == null) sender.sendMessage(ChatColor.RED+"不存在与指定名称相关的BuffCard，请检查输入的名称");
            else {
                Core.getInstance().getDatabaseManager()
                        .get(playerName).addBuffCard(buffCard);
                Core.getInstance().getDatabaseManager().getEngine()
                        .updateBuffCard(sender.getName(), buffCard);
                sender.sendMessage(ChatColor.GREEN+String.format("成功给予玩家%s一张【%s】！", playerName, cardName));
            }
        } else {
            sender.sendMessage(ChatColor.RED + "该命令只允许OP执行");
        }
    }

    private void showHelpInfo(CommandSender sender) {
        sender.sendMessage(new String[]{
                ChatColor.BLUE+"==="+ChatColor.WHITE+"Buff卡购买菜单"+ChatColor.BLUE+"===",
                ChatColor.GOLD+"奖励类型： 绑定点卷",
                ChatColor.WHITE+"/kvip buff get"+ChatColor.GOLD+"领取奖励",
                ChatColor.WHITE+"/kvip buff show"+ChatColor.GOLD+"查看当前拥有的Buff卡列表",
                ChatColor.WHITE+"/kvip buff buy <name> "+ChatColor.GOLD+"购买指定BuffCard，如月卡，周卡等",
                ChatColor.WHITE+"/kvip buff give <playerName> <buffcardName> "
                        +ChatColor.GOLD+"给予玩家指定BuffCard，如月卡，周卡等"
        });
        Core.getInstance().getTemplateManager().getBuffCardTemplateMap()
                .values()
                .forEach(t -> sender.sendMessage(String.format("%s 购买价格%d 有效时间%d 即时奖励%d 每日奖励%d",
                        t.getName(), t.getPrice(), t.getDays(), t.getImmediately(), t.getDaily())));
    }

    private void handleGet(CommandSender sender, User user) {
        int rewardSum = user.getRewardFromBuff(Core.getInstance().getTick().getCurrentMs());

        if (rewardSum > 0) {
            Account account = Core.getInstance()
                    .getCraftconomy().getAccountManager().getAccount(user.getUsername(), false);
            String ccConfigPath = "Constant.craftconomy";
            account.deposit(rewardSum,
                    Core.getInstance().getConfig().getString(ccConfigPath + ".world"),
                    Core.getInstance().getConfig().getString(ccConfigPath + "currency"),
                    Cause.PLUGIN, "Reward from BuffCard of plugin <KoalaVIP>");

            sender.sendMessage(ChatColor.BLUE + "恭喜您，得到BuffCard的奖励,共计 "
                            + ChatColor.GREEN + rewardSum
                            + ChatColor.BLUE + " 绑定点卷");
        } else {
            sender.sendMessage(ChatColor.RED + "很遗憾，目前没有可领取的奖励");
        }
    }

    private void handleShow(CommandSender sender, User user) {
        sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.WHITE + "Buff卡列表菜单" + ChatColor.BLUE + "===");
        user.getBuffCards().forEach(t ->
                sender.sendMessage(ChatColor.DARK_GRAY +
                        String.format("%s 剩余有效时间%d 即时奖励%d 每日奖励%d",
                                t.getTemplate().getName(), t.getRemainDays(),
                                t.getTemplate().getImmediately(), t.getTemplate().getDaily())));
    }

    private void handleBuy(CommandSender sender, String cardName, User user) {
        BuffCard buffCard = Core.getInstance().getTemplateManager().newBuffCard(cardName);
        if(buffCard == null) sender.sendMessage(ChatColor.RED+"不存在与指定名称相关的BuffCard，请检查输入的名称");
        else {
            PlayerPoints playerPoints = Core.getInstance().getPlayerPoints();
            int price = buffCard.getTemplate().getPrice();
            boolean paid = playerPoints.getAPI().take(
                    playerPoints.translateNameToUUID(sender.getName()), price);
            if(paid) {
                user.addBuffCard(buffCard);
                Core.getInstance().getDatabaseManager().getEngine()
                        .updateBuffCard(sender.getName(), buffCard);
                sender.sendMessage(ChatColor.GREEN+String.format("成功购买【%s】！", cardName));
            } else {
                sender.sendMessage(ChatColor.RED+String.format("通用点卷余额不足%d", price));
            }
        }
    }
}
