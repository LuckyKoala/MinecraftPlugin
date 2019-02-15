package tech.zuosi.rebelwar.data;

import org.bukkit.configuration.file.FileConfiguration;
import tech.zuosi.rebelwar.RebelWar;
import tech.zuosi.rebelwar.game.object.GameConfig;

/**
 * Created by iwar on 2016/10/10.
 */
public class ConfigLoader {
    private RebelWar plugin = RebelWar.getINSTANCE();

    public void read() {
        FileConfiguration config = plugin.getConfig();
        GameConfig.getInstance().initConfig(config.getInt("MEMBER_LIMIT"),config.getInt("REBEL_LIMIT"),
                config.getInt("TIME_LIMIT"),config.getString("INFO_PREFIX"),config.getString("LOBBY_SERVER_NAME"));
    }
}
