package tech.zuosi.koalaitem;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import tech.zuosi.koalaitem.handler.KICommand;
import tech.zuosi.koalaitem.handler.ListenerRegister;

/**
 * Created by iwar on 2016/7/16.
 */
public class KoalaItem extends JavaPlugin {
    public static KoalaItem INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.saveDefaultConfig();
        getLogger().info("KoalaItem has been loaded.");
        getCommand("ki").setExecutor(new KICommand());
        new ListenerRegister();
    }

    @Override
    public void onDisable() {
        getLogger().info("KoalaItem has been unloaded.");
        HandlerList.unregisterAll(INSTANCE);
    }
}
