package tech.zuosi.koalaitem.handler.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.item.ItemWine;
import tech.zuosi.koalaitem.util.NBTUtil;

import java.util.Iterator;

/**
 * Created by iwar on 2016/7/21.
 */
public class Brew extends GuiHandler implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Inventory inv = e.getView().getTopInventory();
        if (!"酿造台".equals(inv.getTitle())) return;
        ItemStack[] itemStacks = inv.getContents();
        int size = itemStacks.length;
        int emptySize = 0;

        for (ItemStack itemStack : itemStacks) {
            if (Material.AIR == safeMaterial(itemStack)) emptySize++;
        }
        size -= emptySize;
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        ItemStack air = new ItemStack(Material.AIR);
        if (!air.equals(safeItemStack(inv.getItem(3)))) {
            player.sendMessage(ChatColor.RED + "请先移除在产物栏的物品");
            return;
        }
        if (size != 3) return;

        String[] currentRecipe = {
                (String)new NBTUtil(itemStacks[0]).getData("name"),
                (String)new NBTUtil(itemStacks[1]).getData("name"),
                (String)new NBTUtil(itemStacks[2]).getData("name")
        };
        ConfigurationSection cs = KoalaItem.INSTANCE.getConfig().getConfigurationSection("Wine");
        Iterator iterator = cs.getKeys(false).iterator();
        int count = 0;
        String wineName,wineRecipe,wineEffect;
        ItemStack wine;
        while(iterator.hasNext()) {
            wineName = (String) iterator.next();
            wineRecipe = cs.getString(wineName + ".recipe");
            wineEffect = cs.getString(wineName + ".effect");
            String[] recipe = wineRecipe.split("/");
            for (int i = 0;i < 3;i++) {
                if (currentRecipe[i].equalsIgnoreCase(recipe[i]))
                    count++;
            }
            if (count == 3) {
                inv.setContents(new ItemStack[]{air,air,air,air});
                wine = new ItemWine(new String[]{
                        wineName,
                        wineRecipe,
                        wineEffect
                }).defaultItem();
                inv.setContents(new ItemStack[]{air,air,air,wine});
                return;
            }
        }
    }
}
