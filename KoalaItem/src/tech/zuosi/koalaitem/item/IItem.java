package tech.zuosi.koalaitem.item;

import org.bukkit.inventory.ItemStack;

/**
 * Created by iwar on 2016/7/18.
 */
public interface IItem {
    ItemStack defaultItem();
    boolean canLevelUp();
    boolean validate(ItemStack is);
}
