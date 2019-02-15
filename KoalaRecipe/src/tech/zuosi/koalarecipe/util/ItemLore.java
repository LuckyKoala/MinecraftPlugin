package tech.zuosi.koalarecipe.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Created by iwar on 2016/8/23.
 */
public class ItemLore {
    private ItemStack itemstack;
    private ItemMeta itemMeta;

    public ItemLore() {}

    //TODO 使用PowerNBT实现
    public ItemLore item(ItemStack is) {
        this.itemstack = is;
        this.itemMeta = itemstack.getItemMeta();

        return this;
    }

    public ItemLore lore(String[] description) {
        itemMeta.setLore(Arrays.asList(description));

        return this;
    }

    public ItemLore name(String displayName) {
        itemMeta.setDisplayName(displayName);

        return this;
    }

    public ItemStack build() {
        itemstack.setItemMeta(itemMeta);

        return itemstack;
    }
}
