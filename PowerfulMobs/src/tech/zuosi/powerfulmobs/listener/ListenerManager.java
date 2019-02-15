package tech.zuosi.powerfulmobs.listener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import tech.zuosi.powerfulmobs.PowerfulMobs;
import tech.zuosi.powerfulmobs.listener.fight.*;
import tech.zuosi.powerfulmobs.listener.fight.util.ShowFightInfo;
import tech.zuosi.powerfulmobs.listener.trigger.BoomListener;
import tech.zuosi.powerfulmobs.listener.trigger.DeathListener;
import tech.zuosi.powerfulmobs.listener.trigger.SpawnListener;

/**
 * Created by iwar on 2016/5/5.
 */
public class ListenerManager {
    private PowerfulMobs plugin;
    private PluginManager pm;
    public ListenerManager(PowerfulMobs plugin) {
        this.plugin = plugin;
        this.pm = plugin.getServer().getPluginManager();
    }
    void register(Listener listener) {
        pm.registerEvents(listener,plugin);
    }
    public void registerAllListener() {
        register(new PMZombie());
        register(new PMSkeleton());
        register(new PMPigZombie());
        register(new PMOtherMobs());
        register(new PMSpider(plugin));
        register(new BoomListener());
        register(new ShowFightInfo());
        register(new SpawnListener(plugin));
        register(new DeathListener(plugin));
        register(new PMDistanceAttack(plugin));

    }
}
