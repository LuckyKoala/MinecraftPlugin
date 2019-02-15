package tech.zuosi.powerfulmobs.listener.fight;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import tech.zuosi.powerfulmobs.util.MobInfo;
import tech.zuosi.powerfulmobs.util.Validate;

/**
 * Created by iwar on 2016/5/17.
 */
public class PMOtherMobs implements Listener {
    final static double other1Ratio = 1.1;
    final static double other2Ratio = 1.3;
    final static double other3Ratio = 1.5;
    final static double other4Ratio = 1.5;
    final static double other5Ratio = 1.7;
    final static double other6Ratio = 2.0;

    public boolean validateType(Entity monster,Entity player) {
        if (monster == null || player == null) return false;
        if (!Validate.isMonster(monster.getType())) return false;
        switch (monster.getType()) {
            case ZOMBIE:
            case SPIDER:
            case PIG_ZOMBIE:
            case SKELETON:
            case CREEPER:
                return false;
        }
        return player.getType() == EntityType.PLAYER;
    }

    @EventHandler
    public void increaseDamage(EntityDamageByEntityEvent var1) {
        Entity damager = var1.getDamager();
        Entity defender = var1.getEntity();
        if (!validateType(damager,defender)) return;
        int mobLevel = new MobInfo().getMobInfo(damager).ordinal();
        double damage = var1.getDamage();
        switch (mobLevel) {
            case 1:
                damage *= other1Ratio;
                break;
            case 2:
                damage *= other2Ratio;
                break;
            case 3:
                damage *= other3Ratio;
                break;
            case 4:
                damage *= other4Ratio;
                break;
            case 5:
                damage *= other5Ratio;
                break;
            case 6:
                damage *= other6Ratio;
                break;
            default:
                break;
        }
        var1.setDamage(damage);
    }
}
