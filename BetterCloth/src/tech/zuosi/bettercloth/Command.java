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
                    sender.sendMessage("§6[§c动力装甲§6]§a查看具体命令格式解析请输入/bch help");
                    sender.sendMessage("§6[§c动力装甲§6]§a/bch give [playerName] [ArmorName]");
					sender.sendMessage("§6[§c动力装甲§6]§a/bch create [ArmorName] [DisplayName]");
                    sender.sendMessage("§6[§c动力装甲§6]§a/bch settext [ArmorName] [Line] [text]");
                    sender.sendMessage("§6[§c动力装甲§6]§a/bch setdata [ArmorName] [Type] [data]");
                    sender.sendMessage("§6[§c动力装甲§6]§a/bch show [ArmorName]");
					return true;
				}
                if (args.length == 1) {
                    if ("help".equalsIgnoreCase(args[0])) {
                        sender.sendMessage("§6[§c动力装甲§6]§a版本:§c"+sr.getDescription().getVersion());
                        sender.sendMessage(ChatColor.GRAY + "[playerName-玩家ID] [ArmorName-盔甲ID]"
                         + " [DisplayName-物品展示名] [ArmorName-盔甲ID]"
                         + " [Line-行数] [text-文本] [Type-类型] [data-数据值]");
                        sender.sendMessage(ChatColor.GOLD + "关于[Type-类型]中的数据类型说明"
                                + " ||支持的数据类型名称有:ItemID/MAXPoint/RestoreSpeed/DisplayName/Sheild||"
                                + " 分别支持的数据类型为int/int/int/String/double");
                        return true;
                    }
                }
				if(args.length >= 2) {
					if("give".equalsIgnoreCase(args[0])) {
                        if (args.length != 3) return false;
                        Player p = sr.getServer().getPlayer(args[1]);
						String ArmorName = args[2];
						if (p == null) {
                            sender.sendMessage("找不到对应的玩家,请检查输入的玩家ID正确而有效.");
                            return true;
                        }
                        if (!sr.getConfig().contains("item."+ArmorName+".ItemID")) {
							sender.sendMessage("配置文件中没有这件物品,可以先尝试创建.");
							return true;
						}
						Util.giveItem(p, ArmorName);
                        sender.sendMessage(ChatColor.GREEN + "已给于玩家[" + p.getPlayerListName() + "] 一件[" + ArmorName + "]");
						return true;
					} else if ("create".equalsIgnoreCase(args[0])) {
                        if (args.length != 3) return false;
                        if (!(sender instanceof Player)) {
                            sender.sendMessage("无法获取玩家，请不要在控制台执行本命令!");
                            return true;
                        }
                        String ArmorName = args[1];
                        String DisplayName = args[2];
                        Player p = (Player) sender;
                        @SuppressWarnings("deprecation")
                        int ItemID = p.getItemInHand().getTypeId();
                        if (ItemID == 0) {
                            p.sendMessage(ChatColor.RED + "手中物品为空，无法创建");
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
                        p.sendMessage(ChatColor.GREEN + "已成功创建[" + ArmorName + "]");

                        return true;
                    } else if ("settext".equalsIgnoreCase(args[0])) {
                        if (args.length != 4) return false;
                        String ArmorName = args[1];
                        if (!sr.getConfig().contains("item."+ArmorName+".ItemID")) {
                            sender.sendMessage("§6[§c动力装甲§6]§a配置文件中没有这件物品,可以先尝试创建.");
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
                                sender.sendMessage("§6[§c动力装甲§6]§a由于行数过大,将在当前列表最后一行后添加该文本.");
                            } else {
                                lore = cs.getStringList("Lore");
                                lore.set(line,text);
                            }
                            cs.set("Lore",lore);
                            sender.sendMessage("§6[§c动力装甲§6]§a修改成功,请注意此操作仅对之后生成的物品有效.");
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                        }
                        sr.saveConfig();

                        return true;
                    } else if ("setdata".equalsIgnoreCase(args[0])) {
                        if (args.length != 4) return false;
                        String ArmorName = args[1];
                        if (!sr.getConfig().contains("item."+ArmorName+".ItemID")) {
                            sender.sendMessage("§6[§c动力装甲§6]§a配置文件中没有这件物品,可以先尝试创建.");
                            return true;
                        }
                        String type = args[2];
                        String data = args[3];
                        ConfigurationSection cs = sr.getConfig().getConfigurationSection("item."+ArmorName);
                        if ("ItemID".equals(type) || "MAXPoint".equals(type) || "RestoreSpeed".equals(type)) {
                            try {
                                int num = Integer.parseInt(data);
                                cs.set(type,num);
                                sender.sendMessage("§6[§c动力装甲§6]§a修改成功,请注意此操作仅对之后生成的物品有效.");
                            } catch (NumberFormatException e) {
                                sender.sendMessage("§6[§c动力装甲§6]§aIllegal Argument.");
                                e.printStackTrace();
                            }
                        } else if ("DisplayName".equals(type)) {
                            cs.set(type,data);
                        } else if ("Sheild".equals(type)) {
                            try {
                                double num = Double.parseDouble(data);
                                cs.set(type,num);
                                sender.sendMessage("§6[§c动力装甲§6]§a修改成功,请注意此操作仅对之后生成的物品有效.");
                            } catch (NumberFormatException e) {
                                sender.sendMessage("§6[§c动力装甲§6]§aIllegal Argument.");
                                e.printStackTrace();
                            }
                        } else {
                            sender.sendMessage("§6[§c动力装甲§6]§a支持的数据类型名称有:ItemID/MAXPoint/RestoreSpeed/DisplayName/Sheild.");
                            return true;
                        }
                        sr.saveConfig();

                        return true;
                    } else if ("show".equalsIgnoreCase(args[0])) {
                        if (args.length != 2) return false;
                        String ArmorName = args[1];
                        if (!sr.getConfig().contains("item."+ArmorName+".ItemID")) {
                            sender.sendMessage("§6[§c动力装甲§6]§a配置文件中没有这件物品,可以先尝试创建.");
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
				sender.sendMessage("§6[§c动力装甲§6]§a您没有bch.command.main权限，默认OP拥有!");
				return true;
			}
		}
		return false;
	}


}
