package tech.zuosi.koalaitem.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.GemType;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/18.
 */
public class ItemGem implements IItem {
    private ItemStack model = new ItemStack(Material.EMERALD);
    private GemType gemType;
    private int level;
    private ItemStack itemStack;

    public ItemGem() {
        this.gemType = GemType.EMPTY;
        this.itemStack = model;
        this.level = 0;
    }

    public ItemGem(GemType gemType) {
        this.gemType = gemType;
        this.itemStack = model;
        this.level = 0;
    }

    public ItemGem(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.gemType = GemType.valueOf((String)new NBTUtil(itemStack).getData("name"));
        this.level = (int)(new NBTUtil(itemStack).getData("gemLevel"));
    }

    @Override
    public ItemStack defaultItem() {
        return new NBTUtil(itemStack)
                .initData(gemType.name(), ItemType.GEM, new String[]{"0","0","0"})
                .setDisplayName(ChatColor.GREEN + gemType.getName()+"宝石")
                .initLore(new String[]{
                        ChatColor.GREEN + "【功能】" + ChatColor.LIGHT_PURPLE + "赋予武器额外属性",
                        ChatColor.GREEN + "【用法】" + ChatColor.RED + "与物品一起放入锻造台",
                        ChatColor.GREEN + "【等级】" + ChatColor.RED + level
                })
                .getItemStack();
    }

    @Override
    public boolean canLevelUp() {
        return level <= 9;
    }

    @Override
    public boolean validate(ItemStack is) {
        NBTUtil util = new NBTUtil(is);
        Object type = util.getData("type");
        if (type == null) return false;
        return ((String) type).contains("GEM");
    }

    public int getLevel() {
        return this.level;
    }

    public GemType getGemType() {
        return gemType;
    }

    public ItemStack levelUp() {
        NBTUtil nbtUtil = new NBTUtil(itemStack);
        if (canLevelUp()) level++;
        nbtUtil.setData("gemLevel",level);
        return nbtUtil.initLore(new String[]{
                ChatColor.GOLD + "可以镶嵌以赋予武器额外属性",
                ChatColor.RED + "等级:" + level
        }).getItemStack();
    }
}
