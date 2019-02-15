package tech.zuosi.minecraft.parkour.database;

import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.GameMap;
import tech.zuosi.minecraft.parkour.game.MapPath;

import java.util.*;

/**
 * Created by LuckyKoala on 18-9-16.
 */
public class PlayerData {
    private final Map<MapPath, Long> bestTimeRecord;
    public static final PlayerData systemBestTimeData = Core.getInstance().dataManager.loadBestData();

    public PlayerData(Map<String, Long> bestTimeRecord) {
        this.bestTimeRecord = new HashMap<>(bestTimeRecord.size());
        bestTimeRecord.forEach((pathStr, bestTime) ->
                this.bestTimeRecord.put(MapPath.fromString(pathStr), bestTime));
    }

    public long bestTime(MapPath path) {
        Long result = bestTimeRecord.get(path);
        if(result == null) return 0L;
        else return result;
    }

    public boolean newRecord(String username, MapPath mapPath, long newTime) {
        long oldTime = bestTime(mapPath);
        if (0L == oldTime || Long.compare(newTime, oldTime) < 0) {
            bestTimeRecord.put(mapPath, newTime);
            Core.getInstance().dataManager.updateBestTime(username, mapPath, newTime, oldTime);
            return true;
        } else {
            return false;
        }
    }

    public int starsUnlocked(MapPath path) {
        long bestTime = bestTime(path);
        if(bestTime == 0L) return 0; //Fast fail

        GameMap gameMap = Core.getInstance().gameManager.fromMapPath(path);
        if(Long.compare(bestTime, gameMap.getTimeRequiredForTwoStars()) > 0)
            if (Long.compare(bestTime, gameMap.getTimeRequiredForOneStar()) > 0) return 0;
            else return 1;
        else if (Long.compare(bestTime, gameMap.getTimeRequiredForThreeStars()) > 0) return 2;
        else return 3;
    }

    public int starsUnlockedForScene(MapPath path) {
        Map<String, Map<String, List<String>>> unfoldedPathToMap = Core.getInstance().gameManager.getUnfoldedPathToMap();
        List<String> pathStrList = Optional.ofNullable(unfoldedPathToMap.get(path.getDifficulty()))
                .map(sceneMap -> Optional.ofNullable(sceneMap.get(path.getScene())).orElse(Collections.emptyList()))
                .orElse(Collections.emptyList());
        return pathStrList.stream()
                .map(MapPath::fromString)
                .mapToInt(this::starsUnlocked)
                .sum();
    }

    public int starsUnlockedTotal() {
        return bestTimeRecord.keySet()
                .stream()
                .mapToInt(this::starsUnlocked)
                .sum();
    }
}
