package tech.zuosi.koalaitem.handler.gui;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

import java.util.logging.Logger;

/**
 * Created by iwar on 2016/7/19.
 */
public class GuiHandler {
    private Logger logger = KoalaItem.INSTANCE.getLogger();

    public Material safeMaterial(ItemStack is) {
        return safeItemStack(is).getType();
    }

    public ItemStack safeItemStack(ItemStack is) {
        return is==null?new ItemStack(Material.AIR):is;
    }

    public boolean isInCorrectInventory(Inventory inv,int rawSlot) {
        if (rawSlot<inv.getSize() && rawSlot >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public void log(String message) {
        logger.info(message);
    }

    public boolean isTool(ItemStack is) {
        return new NBTUtil(is).getData("isInit") != null;
    }

    public boolean isInfo(ItemStack is) {
        NBTUtil util = new NBTUtil(is);
        ItemType itemType = ItemType.valueOf((String)util.getData("type"));
        if (itemType == null) return false;
        return ItemType.INFO == itemType;
    }

    public boolean isMenu(ItemStack is) {
        NBTUtil util = new NBTUtil(is);
        ItemType itemType = ItemType.valueOf((String)util.getData("type"));
        if (itemType == null) return false;
        return ItemType.MENU == itemType;
    }
}
