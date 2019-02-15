package tech.zuosi.minecraft.parkour.game;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by LuckyKoala on 18-9-16.
 */
@Data
@Builder
public class GameMap implements ConfigurationSerializable {
    Location startPoint, endPoint;
    Set<GameRegion> regions;
    MapPath path;
    long timeRequiredForThreeStars, timeRequiredForTwoStars, timeRequiredForOneStar;

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(7);
        map.put("startPoint", startPoint);
        map.put("endPoint", endPoint);
        map.put("regions", regions);
        map.put("path", path.toString());
        map.put("timeRequiredForThreeStars", timeRequiredForThreeStars);
        map.put("timeRequiredForTwoStars", timeRequiredForTwoStars);
        map.put("timeRequiredForOneStar", timeRequiredForOneStar);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static GameMap deserialize(Map<String, Object> args) {
        return GameMap.builder()
                .startPoint((Location) args.get("startPoint"))
                .endPoint((Location) args.get("endPoint"))
                .regions((Set<GameRegion>) args.get("regions"))
                .path(MapPath.fromString((String) args.get("path")))
                .timeRequiredForThreeStars((Integer) args.get("timeRequiredForThreeStars"))
                .timeRequiredForTwoStars((Integer) args.get("timeRequiredForTwoStars"))
                .timeRequiredForOneStar((Integer) args.get("timeRequiredForOneStar"))
                .build();
    }
}
