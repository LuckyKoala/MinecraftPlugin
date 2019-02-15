package tech.zuosi.bettercloth;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tech.zuosi.bettercloth.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor{
	private BetterCloth sr;
	public Command(BetterCloth args){
		this.sr=args;
	}
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String Label,String[] args){
		if(cmd.getLabel().equalsIgnoreCase("bch")){
			if(sender.hasPermission("bch.command.main")){
				if(args.length == 0) {
                    sender.sendMessage("��6[��c����װ�ס�6]��a�鿴���������ʽ����������/bch help");
                    sender.sendMessage("��6[��c����װ�ס�6]��a/bch give [playerName] [ArmorName]");
					sender.sendMessage("��6[��c����װ�ס�6]��a/bch create [ArmorName] [DisplayName]");
                    sender.sendMessage("��6[��c����װ�ס�6]��a/bch settext [ArmorName] [Line] [text]");
                    sender.sendMessage("��6[��c����װ�ס�6]��a/bch setdata [ArmorName] [Type] [data]");
                    sender.sendMessage("��6[��c����װ�ס�6]��a/bch show [ArmorName]");
					return true;
				}
                if (args.length == 1) {
                    if ("help".equalsIgnoreCase(args[0])) {
                        sender.sendMessage("��6[��c����װ�ס�6]��a�汾:��c"+sr.getDescription().getVersion());
                        sender.sendMessage(ChatColor.GRAY + "[playerName-���ID] [ArmorName-����ID]"
                         + " [DisplayName-��Ʒչʾ��] [ArmorName-����ID]"
                         + " [Line-����] [text-�ı�] [Type-����] [data-����ֵ]");
                        sender.sendMessage(ChatColor.GOLD + "����[Type-����]�е���������˵��"
                                + " ||֧�ֵ���������������:ItemID/MAXPoint/RestoreSpeed/DisplayName/Sheild||"
                                + " �ֱ�֧�ֵ���������Ϊint/int/int/String/double");
                        return true;
                    }
                }
				if(args.length >= 2) {
					if("give".equalsIgnoreCase(args[0])) {
                        if (args.length != 3) return false;
                        Player p = sr.getServer().getPlayer(args[1]);
						String ArmorName = args[2];
						if (p == null) {
                            sender.sendMessage("�Ҳ�����Ӧ�����,������������ID��ȷ����Ч.");
                            return true;
                        }
                        if (!sr.getConfig().contains("item."+ArmorName+".ItemID")) {
							sender.sendMessage("�����ļ���û�������Ʒ,�����ȳ��Դ���.");
							return true;
						}
						Util.giveItem(p, ArmorName);
                        sender.sendMessage(ChatColor.GREEN + "�Ѹ������[" + p.getPlayerListName() + "] һ��[" + ArmorName + "]");
						return true;
					} else if ("create".equalsIgnoreCase(args[0])) {
                        if (args.length != 3) return false;
                        if (!(sender instanceof Player)) {
                            sender.sendMessage("�޷���ȡ��ң��벻Ҫ�ڿ���ִ̨�б�����!");
                            return true;
                        }
                        String ArmorName = args[1];
                        String DisplayName = args[2];
                        Player p = (Player) sender;
                        @SuppressWarnings("deprecation")
                        int ItemID = p.getItemInHand().getTypeId();
                        if (ItemID == 0) {
                            p.sendMessage(ChatColor.RED + "������ƷΪ�գ��޷�����");
                            return true;
                        }

                        ConfigurationSection cs = sr.getConfig().getConfigurationSection("item").createSection(ArmorName);

                        List<String> lore = new ArrayList<>();
                        lore.add("This is Lore");
                        lore.add("Change it in config.yml!");

                        cs.set("ItemID",ItemID);
                        cs.set("DisplayName",DisplayName);
                        cs.set("Lore",lore);
                        cs.set("MAXPoint",5);
                        cs.set("RestoreSpeed",30);
                        cs.set("Sheild",2.0);

                        sr.saveConfig();
                        p.sendMessage(ChatColor.GREEN + "�ѳɹ�����[" + ArmorName + "]");

                        return true;
                    } else if ("settext".equalsIgnoreCase(args[0])) {
                        if (args.length != 4) return false;
                        String ArmorName = args[1];
                        if (!sr.getConfig().contains("item."+ArmorName+".ItemID")) {
                            sender.sendMessage("��6[��c����װ�ס�6]��a�����ļ���û�������Ʒ,�����ȳ��Դ���.");
                            return true;
                        }
                        ConfigurationSection cs = sr.getConfig().getConfigurationSection("item."+ArmorName);
                        try {
                            int line = Integer.parseInt(args[2]);
                            String text = args[3];
                            List<String> lore = new ArrayList<>();
                            if (line > cs.getStringList("Lore").size()) {
                                lore.add(text);
                                lore.addAll(0,cs.getStringList("Lore"));
                                sender.sendMessage("��6[��c����װ�ס�6]��a������������,���ڵ�ǰ�б����һ�к���Ӹ��ı�.");
                            } else {
                                lore = cs.getStringList("Lore");
                                lore.set(line,text);
                            }
                            cs.set("Lore",lore);
                            sender.sendMessage("��6[��c����װ�ס�6]��a�޸ĳɹ�,��ע��˲�������֮�����ɵ���Ʒ��Ч.");
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                        }
                        sr.saveConfig();

                        return true;
                    } else if ("setdata".equalsIgnoreCase(args[0])) {
                        if (args.length != 4) return false;
                        String ArmorName = args[1];
                        if (!sr.getConfig().contains("item."+ArmorName+".ItemID")) {
                            sender.sendMessage("��6[��c����װ�ס�6]��a�����ļ���û�������Ʒ,�����ȳ��Դ���.");
                            return true;
                        }
                        String type = args[2];
                        String data = args[3];
                        ConfigurationSection cs = sr.getConfig().getConfigurationSection("item."+ArmorName);
                        if ("ItemID".equals(type) || "MAXPoint".equals(type) || "RestoreSpeed".equals(type)) {
                            try {
                                int num = Integer.parseInt(data);
                                cs.set(type,num);
                                sender.sendMessage("��6[��c����װ�ס�6]��a�޸ĳɹ�,��ע��˲�������֮�����ɵ���Ʒ��Ч.");
                            } catch (NumberFormatException e) {
                                sender.sendMessage("��6[��c����װ�ס�6]��aIllegal Argument.");
                                e.printStackTrace();
                            }
                        } else if ("DisplayName".equals(type)) {
                            cs.set(type,data);
                        } else if ("Sheild".equals(type)) {
                            try {
                                double num = Double.parseDouble(data);
                                cs.set(type,num);
                                sender.sendMessage("��6[��c����װ�ס�6]��a�޸ĳɹ�,��ע��˲�������֮�����ɵ���Ʒ��Ч.");
                            } catch (NumberFormatException e) {
                                sender.sendMessage("��6[��c����װ�ס�6]��aIllegal Argument.");
                                e.printStackTrace();
                            }
                        } else {
                            sender.sendMessage("��6[��c����װ�ס�6]��a֧�ֵ���������������:ItemID/MAXPoint/RestoreSpeed/DisplayName/Sheild.");
                            return true;
                        }
                        sr.saveConfig();

                        return true;
                    } else if ("show".equalsIgnoreCase(args[0])) {
                        if (args.length != 2) return false;
                        String ArmorName = args[1];
                        if (!sr.getConfig().contains("item."+ArmorName+".ItemID")) {
                            sender.sendMessage("��6[��c����װ�ס�6]��a�����ļ���û�������Ʒ,�����ȳ��Դ���.");
                            return true;
                        }
                        ConfigurationSection cs = sr.getConfig().getConfigurationSection("item."+ArmorName);
                        List<String> lore = cs.getStringList("Lore");

                        sender.sendMessage(ChatColor.YELLOW + "ArmorName:" + ChatColor.GRAY + ArmorName);
                        sender.sendMessage(ChatColor.GREEN + "ItemID:" + ChatColor.GRAY + cs.getInt("ItemID"));
                        sender.sendMessage(ChatColor.GREEN + "DisplayName:" + ChatColor.GRAY + cs.getString("DisplayName"));
                        sender.sendMessage(ChatColor.GREEN + "Lore:");
                        for (int i=0;i < lore.size();i++) {
                            sender.sendMessage(ChatColor.GREEN + "[" + i + "]" + ChatColor.GRAY + lore.get(i));
                        }
                        sender.sendMessage(ChatColor.GREEN + "MAXPoint:" + ChatColor.GRAY + cs.getInt("MAXPoint"));
                        sender.sendMessage(ChatColor.GREEN + "RestoreSpeed:" + ChatColor.GRAY + cs.getInt("RestoreSpeed"));
                        sender.sendMessage(ChatColor.GREEN + "Sheild:" + ChatColor.GRAY + cs.getDouble("Sheild"));

                        return true;
                    }
				}
			} else {
				sender.sendMessage("��6[��c����װ�ס�6]��a��û��bch.command.mainȨ�ޣ�Ĭ��OPӵ��!");
				return true;
			}
		}
		return false;
	}


}
