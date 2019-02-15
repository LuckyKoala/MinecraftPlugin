package tech.zuosi.koalaprefix;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.plugin.Plugin;

/**
 * Created by iwar on 2017/2/7.
 */
public class PlayerPointUtil {
    private PlayerPoints playerPoints;
    private static PlayerPointUtil instance;

    private PlayerPointUtil() {
        if(playerPoints==null)
            if(!hookPlayerPoints()) throw new RuntimeException("No player point");
    }

    public static PlayerPointUtil getInstance() {
        if(instance==null) instance=new PlayerPointUtil();
        return instance;
    }

    private boolean hookPlayerPoints() {
        final Plugin plugin = main.getInstance().getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = PlayerPoints.class.cast(plugin);
        return playerPoints != null;
    }

    public PlayerPoints getPlayerPoints() {
        return playerPoints;
    }

    public boolean consumePoints(String playerName, int amount) {
        return playerPoints.getAPI().take(playerName, amount);
    }
}
