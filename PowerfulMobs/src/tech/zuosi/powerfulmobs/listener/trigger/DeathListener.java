package tech.zuosi.powerfulmobs.listener.trigger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.powerfulmobs.PowerfulMobs;
import tech.zuosi.powerfulmobs.util.MobInfo;

import java.util.Random;

/**
 * Created by iwar on 2016/5/5.
 */
public class DeathListener implements Listener {
    private PowerfulMobs plugin;

    public DeathListener(PowerfulMobs plugin) {
        this.plugin = plugin;
    }
    boolean roll(double chance) {
        return new Random().nextInt(100) < (int)chance*100;
    }
    @EventHandler
    public void onDeath(EntityDeathEvent var1) {
        EntityType entityType = var1.getEntityType();
        if (plugin.getConfig().contains(entityType.name().toLowerCase())) {
            if (PowerfulMobs.DEBUG)
                System.out.println("onDeath contain.");
            int level = new MobInfo().getMobInfo(var1.getEntity()).ordinal();
            //TODO 掉落物只能设定一次
            if (roll(plugin.getConfig().getDouble(entityType.name().toLowerCase()+"."+level+".chance"))) {
                ItemStack itemStack = plugin.getConfig().getItemStack(entityType.name().toLowerCase()+"."+level+".drop");
                if (itemStack == null || itemStack.getType() == Material.AIR) return;
                if (PowerfulMobs.DEBUG)
                    System.out.println("onDeath activate.");
                Location location = var1.getEntity().getLocation();
                location.getWorld().dropItem(location,itemStack);
            }
        }
        if (MobInfo.mobLevelMap.containsKey(var1.getEntity())) {
            MobInfo.mobLevelMap.remove(var1.getEntity(),MobInfo.mobLevelMap.get(var1.getEntity()));
            if (PowerfulMobs.DEBUG) {
                System.out.println("Remove compelete.");
            }
        }
    }
}
