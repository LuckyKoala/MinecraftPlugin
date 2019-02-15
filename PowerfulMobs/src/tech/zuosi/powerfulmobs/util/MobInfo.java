package tech.zuosi.powerfulmobs.util;

import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/4/26.
 */
public class MobInfo {
    public static Map<Entity,MobLevel> mobLevelMap = new HashMap<>();
    public void putMobInfo(Entity entity,MobLevel mobLevel) {
        mobLevelMap.put(entity,mobLevel);
    }
    public MobLevel getMobInfo(Entity entity) {
        if (!mobLevelMap.containsKey(entity)) return MobLevel.EASY;
        return mobLevelMap.get(entity);
    }
}
