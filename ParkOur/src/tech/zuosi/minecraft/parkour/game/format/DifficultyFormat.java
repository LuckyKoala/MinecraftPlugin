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
public class DifficultyFormat implements ConfigurationSerializable {
    public static final DifficultyFormat DEFAULT = DifficultyFormat.builder()
            .baseFormat(FormatEntity.builder()
                    .model(Material.STAINED_GLASS_PANE)
                    .nameTemplate("$dN$")
                    .loreTemplate(new ArrayList<String>(){{
                        add("点击选择场景");
                    }})
                    .build())
            .sceneFormatMap(new HashMap<>())
            .build();

    FormatEntity baseFormat;
    Map<String, SceneFormat> sceneFormatMap;

    public SceneFormat getSceneFormat(String scene) {
        return sceneFormatMap.getOrDefault(scene, SceneFormat.DEFAULT);
    }

    public void setSceneFormat(String scene, SceneFormat sceneFormat) {
        sceneFormatMap.put(scene, sceneFormat);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("baseFormat", baseFormat);
        map.put("sceneFormatMap", sceneFormatMap);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static DifficultyFormat deserialize(Map<String, Object> args) {
        return DifficultyFormat.builder()
                .baseFormat((FormatEntity) args.getOrDefault("baseFormat", FormatEntity.UNDEFINED_TEMPLATE))
                .sceneFormatMap((Map<String, SceneFormat>) args.getOrDefault("sceneFormatMap", new HashMap<>()))
                .build();
    }
}
