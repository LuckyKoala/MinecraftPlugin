package tech.zuosi.koalaitem.handler.interact;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import tech.zuosi.koalaitem.gui.MenuUI;
import tech.zuosi.koalaitem.item.ItemWatch;

/**
 * Created by iwar on 2016/7/23.
 */
public class ShowMenu implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasItem()) {
            if (!Material.WATCH.equals(e.getItem().getType())) return;
            if (!new ItemWatch().validate(e.getItem())) return;

            Player player = e.getPlayer();
            /*if (!e.getPlayer().hasPermission("visualintensify.use")) {
                e.getPlayer().sendMessage(ChatColor.RED+"没有权限打开面板哦~");
            }*/
            Action action = e.getAction();
            if (Action.RIGHT_CLICK_AIR == action || Action.RIGHT_CLICK_BLOCK == action) {
                //打开GUI
                if (player == null) return;
                player.closeInventory();
                player.openInventory(new MenuUI().createGUI());
            }
        }
    }
}
