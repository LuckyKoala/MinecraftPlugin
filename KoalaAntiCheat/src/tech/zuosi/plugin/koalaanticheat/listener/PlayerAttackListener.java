package tech.zuosi.plugin.koalaanticheat.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import tech.zuosi.plugin.koalaanticheat.KoalaAntiCheat;
import tech.zuosi.plugin.koalaanticheat.util.ActionBar;

import java.util.List;

/**
 * Created by iwar on 2016/10/5.
 */
public class PlayerAttackListener implements Listener {
    private static final String actionbarMessage = ChatColor
            .translateAlternateColorCodes('&',"&c&l请勿开启超远攻击距离外挂！");

    public PlayerAttackListener() {}

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        Entity entity = event.getEntity();
        KoalaAntiCheat plugin = KoalaAntiCheat.getINSTANCE();
        double limitDistance = plugin.getConfig().getDouble("AttackDistanceLimit",4D);
        double currentDistance = player.getLocation().distance(entity.getLocation());
        if (Double.compare(currentDistance,limitDistance) <= 0) return;

        List<String> kickMessage = plugin.getConfig().getStringList("KickMessage");
        String broadcastMessage = ChatColor.translateAlternateColorCodes('&',plugin.getConfig()
                .getString("BroadcastMessage","&b梦灵之都>>&a玩家{player}因涉嫌使用外挂，故踢出游戏，请大家自重！"))
                .replace("{player}",player.getName());
        StringBuilder builder = new StringBuilder();

        for (String str:kickMessage) {
            builder.append(ChatColor.translateAlternateColorCodes('&',str)).append("\n");
        }

        ActionBar.sendAction(player,actionbarMessage);
        Bukkit.broadcastMessage(broadcastMessage);

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                player.kickPlayer(builder.toString());
            }
        },30L);
    }
}
