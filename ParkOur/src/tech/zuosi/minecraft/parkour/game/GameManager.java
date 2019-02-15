package tech.zuosi.minecraft.parkour.game;

import org.bukkit.entity.Player;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.database.DataManager;
import tech.zuosi.minecraft.parkour.game.format.DifficultyFormat;
import tech.zuosi.minecraft.parkour.game.format.SceneFormat;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by LuckyKoala on 18-9-16.
 * GameDifficulty -> Scene -> Map
 */
public class GameManager {
    private static final int UNLOCK_STARS_LIMIT = 2; //Indicate how many stars should be unlocked to unlock next level

    private final Map<String, GamePlayer> nameToGamePlayer;
    private final Map<MapPath, GameMap> pathToMap;
    // Difficulty -> Scene -> MapPath
    private final Map<String, Map<String, List<String>>> unfoldedPathToMap;
    private final Map<String, DifficultyFormat> formatMap;

    public GameManager(Map<MapPath, GameMap> pathToMap, Map<String, DifficultyFormat> formatMap) {
        this.pathToMap = pathToMap;
        this.unfoldedPathToMap = pathToMap.keySet()
                .stream()
                .map(MapPath::toString)
                .collect(groupingBy(s -> MapPath.fromString(s).difficulty,
                        groupingBy(s -> MapPath.fromString(s).scene)));
        this.nameToGamePlayer = new HashMap<>();
        this.formatMap = formatMap;
    }

    public Collection<GamePlayer> getAllGamePlayer() {
        return Collections.unmodifiableCollection(nameToGamePlayer.values());
    }

    void tickAll(long currentMs) {
        nameToGamePlayer.values()
                .stream()
                .filter(GamePlayer::isPlaying)
                .forEach(gamePlayer -> gamePlayer.tick(currentMs));
    }

    public boolean isMapUnlock(Player player, MapPath mapPath) {
        DataManager dataManager = Core.getInstance().dataManager;
        if(MapPath.BASE_LEVEL.equals(mapPath.getLevel())) return true;
        MapPath prev = mapPath.previousLevel();
        if(isPathExist(prev))
            return dataManager.loadPlayerData(player.getName()).starsUnlocked(prev) >= UNLOCK_STARS_LIMIT;
        else {
            Core.getInstance().getLogger().warning(mapPath+"没有上一等级地图，请检查！");
            return false;
        }
    }

    public boolean isPathExist(MapPath mapPath) {
        return pathToMap.keySet().contains(mapPath);
    }

    public boolean addPlayerToGame(Player player, MapPath mapPath) {
        return addPlayerToGame(player, mapPath, false);
    }

    public boolean addPlayerToGame(Player player, MapPath mapPath, boolean rejoin) {
        if(mapPath == null || mapPath.isPartial()) return false;
        if(pathToMap.containsKey(mapPath)) {
            if(isMapUnlock(player, mapPath)) {
                GamePlayer gamePlayer = gamePlayerFor(player);
                if(rejoin) {
                    gamePlayer.rejoin(mapPath);
                } else {
                    gamePlayer.join(mapPath);
                }
                return true;
            }
        }
        return false;
    }

    public void removeAllPlayerFromGame() {
        this.nameToGamePlayer.values()
                .stream()
                .filter(gamePlayer -> gamePlayer.getMapPath()!=null)
                .forEach(GamePlayer::leave);
    }

    public void removePlayerFromGame(Player player) {
        gamePlayerFor(player).leave();
    }

    public void removePlayerFromManager(Player player) {
        this.nameToGamePlayer.remove(player.getName());
    }

    public boolean addGameMap(MapPath mapPath, GameMap gameMap, boolean override) {
        if (pathToMap.containsKey(mapPath)) {
            if(override) {
                pathToMap.put(mapPath, gameMap);
                saveGameMapToConfig(mapPath, gameMap);
                return true;
            } else {
                return false;
            }
        } else {
            pathToMap.put(mapPath, gameMap);
            List<String> list = new ArrayList<>();
            list.add(mapPath.toString());
            Optional.ofNullable(unfoldedPathToMap.putIfAbsent(mapPath.difficulty,
                    new HashMap<String, List<String>>() {{
                        put(mapPath.scene, list);
                    }}))
                    .ifPresent(sceneMap ->
                            Optional.ofNullable(sceneMap.putIfAbsent(mapPath.scene,
                                    list))
                                    .ifPresent(levelList -> levelList.add(mapPath.toString())));
            saveGameMapToConfig(mapPath, gameMap);
            syncFormatToConfig(mapPath);
            return true;
        }
    }

    public void syncFormatToConfig(MapPath mapPath) {
        if(mapPath.isPartial()) return;

        String difficulty = mapPath.getDifficulty();
        if(formatMap.containsKey(difficulty)) {
            DifficultyFormat difficultyFormat = formatMap.get(difficulty);
            String scene = mapPath.getScene();
            if (!difficultyFormat.getSceneFormatMap().containsKey(scene)) {
                SceneFormat sceneFormat = SceneFormat.DEFAULT;
                difficultyFormat.getSceneFormatMap().put(scene, sceneFormat);
                saveFormatToConfig(difficulty, difficultyFormat);
            }
        } else {
            DifficultyFormat difficultyFormat = DifficultyFormat.DEFAULT;
            String scene = mapPath.getScene();
            SceneFormat sceneFormat = SceneFormat.DEFAULT;
            difficultyFormat.getSceneFormatMap().put(scene, sceneFormat);
            saveFormatToConfig(difficulty, difficultyFormat);
        }
    }

    public void addFormat(String difficulty, DifficultyFormat format, boolean override) {
        if(override) {
            formatMap.put(difficulty, format);
            saveFormatToConfig(difficulty, format);
        } else {
            Object val = formatMap.putIfAbsent(difficulty, format);
            if(null == val) saveFormatToConfig(difficulty, format);
        }
    }

    private void saveFormatToConfig(String difficulty, DifficultyFormat format) {
        Core.getInstance().getConfig().set("Format."+difficulty, format);
        Core.getInstance().saveConfig();
    }

    private void saveGameMapToConfig(MapPath mapPath, GameMap gameMap) {
        Core.getInstance().getConfig().set("Map."+mapPath.toString(), gameMap);
        Core.getInstance().saveConfig();
    }

    public DifficultyFormat getDifficultyFormatFor(String difficulty) {
        return formatMap.getOrDefault(difficulty, DifficultyFormat.DEFAULT);
    }

    public GameMap fromMapPath(MapPath mapPath) {
        return pathToMap.get(mapPath);
    }

    public GamePlayer gamePlayerFor(Player player) {
        return nameToGamePlayer.computeIfAbsent(player.getName(), name -> new GamePlayer(player));
    }

    public GamePlayer gamePlayerFor(String name) {
        return nameToGamePlayer.computeIfAbsent(name, GamePlayer::new);
    }

    public Map<String, Map<String, List<String>>> getUnfoldedPathToMap() {
        return Collections.unmodifiableMap(unfoldedPathToMap);
    }

    public Map<MapPath, GameMap> getPathToMap() {
        return Collections.unmodifiableMap(pathToMap);
    }

    public int starsForSceneTotal(MapPath path) {
        return Optional.ofNullable(unfoldedPathToMap.get(path.difficulty))
                .map(sceneMap ->
                        Optional.ofNullable(sceneMap.get(path.scene))
                                .map(levelList -> levelList.size()*3).orElse(0))
                .orElse(0);
    }

    public int starsTotal() {
        return pathToMap.size()*3;
    }
}
