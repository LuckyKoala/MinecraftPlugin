package tech.zuosi.minecraft.parkour.game;

import lombok.Builder;
import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LuckyKoala on 18-9-22.
 */
@Data
@Builder
public class GameRegion implements ConfigurationSerializable {
    Vector min, max;

    public boolean isInRegion(Vector vector) {
        return vector.isInAABB(min, max);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("min", min);
        map.put("max", max);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static GameRegion deserialize(Map<String, Object> args) {
        return GameRegion.builder()
                .min((Vector) args.get("min"))
                .max((Vector) args.get("max"))
                .build();
    }
}
