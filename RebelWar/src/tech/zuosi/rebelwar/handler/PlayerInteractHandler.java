package tech.zuosi.rebelwar.handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import tech.zuosi.rebelwar.game.manager.PlayerManager;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.handler.event.ChestOpenEvent;
import tech.zuosi.rebelwar.handler.event.KeyPlaceEvent;
import tech.zuosi.rebelwar.message.MessageSender;

/**
 * Created by iwar on 2016/10/1.
 */
public class PlayerInteractHandler implements Listener {
    private static Location down,up;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            Location location = event.getClickedBlock().getLocation();
            Material type = event.getClickedBlock().getType();
            GamePlayer gamePlayer = PlayerManager.getInstance().getGamePlayer(event.getPlayer().getName());
            MessageSender messageSender = MessageSender.getINSTANCE();
            boolean isInStartedGame = gamePlayer.getCurrentGame()!=null;

            if (isInStartedGame && Material.ENCHANTMENT_TABLE == type) {
                KeyPlaceEvent keyPlaceEvent = new KeyPlaceEvent(gamePlayer,location);
                Bukkit.getServer().getPluginManager().callEvent(keyPlaceEvent);
                if (keyPlaceEvent.isCancelled()) event.setCancelled(true);
            } else if (isInStartedGame && Material.CHEST == type) {
                ChestOpenEvent chestOpenEvent = new ChestOpenEvent(gamePlayer,location);
                Bukkit.getServer().getPluginManager().callEvent(chestOpenEvent);
                if (chestOpenEvent.isCancelled()) event.setCancelled(true);
            } else {
                //ѡ��λ��
                String playerName = gamePlayer.getPlayerName();
                if (!event.hasItem() || Material.WOOD_AXE != event.getItem().getType() || !event.hasBlock()) return;
                if (Action.LEFT_CLICK_BLOCK == event.getAction()) {
                    //������С��
                    down = event.getClickedBlock().getLocation();
                    messageSender.echo(gamePlayer,
                            ChatColor.GOLD+"�ɹ�����Aλ�꣬����ľ���Ҽ�����Bλ�꣬Aλ��Ҳ���ǵ����ص��Ĭ�ϲ���ֵ");
                } else if (Action.RIGHT_CLICK_BLOCK == event.getAction()) {
                    //��������
                    up = event.getClickedBlock().getLocation();
                    messageSender.echo(gamePlayer, ChatColor.GOLD+"�ɹ�����Bλ�꣬����ľ���������Aλ��");
                }
            }
        }
    }

    public static Location getDown() {
        return down;
    }

    public static Location getUp() {
        return up;
    }
}
