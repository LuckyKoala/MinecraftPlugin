package tech.zuosi.koalaitem.handler.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/19.
 */
public class Item extends GuiHandler implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getView().getTopInventory();
        if ("物品菜单".equalsIgnoreCase(inv.getTitle())) {
            Player p = (Player) e.getWhoClicked();
            ItemStack pointItem = safeItemStack(e.getCurrentItem());
            Material type = pointItem.getType();
            ClickType clickType = e.getClick();
            if (!isInCorrectInventory(inv,e.getRawSlot())) return;
            if (!p.hasPermission("koalaitem.itemui")) {
                p.sendMessage(ChatColor.RED + "缺少权限节点koalaitem.itemui");
                e.setCancelled(true);
                return;
            }
            if (Material.STAINED_GLASS_PANE == type) {
                if (!isInfo(e.getCurrentItem())) return;
                e.setCancelled(true);
                return;
            } else if (Material.AIR == type) {
                return;
            }
            NBTUtil util = new NBTUtil(pointItem);
            String name = (String)util.getData("name");
            e.setCancelled(true);
            if (name == null || name.isEmpty()) return;
            ItemStack var = pointItem.clone();
            if (ClickType.LEFT == clickType) {
                log("Player:"+p.getName()+"Taken-Piece-Item:"+name);
                p.getWorld().dropItem(p.getLocation(),var);
            }
        }
    }
}
