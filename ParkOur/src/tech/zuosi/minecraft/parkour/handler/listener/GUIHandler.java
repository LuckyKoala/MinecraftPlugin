package tech.zuosi.minecraft.parkour.handler.listener;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.ui.PaneManager;
import tech.zuosi.minecraft.parkour.util.NBTUtil;

import java.util.logging.Logger;

/**
 * Created by iwar on 2016/7/19.
 */
class GUIHandler {
    private final Logger logger = Core.getInstance().getLogger();

    private static ItemStack nullToAir(ItemStack is) {
        return is==null?new ItemStack(Material.AIR):is;
    }

    static boolean isNotAir(ItemStack is) {
        return nullToAir(is).getType() != Material.AIR;
    }

    boolean isInParkOurInventory(Inventory inv, int rawSlotClicked) {
        return inv.getTitle().startsWith(PaneManager.PANE_PREFIX)
                && rawSlotClicked < inv.getSize() && rawSlotClicked >= 0;
    }

    public void log(String message) {
        logger.info(message);
    }

    boolean isMenu(ItemStack is) {
        return new NBTUtil(is).getData("target") != null;
    }
}
