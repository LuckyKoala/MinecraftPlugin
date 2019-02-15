package tech.zuosi.koalaitem.handler.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.gui.BrewUI;
import tech.zuosi.koalaitem.gui.CraftUI;
import tech.zuosi.koalaitem.gui.ItemUI;
import tech.zuosi.koalaitem.gui.MakerUI;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/21.
 */
public class Menu extends GuiHandler implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getView().getTopInventory();
        if ("Ö÷²Ëµ¥".equals(inv.getTitle())) {
            ItemStack click = safeItemStack(e.getCurrentItem());
            Material type = click.getType();
            int rawSlot = e.getRawSlot();
            if (!isInCorrectInventory(inv,rawSlot)) return;
            if (Material.STAINED_GLASS_PANE == type && isInfo(click)) {
                e.setCancelled(true);
            } else if (Material.BOOK == type && isMenu(click)) {
                NBTUtil util = new NBTUtil(click);
                if (e.getWhoClicked() instanceof Player) {
                    Player p = (Player)e.getWhoClicked();
                    e.setCancelled(true);
                    String uiName = (String)util.getData("name");
                    switch (uiName) {
                        case "MakerUI":
                            p.closeInventory();
                            p.openInventory(new MakerUI().createGUI());
                            break;
                        case "ItemUI":
                            p.closeInventory();
                            p.openInventory(new ItemUI().createGUI());
                            break;
                        case "CraftUI":
                            p.closeInventory();
                            p.openInventory(new CraftUI().createGUI());
                            break;
                        case "BrewUI":
                            p.closeInventory();
                            p.openInventory(new BrewUI().createGUI());
                            break;
                    }
                }
            }
        }
    }
}
