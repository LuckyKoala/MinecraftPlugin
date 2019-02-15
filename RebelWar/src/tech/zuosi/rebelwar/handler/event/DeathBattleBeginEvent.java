package tech.zuosi.rebelwar.handler.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import tech.zuosi.rebelwar.game.manager.GameManager;

/**
 * Created by iwar on 2016/10/1.
 */
public class DeathBattleBeginEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private GameManager gameManager;

    public DeathBattleBeginEvent(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
