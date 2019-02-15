package tech.zuosi.minecraft.koalakit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tech.zuosi.minecraft.koalakit.database.DatabaseEngine;
import tech.zuosi.minecraft.koalakit.database.implementation.MySQLEngine;
import tech.zuosi.minecraft.koalakit.kits.Kit;
import tech.zuosi.minecraft.koalakit.kits.KitManager;

import java.io.IOException;
import java.util.Objects;

public class Core extends JavaPlugin implements CommandExecutor {
    private static Core INSTANCE;
    private static DatabaseEngine databaseEngine;
    private static KitManager kitManager;
    private static FileConfiguration realConfig;

    public Core() {
        if(INSTANCE == null) INSTANCE=this;
    }

    public static Core getInstance() {
        return INSTANCE;
    }

    public static FileConfiguration getRealConfig() {
        return realConfig;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();
        loadRealConfig();
        loadDatabaseConfig();
        kitManager = new KitManager();
        getCommand("kkit").setExecutor(this);
    }

    private void loadRealConfig() {
        String realPath = getConfig().getString("RemoteConfigPath");
        try {
            this.getConfig().load(realPath);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kkit")) {
            if(args.length == 2) {
                String username = args[0];
                String kitname = args[1];
                Kit kit = kitManager.getKit(kitname);

                if(kit == null) {
                    sender.sendMessage(ChatColor.RED + String.format("找不到名为%s的礼包", kitname));
                } else {
                    Player user = getServer().getPlayer(username);
                    if(kit.getPermission().equals(Kit.NO_PERMISSION_REQUIRED)
                            || Objects.requireNonNull(user, String.format("找不到名为[ %s ]的玩家", username)).hasPermission(kit.getPermission())) {
                        //不需要权限 或 指定玩家拥有此权限
                        if(databaseEngine.compareAndInc(username, kitname, kit.getLimit(), kit.getPeriod())) {
                            kit.getCommands().stream()
                                    .map(cmdFormat -> cmdFormat.replace("%player%", username))
                                    .forEach(cmdToExecute ->
                                            getServer().dispatchCommand(getServer().getConsoleSender(), cmdToExecute));
                        } else {
                            sendWarning(user, "领取间隔未到或已达领取次数上限");
                        }
                    } else {
                        sendWarning(user, "您没有对应的权限");
                    }

                }
            } else {
                sendWarning(sender,"命令格式错误，应使用 /kkit username kitname");
            }
            return true; //永远返回true，覆盖默认的错误提示
        }
        return false;
    }

    private void sendWarning(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.RED + message);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void loadDatabaseConfig() {
        ConfigurationSection databaseSection = this.getConfig().getConfigurationSection("Database");
        String engineType = databaseSection.getString("engine", "mysql");
        String host = databaseSection.getString("host");
        String username = databaseSection.getString("username");
        String password = databaseSection.getString("password");
        String database = databaseSection.getString("database");
        int port = databaseSection.getInt("port", 3306);

        if(engineType.equalsIgnoreCase("mysql")) {
            DatabaseEngine engine = new MySQLEngine();
            if(engine.init(host, username, password, database, port)) {
                databaseEngine = engine;
            } else {
                throw new RuntimeException("数据库初始化错误，请检查配置");
            }
        } else {
            throw new RuntimeException("不支持的数据库类型，目前只支持mysql");
        }
    }
}
