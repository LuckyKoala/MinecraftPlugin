package tech.zuosi.rebelwar.handler.event;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import tech.zuosi.rebelwar.game.object.GamePlayer;

/**
 * Created by iwar on 2016/10/1.
 */
public class ChestOpenEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private GamePlayer gamePlayer;
    private Location location;
    private boolean cancelled;

    public ChestOpenEvent(GamePlayer gamePlayer,Location location) {
        this.gamePlayer = gamePlayer;
        this.location = location;
    }

    public GamePlayer getGamePlayer() {
        return this.gamePlayer;
    }

    public Location getLocation() {
        return this.location;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
