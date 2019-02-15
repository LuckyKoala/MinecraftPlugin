package tech.zuosi.powerfulmobs.listener.fight.util;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by iwar on 2016/4/28.
 */
public interface PowerMob {
    boolean validateType(Entity monster, Entity player);
    void addBuff(EntityDamageByEntityEvent var1);
}
