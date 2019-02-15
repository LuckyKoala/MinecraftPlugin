package tech.zuosi.minecraft.koalavip.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tech.zuosi.minecraft.koalavip.Core;

/**
 * Created by luckykoala on 18-3-29.
 */
public class PlayerJoinOrLeaveEventHandler implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Core.getInstance().getDatabaseManager().get(event.getPlayer().getName());
    }

    //退出时清除相应缓存，避免在多服务端同时访问
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Core.getInstance().getDatabaseManager().getEngine().unloadUser(event.getPlayer().getName());
    }
}
