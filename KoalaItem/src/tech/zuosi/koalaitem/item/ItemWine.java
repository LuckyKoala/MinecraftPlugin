package tech.zuosi.koalaitem.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.util.NBTOperator;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/18.
 */
public class ItemWine implements IItem {
    private ItemStack model;
    private String[] wineString;

    public ItemWine() {}

    public ItemWine(String[] wineString) {
        model = new ItemStack(Material.POTION);
        model.setDurability((short)0);
        this.wineString = wineString;
    }

    @Override
    public ItemStack defaultItem() {
        return new NBTOperator(new NBTUtil(model)).brew(wineString).getItemStack();
    }

    @Override
    public boolean canLevelUp() {
        return false;
    }

    @Override
    public boolean validate(ItemStack is) {
        NBTUtil util = new NBTUtil(is);
        Object type = util.getData("type");
        if (type == null) return false;
        return ((String) type).contains("WINE");
    }
}
