package tech.zuosi.rebelwar.handler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import tech.zuosi.rebelwar.game.manager.PlayerManager;
import tech.zuosi.rebelwar.game.object.Game;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.message.MessageSender;

/**
 * Created by iwar on 2016/10/9.
 */
public class PlayerModerateHandler implements Listener {


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = PlayerManager.getInstance().getGamePlayer(player.getName());
        Game game = gamePlayer.getCurrentGame();
        if (null != game) {
            if (!game.getCurrentManager().boundCheck(gamePlayer)) {
                event.setTo(event.getFrom());
                MessageSender.getINSTANCE().echo(gamePlayer, ChatColor.RED+"游戏未结束，无法直接离开游戏场地");
            }
        }
    }
}
