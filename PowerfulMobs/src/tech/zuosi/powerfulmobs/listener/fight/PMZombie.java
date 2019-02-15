package tech.zuosi.powerfulmobs.listener.fight;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.powerfulmobs.listener.fight.util.PowerMob;
import tech.zuosi.powerfulmobs.util.MobInfo;

/**
 * Created by iwar on 2016/4/28.
 */
public class PMZombie implements Listener,PowerMob {
    final static double zombie1Ratio = 1.5;
    final static double zombie2Ratio = 2;
    final static double zombie3Ratio = 2.5;
    final static double zombie4Ratio = 3;
    final static double zombie5Ratio = 3.5;
    final static double zombie6Ratio = 4;

    @Override
    public boolean validateType(Entity monster,Entity player) {
        if (monster == null || player == null) return false;
        return monster.getType() == EntityType.ZOMBIE && player.getType() == EntityType.PLAYER;
    }

    @EventHandler
    public void whenTarget(EntityTargetLivingEntityEvent var1) {
        Entity entity = var1.getEntity();
        LivingEntity target = var1.getTarget();
        if (!validateType(entity,target)) return;
        int mobLevel = new MobInfo().getMobInfo(entity).ordinal();
        if (mobLevel >= 1) {
            Zombie zombie = (Zombie)entity;
            zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20,0));
            if (mobLevel >= 5) {
                target.teleport(zombie, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
    }

    @EventHandler
    public void increaseDamage(EntityDamageByEntityEvent var2) {
        Entity damager = var2.getDamager();
        Entity defender = var2.getEntity();
        if (!validateType(damager,defender)) return;
        int mobLevel = new MobInfo().getMobInfo(damager).ordinal();
        double damage = var2.getDamage();
        switch (mobLevel) {
            case 1:
                damage *= zombie1Ratio;
                break;
            case 2:
                damage *= zombie2Ratio;
                break;
            case 3:
                damage *= zombie3Ratio;
                break;
            case 4:
                damage *= zombie4Ratio;
                break;
            case 5:
                damage *= zombie5Ratio;
                break;
            case 6:
                damage *= zombie6Ratio;
                break;
            default:
                break;
        }
        var2.setDamage(damage);
    }

    @Override
    @EventHandler
    public void addBuff(EntityDamageByEntityEvent var3) {
        Entity damager = var3.getDamager();
        Entity defender = var3.getEntity();
        if (!validateType(damager,defender)) return;
        int mobLevel = new MobInfo().getMobInfo(damager).ordinal();
        if (mobLevel >= 2) {
            Player player = (Player)defender;
            player.setFireTicks(200);
            if (mobLevel >= 3) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,400,0));
                if (mobLevel >= 4) {
                    player.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS,400,0)));
                }
                if (mobLevel < 5) return;
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,1200,0));
                if (mobLevel == 6) {
                    Location c;
                    c = getPoint(player.getWorld(),damager.getLocation(),player.getLocation());
                    player.teleport(c);
                }
            }
        }
    }
    Location getPoint(World w,Location a, Location b) {
        double x,y,z;
        x = 2*b.getX()-a.getX();
        y = 2*b.getY()-a.getY();
        z = 2*b.getZ()-a.getZ();
        return new Location(w,x,y,z);
    }
}
