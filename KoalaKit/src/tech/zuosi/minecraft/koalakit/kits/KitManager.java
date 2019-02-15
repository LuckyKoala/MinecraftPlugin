package tech.zuosi.minecraft.koalakit.kits;

import org.bukkit.configuration.ConfigurationSection;
import tech.zuosi.minecraft.koalakit.Core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KitManager {
    private Map<String, Kit> kitMap;

    public KitManager() {
        ConfigurationSection section = Core.getInstance().getConfig().getConfigurationSection("Kits");
        Set<String> keys = section.getKeys(false);
        this.kitMap = new HashMap<>(keys.size());
        for(String kitname: keys) {
            ConfigurationSection data = section.getConfigurationSection(kitname);
            this.kitMap.put(kitname,
                    new Kit(data.getInt("limit", 1),
                            data.getInt("period", 0),
                            data.getString("permission", Kit.NO_PERMISSION_REQUIRED),
                            data.getStringList("commands")));
        }
    }

    public Kit getKit(String kitname) {
        return this.kitMap.get(kitname);
    }
}
