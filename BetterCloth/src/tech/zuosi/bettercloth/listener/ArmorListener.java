package tech.zuosi.bettercloth.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import tech.zuosi.bettercloth.util.ActionBar;
import tech.zuosi.bettercloth.util.Data;

/**
 * Created by iwar on 2016/6/14.
 */
public class ArmorListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (Data.lockArmor.contains(p)) {
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
                    e.setCancelled(true);
                    ActionBar.sendAction(p, ChatColor.RED + "遭受攻击后十秒内您的装备栏处于被锁定状态。");
                }
            }
        }
    }
}
