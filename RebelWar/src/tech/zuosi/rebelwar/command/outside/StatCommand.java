package tech.zuosi.rebelwar.command.outside;

import org.bukkit.ChatColor;
import tech.zuosi.rebelwar.command.ICommandHandler;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.game.object.GameStat;
import tech.zuosi.rebelwar.message.MessageSender;

/**
 * Created by iwar on 2016/10/1.
 */
public class StatCommand implements ICommandHandler {

    @Override
    public boolean handle(GamePlayer gamePlayer, String args[]) {
        GameStat gameStat = gamePlayer.getGameStat();
        MessageSender.getINSTANCE().echo(gamePlayer,new String[]{
                ChatColor.GRAY+"历史击杀数 -> "+ChatColor.RED+gameStat.getKillCount(),
                ChatColor.GRAY+"游戏币余额 -> "+ChatColor.RED+gameStat.getCoinCount()
        });
        return true;
    }
}
