package net.myplugin.visualintensify.menu;

import net.myplugin.visualintensify.VisualIntensify;
import net.myplugin.visualintensify.util.NBTUtil;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2016/2/3.
 */
public class MenuManager implements SlotValue {
    private VisualIntensify plugin;

    
    public MenuManager(VisualIntensify plugin) {
        this.plugin = plugin;
    }

    public enum MenuType {
        MAIN,      //主菜单
        ITEM,      //获取物品
        INTENSIFY, //强化
        REBORN,    //转生
        GEM,       //镶嵌
        REMOVE;    //摘取

        MenuType() {
        }
    }

    public ItemStack viMenu (ItemStack is,String name) {
        NBTUtil.writeMenu(is,name);
        return is;
    }

    public ItemStack viInfo (String name) {
        ItemStack is = new ItemStack(Material.SIGN);
        ItemMeta im = is.getItemMeta();
        List<String> stringList = new ArrayList<>();
        im.setDisplayName(name);
        stringList.add(0,"vi:信息");
        im.setLore(stringList);
        is.setItemMeta(im);
        return is;
    }

    public ItemStack viItem (ItemStack is,String displayName,String typeName) {
        NBTUtil.writeItem(is,displayName,typeName);
        return is;
    }

    public ItemStack viItem (ItemStack is,String displayName,String typeName,String description) {
        String[] args = {description};
        NBTUtil.writeItem(is,displayName,typeName);
        NBTUtil.rewriteLore(is,args);
        return is;
    }

    public boolean isNothing(Inventory inventory,int INT) {
        return null == inventory.getItem(INT) || Material.AIR.equals(inventory.getItem(INT).getType());
    }

    public boolean canOperateType(ItemStack itemStack) {
        return plugin.getConfig().getBoolean("list."+itemStack.getType().name());
    }

    public Inventory createGUI(MenuType type) {
        Inventory editor;
        int i;
        if (type == MenuType.MAIN) {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for (i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            //editor.setItem(,viMenu(new ItemStack(Material),""));
            editor.setItem(0, viMenu(new ItemStack(Material.WORKBENCH), "物品菜单"));
            editor.setItem(1, viMenu(new ItemStack(Material.ANVIL), "强化菜单"));
            editor.setItem(2, viMenu(new ItemStack(Material.EMERALD), "镶嵌菜单"));
            editor.setItem(3, viMenu(new ItemStack(Material.EGG), "转生菜单"));
            editor.setItem(4, viMenu(new ItemStack(Material.WOOD_AXE), "摘取菜单"));
            editor.setItem(8, viMenu((new Wool(DyeColor.GREEN)).toItemStack(), "关闭"));

            return editor;
        } else if (type == MenuType.ITEM) {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for (i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            //editor.setItem(,viItem(new ItemStack(Material),""));
            editor.setItem(0, viItem(new ItemStack(Material.EMERALD), "§d强化石","强化石","§2强化石，用于强化你的武器"));
            editor.setItem(1, viItem(new ItemStack(Material.EMERALD), "§d幸运系强化石","幸运系强化石","§2幸运强化石，增加你强化成功的几率"));
            editor.setItem(2, viItem(new ItemStack(Material.PAPER), "§d安全护符","安全护符","§2安全护符，保护你的武器，强化失败也能保存武器"));
            editor.setItem(3, viItem(new ItemStack(Material.NETHER_STAR), "§c转生石","转生石","§2转生石，使你的武器转生，提高品质"));
            editor.setItem(4, viItem(new ItemStack(Material.MAGMA_CREAM), "§e烈焰","武器核心-烈焰","§2烈焰核心，使你的武器拥有粒子特效"));
            editor.setItem(5, viItem(new ItemStack(Material.EYE_OF_ENDER), "§e末影","武器核心-末影","§2末影核心，使你的武器拥有粒子特效"));
            editor.setItem(6, viItem(new ItemStack(Material.SHEARS), "§d采集工具","采集工具","§2采集工具，将你的宝石采摘下来"));
            editor.setItem(7, viItem(new ItemStack(Material.EMERALD), "暴击伤害提升","宝石-暴击伤害提升"));
            editor.setItem(8, viItem(new ItemStack(Material.EMERALD), "暴击概率提升","宝石-暴击概率提升"));
            editor.setItem(9, viItem(new ItemStack(Material.EMERALD), "吸血","宝石-吸血"));
            editor.setItem(10, viItem(new ItemStack(Material.EMERALD), "闪避","宝石-闪避"));
            editor.setItem(11, viItem(new ItemStack(Material.EMERALD), "经验加成","宝石-经验加成"));
            editor.setItem(12, viItem(new ItemStack(Material.EMERALD), "快速移动","宝石-快速移动"));
            editor.setItem(16, viInfo("滑轮中键复制一组"));
            editor.setItem(17, viMenu((new Wool(DyeColor.GREEN)).toItemStack(), "关闭"));

            return editor;
        } else if (type == MenuType.INTENSIFY) {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for (i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            editor.setItem(MATERIAL-1, viInfo("右侧放入待强化物品"));
            editor.setItem(MATERIAL, new ItemStack(Material.AIR));
            editor.setItem(UP-1, viInfo("右侧放入强化石或幸运强化石"));
            editor.setItem(UP, new ItemStack(Material.AIR));
            editor.setItem(PRODUCT+1, viInfo("此处将生成强化后物品"));
            editor.setItem(PRODUCT, new ItemStack(Material.AIR));
            editor.setItem(DOWN-1, viInfo("右侧放入安全护符"));
            editor.setItem(DOWN, new ItemStack(Material.AIR));
            editor.setItem(25, viMenu(new ItemStack(Material.ANVIL), "强化"));
            editor.setItem(26, viMenu((new Wool(DyeColor.GREEN)).toItemStack(), "返回"));

            return editor;
        } else if (type == MenuType.REBORN) {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for (i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            editor.setItem(MATERIAL-1, viInfo("右侧放入待转生物品"));
            editor.setItem(MATERIAL, new ItemStack(Material.AIR));
            editor.setItem(PRODUCT+1, viInfo("此处将生成转生后物品"));
            editor.setItem(PRODUCT, new ItemStack(Material.AIR));
            editor.setItem(DOWN-1, viInfo("右侧放入转生石"));
            editor.setItem(DOWN, new ItemStack(Material.AIR));
            editor.setItem(UP-1, viInfo("右侧放入武器核心"));
            editor.setItem(UP, new ItemStack(Material.AIR));
            editor.setItem(25, viMenu(new ItemStack(Material.EGG), "转生"));
            editor.setItem(26, viMenu((new Wool(DyeColor.GREEN)).toItemStack(), "返回"));

            return editor;
        } else if(type == MenuType.REMOVE) {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for (i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            editor.setItem(MATERIAL-1, viInfo("右侧放入待处理物品"));
            editor.setItem(MATERIAL, new ItemStack(Material.AIR));
            editor.setItem(PRODUCT+1, viInfo("此处将生成处理后物品"));
            editor.setItem(PRODUCT, new ItemStack(Material.AIR));
            editor.setItem(PRODUCT_GEM+1, viInfo("此处将生成处理后宝石"));
            editor.setItem(PRODUCT_GEM, new ItemStack(Material.AIR));
            editor.setItem(DOWN-1, viInfo("右侧放入采集工具"));
            editor.setItem(DOWN, new ItemStack(Material.AIR));
            editor.setItem(25, viMenu(new ItemStack(Material.WOOD_AXE), "摘取"));
            editor.setItem(26, viMenu((new Wool(DyeColor.GREEN)).toItemStack(), "返回"));

            return editor;
        } else if(type != MenuType.GEM) {
            return null;
        } else {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for(i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            editor.setItem(UP-1, viInfo("右侧放入宝石"));
            editor.setItem(UP, new ItemStack(Material.AIR));
            editor.setItem(MATERIAL-1, viInfo("右侧放入待处理物品"));
            editor.setItem(MATERIAL, new ItemStack(Material.AIR));
            editor.setItem(PRODUCT+1, viInfo("此处将生成处理后物品"));
            editor.setItem(PRODUCT, new ItemStack(Material.AIR));
            editor.setItem(16,viMenu(new ItemStack(Material.EMERALD),"镶嵌"));
            editor.setItem(17,viMenu((new Wool(DyeColor.GREEN)).toItemStack(),"返回"));

            return editor;
        }
    }

}
