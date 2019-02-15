package tech.zuosi.minecraft.parkour.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.database.PlayerData;

/**
 * Created by LuckyKoala on 18-9-16.
 */
public class PlaceHolders extends PlaceholderExpansion {
    /**
     * This expansion does not require a dependency so we will always return true
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * The name of the person who created this expansion should go here
     */
    @Override
    public String getAuthor() {
        return "clip";
    }

    /**
     * The placeholder identifier should go here
     * This is what tells PlaceholderAPI to call our onPlaceholderRequest method to obtain
     * a value if a placeholder starts with our identifier.
     * This must be unique and can not contain % or _
     */
    @Override
    public String getIdentifier() {
        return "parkour";
    }

    /**
     * This is the version of this expansion
     */
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    // Example: %parkour_bestTime_easy-barn-1%
    //   bestTime(MapPath.fromString("easy-barn-1"))
    /*private static final String BEST_TIME_PREFIX = "bestTime";
    private static final String SYSTEM_BEST_TIME_PREFIX = "systemBestTime";
    private static final String STARS_UNLOCKED_PREFIX = "starsUnlocked";
    private static final String STARS_UNLOCKED_FOR_SCENE_PREFIX = "starsUnlockedForScene";
    private static final String STARS_UNLOCKED_FOR_SCENE_TOTAL_PREFIX = "starsUnlockedForSceneTotal";*/

    /**
     * This is the method called when a placeholder with our identifier is found and needs a value
     * We specify the value identifier in this method
     */
    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        PlayerData playerData = Core.getInstance().dataManager
                .loadPlayerData(p.getName());

        if (identifier.equals("unlocked")) {
            return String.valueOf(playerData.starsUnlockedTotal());
        } else if (identifier.equals("total")) {
            return String.valueOf(Core.getInstance().gameManager.starsTotal());
        }
        /*else {

            //Match prefix and return dynamic data
            if(identifier.startsWith(BEST_TIME_PREFIX)) {
                identifier.substring()
            }
        }*/

        return null;
    }
}
