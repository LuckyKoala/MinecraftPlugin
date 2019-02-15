package tech.zuosi.rebelwar.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import tech.zuosi.rebelwar.game.manager.ArenaManager;
import tech.zuosi.rebelwar.game.object.Arena;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/10/6.
 */
public class ArenaLoader implements IDataLoader {
    private static final YamlFile yamlFile = new YamlFile("arena.yml");

    public ArenaLoader() {}

    //LATER 或许加个备份还原的功能
    //考虑留一个缓冲文件 免得保存出错
    @Override
    public void saveToDisk() {
        Map<String,Arena> map = ArenaManager.getInstance().getArenaMap();
        for (String str:map.keySet()) {
            System.out.println("[Arena]"+str+"  -->  Exist");
            Arena arena = map.get(str);
            if (!arena.check()) {
                System.out.println("[Arena]"+str+"  -->  HalfFinished");
            }
        }
        if (map.size() == 0) return;
        FileConfiguration config = yamlFile.getCustomConfig();
        ConfigurationSection section;
        if (!config.contains("data")) {
            config.createSection("data",map);
        } else {
            section=config.getConfigurationSection("data");
            for (Map.Entry<String,Arena> entry:map.entrySet()) {
                section.set(entry.getKey(),entry.getValue());
            }
        }
        yamlFile.saveCustomConfig();
    }

    @Override
    public void readFromDisk() {
        Map<String,Object> map = yamlFile.getMap();
        if (map == null) return;
        Map<String,Arena> arenaMap = new HashMap<>();

        for (Map.Entry<String,Object> entry:map.entrySet()) {
            if (entry.getValue() instanceof Arena) {
                arenaMap.put(entry.getKey(),(Arena) entry.getValue());
            }
        }
        ArenaManager.getInstance().addAllArena(arenaMap);
    }
}
