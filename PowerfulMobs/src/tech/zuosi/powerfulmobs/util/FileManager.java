package tech.zuosi.powerfulmobs.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

/**
 * Created by iwar on 2016/5/17.
 */
public class FileManager {
    private final String fileName;
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration fileConfiguration;

    public FileManager(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.configFile = new File(plugin.getDataFolder(), fileName);
    }

    public boolean exists() {
        return configFile.exists();
    }

    public boolean delete() {
        return configFile.getAbsoluteFile().delete();
    }

    public void reloadConfig() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defConfigStream = this.plugin.getResource(this.fileName);
        if(defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            this.fileConfiguration.setDefaults(defConfig);
        }

    }

    public FileConfiguration getConfig() {
        if(this.fileConfiguration == null) {
            this.reloadConfig();
        }

        return this.fileConfiguration;
    }

    public void saveConfig() {
        if(this.fileConfiguration != null && this.configFile != null) {
            try {
                this.getConfig().save(this.configFile);
            } catch (IOException var2) {
                this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, var2);
            }

        }
    }

    public void saveDefaultConfig() {
        if(!this.configFile.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }

    }

    /*public void loadConfig() {
        this.fileConfiguration = this.getConfig();
        this.fileConfiguration.options().copyDefaults(true);
        if((new File(this.plugin.getDataFolder() + "/config.yml")).exists()) {
            System.out.println("[" + this.plugin + "]" + "config.yml geladen.");
        } else {
            this.saveDefaultConfig();
            System.out.println("[" + this.plugin + "]" + "config.yml erstellt und geladen.");
        }

    }*/

}
