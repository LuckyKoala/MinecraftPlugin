package tech.zuosi.koalaitem.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/18.
 */
public class ItemDrill implements IItem {
    private static ItemStack model = new ItemStack(Material.OBSIDIAN);

    static {
        model = new NBTUtil(model)
                .initData("打孔石", ItemType.ITEM,new String[]{"0","0","0"})
                .initLore(new String[]{
                        ChatColor.GREEN + "【功能】" + ChatColor.LIGHT_PURPLE + "在武器上打孔",
                        ChatColor.GREEN + "【用法】" + ChatColor.RED + "与物品一起放入锻造台"
                })
                .setDisplayName(ChatColor.GREEN + "打孔石")
                .getItemStack();
    }

    public ItemDrill() {}

    @Override
    public ItemStack defaultItem() {
        return model;
    }

    @Override
    public boolean canLevelUp() {
        return false;
    }

    @Override
    public boolean validate(ItemStack is) {
        NBTUtil util = new NBTUtil(is);
        Object type = util.getData("type");
        Object name = util.getData("name");
        if (type == null || name == null) return false;
        return ((String) type).contains("ITEM") && ((String) name).contains("打孔石");
    }
}
