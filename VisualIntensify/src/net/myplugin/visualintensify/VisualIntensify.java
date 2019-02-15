package net.myplugin.visualintensify;

import net.myplugin.visualintensify.menu.MenuRegister;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by iwar on 2015/12/6.
 */
public class VisualIntensify extends JavaPlugin {
    //²å¼þÖ÷Àà

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getLogger().info("VisualIntensify has been loaded.");
        getLogger().info("[VI-VERSION]" + getDescription().getVersion());
        this.getCommand("vi").setExecutor(new CommandExecutor(this));
        new MenuRegister(this);
        getServer().getPluginManager().registerEvents(new EventListener(this),this);
    }

    @Override
    public  void onDisable(){
        this.saveConfig();
        getLogger().info("VisualIntensify has been unloaded.");
        HandlerList.unregisterAll(this);
    }


}
