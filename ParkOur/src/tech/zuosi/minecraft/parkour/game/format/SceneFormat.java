package tech.zuosi.minecraft.parkour.game.format;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LuckyKoala on 18-9-22.
 */
@Data
@Builder
public class SceneFormat implements ConfigurationSerializable {
    public static final SceneFormat DEFAULT = SceneFormat.builder()
            .base(FormatEntity.builder()
                    .model(Material.STAINED_GLASS_PANE)
                    .nameTemplate("$sN$")
                    .loreTemplate(new ArrayList<String>(){{
                        add("进度： $sceneUnlocked$/$sceneTotal$");
                        add(" ");
                        add("点击选择关卡");
                    }})
                    .build())
            .lock(FormatEntity.DEFAULT)
            .unlock(FormatEntity.DEFAULT)
            .build();

    FormatEntity base, lock, unlock;

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(3);
        map.put("base", base);
        map.put("lock", lock);
        map.put("unlock", unlock);
        return map;
    }

    public static SceneFormat deserialize(Map<String, Object> args) {
        return SceneFormat.builder()
                .base((FormatEntity) args.getOrDefault("base", FormatEntity.UNDEFINED_TEMPLATE))
                .lock((FormatEntity) args.getOrDefault("lock", FormatEntity.UNDEFINED_TEMPLATE))
                .unlock((FormatEntity) args.getOrDefault("unlock", FormatEntity.UNDEFINED_TEMPLATE))
                .build();
    }
}
