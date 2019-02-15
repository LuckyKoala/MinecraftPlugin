package net.myplugin.visualintensify.lore;

import net.myplugin.visualintensify.EventListener;
import net.myplugin.visualintensify.VisualIntensify;
import net.myplugin.visualintensify.util.Probability;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2016/1/30.
 */
public class LoreManager {
    private VisualIntensify plugin;
    private ItemStack is;
    private ItemMeta im;
    private boolean debug;
    public static int vi=0,quality_level=1,intensify_level=2,reborn_level=3,socket=4,track=5,attack=6,count=7;

    //本类只操作Lore，检测Lore以及概率运算
    public LoreManager(VisualIntensify plugin, ItemStack is) {
        this.plugin=plugin;
        this.is=is;
        this.im=is.getItemMeta();
        this.debug = plugin.getConfig().getBoolean("debug");
    }

    //检测是否为合法的vi物品
    public boolean isITEM() {
        if (!is.hasItemMeta()) return false;
        if (!im.hasLore()) return false;
        if (im.getLore().size() > 2 || im.getLore().size() == 0) return false;
        return getSubString(im.getLore().get(0)).equals(im.getDisplayName());
    }

    //检测是否为合法的vi物品-指定名称
    public boolean isITEM(String displayname) {
        if (!is.hasItemMeta()) return false;
        if (!im.hasLore()) return false;
        if (im.getLore().size() > 2 || im.getLore().size() == 0) return false;
        if (!displayname.equals(getSubString(im.getLore().get(0)))) return false;
        return getSubString(im.getLore().get(0)).equals(im.getDisplayName());
    }

    public boolean isINIT() {
        if (!is.hasItemMeta()) return false;
        if (!im.hasLore()) return false;
        if (im.getLore().size() < 8 || im.getLore().size() == 0) return false;
        return true;
    }

    //处理并获得可处理的Lore中int数据以便操作
    public int getNum(String str) {
        StringBuilder sb = new StringBuilder();
        if(str != null && !"".equals(str)){
            for(int i=0;i<str.length();i++){
                if(str.charAt(i)>=48 && str.charAt(i)<=57){
                    sb.append(str.charAt(i));
                }
            }
        }
        return Integer.parseInt(sb.toString());
    }

    //处理并获得可处理的Lore中String数据以便操作
    public String getSubString(String str) {
        int index = str.indexOf(":");
        return str.substring(index+1);
    }

    public String getSubString2(String str) {
        int index = str.indexOf("-");
        return str.substring(index+1);
    }

    //强化
    public boolean isIntensifyMax() {
        return 10 == getNum(im.getLore().get(intensify_level));
    }

    public boolean initLore(boolean hasLuckygem) {
        //初次强化
        int chance = plugin.getConfig().getInt("chance.0");
        if (hasLuckygem) {
            chance += plugin.getConfig().getInt("chance.lucky_gem");
        }
        if (debug) {
            plugin.getLogger().info("Chance:"+chance);
        }
        if (!new Probability(plugin).getResult(chance)) return false;
        List<String> lore = new ArrayList<>();
        lore.add(vi,"vi:用户物品");
        lore.add(quality_level,"品质:普通");
        lore.add(intensify_level,"强化等级:1");
        lore.add(reborn_level,"转生等级:0");
        lore.add(socket,"宝石增益:null");
        lore.add(track,"武器核心:null");
        lore.add(attack,"攻击力加成:"+plugin.getConfig().getInt("intensify.attack_up"));
        lore.add(count,"编号:"+(plugin.getConfig().getInt("count")+1));
        plugin.getConfig().set("count",plugin.getConfig().getInt("count")+1);
        im.setLore(lore);
        is.setItemMeta(im);
        return true;
    }

    public boolean intensifyItem(boolean hasLuckygem) {
        if (!is.hasItemMeta()) {
            if (debug) plugin.getLogger().info("NoIM");
            return false;
        }
        //强化-此处有概率运算,-1 fail;0 false;1 true
        if (!im.hasLore()) {
            if (debug) plugin.getLogger().info("NoLORE");
            return false;
        }
        if (im.getLore().size() < 8 || im.getLore().size() == 0) {
            if (debug) plugin.getLogger().info("SizeWrong");
            return false;
        }
        int chance,intensify_level_value,attack_value;
        String chance_path;
        List<String> lore;
        lore = im.getLore();
        intensify_level_value = getNum(lore.get(intensify_level));
        attack_value = getNum(lore.get(attack));
        chance_path = "chance." + intensify_level_value;
        chance = plugin.getConfig().getInt(chance_path);
        if (hasLuckygem) {
            chance += plugin.getConfig().getInt("chance.lucky_gem");
        }
        if (debug) {
            plugin.getLogger().info("Chance:"+chance);
        }
        if (!new Probability(plugin).getResult(chance)) return false;
        //判断完毕，开始执行强化操作
        lore.set(intensify_level, "强化等级:" + (intensify_level_value + 1));
        if (debug) {
            plugin.getServer().broadcastMessage("Value+1");
        }
        lore.set(attack, "攻击力加成:" + (attack_value + plugin.getConfig().getInt("intensify.attack_up")));
        im.setLore(lore);
        is.setItemMeta(im);
        return true;
    }

    //转生
    public boolean isRebornMax() {
        return 5 == getNum(im.getLore().get(reborn_level));
    }

    public boolean rebornItem() {
        if (!is.hasItemMeta()) return false;
        if (!im.hasLore()) return false;
        if (im.getLore().size() < 8 || im.getLore().size() == 0) return false;
        int level,attack_up;
        String quality,quality_pre;
        List<String> lore;
        if (isIntensifyMax()) {
            lore = im.getLore();
            quality_pre = getSubString(lore.get(quality_level));
            level = getNum(lore.get(reborn_level));
            attack_up = getNum(lore.get(attack));
            //品质判断与提升
            switch (quality_pre) {
                case "普通":
                    quality = "精良";
                    break;
                case "精良":
                    quality = "卓越";
                    break;
                case "卓越":
                    quality = "传说";
                    break;
                case "传说":
                    quality = "史诗";
                    break;
                case "史诗":
                    quality = "无双";
                    break;
                default:
                    return false;
            }
            lore.set(quality_level,"品质:"+quality);
            //强化等级清零
            lore.set(intensify_level,"强化等级:0");
            //转生等级变更
            lore.set(reborn_level,"转生等级:"+(level+1));
            //计算保留de攻击力加成
            lore.set(attack,new StringBuilder().append((int)attack_up*(plugin.getConfig().getInt("reborn.percent")/100)).toString());
            im.setLore(lore);
            is.setItemMeta(im);
            return true;
        }
        return false;
    }

    //宝石
    public String getGemName() {
        return getSubString(im.getLore().get(socket));
    }

    //-1 noGem 0 false 1 hasGem
    public int hasGem() {
        if (!is.hasItemMeta()) return 0;
        if (!im.hasLore()) return 0;
        if (im.getLore().size() < 8 || im.getLore().size() == 0) return 0;
        return "null".equals(getSubString(im.getLore().get(socket)))?-1:1;
    }

    public boolean addGem(ItemStack gem,Player p) {
        if (!is.hasItemMeta()) return false;
        //镶嵌宝石
        if (hasGem() >= 0) return false;
        if (getNum(im.getLore().get(intensify_level)) < 7) return false;
        String socket_value = getSubString2(gem.getItemMeta().getLore().get(0));
        List<String> lore;
        lore = im.getLore();
        lore.set(socket,"宝石增益:"+socket_value);
        im.setLore(lore);
        is.setItemMeta(im);
        EventListener.gemOnAffect.put(p,socket_value);
        EventListener.canAffect.put(p,true);
        return true;
    }

    public boolean canRemoveGem() {
        return hasGem() > 0;
    }

    public void removeGem(Player p) {
        //摘取宝石
        List<String> lore;
        lore = im.getLore();
        lore.set(socket,"宝石增益:null");
        im.setLore(lore);
        is.setItemMeta(im);
        EventListener.gemOnAffect.put(p,"null");
        EventListener.canAffect.put(p,false);
    }

    public ItemStack getGem() {
        String name = new StringBuilder().append("宝石-").append(getGemName()).toString();
        List<String> stringList = new ArrayList<>();
        stringList.add(0,"vi:"+name);
        //0w0
        ItemStack GEM = new ItemStack(Material.EMERALD);
        ItemMeta GEM_IM = GEM.getItemMeta();
        GEM_IM.setDisplayName(name);
        GEM_IM.setLore(stringList);
        GEM.setItemMeta(GEM_IM);
        return GEM;
    }

    //track
    public String getTrackName() {
        return getSubString(im.getLore().get(track));
    }

    public boolean hasTrack() {
        if (!im.hasLore()) return false;
        if (im.getLore().size() < 8 || im.getLore().size() == 0) return false;
        return !("null".equals(getSubString(im.getLore().get(track))));
    }

    public boolean addTrack(ItemStack track_gem) {
        if (hasTrack()) return false;
        String track_value = getSubString2(track_gem.getItemMeta().getLore().get(0));
        List<String> lore;
        lore = im.getLore();
        lore.set(track,"武器核心:"+track_value);
        im.setLore(lore);
        is.setItemMeta(im);
        return true;
    }

}
