package tech.zuosi.powerfulmobs.listener.fight.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import tech.zuosi.powerfulmobs.util.ActionBar;
import tech.zuosi.powerfulmobs.util.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/5/9.
 */
public class ShowFightInfo implements Listener {
    Map<Arrow,Player> shooterList = new HashMap<>();
    public boolean validateType(Entity player,Entity monster) {
        if (monster == null || player == null) return false;
        return Validate.isMonster(monster.getType()) && player.getType() == EntityType.PLAYER;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent var) {
        Entity damager = var.getDamager();
        Entity defender = var.getEntity();
        double currentHealth;
        if (damager.getType() == EntityType.ARROW) {
            Arrow arrow = (Arrow) damager;
            if (Validate.isMonster(defender.getType())) {
                if (shooterList.containsKey(arrow)) {
                    Player player = shooterList.get(arrow);
                    Damageable damageable = (Damageable) defender;
                    currentHealth = damageable.getHealth()-var.getFinalDamage();
                    if (currentHealth > 0)
                        ActionBar.sendAction(player, ChatColor.GREEN + damageable.getCustomName() + "血量:" + currentHealth
                            + "/" + damageable.getMaxHealth());
                }
            }
        } else if (validateType(damager,defender)) {
            Player player = (Player) damager;
            Damageable damageable = (Damageable) defender;
            currentHealth = damageable.getHealth()-var.getFinalDamage();
            if (currentHealth > 0)
                ActionBar.sendAction(player, ChatColor.GREEN + damageable.getCustomName() + "血量:" + currentHealth
                    + "/" + damageable.getMaxHealth());
        }
    }

    @EventHandler
    public void onShot(EntityShootBowEvent var1) {
        if (var1.getProjectile().getType() != EntityType.ARROW) return;
        Arrow arrow = (Arrow) var1.getProjectile();
        if (var1.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) var1.getEntity();
            shooterList.put(arrow,player);
        }
    }
}
