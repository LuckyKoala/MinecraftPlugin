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
    //命令执行器
    private final VisualIntensify plugin;

    public CommandExecutor(VisualIntensify plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // 和以前一样执行就好…
        //-上线时提示假身情况（被杀-可配置是否显示玩家名称-掉落损失清单）
        if (label.equalsIgnoreCase("vi")) {
            if (args.length==0) {
                sender.sendMessage("§b-----§6强化面板§b-----");
                sender.sendMessage("§b-----§6作者:iwar§b-----");
                sender.sendMessage("§c/vi panel §7--- §3获得强化面板");
                sender.sendMessage("§c/vi open §7--- §3直接打开强化面板");
                if (sender.hasPermission("visualintensify.admin") || sender.isOp()) {
                    sender.sendMessage("§c-----§6管理员指令§c-----");
                    sender.sendMessage("§c/vi reload §7--- §3重载插件配置");
                    sender.sendMessage("§c/vi version §7--- §3获取当前插件版本");
                    sender.sendMessage("§c/vi debug §7--- §3开启DEBUG模式");
                }
                return true;
            }
            if (args.length>=1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED+"非在线玩家操作");
                    return true;
                }
                Player p = (Player) sender;
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!p.hasPermission("visualintensify.admin")) {
                        p.sendMessage(ChatColor.RED+"缺少\"visualintensify.admin\"权限哦~");
                    } else {
                        plugin.reloadConfig();
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("panel")) {
                    if (!p.hasPermission("visualintensify.getitem.panel")) {
                        p.sendMessage(ChatColor.RED+"缺少\"visualintensify.getitem.panel\"权限哦~");
                    } else {
                        ItemStack panel = new ItemStack(Material.WATCH);
                        ItemMeta panel_meta = panel.getItemMeta();
                        List<String> lore = new ArrayList<>();
                        lore.add(0,"vi:强化面板");
                        panel_meta.setLore(lore);
                        panel_meta.setDisplayName("强化面板");
                        panel.setItemMeta(panel_meta);
                        p.getWorld().dropItem(p.getLocation(),panel);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("open")) {
                    p.openInventory(new MenuManager(plugin).createGUI(MenuManager.MenuType.MAIN));
                    return true;
                } else if (args[0].equalsIgnoreCase("version")) {
                    if (!p.hasPermission("visualintensify.admin")) {
                        p.sendMessage(ChatColor.RED+"缺少\"visualintensify.admin\"权限哦~");
                    } else {
                        p.sendMessage(plugin.getDescription().getVersion());
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("debug")) {
                    if (!p.hasPermission("visualintensify.admin")) {
                        p.sendMessage(ChatColor.RED+"缺少\"visualintensify.admin\"权限哦~");
                    } else {
                        if (plugin.getConfig().getBoolean("debug")) {
                            plugin.getConfig().set("debug",false);
                            p.sendMessage(ChatColor.GREEN+"已经关闭debug模式");
                        } else {
                            plugin.getConfig().set("debug",true);
                            p.sendMessage(ChatColor.GREEN+"已经开启debug模式");
                        }
                    }
                    return true;
                }
            }
        }
    return false;
    }
}
