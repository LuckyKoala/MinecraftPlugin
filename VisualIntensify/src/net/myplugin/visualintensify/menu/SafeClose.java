package net.myplugin.visualintensify.menu;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

/**
 * Created by iwar on 2016/7/15.
 */
public class SafeClose implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent ice) {
        Inventory editor = ice.getView().getTopInventory();
        if (editor.getTitle() != null && editor.getTitle().equalsIgnoreCase("IntensifyPanel")) {
            if (Material.STAINED_GLASS_PANE == editor.getItem(25).getType()) {
                //TODO 更改VI物品验证机制
            }
        }
    }
}
