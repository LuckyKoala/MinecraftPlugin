package tech.zuosi.koalaattribute.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import tech.zuosi.koalaattribute.KoalaAttribute;
import tech.zuosi.koalaattribute.tool.HealthAndSpeed;

/**
 * Created by iwar on 2016/8/12.
 */
public class PlayerItemHeld implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHeld(PlayerItemHeldEvent event) {
        /*Player player = event.getPlayer();
        boolean isOriMaxHealth = Double.compare(20D,player.getMaxHealth()) == 0;

        if (!isOriMaxHealth) return;
        HealthAndSpeed.putExtraHealth(player.getName(),0D);
        HealthAndSpeed.update(player);*/
        KoalaAttribute.INSTANCE.logger.info("onHeld");
    }
}
