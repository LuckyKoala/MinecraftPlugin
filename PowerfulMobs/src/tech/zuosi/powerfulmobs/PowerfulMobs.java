package tech.zuosi.powerfulmobs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import tech.zuosi.powerfulmobs.listener.ListenerManager;
import tech.zuosi.powerfulmobs.util.DataManager;

/**
 * Created by iwar on 2016/4/26.
 */
public class PowerfulMobs extends JavaPlugin {
    public static boolean DEBUG;
    private DataManager dataManager = new DataManager(this);
    @Override
    public void onEnable() {
        dataManager.read();
        new ListenerManager(this).registerAllListener();
        getLogger().info("PowerfulMobs load.");
        this.saveDefaultConfig();
        DEBUG = this.getConfig().getBoolean("debug");
    }

    @Override
    public void onDisable() {
        this.getConfig().set("canwritesomething","hahsdaw");
        this.getConfig().set("debug",DEBUG);
        this.saveConfig();
        getLogger().info("PowerfulMobs unload.");
        HandlerList.unregisterAll(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            System.out.println(ChatColor.RED + "You must be a player to do that");
            return true;
        } else {
            if (command.getName().equalsIgnoreCase("pmob")) {
                Player player = (Player)sender;
                if (!player.hasPermission("powerfulmobs.*")) {
                    player.sendMessage(ChatColor.RED + "You don't have the permission.");
                    return true;
                }
                if (args.length == 4) {
                    if (args[0].equalsIgnoreCase("setdrop")) {
                        String _type = args[1].toLowerCase();
                        int _level = Integer.valueOf(args[2]);
                        double _probability = Float.valueOf(args[3]);
                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                        if (itemStack == null || itemStack.getType() == Material.AIR) {
                            player.sendMessage(ChatColor.RED+"You must have something in hand.");
                        }
                        this.getConfig().set(_type+"."+_level+".chance",_probability);
                        this.getConfig().set(_type+"."+_level+".drop",itemStack);
                        player.sendMessage(ChatColor.GOLD+"ItemStack Data:"+itemStack);
                        this.saveConfig();
                        return true;
                    }
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("debug")) {
                        if (args[1].equalsIgnoreCase("on")) {
                            DEBUG = true;
                            player.sendMessage(ChatColor.GREEN + "Debug on.");
                        } else if (args[1].equalsIgnoreCase("off")) {
                            DEBUG = false;
                            player.sendMessage(ChatColor.YELLOW + "Debug off.");
                        }
                    }
                    return true;
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("debug")) {
                        if (DEBUG) {
                            player.sendMessage(ChatColor.GOLD + "[PowerfulMobs]" + ChatColor.GRAY + "Debug:on");
                        } else {
                            player.sendMessage(ChatColor.GOLD + "[PowerfulMobs]" + ChatColor.GRAY + "Debug:off");
                        }
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("setdrop")) {
                        player.sendMessage(ChatColor.GOLD + "==============    PowerfulMobs    ==============");
                        player.sendMessage(ChatColor.GOLD + "Level等级(输入数字):");
                        player.sendMessage(ChatColor.GRAY + "普通" + ChatColor.YELLOW + "0" + ChatColor.WHITE + "  ||  "
                        + ChatColor.GREEN + "一般" + ChatColor.YELLOW + "1" + ChatColor.WHITE + "  ||  "
                        + ChatColor.BLUE + "困难" + ChatColor.YELLOW + "2" + ChatColor.WHITE + "  ||  " + ChatColor.DARK_BLUE + "地狱" + ChatColor.YELLOW + "3");
                        player.sendMessage(ChatColor.RED + "传说" + ChatColor.YELLOW + "4" + ChatColor.WHITE + "  ||  "
                        + ChatColor.YELLOW + "史诗" + ChatColor.YELLOW + "5" + ChatColor.WHITE + "  ||  " + ChatColor.DARK_PURPLE + "噩梦" + ChatColor.YELLOW + "6");
                        player.sendMessage(ChatColor.GRAY + "Type类型(输入英文):");
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "spider蜘蛛    | zombie僵尸    | creeper苦力怕");
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "skeleton骷髅  | pigzombie猪人 | cave_spider洞穴蜘蛛");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("update")) {
                        if (dataManager.update()) {
                            player.sendMessage(ChatColor.GREEN + "成功更新数据文件.");
                        } else {
                            player.sendMessage(ChatColor.RED + "更新失败,请检查数据文件是否存在.");
                        }
                        return true;
                    }
                }
                if (args.length == 0) {
                    player.sendMessage(ChatColor.GOLD + "==============    PowerfulMobs    ==============");
                    player.sendMessage(ChatColor.YELLOW + "Author: LuckyKoala");
                    player.sendMessage(ChatColor.GREEN + "/pmob setdrop <type> <level> <probability>");
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "将指定等级，类型的怪物掉落物设置为手中物品");
                    player.sendMessage(ChatColor.GREEN + "/pmob debug [on/off]");
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "DEBUG模式开启或关闭");
                    player.sendMessage(ChatColor.GREEN + "/pmob update");
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "更新数据文件");
                    return true;
                }
            }
        }
        return false;
    }
}
