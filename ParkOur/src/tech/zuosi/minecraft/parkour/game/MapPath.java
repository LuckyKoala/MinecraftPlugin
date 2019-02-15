package tech.zuosi.minecraft.parkour.game;

import lombok.Builder;
import lombok.Data;

/**
 * Created by LuckyKoala on 18-9-16.
 */
@Data
@Builder
public class MapPath {
    public static final String DIFFICULTY = "difficulty";
    public static final String SCENE = "scene";
    public static final String LEVEL = "level";
    public static final String SEPRATOR = "-";
    public static final String PATH_FORMAT = "难度"+SEPRATOR+"场景"+SEPRATOR+"等级";

    public static final String EMPTY_PATH = "$";
    public static final String BASE_LEVEL = "1";
    public static final String EMPTY_LEVEL = "0"; //For partial
    public static final MapPath EMPTY = MapPath.builder().difficulty(EMPTY_PATH).scene(EMPTY_PATH).level(EMPTY_LEVEL).build();

    String difficulty, scene, level;

    boolean isPartial() {
        return EMPTY_PATH.equals(difficulty) || EMPTY_PATH.equals(scene) || EMPTY_LEVEL.equals(level);
    }

    public static MapPath partialPath(String difficulty) {
        return new MapPath(difficulty, EMPTY_PATH, EMPTY_LEVEL);
    }

    public static MapPath partialPath(String difficulty, String scene) {
        return new MapPath(difficulty, scene, EMPTY_LEVEL);
    }

    MapPath previousLevel() {
        Integer prev = Integer.valueOf(level)-1;
        return MapPath.builder()
                .difficulty(difficulty)
                .scene(scene)
                .level(prev.toString())
                .build();
    }

    public MapPath nextLevel() {
        Integer next = Integer.valueOf(level)+1;
        return MapPath.builder()
                .difficulty(difficulty)
                .scene(scene)
                .level(next.toString())
                .build();
    }

    public String toString() {
        return difficulty + SEPRATOR + scene + SEPRATOR + level;
    }

    public static MapPath fromString(String arg) {
        String[] data = arg.split(SEPRATOR);
        if(data.length!=3) return EMPTY;
        return MapPath.builder()
                .difficulty(data[0])
                .scene(data[1])
                .level(data[2])
                .build();
    }
}
