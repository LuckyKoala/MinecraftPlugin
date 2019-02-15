package tech.zuosi.powerfulmobs.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import tech.zuosi.powerfulmobs.PowerfulMobs;

import java.util.Collection;
import java.util.List;

/**
 * Created by iwar on 2016/5/17.
 */
public class DataManager {
    private PowerfulMobs plugin;
    private FileManager data;
    private MobInfo mobInfo;
    private FileConfiguration dataFile;

    public DataManager(PowerfulMobs plugin) {
        this.plugin = plugin;
        data = new FileManager(plugin,"mobs.data");
        mobInfo = new MobInfo();
        dataFile = data.getConfig();
    }
    public boolean exists() {
        return data.exists();
    }
    public void add(Entity monster) {
        dataFile.set(monster.getUniqueId().toString(),mobInfo.getMobInfo(monster).name());
        data.saveConfig();
    }
    public void write() {
        System.out.println("[PowerfulMobs]Writing data into file...");
        List<World> worldList = Bukkit.getWorlds();
        for (World world : worldList) {
            Collection<Monster> monsterCollection = world.getEntitiesByClass(Monster.class);
            for (Monster monster : monsterCollection) {
                dataFile.set(monster.getUniqueId().toString(),mobInfo.getMobInfo(monster).name());
            }
        }
        data.saveConfig();
        System.out.println("[PowerfulMobs]Writing data into file success.");
    }
    public void read() {
        System.out.println("[PowerfulMobs]Reading data from file...");
        List<World> worldList = Bukkit.getWorlds();
        for (World world : worldList) {
            Collection<Monster> monsterCollection = world.getEntitiesByClass(Monster.class);
            monsterCollection.stream().filter(monster -> dataFile.contains(monster.getUniqueId().toString())).forEach(monster -> mobInfo.putMobInfo(monster, MobLevel.valueOf(dataFile.getString(monster.getUniqueId().toString()))));
        }
        System.out.println("[PowerfulMobs]Reading data from file success.");
    }
    public boolean update() {
        boolean flag = data.delete();
        write();
        return flag;
    }
}
