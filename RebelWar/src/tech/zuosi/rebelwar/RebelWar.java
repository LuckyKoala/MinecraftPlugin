package tech.zuosi.rebelwar;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tech.zuosi.rebelwar.command.SetupCommandsHandler;
import tech.zuosi.rebelwar.data.ArenaLoader;
import tech.zuosi.rebelwar.data.ConfigLoader;
import tech.zuosi.rebelwar.data.RoleLoader;
import tech.zuosi.rebelwar.game.object.Arena;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.game.object.GameStat;
import tech.zuosi.rebelwar.handler.PlayerInteractHandler;
import tech.zuosi.rebelwar.handler.PlayerModerateHandler;
import tech.zuosi.rebelwar.handler.listener.GameListener;

/**
 * Created by iwar on 2016/9/29.
 */
public class RebelWar extends JavaPlugin {
    private static RebelWar INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE=this;
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getCommand("rebel").setExecutor(new SetupCommandsHandler());

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PlayerInteractHandler(),this);
        pm.registerEvents(new PlayerModerateHandler(),this);
        pm.registerEvents(new GameListener(),this);

        ConfigurationSerialization.registerClass(Arena.class);
        ConfigurationSerialization.registerClass(GamePlayer.class);
        ConfigurationSerialization.registerClass(GameStat.class);

        this.saveDefaultConfig();
        this.reloadConfig();
        new ConfigLoader().read();
        new RoleLoader().readFromDisk();
        new ArenaLoader().readFromDisk();
        this.getLogger().info("反贼战争插件加载完毕!");
    }

    @Override
    public void onDisable() {
        new RoleLoader().saveToDisk();
        new ArenaLoader().saveToDisk();
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        this.getLogger().info("反贼战争插件卸载完毕!");
    }

    public static RebelWar getINSTANCE() {
        return INSTANCE;
    }
}
