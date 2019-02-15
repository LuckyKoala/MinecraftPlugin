package tech.zuosi.koalaitem.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/18.
 */
public class ItemLuckyIntensify implements IItem {
    private ItemStack model = new ItemStack(Material.EMERALD);

    public ItemLuckyIntensify() {}

    @Override
    public ItemStack defaultItem() {
        return new NBTUtil(model)
                .initData("幸运强化石", ItemType.ITEM,new String[]{"0","0","0"})
                .initLore(new String[]{
                        ChatColor.GREEN + "【功能】" + ChatColor.LIGHT_PURPLE + "强化武器，成功几率更高",
                        ChatColor.GREEN + "【用法】" + ChatColor.RED + "与物品一起放入锻造台"
                })
                .setDisplayName(ChatColor.GREEN + "幸运强化石")
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
        return ((String) type).contains("ITEM") && name.equals("幸运强化石");
    }
}
