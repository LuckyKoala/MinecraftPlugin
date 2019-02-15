package tech.zuosi.powerfulmobs.listener.fight.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import tech.zuosi.powerfulmobs.util.MobInfo;
import tech.zuosi.powerfulmobs.util.Validate;

/**
 * Created by iwar on 2016/5/14.
 */
public class DistanceUtil {
    public static double getMaxEffectiveDistance(Entity entity) {
        int mobLevel;
        double distance;
        if (!isMeleeType(entity)) {
            return 0;
        }
        mobLevel = new MobInfo().getMobInfo(entity).ordinal();
        System.out.println(mobLevel + "---");
        if (mobLevel >= 1 && mobLevel <= 6) {
            distance = mobLevel*3;
        } else {
            distance = 0;
        }
        return distance;
    }
    public static boolean isMeleeType(Entity entity) {
        EntityType type = entity.getType();
        if (!Validate.isMonster(type)) {
            return false;
        }
        int mobLevel = new MobInfo().getMobInfo(entity).ordinal();
        if (type == EntityType.ZOMBIE && mobLevel >= 5) {
            return false;
        }
        if (type == EntityType.SKELETON || type == EntityType.CREEPER) {
            return false;
        }
        if (type == EntityType.GUARDIAN) {
            return false;
        }
        return true;
    }
}
