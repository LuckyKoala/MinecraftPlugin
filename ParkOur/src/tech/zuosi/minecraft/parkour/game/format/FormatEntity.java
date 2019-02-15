package tech.zuosi.minecraft.parkour.game.format;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import tech.zuosi.minecraft.parkour.Core;

import java.util.*;

/**
 * Created by LuckyKoala on 18-9-22.
 */
@Data
@Builder
public class FormatEntity implements ConfigurationSerializable {
    static final String UNDEFINED_TEMPLATE = "undefined template";
    public static final FormatEntity DEFAULT = FormatEntity.builder()
            .model(Material.STAINED_GLASS_PANE)
            .modelData(0)
            .nameTemplate("$sN$ [Lv $lN$]")
            .loreTemplate(new ArrayList<String>(){{
                add("全服最佳时间: $mapAllBest$");
                add("个人最佳时间: $mapBest$");
                add(" ");
                add("*** $3S$");
                add("**  $2S$");
                add("*   $1S$");
                add("  ");
                add("点击进入游戏");
            }})
            .build();

    Material model;
    int modelData;
    String nameTemplate;
    List<String> loreTemplate;

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>(4);
        map.put("model", model.name());
        map.put("modelData", modelData);
        map.put("nameTemplate", nameTemplate);
        map.put("loreTemplate", loreTemplate);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static FormatEntity deserialize(Map<String, Object> args) {
        String modelName = (String) args.getOrDefault("model", "STAINED_GLASS_PANE");
        Material model = Material.getMaterial(modelName);
        if(model == null) {
            Core.getInstance().getLogger().warning("Can't find material for name ["+modelName+"]");
            model = Material.STAINED_GLASS_PANE;
        }
        return FormatEntity.builder()
                .model(model)
                .modelData((int) args.getOrDefault("modelData", 0))
                .nameTemplate((String) args.getOrDefault("nameTemplate", UNDEFINED_TEMPLATE))
                .loreTemplate((List<String>) args.getOrDefault("loreTemplate", Collections.singletonList(UNDEFINED_TEMPLATE)))
                .build();
    }
}
