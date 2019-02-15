package tech.zuosi.koalaprefixbc;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by iwar on 2017/1/25.
 */
public class DataManager {
    //Create data/playername.yml to store prefix data
    //Read/Write prefix data from/to data file
    private static String dataFolderPath = main.getInstance().getDataFolder()+File.pathSeparator+"data";
    private static Map<String,Prefix> prefixCache = new HashMap<>();
    private static Map<String,PlayerPrefix> playerCache = new HashMap<>();

    public static void updateAllPrefixToLoadingServer(List<String> list) {
        StringBuilder messageBuilder = new StringBuilder();
        Set<Map.Entry<String,Prefix>> entrySet = prefixCache.entrySet();
        for(Map.Entry<String,Prefix> entry:entrySet) {
            messageBuilder.append(entry.getKey()).append('>').append(entry.getValue().toString()).append('<');
        }
        for(String server:list) {
            MessageListener.sendToBukkit("update",messageBuilder.toString(), BungeeCord.getInstance().getServerInfo(server));
        }
    }

    public static void loadAllPrefix() {
        File folder  = main.getInstance().getDataFolder();
        if(!folder.exists()) folder.mkdir();
        File configFile = new File(folder+File.separator+"config.yml");
        if(!configFile.exists()) {
            try (InputStream in = main.getInstance().getResourceAsStream("config.yml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Configuration configuration = null;
        try {
            configuration= ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(configFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //Load All Availiable Prefix
        if(configuration==null) return;
        Map<String,Prefix> prefixMap = new HashMap<>();
        Configuration config = configuration.getSection("Prefix");
        Collection<String> configCollection = config.getKeys();
        for(String s:configCollection) {
            String name = config.getString(s+".name");
            List<String> description = config.getStringList(s+".description");
            String model = config.getString(s+".model");
            String aquire_mode = config.getString(s+".aquire_mode");
            int mode_value = config.getInt(s+".mode_value",0);
            prefixMap.put(name, new Prefix(name,description,model,Prefix.Aquire_Mode.valueOf(aquire_mode),mode_value));
        }
        prefixCache = prefixMap;
        //Update
        updateAllPrefixToLoadingServer(configuration.getStringList("ServerList"));
    }

    public static Map<String,Prefix> getAllPrefixCache() {
        return prefixCache;
    }

    public static void saveAllCachedData() {
        Set<String> keySet = playerCache.keySet();
        for(String name:keySet) {
            saveCachedDataByName(name);
        }
    }

    public static Configuration getConfigurationByName(String playerName) {
        String dataPath = dataFolderPath+File.pathSeparator+playerName+".yml";
        File dataFolder = new File(dataFolderPath);
        if(!dataFolder.exists()) dataFolder.mkdirs();
        File dataFile = new File(dataPath);
        if(!dataFile.exists())
            try {
                dataFile.createNewFile();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        Configuration configuration = null;
        try {
            configuration= ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(dataFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return configuration;
    }

    public static void saveCachedDataByName(String playerName) {
        Configuration configuration = getConfigurationByName(playerName);
        configuration.set("Data",getPlayerPrefix(playerName).toString());
    }

    public static void readAndCacheData(String playerName) {
        Configuration configuration = getConfigurationByName(playerName);
        if(configuration==null) return;
        PlayerPrefix playerPrefix = PlayerPrefix.loadFromString(playerName,configuration.getString("Data"));
        if(playerPrefix!=null) playerCache.put(playerName,playerPrefix);
    }

    public static PlayerPrefix getPlayerPrefix(String playerName) {
        PlayerPrefix playerPrefix = playerCache.get(playerName);
        if (playerPrefix==null) {
            //Try read data from file
            readAndCacheData(playerName);
            playerPrefix = playerCache.get(playerName);
        }
        return playerPrefix;
    }

    public static void setState(String playerName, String prefixName, Prefix.PrefixState state) {
        getPlayerPrefix(playerName).setPrefixState(prefixName,state);
        saveCachedDataByName(playerName);
    }
}
