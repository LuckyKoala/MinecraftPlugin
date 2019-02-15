package tech.zuosi.rebelwar.handler.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.zuosi.rebelwar.game.manager.PlayerManager;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.handler.event.ChestOpenEvent;
import tech.zuosi.rebelwar.handler.event.KeyPlaceEvent;
import tech.zuosi.rebelwar.message.MessageSender;
import tech.zuosi.rebelwar.util.ScoreBoardUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2016/10/6.
 */
public class GameListener implements Listener {
    private ItemStack keyItem = new ItemStack(Material.EMERALD,1);
    private static ScoreBoardUtil sbUtil = new ScoreBoardUtil();

    public GameListener() {

    }

    private ItemStack writeIM(GamePlayer gamePlayer) {
        ItemStack key = keyItem.clone();
        ItemMeta keyIM = key.getItemMeta();
        keyIM.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&7[&6钥匙&7]"));
        List<String> keyLore = new ArrayList<>();
        keyLore.add(ChatColor.translateAlternateColorCodes('&',"&a放到中间的附魔台中去"));
        keyLore.add(ChatColor.translateAlternateColorCodes('&',"&e钥匙被发现数量，&b{钥匙数量} &e个"
                .replace("{钥匙数量}",String.valueOf(gamePlayer.getCurrentGame().getCurrentManager().getKeyNum()))));
        return key;
    }

    @EventHandler
    public void chestOpen(ChestOpenEvent event) {
        if (GamePlayer.Status.PLAYER == event.getGamePlayer().getStatus()) {
            event.getLocation().getBlock().setType(Material.AIR);
            event.getGamePlayer().getPlayer().getInventory().addItem(writeIM(event.getGamePlayer()));
            MessageSender.getINSTANCE().echo(event.getGamePlayer(),ChatColor.GOLD+"发现一把钥匙！");
        }
        event.setCancelled(true);
    }

    /*@EventHandler
    public void deathBattleBegin(DeathBattleBeginEvent event) {

    }*/

    @EventHandler
    public void keyPlace(KeyPlaceEvent event) {
        GamePlayer gamePlayer = event.getGamePlayer();
        if (GamePlayer.Status.PLAYER == event.getGamePlayer().getStatus()) {
            event.getLocation().getBlock().setType(Material.AIR);
            gamePlayer.getPlayer().getInventory().remove(keyItem);
            gamePlayer.getCurrentGame().getCurrentManager().addKey();
            MessageSender.getINSTANCE().broadcastKeyPlace(gamePlayer.getCurrentGame().getGamePlayerSet());
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        sbUtil.show(PlayerManager.getInstance().getGamePlayer(event.getPlayer().getName()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = PlayerManager.getInstance().getGamePlayer(player.getName());
        gamePlayer.getCurrentGame().getCurrentManager().quit(gamePlayer);
        gamePlayer.setStatus(GamePlayer.Status.NONE);
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (null==killer) return;
        GamePlayer gamePlayer = PlayerManager.getInstance().getGamePlayer(player.getName());
        GamePlayer gamePlayerKiller = PlayerManager.getInstance().getGamePlayer(killer.getName());

        if (GamePlayer.Status.PLAYER == gamePlayer.getStatus()) {
            gamePlayer.getCurrentGame().getCurrentManager().delPlayer();
            gamePlayer.setStatus(GamePlayer.Status.DEAD);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gamemode 3 "+gamePlayer.getPlayerName());
            if (GamePlayer.Status.REBEL == gamePlayerKiller.getStatus()) {
                gamePlayerKiller.getGameStat().addKill(1);
            }
        } else if (GamePlayer.Status.REBEL == gamePlayer.getStatus()) {
            gamePlayer.getCurrentGame().getCurrentManager().delRebel();
            gamePlayer.setStatus(GamePlayer.Status.DEAD);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gamemode 3 "+gamePlayer.getPlayerName());
            if (GamePlayer.Status.PLAYER == gamePlayerKiller.getStatus()) {
                gamePlayerKiller.getGameStat().addKill(1);
            }
        }
    }
}
