package tech.zuosi.koalaitem.handler.gui;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.item.*;

/**
 * Created by iwar on 2016/7/21.
 */
public class Craft extends GuiHandler implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getView().getTopInventory();
        if (!"∫œ≥…Ã®".equals(inv.getTitle())) return;
        ItemStack[] itemStacks = inv.getContents();
        int size = itemStacks.length;
        int emptySize = 0;

        for (ItemStack itemStack : itemStacks) {
            if (Material.AIR == safeMaterial(itemStack)) emptySize++;
        }
        size -= emptySize;

        ItemStack air = new ItemStack(Material.AIR);
        ItemStack obsidian = new ItemStack(Material.OBSIDIAN);
        if (size == 9) {
            ItemStack diamond = new ItemStack(Material.DIAMOND);
            ItemStack emerald = new ItemStack(Material.EMERALD);
            ItemStack iron = new ItemStack(Material.IRON_INGOT);

            if (inv.contains(diamond,9)) {
                inv.setContents(new ItemStack[]{air,air,air,air,air,air,air,air,air});
                inv.setItem(4,new ItemIdentify().defaultItem());
            } else if (inv.contains(emerald,9)) {
                inv.setContents(new ItemStack[]{air,air,air,air,air,air,air,air,air});
                inv.setItem(4,new ItemIntensify().defaultItem());
            } else if (inv.contains(obsidian,8)) {
                if (inv.contains(iron,1)) {
                    inv.setContents(new ItemStack[]{air,air,air,air,air,air,air,air,air});
                    inv.setItem(4,new ItemDrill().defaultItem());
                }
            } else {
                for (int i = 1;i < size;i++) {
                    if (!(inv.getItem(i-1).equals(inv.getItem(i)))) return;
                }
                if (new ItemGem().validate(inv.getItem(0))) {
                    ItemGem itemGem = new ItemGem(inv.getItem(0));
                    if (itemGem.canLevelUp()) {
                        inv.setContents(new ItemStack[]{air,air,air,air,air,air,air,air,air});
                        inv.setItem(4,itemGem.levelUp());
                    }
                }
            }
        } else if (size == 2) {
            if (inv.contains(new ItemIntensify().defaultItem())) {
                if (inv.contains(new ItemStack(Material.EMERALD_ORE))) {
                    inv.setContents(new ItemStack[]{air,air,air,air,air,air,air,air,air});
                    inv.setItem(4,new ItemLuckyIntensify().defaultItem());
                } else if (inv.contains(obsidian)) {
                    inv.setContents(new ItemStack[]{air,air,air,air,air,air,air,air,air});
                    inv.setItem(4,new ItemSafetyIntensify().defaultItem());
                }
            }
        } else if (size == 3) {
            if (inv.contains(new ItemIdentify().defaultItem())
                    && inv.contains(new ItemIntensify().defaultItem())
                    && inv.contains(new ItemStack(Material.NETHER_STAR))) {
                inv.setContents(new ItemStack[]{air,air,air,air,air,air,air,air,air});
                inv.setItem(4,new ItemReborn().defaultItem());
            }
        }
    }


}
