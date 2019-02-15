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
 * �޸������˺����ı���Ϣ
 * �������¼��ɱ��������ﵽһ���������������������������˺�
 * ָ��壺
 *   ������ʼ�˺�����ɱ�ı���Ϣ�����������ɱ����
 *   �������������˺�ֵ�������ȼ�����
 * Lore:
 *   ��ɱ��
 *   �ȼ�
 *   �˺���Ϣ
 * �汾��
 *   1.8.x
 */
public class core extends JavaPlugin implements Listener {
    private static final int VALIDATE_TOKEN_INDEX = 0;
    private static final int KILL_COUNT_INDEX = 1;
    private static final int LEVEL_INDEX = 2;
    private static final int LORE_SIZE = 3;
    private static final String VALIDATE_TOKEN = "��5��";
    private static final String KILL_COUNT_FORMAT = "��4KillCount: %";
    private static final String LEVEL_FORMAT = "��4Level: %";

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
                sender.sendMessage("��b-----��6KillerWapon���������b-----");
                if (this.getConfig().getBoolean("ad")){
                    sender.sendMessage("��b-----��6����:iwar��b-----");
                }
                sender.sendMessage("��b/kw activate ��7----- ��3�������е�����");
                sender.sendMessage("��b/kw deactivate ��7----- ��3���������е�����");
                if (sender.isOp() || sender.hasPermission("killerweapon.command")) {
                    sender.sendMessage("��c-----��6����Աָ���c-----");
                    sender.sendMessage("��c/kw reload ��7--- ��3���ز������");
                    sender.sendMessage("��c/kw debug ��7--- ��3��ⲿ�ִ���");
                    sender.sendMessage("��c/kw set [type] [value] ��7--- ��3�޸�����");
                    sender.sendMessage("��c/kw type ��7--- ��3�鿴���������Լ�����ţ�������/kw set����Ĵ��ţ���֧�ֵ�ֵ����");
                    sender.sendMessage("��c/kw show ��7--- ��3��ʾ����");
                }

                return true;
            } else if (args.length > 3) {
                sender.sendMessage("��c��������.");
                return true;
            } else if (args[0].equalsIgnoreCase("activate")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("klllerweapon.activate")) {
                        if (validateWeapon(p, true)) {
                            ItemStack item_hand = p.getItemInHand();
                            loreInit(item_hand);
                            p.sendMessage("��c����ɹ���");
                        } else {
                            p.sendMessage("��c��ǰ��Ʒ�޷�������ȼ���Ƿ��ڿɼ����б��С�");
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
                            p.sendMessage("��c"+deactivate);
                        }
                    }
                } else {
                    sender.sendMessage(controlpanel);
                }
                return true;
            } else if (sender.isOp() || sender.hasPermission("killerweapon.command")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    this.reloadConfig();
                    sender.sendMessage("��a[KillerWeapon]"+reload);
                    return true;
                } else if (args[0].equalsIgnoreCase("type")) {
                    if (sender.isOp() || sender.hasPermission("killerweapon.*")) {
                        sender.sendMessage("��c-----��6type��Ϣ��c-----");
                        sender.sendMessage("��c[ess]�����˺� ��7--- ��3������С��");
                        sender.sendMessage("��c[level]���������ɱ�� ��7--- ��3����");
                        sender.sendMessage("��c[message]��ɱ�ı���Ϣ [value] ��7--- ��3�ı�");
                        sender.sendMessage("��c[extra]ÿһ�ȼ������˺�ֵ ��7--- ��3������С��");
                        sender.sendMessage("��c[limit]�ȼ����� ��7--- ��3����");
                    }
                    return true;
                } else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                    String type = args[1];
                    String value = args[2];
                    ConfigurationSection etc = this.getConfig().getConfigurationSection("etc");
                    switch (type) {
                        case "ess":
                            etc.set("ess", Double.parseDouble(value));
                            sender.sendMessage("��c���óɹ�.");
                            break;
                        case "level":
                            etc.set("level", Integer.parseInt(value));
                            sender.sendMessage("��c���óɹ�.");
                            break;
                        case "message":
                            etc.set("message", value);
                            sender.sendMessage("��c���óɹ�.");
                            break;
                        case "extra":
                            etc.set("extra", Double.parseDouble(value));
                            sender.sendMessage("��c���óɹ�.");
                            break;
                        case "limit":
                            etc.set("limit", Integer.parseInt(value));
                            sender.sendMessage("��c���óɹ�.");
                            break;
                        default:
                            sender.sendMessage("��c�����ڵ����ʹ��ţ�����/kw type�鿴�������ʹ���");
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("show")) {
                    ConfigurationSection etc = this.getConfig().getConfigurationSection("etc");

                    sender.sendMessage("��c-----��6������Ϣ��c-----");
                    sender.sendMessage("��c[ess] ��7--- ��3�����˺�=>"+etc.getDouble("ess", 0D));
                    sender.sendMessage("��c[level] ��7--- ��3���������ɱ��=>"+etc.getInt("level", 0));
                    sender.sendMessage("��c[message] [value] ��7--- ��3��ɱ�ı���Ϣ=>"+etc.getString("message", "��"));
                    sender.sendMessage("��c[extra] ��7--- ��3ÿһ�ȼ������˺�ֵ=>"+etc.getDouble("extra", 0D));
                    sender.sendMessage("��c[limit] ��7--- ��3�ȼ�����=>"+etc.getInt("limit", 0));
                    return true;
                } else if (args[0].equalsIgnoreCase("debug")){
                    sender.sendMessage("��cChecking debug mode...");
                    boolean debug = this.getConfig().getBoolean("debug");
                    if (debug){
                        sender.sendMessage("��cDebug mode has already been enable.Now start disabling debug mode.");
                        this.getConfig().set("debug", false);
                        sender.sendMessage("��cDone.");
                        this.saveConfig();
                    } else {
                        sender.sendMessage("��cDebug mode was disable.Now start abling debug mode.");
                        this.getConfig().set("debug", true);
                        sender.sendMessage("��cDone.");
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
