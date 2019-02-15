package tech.zuosi.koalaattribute;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tech.zuosi.koalaattribute.data.AttributeFile;
import tech.zuosi.koalaattribute.event.*;
import tech.zuosi.koalaattribute.tool.CommandHandler;

/**
 * Created by iwar on 2016/8/10.
 */
public class KoalaAttribute extends JavaPlugin {
    //TODO 完善单例模式
    private PluginManager pm;
    private AttributeFile attributeFile;
    public static KoalaAttribute INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        attributeFile = new AttributeFile(this);
        getCommand("ka").setExecutor(new CommandHandler());
        pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerItemHeld(),this);
        pm.registerEvents(new PlayerJoin(),this);
        pm.registerEvents(new EntityAttack(),this);
        pm.registerEvents(new EntityDeath(),this);
        pm.registerEvents(new PlayerAttack(this),this);
        pm.registerEvents(new PlayerMoveAndClose(),this);
        this.saveDefaultConfig();
        attributeFile.readAll();
        getLogger().info("load.");
    }

    @Override
    public void onDisable() {
        attributeFile = new AttributeFile(this);
        attributeFile.writeAll();
        HandlerList.unregisterAll(this);
        getLogger().info("unload.");
    }
}
