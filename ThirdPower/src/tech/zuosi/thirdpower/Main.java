package tech.zuosi.thirdpower;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2017/7/20.
 */
public class Main extends JavaPlugin implements Listener {
    public static Map<String, Integer> map = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            event.setDamage(tryEnhance(damager, event.getDamage()));
        } else if(event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if(arrow.getShooter() instanceof Player) {
                Player damager = (Player)arrow.getShooter();
                event.setDamage(tryEnhance(damager, event.getDamage()));
            }
        }
    }

    double tryEnhance(Player player, double oldDamage) {
        String name = player.getName();
        int prevCount = map.getOrDefault(name, 0);
        int currentCount = prevCount+1;
        double finalDamage = oldDamage;

        if(currentCount >= 3) {
            map.put(name, 0);
            finalDamage+=2;
            player.sendMessage(ChatColor.GREEN + "已触发连击");
        } else if(currentCount < 0) {
            map.put(name, 0);
            //player.sendMessage(ChatColor.RED + "计数异常，已重置计数");
        } else {
            map.put(name, currentCount);
        }
        //System.out.println(String.format("oldDamage: %f, currentDamage: %f", oldDamage, finalDamage));

        return finalDamage;
    }
}
