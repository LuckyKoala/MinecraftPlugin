package tech.zuosi.minecraft.parkour.util.tellraw;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by iwar on 2016/9/6.
 */
class TellRawCaller implements Listener {

    public void onInit(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
    }
}
