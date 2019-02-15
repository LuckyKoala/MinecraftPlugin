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

        sender.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "�޷�ʶ�������������������ʽ");
        return true;
    }

    //Only for debug
    private void handleTestCmd(String[] args, Player player) {
        if(args.length < 1) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "�޷�ʶ�������������������ʽ");
            return;
        }
        if(!player.isOp()) {
            player.sendMessage(ChatColor.RED + "�������OP����");
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
        sender.sendMessage(ChatColor.GRAY + "�������");
        sender.sendMessage(ChatColor.GREEN + "/ka show <id> <page> " + ChatColor.AQUA + "��ʾָ����ҵ�������Ϣ");
        sender.sendMessage(ChatColor.GREEN + "/ka give [id] [amount] " + ChatColor.AQUA + "����ָ�����ָ��������δ�������Ե�");
        sender.sendMessage(ChatColor.GREEN + "/ka add [name] [amount] " + ChatColor.AQUA + "����ָ��������δ�������Ե��ָ������");
        if (sender.isOp() || Permissions.canRemove(sender)) {
            sender.sendMessage(ChatColor.GOLD + "ӵ��<koalaattribute.remove>Ȩ�޵Ĺ���");
            sender.sendMessage(ChatColor.GOLD + "--������amount�����븺�����Ƴ���Ӧ���Ե�");
            sender.sendMessage(ChatColor.GOLD + "--��Ϳ۳�����");
        }
    }

    private void handleAddCmd(String[] args, Player player) {
        if (not(Permissions.canAdd(player))) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "ȱ��Ȩ��<koalaattribute.add>");
            return;
        }
        if (not(isNumeric(args[2]))) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "ָ������������Ϊ�ջ��ʽ����");
            return;
        }
        String attributeName = args[1];
        int amount = Integer.parseInt(args[2]);
        AttributeUtil.adjustAttributePoint(player, attributeName, amount);
    }




    @SuppressWarnings("deprecation")
    private void handleGiveCmd(String[] args, Player player) {
        if (not(Permissions.canGive(player))) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "ȱ��Ȩ��<koalaattribute.give>");
            return;
        }
        Player chosenPlayer = Bukkit.getPlayerExact(args[1]);
        if (chosenPlayer == null) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "δ�ҵ����");
            return;
        }
        if (!isNumeric(args[2])) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "ָ�����������Ϸ�");
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
                        player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "ȱ��Ȩ��<koalaattribute.show.other>");
                    }
                } else {
                    player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "δ�ҵ����");
                }
            } else {
                //args.length == 2
                pageStr = args[1];
                if(not(isNumeric(pageStr))) {
                    player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "ָ������������Ϊ�ջ��ʽ����");
                    return;
                }
                page = Integer.parseInt(pageStr);
                PanelBuilder.show(player, page);
            }
        } else {
            if (Permissions.canShowSelf(player)) {
                PanelBuilder.show(player, page);
            } else {
                player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "ȱ��Ȩ��<koalaattribute.show.self>");
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
