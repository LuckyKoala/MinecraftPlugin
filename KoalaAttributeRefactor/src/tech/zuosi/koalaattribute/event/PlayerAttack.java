package tech.zuosi.koalaattribute.event;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import tech.zuosi.koalaattribute.KoalaAttribute;
import tech.zuosi.koalaattribute.attribute.Attribute;
import tech.zuosi.koalaattribute.attribute.AttributeUtil;
import tech.zuosi.koalaattribute.data.AttributeCache;

import java.util.Map;
import java.util.Random;

/**
 * Created by iwar on 2016/8/11.
 */
public class PlayerAttack implements Listener {
    private KoalaAttribute plugin;

    public PlayerAttack(KoalaAttribute plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAttack(EntityDamageByEntityEvent event) {
        Player damager;
        if (!(event.getDamager() instanceof Player)) {
            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    damager = (Player) arrow.getShooter();
                } else {
                    damager = null;
                }
            } else {
                damager = null;
            }
        } else {
            damager = (Player) event.getDamager();
        }
        if (damager == null) return;
        Attribute attribute = AttributeCache.INSTANCE.getAttribute(damager.getName());
        Map<String,Double> essAttribute = AttributeUtil.convertToEssAttribute(attribute);

        //…À∫¶ ±©ª˜ Œ¸—™
        double damage = event.getDamage();
        damage += essAttribute.get("damage");
        if (new Random().nextDouble() <= essAttribute.get("critical")) {
            damage *= plugin.getConfig().getDouble("Critical",2.0D);
        }
        if (new Random().nextDouble() <= essAttribute.get("blood")) {
            double health = damager.getHealth() + damage*plugin.getConfig().getDouble("Blood",0.5D);
            health = health > damager.getMaxHealth() ? damager.getMaxHealth() : health;
            damager.setHealth(health);
        }

        //damager.sendMessage(ChatColor.GOLD + "[KoalaAttribute] " + ChatColor.GREEN + "≥ı º…À∫¶[" + event.getDamage() + "]");
        //damager.sendMessage(ChatColor.GOLD + "[KoalaAttribute] " + ChatColor.GREEN + "◊Ó÷’…À∫¶[" + damage + "]");
        event.setDamage(damage);
    }
}
