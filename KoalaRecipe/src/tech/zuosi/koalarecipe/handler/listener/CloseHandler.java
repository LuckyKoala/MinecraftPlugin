package tech.zuosi.koalarecipe.handler.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalarecipe.recipe.RecipeLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2016/8/27.
 */
public class CloseHandler implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent ice) {
        Inventory inv = ice.getView().getTopInventory();

        if (!(ice.getPlayer() instanceof Player)) return;
        Player player = (Player) ice.getPlayer();

        if (!("合成面板-设置".equalsIgnoreCase(inv.getTitle()))
                && !("合成面板".equalsIgnoreCase(inv.getTitle()))) return;

        List<ItemStack> itemStackList = new ArrayList<>();

        for (int slot : RecipeLoader.MATERIALSLOT) {
            ItemStack is = RecipeLoader.safeItemStack(inv.getItem(slot));

            if (Material.AIR != is.getType()) itemStackList.add(is);
        }

        Location location = player.getLocation();
        for (ItemStack itemStack : itemStackList) {
            player.getWorld().dropItem(location,itemStack);
        }
    }
}
