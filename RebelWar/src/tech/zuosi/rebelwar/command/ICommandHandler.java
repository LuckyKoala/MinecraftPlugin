package tech.zuosi.rebelwar.command;

import tech.zuosi.rebelwar.game.object.GamePlayer;

/**
 * Created by iwar on 2016/10/1.
 */
public interface ICommandHandler {
    boolean handle(GamePlayer gamePlayer, String args[]);
}
