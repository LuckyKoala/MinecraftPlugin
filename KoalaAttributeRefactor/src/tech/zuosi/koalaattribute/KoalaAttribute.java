package tech.zuosi.koalaattribute;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tech.zuosi.koalaattribute.data.AttributeFile;
import tech.zuosi.koalaattribute.event.*;
import tech.zuosi.koalaattribute.tool.CommandHandler;
import tech.zuosi.koalaattribute.tool.HealthAndSpeed;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * Created by iwar on 2016/8/10.
 */
public class KoalaAttribute extends JavaPlugin {
    private PluginManager pm;
    private AttributeFile attributeFile;
    public static KoalaAttribute INSTANCE;
    public Logger logger;

    @Override
    public void onEnable() {
        INSTANCE = this;
        attributeFile = new AttributeFile(this);
        logger = getLogger();

        getCommand("ka").setExecutor(new CommandHandler());

        pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoin(),this);
        pm.registerEvents(new EntityAttack(),this);
        pm.registerEvents(new EntityDeath(),this);
        pm.registerEvents(new PlayerAttack(this),this);
        //pm.registerEvents(new PlayerItemHeld(),this);
        //pm.registerEvents(new PlayerMoveAndClose(),this);

        this.saveDefaultConfig();
        attributeFile.readAll();
        logger.info("load.");

        int ticks_per_second = 20;
        long minute_ticks = 60 * ticks_per_second;
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for(Player player : Bukkit.getOnlinePlayers()) {
                HealthAndSpeed.update(player);
            }
        }, minute_ticks, minute_ticks);
    }

    @Override
    public void onDisable() {
        attributeFile = new AttributeFile(this);
        attributeFile.writeAll();
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        logger.info("unload.");
    }
}
