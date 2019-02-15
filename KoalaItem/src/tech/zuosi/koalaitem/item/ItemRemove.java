package tech.zuosi.koalaitem.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/18.
 */
public class ItemRemove implements IItem {
    private ItemStack model = new ItemStack(Material.SHEARS);

    public ItemRemove() {}

    @Override
    public ItemStack defaultItem() {
        return new NBTUtil(model)
                .initData("采集器", ItemType.ITEM,new String[]{"0","0","0"})
                .initLore(new String[]{
                        ChatColor.GOLD + "可以作为工具，采集武器上已镶嵌的宝石.",
                        ChatColor.RED + "一个采集器只能采集一颗宝石，之后便会消失"
                })
                .setDisplayName("采集器")
                .getItemStack();
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
        return ((String) type).contains("ITEM") && ((String) name).contains("采集器");
    }
}
