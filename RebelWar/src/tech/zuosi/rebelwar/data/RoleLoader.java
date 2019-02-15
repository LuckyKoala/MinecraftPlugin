package tech.zuosi.rebelwar.data;

import tech.zuosi.rebelwar.RebelWar;
import tech.zuosi.rebelwar.game.manager.PlayerManager;
import tech.zuosi.rebelwar.game.object.GamePlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/10/6.
 */
public class RoleLoader implements IDataLoader {

    @Override
    public void readFromDisk() {
        File dir = new File(RebelWar.getINSTANCE().getDataFolder().getPath()+File.separator+"players");
        System.out.println(RebelWar.getINSTANCE().getDataFolder().getPath());
        if (!dir.exists()) {
            dir.mkdirs();
            return;
        }
        File[] fileList = dir.listFiles();
        if (fileList == null) return;
        Map<String,GamePlayer> map = new HashMap<>();
        for (File file:fileList) {
            GamePlayer gamePlayer = (GamePlayer) new YamlFile(file).getCustomConfig().get("data");
            if (gamePlayer != null) map.put(gamePlayer.getPlayerName(),gamePlayer);
        }
        PlayerManager.setGamePlayerMap(map);
    }

    @Override
    public void saveToDisk() {
        Map<String, GamePlayer> map = PlayerManager.getGamePlayerMap();
        for (Map.Entry<String,GamePlayer> entry:map.entrySet()) {
            YamlFile yamlFile = new YamlFile("players",entry.getKey()+".yml");
            yamlFile.getCustomConfig().set("data",entry.getValue());
            yamlFile.saveCustomConfig();
        }
    }
}
