package net.myplugin.visualintensify.menu;

import net.myplugin.visualintensify.VisualIntensify;
import org.bukkit.plugin.PluginManager;

/**
 * Created by iwar on 2016/7/15.
 */
public class MenuRegister {
    public MenuRegister(VisualIntensify plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new MainMenu(plugin),plugin);
        pm.registerEvents(new IntensifyMenu(plugin),plugin);
        pm.registerEvents(new GemMenu(plugin),plugin);
        pm.registerEvents(new RebornMenu(plugin),plugin);
        pm.registerEvents(new RemoveMenu(plugin),plugin);
    }
}
