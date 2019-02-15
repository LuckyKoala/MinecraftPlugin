package tech.zuosi.koalaattribute.tool;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.koalaattribute.KoalaAttribute;
import tech.zuosi.koalaattribute.attribute.Attribute;
import tech.zuosi.koalaattribute.attribute.AttributeUtil;
import tech.zuosi.koalaattribute.attribute.CustomAttribute;
import tech.zuosi.koalaattribute.data.AttributeCache;

import static tech.zuosi.koalaattribute.tool.Util.nil;
import static tech.zuosi.koalaattribute.tool.Util.not;

/**
 * Created by iwar on 2016/8/10.
 */
public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getLabel().equalsIgnoreCase("ka")) {
            if (args.length == 0) {
                handleHelpCmd(sender);
                return true;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String subCmd = args[0];
                if ("show".equalsIgnoreCase(subCmd)) {
                    handleShowCmd(args, player);
                    return true;
                } else if ("test".equalsIgnoreCase(subCmd)) {
                    handleTestCmd(args, player);
                    return true;
                }

                if (args.length >= 3) {
                    if ("give".equalsIgnoreCase(subCmd)) {
                        handleGiveCmd(args, player);
                        return true;
                    } else if ("add".equalsIgnoreCase(subCmd)) {
                        handleAddCmd(args, player);
                        return true;
                    }
                }
            }
        }

        sender.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "无法识别该类型命令，请检查命令格式");
        return true;
    }

    //Only for debug
    private void handleTestCmd(String[] args, Player player) {
        if(args.length < 1) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "无法识别该类型命令，请检查命令格式");
            return;
        }
        if(!player.isOp()) {
            player.sendMessage(ChatColor.RED + "该命令仅OP可用");
            return;
        }

        String param = args[1];
        if ("reset".equalsIgnoreCase(param)) {
            player.setMaxHealth(20D);
            HealthAndSpeed.putExtraHealth(player.getName(), 0D); //Reset cached data
            //player.setHealthScaled(false);
        } else if ("update".equalsIgnoreCase(param)) {
            HealthAndSpeed.update(player);
        }
    }

    private void handleHelpCmd(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "--------------------------------");
        if(sender.isOp()) {
            sender.sendMessage(ChatColor.GOLD + "[KoalaAttribute-"+ KoalaAttribute.INSTANCE.getDescription().getVersion() +"]");
        }
        sender.sendMessage(ChatColor.GRAY + "命令帮助");
        sender.sendMessage(ChatColor.GREEN + "/ka show <id> <page> " + ChatColor.AQUA + "显示指定玩家的属性信息");
        sender.sendMessage(ChatColor.GREEN + "/ka give [id] [amount] " + ChatColor.AQUA + "给予指定玩家指定数量的未分配属性点");
        sender.sendMessage(ChatColor.GREEN + "/ka add [name] [amount] " + ChatColor.AQUA + "分配指定数量的未分配属性点给指定属性");
        if (sender.isOp() || Permissions.canRemove(sender)) {
            sender.sendMessage(ChatColor.GOLD + "拥有<koalaattribute.remove>权限的管理");
            sender.sendMessage(ChatColor.GOLD + "--可以在amount处输入负数来移除对应属性点");
            sender.sendMessage(ChatColor.GOLD + "--最低扣除到零");
        }
    }

    private void handleAddCmd(String[] args, Player player) {
        if (not(Permissions.canAdd(player))) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "缺少权限<koalaattribute.add>");
            return;
        }
        if (not(isNumeric(args[2]))) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "指定的数量数据为空或格式错误");
            return;
        }
        String attributeName = args[1];
        int amount = Integer.parseInt(args[2]);
        AttributeUtil.adjustAttributePoint(player, attributeName, amount);
    }




    @SuppressWarnings("deprecation")
    private void handleGiveCmd(String[] args, Player player) {
        if (not(Permissions.canGive(player))) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "缺少权限<koalaattribute.give>");
            return;
        }
        Player chosenPlayer = Bukkit.getPlayerExact(args[1]);
        if (chosenPlayer == null) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "未找到玩家");
            return;
        }
        if (!isNumeric(args[2])) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "指定的数量不合法");
            return;
        }
        int amount = Integer.parseInt(args[2]);
        String playerName = chosenPlayer.getName();
        AttributeUtil.adjustAvailableAttributePoint(player, amount, playerName);
    }

    @SuppressWarnings("deprecation")
    private void handleShowCmd(String[] args, Player player) {
        String playerIdStr, pageStr;
        //showCmdStr <=> args[0];
        int page = 0;
        if (args.length >= 2) {
            if (args.length >= 3) {
                playerIdStr = args[1];
                pageStr = args[2];
                page = Integer.parseInt(pageStr);
                Player chosenPlayer = Bukkit.getPlayerExact(playerIdStr);
                if (not(nil(chosenPlayer))) {
                    if (Permissions.canShowOther(player)) {
                        PanelBuilder.show(chosenPlayer, page);
                    } else {
                        player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "缺少权限<koalaattribute.show.other>");
                    }
                } else {
                    player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "未找到玩家");
                }
            } else {
                //args.length == 2
                pageStr = args[1];
                if(not(isNumeric(pageStr))) {
                    player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "指定的数量数据为空或格式错误");
                    return;
                }
                page = Integer.parseInt(pageStr);
                PanelBuilder.show(player, page);
            }
        } else {
            if (Permissions.canShowSelf(player)) {
                PanelBuilder.show(player, page);
            } else {
                player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "缺少权限<koalaattribute.show.self>");
            }
        }
    }

    private boolean isNumeric(String str){
        if(str.isEmpty() || str.length() < 1) return false;
        //signal
        char signal = str.charAt(0);
        if(signal!='-' && !Character.isDigit(signal)) return false;
        //rest
        for (int i = 1; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
