package tech.zuosi.powerfulmobs.listener.trigger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import tech.zuosi.powerfulmobs.PowerfulMobs;
import tech.zuosi.powerfulmobs.util.DataManager;
import tech.zuosi.powerfulmobs.util.MobInfo;
import tech.zuosi.powerfulmobs.util.RandomLevel;
import tech.zuosi.powerfulmobs.util.Validate;

/**
 * Created by iwar on 2016/4/26.
 */
public class SpawnListener implements Listener {
    private PowerfulMobs plugin;
    MobInfo mobInfo = new MobInfo();

    public SpawnListener(PowerfulMobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent cse) {
        if (!Validate.isMonster(cse.getEntityType())) return;
        mobInfo.putMobInfo(cse.getEntity(),new RandomLevel().getResult());
        int mobLevel = mobInfo.getMobInfo(cse.getEntity()).ordinal();
        Damageable monster = cse.getEntity();
        DataManager dataManager = new DataManager(plugin);
        if (!dataManager.exists()) {
            dataManager.write();
        } else {
            dataManager.add(monster);
        }
        ChatColor customColor;
        String customNamePrefix;
        double maxHealth = monster.getMaxHealth();
        switch (mobLevel) {
            case 0:
                customColor = ChatColor.GRAY;
                break;
            case 1:
                customColor = ChatColor.GREEN;
                maxHealth *= 2.5;
                break;
            case 2:
                customColor = ChatColor.BLUE;
                maxHealth *= 5;
                break;
            case 3:
                customColor = ChatColor.DARK_BLUE;
                maxHealth *= 7.5;
                break;
            case 4:
                customColor = ChatColor.RED;
                maxHealth *= 10;
                break;
            case 5:
                customColor = ChatColor.YELLOW;
                maxHealth *= 17.5;
                break;
            case 6:
                customColor = ChatColor.DARK_PURPLE;
                maxHealth *= 25;
                break;
            default:
                customColor = ChatColor.GRAY;
                break;
        }
        customNamePrefix = customColor + "[" + mobInfo.getMobInfo(cse.getEntity()).getName() + "]";
        monster.setCustomNameVisible(true);
        monster.setCustomName(customNamePrefix + cse.getEntity().getType().name());
        monster.setMaxHealth(maxHealth);
        monster.setHealth(maxHealth);
    }
}
