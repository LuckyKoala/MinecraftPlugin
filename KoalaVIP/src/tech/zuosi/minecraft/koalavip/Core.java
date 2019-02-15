package tech.zuosi.minecraft.koalavip;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.tools.interfaces.Loader;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import tech.zuosi.minecraft.koalavip.cli.BasicCommand;
import tech.zuosi.minecraft.koalavip.database.DatabaseEngine;
import tech.zuosi.minecraft.koalavip.database.implementation.MySQLEngine;
import tech.zuosi.minecraft.koalavip.handler.PlayerJoinOrLeaveEventHandler;
import tech.zuosi.minecraft.koalavip.handler.PlayerPointsChangeEventHandler;
import tech.zuosi.minecraft.koalavip.handler.Tick;
import tech.zuosi.minecraft.koalavip.manager.DatabaseManager;
import tech.zuosi.minecraft.koalavip.manager.TemplateManager;
import tech.zuosi.minecraft.koalavip.view.template.BuffCardTemplate;
import tech.zuosi.minecraft.koalavip.view.template.CommandTemplate;
import tech.zuosi.minecraft.koalavip.view.template.GroupTemplate;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by luckykoala on 18-3-11.
 */
public class Core extends JavaPlugin {
    private PlayerPoints playerPoints;
    private Common craftconomy;

    private static boolean initialized;
    private static Core INSTANCE;

    private TemplateManager templateManager;
    private DatabaseManager databaseManager;

    private Tick tick;

    private boolean debugOn;

    public Core() {
        if(INSTANCE == null) INSTANCE=this;
    }

    public static Core getInstance() {
        return INSTANCE;
    }

    public void debug(Supplier<String> supplier) {
        if(debugOn) {
            getLogger().info(supplier);
        }
    }

    public boolean isDebugOn() {
        return debugOn;
    }

    public void setDebugOn(boolean debugOn) {
        this.debugOn = debugOn;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        INSTANCE = this;
        if(!initialized) {
            this.saveDefaultConfig();
            this.debugOn = this.getConfig().getBoolean("debug");
            if(!hookPlayerPoints()) {
                getLogger().warning(() -> "Failed to hook PlayerPoints");
            }
            if(!hookCraftConomy()) {
                getLogger().warning(() -> "Failed to hook CraftConomy3");
            }
            if(!PermissionsEx.isAvailable()) {
                getLogger().warning(() -> "Failed to hook PermissionEx");
            }
            loadTemplate();
            loadDatabaseConfig();
            this.getServer().getPluginManager().registerEvents(new PlayerPointsChangeEventHandler(), this);
            this.getServer().getPluginManager().registerEvents(new PlayerJoinOrLeaveEventHandler(), this);
            this.getCommand("kvip").setExecutor(new BasicCommand());
            tick = new Tick();
            tick.start();
            loadPlayersOnline();
            initialized = true;
        }
    }

    private void loadPlayersOnline() {
        for(Player player : this.getServer().getOnlinePlayers()) {
            this.getDatabaseManager().get(player.getName());
        }
    }

    public void loadDatabaseConfig() {
        ConfigurationSection databaseSection = this.getConfig().getConfigurationSection("Database");
        String engineType = databaseSection.getString("engine", "mysql");
        String username = databaseSection.getString("username");
        String password = databaseSection.getString("password");
        String database = databaseSection.getString("database");
        int port = databaseSection.getInt("port", 3306);

        if(engineType.equalsIgnoreCase("mysql")) {
            DatabaseEngine engine = new MySQLEngine();
            if(engine.init(username, password, database, port)) {
                databaseManager = new DatabaseManager(engine);
            } else {
                throw new RuntimeException("数据库初始化错误，请检查配置");
            }
        } else {
            throw new RuntimeException("不支持的数据库类型，目前只支持mysql");
        }
    }

    private void loadTemplate() {
        //载入物品模板
        ConfigurationSection cardSection = this.getConfig().getConfigurationSection("BuffCard");
        Set<String> cardKeys = cardSection.getKeys(false);
        Map<String, BuffCardTemplate> buffCardTemplateMap = new HashMap<>(cardKeys.size());
        for(String key : cardKeys) {
            ConfigurationSection data = cardSection.getConfigurationSection(key);
            int price = data.getInt("price");
            int days = data.getInt("days");
            int immediately = data.getInt("immediately");
            int daily = data.getInt("daily");
            buffCardTemplateMap.put(key, new BuffCardTemplate(key, price, days, immediately, daily));
        }
        //载入命令模板
        ConfigurationSection commandSection = this.getConfig().getConfigurationSection("Command");
        Set<String> commandsKeys = commandSection.getKeys(false);
        Map<String, CommandTemplate> commandTemplateMap = new HashMap<>(commandsKeys.size());
        for(String key : commandsKeys) {
            ConfigurationSection data = commandSection.getConfigurationSection(key);
            boolean onetime = data.getBoolean("onetime", false);
            int period = data.getInt("period", 0);
            String cmd = data.getString("cmd");
            commandTemplateMap.put(key, new CommandTemplate(key, onetime, period, cmd));
        }
        //载入用户组模板
        ConfigurationSection groupSection = this.getConfig().getConfigurationSection("Group");
        Set<String> groupsKeys = groupSection.getKeys(false);
        Map<Integer,GroupTemplate> groupTemplateData = new HashMap<>(groupsKeys.size());
        for(String key : groupsKeys) {
            ConfigurationSection data = groupSection.getConfigurationSection(key);
            int min = data.getInt("min");
            String groupId = data.getString("groupId");
            List<String> commandList = data.getStringList("commands");
            groupTemplateData.put(min, new GroupTemplate(key, groupId, commandList.stream()
                    .map(commandTemplateMap::get)
                    .collect(Collectors.toList())));
        }
        //构造模组管理器
        templateManager = new TemplateManager(buffCardTemplateMap, commandTemplateMap, groupTemplateData);
    }

    public TemplateManager getTemplateManager() {
        return templateManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public UUID translateNameToUUID(String name) {
        return this.playerPoints.translateNameToUUID(name);
    }

    //了解reload过程会调用哪些方法
    @Override
    public void onDisable() {
        super.onDisable();
        tick.stop();
        this.getDatabaseManager().getEngine().close();
        HandlerList.unregisterAll(this);
    }

    private boolean hookPlayerPoints() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = PlayerPoints.class.cast(plugin);
        return playerPoints != null;
    }

    private boolean hookCraftConomy() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("Craftconomy3");
        if (plugin != null) {
            craftconomy = (Common) ((Loader)plugin).getCommon();
        }
        return craftconomy != null;
    }

    public PlayerPoints getPlayerPoints() {
        return playerPoints;
    }

    public Common getCraftconomy() {
        return craftconomy;
    }

    public Tick getTick() {
        return tick;
    }
}
