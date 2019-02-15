package tech.zuosi.powerfulmobs.listener.trigger;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import tech.zuosi.powerfulmobs.PowerfulMobs;
import tech.zuosi.powerfulmobs.listener.fight.util.PowerMob;
import tech.zuosi.powerfulmobs.util.MobInfo;

/**
 * Created by iwar on 2016/4/26.
 */
public class BoomListener implements Listener,PowerMob {
    @Override
    @EventHandler
    public void addBuff(EntityDamageByEntityEvent var1) {
        //
    }
    @Override
    public boolean validateType(Entity monster, Entity player) {
        return monster.getType() == EntityType.CREEPER;
    }
    @EventHandler
    public void onBoom(EntityExplodeEvent var2) {
        if (PowerfulMobs.DEBUG)
            System.out.println("EntityExplodeEvent activate.");
        Entity bomb = var2.getEntity();
        if (!validateType(bomb,null)) return;
        int mobLevel = new MobInfo().getMobInfo(bomb).ordinal();
        float power = 1.0F;
        switch (mobLevel) {
            case 1:
                power *= 2;
                break;
            case 2:
                power *= 4;
                break;
            case 3:
                power *= 8;
                break;
            case 4:
                power *= 14;
                break;
            case 5:
                power *= 20;
                break;
            case 6:
                power *= 26;
                break;
            default:
                break;
        }
        var2.getLocation().getWorld().createExplosion(var2.getLocation(),power);
        if (PowerfulMobs.DEBUG)
            System.out.println("createExplosion activate.");
    }
}
