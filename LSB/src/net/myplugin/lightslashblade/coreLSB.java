package net.myplugin.lightslashblade;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
 * Created by iwar on 2015/11/10.
 * §a绿色 §9蓝色 §c红色 §4深红色 §6金色
 */
public class coreLSB extends JavaPlugin implements Listener{
    //全局变量

    //基础方法
    @Override
    public void onEnable(){
        //保存默认的配置文件，发送日志
        this.saveDefaultConfig();
        getLogger().info("Lightslashblade has been loaded.");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable(){
        //发送日志
        this.saveConfig();
        getLogger().info("Lightslashblade has been unloaded.");
    }


    //命令处理
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String controlpanel = this.getConfig().getString("message.controlpanel");
        String activate = this.getConfig().getString("message.activate");
        String deactivate = this.getConfig().getString("message.deactivate");
        String reload = this.getConfig().getString("message.reload");

        if (cmd.getName().equalsIgnoreCase("lsb")) {
            // 如果玩家输入了/则执行如下内容...
            // 进一步判断参数，来确定最终的指令...


            if (args.length == 0) {
                sender.sendMessage("§b-----§6轻量级拔刀剑插件帮助§b-----");
                if (this.getConfig().getBoolean("ad")){
                    sender.sendMessage("§b-----§6作者:iwar§b-----");
                }
                sender.sendMessage("§b/lsb activate §7----- §3激活手中的武器");
                sender.sendMessage("§b/lsb deactivate §7----- §3反激活手中的武器");
                if (sender.hasPermission("lightslashblade.*") || sender.isOp()) {
                    sender.sendMessage("§c-----§6管理员指令§c-----");
                    sender.sendMessage("§c/lsb reload §7--- §3重载插件配置");
                    sender.sendMessage("§c/lsb debug §7--- §3检测部分代码");
                    sender.sendMessage("§c/lsb getitem [type] §7--- §3直接获得成长武器");
                }

                return true;
            } else if (args.length > 2) {
                sender.sendMessage("§cIllegal arguements.");
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                this.reloadConfig();
                sender.sendMessage("§a[LightSlashBlade]"+reload);
                return true;
            } else if (args[0].equalsIgnoreCase("activate")) {
                //检测并决定是否激活
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("lightslashblade.activate")) {
                        boolean cA = canAffect(p);
                        if (cA) {
                            List<String> lsb = new ArrayList<String>();
                            //获取手中物品
                            ItemStack item_hand = p.getItemInHand();
                            //获得手中物品的数据
                            ItemMeta im = item_hand.getItemMeta();
                            //判断该List是否为空，为空则继续操作
                            if (im.getLore()==null){
                                //在第一行添加一个lore进行标记
                                lsb.add("§5炼");
                                //在第二行添加一个lore进行计数
                                lsb.add("§4KillCount:0");
                                //在第三行添加一个lore显示增效
                                lsb.add("§4+0 ExtraAttackDamage");
                                im.setLore(lsb);
                                item_hand.setItemMeta(im);
                                p.sendMessage("§a"+activate);
                            } else {
                                p.sendMessage("§c当前武器激活失败，请先反激活一次。");
                            }
                        } else {
                            p.sendMessage("§c当前物品无法激活，请先检查是否在可激活列表中。");
                        }
                    }
                } else {
                    sender.sendMessage(controlpanel);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("deactivate")) {
                //检测并决定是否反激活
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (p.hasPermission("lightslashblade.deactivate")) {
                        boolean cA = canAffect(p);
                        if (cA) {
                            //获取手中物品的类型
                            ItemStack item_hand = p.getItemInHand();
                            //获得手中物品的数据
                            ItemMeta im = item_hand.getItemMeta();
                            if (im!=null){
                                if (im.getLore()!=null){
                                    im.setLore(null);
                                    item_hand.setItemMeta(im);
                                    p.sendMessage("§c"+deactivate);
                                }
                            }
                        }
                    }
                } else {
                    sender.sendMessage(controlpanel);
                }
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
            } else if (args[0].equalsIgnoreCase("getitem")){
                if (args.length == 2) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (p.hasPermission("lightslashblade.getitem")) {
                            if (null != Material.getMaterial(args[1]) && Material.AIR != Material.getMaterial(args[1])) {
                                ItemStack is = new ItemStack(Material.getMaterial(args[1]));
                                ItemMeta im = is.getItemMeta();
                                List<String> lore = new ArrayList<>();
                                //在第一行添加一个lore进行标记
                                lore.add("§5炼");
                                //在第二行添加一个lore进行计数
                                lore.add("§4KillCount:0");
                                //在第三行添加一个lore显示增效
                                lore.add("§4+0 ExtraAttackDamage");
                                im.setLore(lore);
                                is.setItemMeta(im);
                                p.getWorld().dropItem(p.getLocation(),is);
                            } else {
                                p.sendMessage(ChatColor.RED+"指定的物品类型不存在或者为空气");
                            }
                            return true;
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED+"没有指定物品类型哦~");
                    return true;
                }
            }
        } //如果以上内容成功执行，则返回true
        // 如果执行失败，则返回false.
        return false;
    }


    //事件监听
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        //同时检测是否可以提升武器的攻击力
        Entity tar = event.getDamager();
        EntityType victim = event.getEntityType();
        boolean debug = this.getConfig().getBoolean("debug");
        //检查是否玩家
        if (tar instanceof Player){
            //为方便操作，对tar进行向下转型
            Player p = (Player)tar;
            if (canAffect(p)){
                List<String> lsb = new ArrayList<>();
                ItemStack tar_it = p.getItemInHand();
                ItemMeta tar_im = tar_it.getItemMeta();
                if (tar_im!=null) {
                    if (tar_im.getLore() != null) {
                        if (this.getConfig().getBoolean("list.mob."+victim.name().toLowerCase())){
                            if (debug){
                                p.sendMessage(victim.name().toLowerCase());
                            }
                            //Unit
                            int u = getUnit(tar_im);
                            //Operate
                            double d = event.getDamage();
                            d += u;
                            event.setDamage(d);
                            //level_up
                            String temp = "§4+" + u + " ExtraAttackDamage";
                            int killcount = getNum(tar_im.getLore().get(1));
                            String t1 = Integer.toString(killcount);
                            tar_im.setLore(null);
                            //在第一行添加一个lore进行标记
                            lsb.add("§5炼");
                            //在第二行添加一个lore进行计数
                            lsb.add("§4KillCount:"+t1);
                            //在第三行添加一个lore显示增效
                            lsb.add(temp);
                            tar_im.setLore(lsb);
                            tar_it.setItemMeta(tar_im);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        Player killer = event.getEntity().getKiller();
        EntityType victim = event.getEntityType();
        boolean debug = this.getConfig().getBoolean("debug");
        if (killer!=null){
            if (canAffect(killer)){
                List<String> lsb = new ArrayList<String>();
                //获取手中物品的类型
                ItemStack item_hand = killer.getItemInHand();
                //获得手中物品的数据
                ItemMeta im = item_hand.getItemMeta();
                if (im!=null){
                    if (im.getLore()!=null){
                        if (this.getConfig().getBoolean("list.mob."+victim.name().toLowerCase())){
                            if (debug){
                                killer.sendMessage(victim.name().toLowerCase());
                            }
                            int killcount = getNum(im.getLore().get(1));
                            int u = getUnit(im);
                            String temp = "§4+" + u + " ExtraAttackDamage";
                            killcount++;
                            String t1 = Integer.toString(killcount);
                            //im.getLore().set(1,"§4KillCount : "+t1);
                            im.setLore(null);
                            //在第一行添加一个lore进行标记
                            lsb.add("§5炼");
                            //在第二行添加一个lore进行计数
                            lsb.add("§4KillCount:"+t1);
                            //在第三行添加一个lore显示增效
                            lsb.add(temp);
                            im.setLore(lsb);
                            item_hand.setItemMeta(im);
                        }
                    }
                }
            }
        }
    }


    //自用方法
    public boolean canAffect(Player er) {
        //检查玩家是否拥有基础的插件使用权限，手中物品是否为支持的武器类型。
        //it~~item

        if (er.hasPermission("lightslashblade.use")) {
            if (null != er.getItemInHand() && Material.AIR != er.getItemInHand().getType()) {
                ItemStack item = er.getItemInHand();
                if (item.getItemMeta().hasLore()) {
                    if (item.getItemMeta().getLore().size() == 2) {
                        if (item.getItemMeta().getLore().get(0).equals("§5炼")) {
                            String item_type = item.getType().name();
                            if(this.getConfig().getBoolean("list.default."+item_type)){
                                return true;
                            } else if (this.getConfig().getBoolean("list.mod.enable")){
                                if (this.getConfig().getBoolean("list.mod."+item_type)){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    //去除指定Lore中的数字
    public static int getNum(String str){
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

    //当杀怪数量达到一定程度时提升等级,后期可考虑播放粒子效果
    public int getUnit(ItemMeta im){
        //提取Lore
        String kc;
        //后期使用变量
        int killCount,fi,tem,unit;
        kc = im.getLore().get(1);
        //获取数字
        killCount = getNum(kc);
        //获取比例值
        fi = this.getConfig().getInt("formula.fi");
        tem = killCount%fi;
        unit = (killCount-tem)/fi;
        return unit;
    }
}


