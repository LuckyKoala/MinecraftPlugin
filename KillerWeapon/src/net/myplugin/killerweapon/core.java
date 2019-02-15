package net.myplugin.killerweapon;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2017/4/29.
 * 修改武器伤害和文本信息
 * 武器会记录击杀玩家数，达到一定数量后武器会升级并且增加伤害
 * 指令定义：
 *   武器初始伤害，击杀文本信息，升级所需击杀数，
 *   武器升级增加伤害值，武器等级上限
 * Lore:
 *   击杀数
 *   等级
 *   伤害信息
 * 版本：
 *   1.8.x
 */
public class core extends JavaPlugin implements Listener {
    private static final int VALIDATE_TOKEN_INDEX = 0;
    private static final int KILL_COUNT_INDEX = 1;
    private static final int LEVEL_INDEX = 2;
    private static final int LORE_SIZE = 3;
    private static final String VALIDATE_TOKEN = "§5炼";
    private static final String KILL_COUNT_FORMAT = "§4KillCount: %";
    private static final String LEVEL_FORMAT = "§4Level: %";

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getLogger().info("KillerWeapon has been loaded.");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        this.saveConfig();
        getLogger().info("KillerWeapon has been unloaded.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String controlpanel = this.getConfig().getString("message.controlpanel");
        String activate = this.getConfig().getString("message.activate");
        String deactivate = this.getConfig().getString("message.deactivate");
        String reload = this.getConfig().getString("message.reload");

        if (cmd.getName().equalsIgnoreCase("kw")) {
            if (args.length == 0) {
                sender.sendMessage("§b-----§6KillerWapon插件帮助§b-----");
                if (this.getConfig().getBoolean("ad")){
                    sender.sendMessage("§b-----§6作者:iwar§b-----");
                }
                sender.sendMessage("§b/kw activate §7----- §3激活手中的武器");
                sender.sendMessage("§b/kw deactivate §7----- §3反激活手中的武器");
                if (sender.isOp() || sender.hasPermission("killerweapon.command")) {
                    sender.sendMessage("§c-----§6管理员指令§c-----");
                    sender.sendMessage("§c/kw reload §7--- §3重载插件配置");
                    sender.sendMessage("§c/kw debug §7--- §3检测部分代码");
                    sender.sendMessage("§c/kw set [type] [value] §7--- §3修改配置");
                    sender.sendMessage("§c/kw type §7--- §3查看可配置项以及其代号（可用于/kw set命令的代号）与支持的值类型");
                    sender.sendMessage("§c/kw show §7--- §3显示配置");
                }

                return true;
            } else if (args.length > 3) {
                sender.sendMessage("§c参数过多.");
                return true;
            } else if (args[0].equalsIgnoreCase("activate")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("klllerweapon.activate")) {
                        if (validateWeapon(p, true)) {
                            ItemStack item_hand = p.getItemInHand();
                            loreInit(item_hand);
                            p.sendMessage("§c激活成功。");
                        } else {
                            p.sendMessage("§c当前物品无法激活，请先检查是否在可激活列表中。");
                        }
                    }
                } else {
                    sender.sendMessage(controlpanel);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("deactivate")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("killerweapon.deactivate")) {
                        if (validateWeapon(p)) {
                            ItemStack item_hand = p.getItemInHand();
                            ItemMeta im = item_hand.getItemMeta();
                            im.setLore(null);
                            item_hand.setItemMeta(im);
                            p.sendMessage("§c"+deactivate);
                        }
                    }
                } else {
                    sender.sendMessage(controlpanel);
                }
                return true;
            } else if (sender.isOp() || sender.hasPermission("killerweapon.command")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    this.reloadConfig();
                    sender.sendMessage("§a[KillerWeapon]"+reload);
                    return true;
                } else if (args[0].equalsIgnoreCase("type")) {
                    if (sender.isOp() || sender.hasPermission("killerweapon.*")) {
                        sender.sendMessage("§c-----§6type信息§c-----");
                        sender.sendMessage("§c[ess]基础伤害 §7--- §3整数、小数");
                        sender.sendMessage("§c[level]升级所需击杀数 §7--- §3整数");
                        sender.sendMessage("§c[message]击杀文本信息 [value] §7--- §3文本");
                        sender.sendMessage("§c[extra]每一等级提升伤害值 §7--- §3整数、小数");
                        sender.sendMessage("§c[limit]等级上限 §7--- §3整数");
                    }
                    return true;
                } else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                    String type = args[1];
                    String value = args[2];
                    ConfigurationSection etc = this.getConfig().getConfigurationSection("etc");
                    switch (type) {
                        case "ess":
                            etc.set("ess", Double.parseDouble(value));
                            sender.sendMessage("§c设置成功.");
                            break;
                        case "level":
                            etc.set("level", Integer.parseInt(value));
                            sender.sendMessage("§c设置成功.");
                            break;
                        case "message":
                            etc.set("message", value);
                            sender.sendMessage("§c设置成功.");
                            break;
                        case "extra":
                            etc.set("extra", Double.parseDouble(value));
                            sender.sendMessage("§c设置成功.");
                            break;
                        case "limit":
                            etc.set("limit", Integer.parseInt(value));
                            sender.sendMessage("§c设置成功.");
                            break;
                        default:
                            sender.sendMessage("§c不存在的类型代号，输入/kw type查看可用类型代号");
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("show")) {
                    ConfigurationSection etc = this.getConfig().getConfigurationSection("etc");

                    sender.sendMessage("§c-----§6配置信息§c-----");
                    sender.sendMessage("§c[ess] §7--- §3基础伤害=>"+etc.getDouble("ess", 0D));
                    sender.sendMessage("§c[level] §7--- §3升级所需击杀数=>"+etc.getInt("level", 0));
                    sender.sendMessage("§c[message] [value] §7--- §3击杀文本信息=>"+etc.getString("message", "无"));
                    sender.sendMessage("§c[extra] §7--- §3每一等级提升伤害值=>"+etc.getDouble("extra", 0D));
                    sender.sendMessage("§c[limit] §7--- §3等级上限=>"+etc.getInt("limit", 0));
                    return true;
                } else if (args[0].equalsIgnoreCase("debug")){
                    sender.sendMessage("§cChecking debug mode...");
                    boolean debug = this.getConfig().getBoolean("debug");
                    if (debug){
                        sender.sendMessage("§cDebug mode has already been enable.Now start disabling debug mode.");
                        this.getConfig().set("debug", false);
                        sender.sendMessage("§cDone.");
                        this.saveConfig();
                    } else {
                        sender.sendMessage("§cDebug mode was disable.Now start abling debug mode.");
                        this.getConfig().set("debug", true);
                        sender.sendMessage("§cDone.");
                        this.saveConfig();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity tar = event.getDamager();
        EntityType victim = event.getEntityType();
        boolean debug = this.getConfig().getBoolean("debug");

        if (!(tar instanceof Player)) return;
        Player player = (Player)tar;
        if (!validateWeapon(player)) return;
        double damage = computeDamage(player.getItemInHand());
        if (debug){
            player.sendMessage(victim.name().toLowerCase());
            player.sendMessage("Damage-> "+damage);
        }
        event.setDamage(damage);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        Player killer = event.getEntity().getKiller();
        EntityType victim = event.getEntityType();
        boolean debug = this.getConfig().getBoolean("debug");
        if (killer!=null && validateWeapon(killer)){
            ItemStack item_hand = killer.getItemInHand();
            if (this.getConfig().getBoolean("list.mob."+victim.name().toLowerCase(), false)) {
                if (debug) killer.sendMessage(victim.name().toLowerCase());
                killer.sendMessage(this.getConfig().getString("etc.message"));
                loreUpdate(item_hand, false, 1, 0);
            }
        }
    }

    //---Util Methods---
    boolean loreValidate(List<String> lore) {
        return lore.size() != LORE_SIZE || lore.get(VALIDATE_TOKEN_INDEX).equalsIgnoreCase(VALIDATE_TOKEN);
    }

    void loreWrite(ItemStack itemStack, int killcount, int level) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(VALIDATE_TOKEN_INDEX,VALIDATE_TOKEN);
        lore.add(KILL_COUNT_INDEX,KILL_COUNT_FORMAT.replace("%", Integer.toString(killcount)));
        lore.add(LEVEL_INDEX,LEVEL_FORMAT.replace("%", Integer.toString(level)));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    void loreInit(ItemStack itemStack) {
        loreWrite(itemStack, 0, 0);
    }

    boolean loreUpdate(ItemStack itemStack, boolean override, int killcount, int level) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();
        ConfigurationSection etc = this.getConfig().getConfigurationSection("etc");
        int levelfactor = etc.getInt("level");
        int limit = etc.getInt("limit");

        //Validate
        if (!loreValidate(lore)) return false;
        //Append
        if (!override) {
            killcount += getNum(lore.get(KILL_COUNT_INDEX));
            level += getNum(lore.get(LEVEL_INDEX));
        }
        //Try levelUp
        if(killcount >= levelfactor) {
            level = level + killcount/levelfactor;
            killcount = killcount % levelfactor;
            level = level > limit ? limit : level;
        }
        //Write
        loreWrite(itemStack, killcount, level);
        return true;
    }
    double computeDamage(ItemStack itemStack) {
        ConfigurationSection etc = this.getConfig().getConfigurationSection("etc");
        double ess, extra;

        ess = etc.getDouble("ess");
        extra = etc.getDouble("extra");

        return ess+extra*getNum(itemStack.getItemMeta().getLore().get(LEVEL_INDEX));
    }

    boolean validateWeapon(Player er,boolean init) {
        if (er.hasPermission("killerweapon.use")
                && null != er.getItemInHand() && Material.AIR != er.getItemInHand().getType()) {
            ItemStack item = er.getItemInHand();
            if (init || (item.hasItemMeta()
                    && item.getItemMeta().hasLore() && loreValidate(item.getItemMeta().getLore()))) {
                String item_type = item.getType().name();
                if (this.getConfig().getBoolean("list.default." + item_type) || this.getConfig().getBoolean("list.mod.enable")
                        && this.getConfig().getBoolean("list.mod." + item_type)) return true;
            }
        }
        return false;
    }

    boolean validateWeapon(Player er) {
        return validateWeapon(er, false);
    }

    int getNum(String str){
        String temp = "";
        if(!str.isEmpty() && !"".equals(str)){
            for(int i=2;i<str.length();i++){
                if(str.charAt(i)>=48 && str.charAt(i)<=57){
                    temp+=str.charAt(i);
                }
            }
        }
        return Integer.parseInt(temp);
    }
}
