package tech.zuosi.koalaprefixbc;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by iwar on 2017/1/25.
 */
public class main extends Plugin {
    private static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
        BungeeCord.getInstance().getPluginManager().registerListener(this, new MessageListener());
        BungeeCord.getInstance().getPluginManager().registerListener(this, new ChatPrefixListener());
        BungeeCord.getInstance().registerChannel("Return");
        DataManager.loadAllPrefix();
    }

    @Override
    public void onDisable() {
        DataManager.saveAllCachedData();
    }

    public static Plugin getInstance() {
        return instance;
    }
}
