package tech.zuosi.powerfulmobs.listener.fight;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.powerfulmobs.PowerfulMobs;
import tech.zuosi.powerfulmobs.listener.fight.util.PowerMob;
import tech.zuosi.powerfulmobs.util.MobInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/4/28.
 */
public class PMSkeleton implements Listener,PowerMob {
    Map<Skeleton,Integer> knockBackStrength = new HashMap<>();
    Map<Arrow,Skeleton> shooterList = new HashMap<>();
    @Override
    public boolean validateType(Entity monster, Entity player) {
        if (player.getType() != EntityType.PLAYER) return false;
        if (monster.getType() == EntityType.ARROW) {
            Arrow arrow = (Arrow) monster;
            if (shooterList.containsKey(arrow))
                return true;
        }
        return false;
    }
    @EventHandler
    public void knockBack(EntityShootBowEvent var0) {
        if (PowerfulMobs.DEBUG)
            System.out.println("EntityShootBowEvent activate.");
        if (var0.getProjectile().getType() != EntityType.ARROW) return;
        Arrow arrow = (Arrow) var0.getProjectile();
        if (var0.getEntityType() == EntityType.SKELETON) {
            Skeleton skeleton = (Skeleton) var0.getEntity();
            if (PowerfulMobs.DEBUG)
                System.out.println("When shootBow"+new MobInfo().getMobInfo(skeleton).ordinal());
            shooterList.put(arrow,skeleton);
            if (knockBackStrength.containsKey(skeleton)) {
                arrow.setKnockbackStrength(knockBackStrength.get(skeleton));
                if (PowerfulMobs.DEBUG)
                    System.out.println("Knockback activate:" + knockBackStrength.get(skeleton));
            }
        }
    }
    @EventHandler
    public void addBuff(EntityDamageByEntityEvent var1) {
        if (PowerfulMobs.DEBUG)
            System.out.println("EntityDamageByEntityEvent activate.");
        Entity damager = var1.getDamager();
        Entity defender = var1.getEntity();
        if (!validateType(damager,defender)) return;
        if (PowerfulMobs.DEBUG)
            System.out.println("After validateType");
        Arrow arrow = (Arrow) damager;
        Skeleton skeleton = shooterList.get(arrow);
        int mobLevel = new MobInfo().getMobInfo(skeleton).ordinal();
        if (PowerfulMobs.DEBUG)
            System.out.println(mobLevel);
        Player player = (Player) defender;
        int onFireTicks;
        if (mobLevel == 1) {
            knockBackStrength.put(skeleton,1);
        }
        if (mobLevel >= 2 && mobLevel < 6) {
            knockBackStrength.put(skeleton,2);
            onFireTicks = 60;
            if (mobLevel >= 3) {
                onFireTicks = 200;
                if (mobLevel >= 4) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,400,0));
                    if (mobLevel == 5) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,400,0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100,0));
                    }
                }
            }
            player.setFireTicks(onFireTicks);
        }
        if (mobLevel == 6) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,100,0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,100,0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,100,0));
        }
    }
    @EventHandler
    public void increaseDamage(EntityDamageByEntityEvent var2) {
        if (PowerfulMobs.DEBUG)
            System.out.println("increaseDamage activate.");
        Entity damager = var2.getDamager();
        Entity defender = var2.getEntity();
        if (!validateType(damager,defender)) return;
        Arrow arrow = (Arrow) damager;
        Skeleton skeleton = shooterList.get(arrow);
        int mobLevel = new MobInfo().getMobInfo(skeleton).ordinal();
        double damage = var2.getDamage();
        switch (mobLevel) {
            case 0:
                break;
            case 1:
                damage *= 1.5;
                break;
            case 2:
                damage *= 3;
                break;
            case 3:
                damage *= 4.5;
                break;
            case 4:
                damage *= 6;
                break;
            case 5:
                damage *= 7.5;
                break;
            case 6:
                damage *= 9;
                break;
            default:
                break;
        }
        var2.setDamage(damage);
    }
}
