package tech.zuosi.minecraft.parkour;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.PluginNameConversationPrefix;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tech.zuosi.minecraft.parkour.database.DataManager;
import tech.zuosi.minecraft.parkour.game.*;
import tech.zuosi.minecraft.parkour.game.format.DifficultyFormat;
import tech.zuosi.minecraft.parkour.game.format.FormatEntity;
import tech.zuosi.minecraft.parkour.game.format.SceneFormat;
import tech.zuosi.minecraft.parkour.handler.command.TopCommandExecutor;
import tech.zuosi.minecraft.parkour.handler.listener.InGameListener;
import tech.zuosi.minecraft.parkour.handler.listener.PaneListener;
import tech.zuosi.minecraft.parkour.handler.listener.SelectionListener;
import tech.zuosi.minecraft.parkour.selection.SelectionManager;
import tech.zuosi.minecraft.parkour.ui.PaneManager;
import tech.zuosi.minecraft.parkour.util.PlaceHolders;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by LuckyKoala on 18-9-16. 62 hits
 */
public class Core extends JavaPlugin {
    @Getter
    private static Core instance;
    public DataManager dataManager;
    public GameManager gameManager;
    public PaneManager paneManager;
    public SelectionManager selectionManager;
    public ConversationFactory conversationFactory;

    private final Tick tick = new Tick();

    @Override
    public void onEnable() {
        super.onEnable();
        this.saveDefaultConfig();
        instance = this;
        registerSerializableClass();
        parseConfig();
        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            boolean success = new PlaceHolders().register();
            if(!success) getLogger().warning(() -> "Can't register placeholders !");
        }
        this.getCommand("po").setExecutor(new TopCommandExecutor());
        registerListener();
        conversationFactory = new ConversationFactory(this);
        conversationFactory.thatExcludesNonPlayersWithMessage("Player only")
                .withPrefix(new PluginNameConversationPrefix(this));
        selectionManager = new SelectionManager();
        tick.start();
        getLogger().info("ParkOur has been loaded.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        tick.stop();
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
        ConfigurationSerialization.unregisterClass(FormatEntity.class);
        ConfigurationSerialization.unregisterClass(SceneFormat.class);
        ConfigurationSerialization.unregisterClass(DifficultyFormat.class);
        ConfigurationSerialization.unregisterClass(GameRegion.class);
        ConfigurationSerialization.unregisterClass(GameMap.class);
        gameManager.removeAllPlayerFromGame();
        dataManager.close();
        getLogger().info("ParkOur has been unloaded.");
    }

    private void registerListener() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PaneListener(), this);
        pluginManager.registerEvents(new InGameListener(), this);
        pluginManager.registerEvents(new SelectionListener(), this);
    }

    private void registerSerializableClass() {
        ConfigurationSerialization.registerClass(FormatEntity.class);
        ConfigurationSerialization.registerClass(SceneFormat.class);
        ConfigurationSerialization.registerClass(DifficultyFormat.class);
        ConfigurationSerialization.registerClass(GameRegion.class);
        ConfigurationSerialization.registerClass(GameMap.class);
    }

    private void parseConfig() {
        FileConfiguration config = getConfig();
        //Load database config and init DataManager
        ConfigurationSection data = config.getConfigurationSection("Database");
        this.dataManager = new DataManager();
        boolean initSuccess = dataManager.init(
                data.getString("username"), data.getString("password"),
                data.getString("host"), data.getString("database"),
                data.getInt("port"), data.getBoolean("setup"));
        if(!initSuccess) {
            getLogger().warning(() -> "Wrong database configuration, please check it");
            return;
        }
        //Load menu format
        ConfigurationSection format = config.getConfigurationSection("Format");
        Map<String, DifficultyFormat> formatMap;
        if(format!=null) {
            Set<String> paths = format.getKeys(false);
            formatMap = new HashMap<>(paths.size());
            for(String path : paths) {
                DifficultyFormat difficultyFormat = (DifficultyFormat) format.get(path);
                formatMap.put(path, difficultyFormat);
            }
        } else {
            formatMap = new HashMap<>();
        }
        //Load game config and prepare GameManager
        ConfigurationSection map = config.getConfigurationSection("Map");
        if(map != null) {
            Set<String> paths = map.getKeys(false);
            Map<MapPath, GameMap> pathToMap = new HashMap<>(paths.size());
            for(String path : paths) {
                GameMap gameMap = (GameMap) map.get(path);
                pathToMap.put(MapPath.fromString(path), gameMap);
            }
            this.gameManager = new GameManager(pathToMap, formatMap);
        } else {
            this.gameManager = new GameManager(new HashMap<>(), formatMap);
        }
        //Init PaneManager
        this.paneManager = new PaneManager();
    }
}
