package tech.zuosi.koalaattribute.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tech.zuosi.koalaattribute.tool.HealthAndSpeed;

/**
 * Created by iwar on 2016/8/12.
 */
public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        HealthAndSpeed.update(event.getPlayer());
    }
}
