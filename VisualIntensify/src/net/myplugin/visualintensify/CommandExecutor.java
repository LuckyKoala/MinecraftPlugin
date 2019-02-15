package net.myplugin.visualintensify;

import net.myplugin.visualintensify.menu.MenuManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2015/12/6.
 */
public class CommandExecutor implements org.bukkit.command.CommandExecutor {
    //����ִ����
    private final VisualIntensify plugin;

    public CommandExecutor(VisualIntensify plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // ����ǰһ��ִ�оͺá�
        //-����ʱ��ʾ�����������ɱ-�������Ƿ���ʾ�������-������ʧ�嵥��
        if (label.equalsIgnoreCase("vi")) {
            if (args.length==0) {
                sender.sendMessage("��b-----��6ǿ������b-----");
                sender.sendMessage("��b-----��6����:iwar��b-----");
                sender.sendMessage("��c/vi panel ��7--- ��3���ǿ�����");
                sender.sendMessage("��c/vi open ��7--- ��3ֱ�Ӵ�ǿ�����");
                if (sender.hasPermission("visualintensify.admin") || sender.isOp()) {
                    sender.sendMessage("��c-----��6����Աָ���c-----");
                    sender.sendMessage("��c/vi reload ��7--- ��3���ز������");
                    sender.sendMessage("��c/vi version ��7--- ��3��ȡ��ǰ����汾");
                    sender.sendMessage("��c/vi debug ��7--- ��3����DEBUGģʽ");
                }
                return true;
            }
            if (args.length>=1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED+"��������Ҳ���");
                    return true;
                }
                Player p = (Player) sender;
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!p.hasPermission("visualintensify.admin")) {
                        p.sendMessage(ChatColor.RED+"ȱ��\"visualintensify.admin\"Ȩ��Ŷ~");
                    } else {
                        plugin.reloadConfig();
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("panel")) {
                    if (!p.hasPermission("visualintensify.getitem.panel")) {
                        p.sendMessage(ChatColor.RED+"ȱ��\"visualintensify.getitem.panel\"Ȩ��Ŷ~");
                    } else {
                        ItemStack panel = new ItemStack(Material.WATCH);
                        ItemMeta panel_meta = panel.getItemMeta();
                        List<String> lore = new ArrayList<>();
                        lore.add(0,"vi:ǿ�����");
                        panel_meta.setLore(lore);
                        panel_meta.setDisplayName("ǿ�����");
                        panel.setItemMeta(panel_meta);
                        p.getWorld().dropItem(p.getLocation(),panel);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("open")) {
                    p.openInventory(new MenuManager(plugin).createGUI(MenuManager.MenuType.MAIN));
                    return true;
                } else if (args[0].equalsIgnoreCase("version")) {
                    if (!p.hasPermission("visualintensify.admin")) {
                        p.sendMessage(ChatColor.RED+"ȱ��\"visualintensify.admin\"Ȩ��Ŷ~");
                    } else {
                        p.sendMessage(plugin.getDescription().getVersion());
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("debug")) {
                    if (!p.hasPermission("visualintensify.admin")) {
                        p.sendMessage(ChatColor.RED+"ȱ��\"visualintensify.admin\"Ȩ��Ŷ~");
                    } else {
                        if (plugin.getConfig().getBoolean("debug")) {
                            plugin.getConfig().set("debug",false);
                            p.sendMessage(ChatColor.GREEN+"�Ѿ��ر�debugģʽ");
                        } else {
                            plugin.getConfig().set("debug",true);
                            p.sendMessage(ChatColor.GREEN+"�Ѿ�����debugģʽ");
                        }
                    }
                    return true;
                }
            }
        }
    return false;
    }
}
