package tech.zuosi.powerfulmobs.listener.fight;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.powerfulmobs.PowerfulMobs;
import tech.zuosi.powerfulmobs.listener.fight.util.PowerMob;
import tech.zuosi.powerfulmobs.util.MobInfo;

/**
 * Created by iwar on 2016/4/28.
 */
public class PMPigZombie implements Listener,PowerMob {
    @Override
    public boolean validateType(Entity monster, Entity player) {
        if (monster == null || player == null) return false;
        return monster.getType() == EntityType.PIG_ZOMBIE && player.getType() == EntityType.PLAYER;
    }
    @Override
    @EventHandler
    public void addBuff(EntityDamageByEntityEvent var1) {
        Entity damager = var1.getDamager();
        Entity defender = var1.getEntity();
        if (!validateType(damager,defender)) return;
        int mobLevel = new MobInfo().getMobInfo(damager).ordinal();
        Player player = (Player) defender;
        if (mobLevel == 2) {
            player.setFireTicks(100);
        } else if (mobLevel >= 3) {
            player.setFireTicks(300);
            if (mobLevel >= 4) {
                player.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS,200,0)));
            }
            if (mobLevel < 5) return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,1200,0));
            if (mobLevel == 6) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,100,0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,100,0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,100,0));
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
            case 0:
                break;
            case 1:
                damage *= PMOtherMobs.other1Ratio;
                break;
            case 2:
                damage *= PMOtherMobs.other2Ratio;
                break;
            case 3:
                damage *= PMOtherMobs.other3Ratio;
                break;
            case 4:
                damage *= PMOtherMobs.other4Ratio;
                break;
            case 5:
                damage *= PMOtherMobs.other5Ratio;
                break;
            case 6:
                damage *= PMOtherMobs.other6Ratio;
                break;
            default:
                break;
        }
        var2.setDamage(damage);
    }
    @EventHandler
    public void whenTarget(EntityTargetLivingEntityEvent var3) {
        if (PowerfulMobs.DEBUG)
            System.out.println("Entity:" + var3.getEntity() + " | Reson:" + var3.getReason().name());
        Entity entity = var3.getEntity();
        LivingEntity target = var3.getTarget();
        if (!validateType(entity,target)) return;
        int mobLevel = new MobInfo().getMobInfo(entity).ordinal();
        if (mobLevel >= 1) {
            PigZombie pigZombie = (PigZombie)entity;
            pigZombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20,1));
        }
    }
}
