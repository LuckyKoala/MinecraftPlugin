package tech.zuosi.koalaitem.handler.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

/**
 * Created by iwar on 2016/7/19.
 */
public class Test extends GuiHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHurt(EntityDamageByEntityEvent edbee) {
        if (!(edbee.getDamager() instanceof Player)) return;
        Player player = (Player) edbee.getDamager();
        player.sendMessage(ChatColor.LIGHT_PURPLE + "[Damage]" + (int)edbee.getDamage());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Inventory inv = e.getView().getTopInventory();
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        if ("锻造台".equals(inv.getTitle()) || "合成台".equals(inv.getTitle()) || "酿造台".equals(inv.getTitle())) {
            TempSave.SAVER.save(p,inv);
        }
    }
}
