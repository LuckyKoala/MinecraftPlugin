package tech.zuosi.rebelwar.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.zuosi.rebelwar.RebelWar;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by iwar on 2016/10/2.
 */
public class YamlFile {
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;
    private String fileName;
    private String dirName;

    public YamlFile(String fileName) {
        this.fileName = fileName;
        this.dirName = null;
    }

    public YamlFile(String dirName,String fileName) {
        this.dirName = dirName;
        this.fileName = fileName;
    }

    public YamlFile(File file) {
        this.customConfigFile = file;
    }

    public void reloadCustomConfig() {
        if (customConfigFile == null) {
            File dataFolder = RebelWar.getINSTANCE().getDataFolder();
            if (dirName != null) {
                File dir = new File(dataFolder.getPath()+File.separator+dirName);
                if (!dir.exists()) {
                    boolean result = dir.mkdirs();
                    System.out.println("Mkdirs result: "+result);
                }
                customConfigFile = new File(dir, fileName);
            } else {
                customConfigFile = new File(dataFolder, fileName);
            }
            if (!customConfigFile.exists()) {
                try {
                    customConfigFile.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        // Look for defaults in the jar
        /*Reader defConfigStream = new InputStreamReader(RebelWar.getINSTANCE().getResource("customConfig.yml"), "UTF8");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }*/
    }

    public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            reloadCustomConfig();
        }
        return customConfig;
    }

    public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            this.getCustomConfig().save(customConfigFile); //you wen ti
        } catch (IOException ex) {
            RebelWar.getINSTANCE().getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    public Map<String,Object> getMap() {
        ConfigurationSection section = getCustomConfig().getConfigurationSection("data");
        if (section == null) return null;
        return section.getValues(false);
    }
}
