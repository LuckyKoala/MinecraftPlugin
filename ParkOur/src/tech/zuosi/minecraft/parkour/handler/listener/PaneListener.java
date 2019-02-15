package tech.zuosi.minecraft.parkour.handler.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.util.NBTUtil;

/**
 * Created by LuckyKoala on 18-9-17.
 */
public class PaneListener extends GUIHandler implements Listener {
    static ItemStack AIR = new ItemStack(Material.AIR);

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getView().getTopInventory();
        int slot = e.getRawSlot();

        if(isInParkOurInventory(inv, slot)) {
            e.setResult(Event.Result.DENY);
            Player p = (Player) e.getWhoClicked();
            ItemStack itemClicked = e.getCurrentItem();

            if(isNotAir(itemClicked) && isMenu(itemClicked)) {
                NBTUtil itemNBT = new NBTUtil(itemClicked);
                p.closeInventory();
                Core.getInstance().paneManager
                        .requestToViewInventory(p,
                                (String) itemNBT.getData("target"), (String) itemNBT.getData("data"));
            }
        }
    }
}
