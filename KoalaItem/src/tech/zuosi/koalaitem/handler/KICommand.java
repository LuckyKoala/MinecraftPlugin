package tech.zuosi.koalaitem.handler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.compatible.CompatibleUtil;
import tech.zuosi.koalaitem.gui.ItemUI;
import tech.zuosi.koalaitem.gui.MakerUI;
import tech.zuosi.koalaitem.gui.MenuUI;
import tech.zuosi.koalaitem.handler.gui.TempSave;
import tech.zuosi.koalaitem.item.ItemWatch;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/16.
 */
public class KICommand implements CommandExecutor {
    private KoalaItem plugin = KoalaItem.INSTANCE;

    public KICommand() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("ki".equals(label)) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length >= 1) {
                    if ("open".equals(args[0])) {
                        if (args.length == 1) {
                            p.sendMessage(ChatColor.RED + "item/maker/menu/reopen");
                            return true;
                        }
                        if ("maker".equalsIgnoreCase(args[1])) {
                            p.openInventory(new MakerUI().createGUI());
                            return true;
                        } else if ("item".equalsIgnoreCase(args[1])) {
                            if (!p.hasPermission("koalaitem.itemui")) {
                                p.sendMessage(ChatColor.RED + "缺少权限节点koalaitem.itemui");
                                return true;
                            }
                            p.openInventory(new ItemUI().createGUI());
                            return true;
                        } else if ("menu".equalsIgnoreCase(args[1])) {
                            p.openInventory(new MenuUI().createGUI());
                            return true;
                        }
                    } else if ("version".equals(args[0])) {
                        p.sendMessage(ChatColor.GRAY + "Version:" + KoalaItem.INSTANCE.getDescription().getVersion());
                        return true;
                    } else if ("reopen".equals(args[0])) {
                        if (!TempSave.SAVER.reopen(p)) {
                            p.sendMessage(ChatColor.RED + "最近没有记录，无法重新打开");
                        }
                        return true;
                    } else if ("menu".equals(args[0])) {
                        if (!p.hasPermission("koalaitem.menuui")) {
                            p.sendMessage(ChatColor.RED + "缺少权限节点koalaitem.menuui");
                            return true;
                        }
                        p.getWorld().dropItem(p.getLocation(),new ItemWatch().defaultItem());
                        return true;
                    } else if ("convert".equalsIgnoreCase(args[0])) {
                        if (!p.hasPermission("koalaitem.convert")) {
                            p.sendMessage(ChatColor.RED + "缺少权限节点koalaitem.convert");
                            return true;
                        }
                        ItemStack is = p.getItemInHand();
                        p.setItemInHand(new CompatibleUtil().asKICopy(is));
                        return true;
                    } else if ("update".equalsIgnoreCase(args[0])) {
                        if (!p.hasPermission("koalaitem.update")) {
                            p.sendMessage(ChatColor.RED + "缺少权限节点koalaitem.update");
                            return true;
                        }
                        ItemStack is = p.getItemInHand();
                        NBTUtil nbtUtil = new NBTUtil(is).updateDisplayData();
                        if (nbtUtil != null) p.setItemInHand(nbtUtil.getItemStack());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
