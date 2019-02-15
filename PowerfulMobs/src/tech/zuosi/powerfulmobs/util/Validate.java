package tech.zuosi.powerfulmobs.util;

import org.bukkit.entity.EntityType;

/**
 * Created by iwar on 2016/4/28.
 */
public class Validate {
    public static boolean isMonster(EntityType type) {
        return type == EntityType.BLAZE || type == EntityType.CAVE_SPIDER || type == EntityType.CREEPER
                || type == EntityType.ENDERMAN || type == EntityType.ENDERMITE || type == EntityType.GIANT
                || type == EntityType.GUARDIAN || type == EntityType.PIG_ZOMBIE || type == EntityType.SILVERFISH
                || type == EntityType.SKELETON || type == EntityType.SPIDER || type == EntityType.ZOMBIE;
    }
}
