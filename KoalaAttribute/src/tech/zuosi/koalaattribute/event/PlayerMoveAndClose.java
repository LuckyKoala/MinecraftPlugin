package tech.zuosi.koalaattribute.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import tech.zuosi.koalaattribute.tool.HealthAndSpeed;

/**
 * Created by iwar on 2016/8/12.
 */
public class PlayerMoveAndClose implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        HealthAndSpeed.update(player);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        HealthAndSpeed.update(player);
    }
}
