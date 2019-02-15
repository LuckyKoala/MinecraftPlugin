package tech.zuosi.koalaitem.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * Created by iwar on 2016/7/21.
 */
public class CraftUI implements GUICreator {


    @Override
    public Inventory createGUI() {
        return Bukkit.createInventory(null, InventoryType.DISPENSER,"∫œ≥…Ã®");
    }
}
