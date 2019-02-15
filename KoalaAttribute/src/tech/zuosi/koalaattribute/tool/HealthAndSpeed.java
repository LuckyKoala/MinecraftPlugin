package tech.zuosi.koalaattribute.tool;

import org.bukkit.entity.Player;
import tech.zuosi.koalaattribute.attribute.Attribute;
import tech.zuosi.koalaattribute.attribute.AttributeUtil;
import tech.zuosi.koalaattribute.data.AttributeCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/8/11.
 */
public class HealthAndSpeed {
    private static Map<String,Double> extraHealth = new HashMap<>();

    public static void update(Player player) {
        Attribute attribute = AttributeCache.INSTANCE.getAttribute(player.getName());
        Map<String,Double> essAttribute = AttributeUtil.convertToEssAttribute(attribute);
        double extraHealth = essAttribute.get("health");
        double oriMaxHealth = player.getMaxHealth();
        float speed = (float) (0.2D*(1+essAttribute.get("speed")));
        String name = player.getName();

        if (speed > 1.0F) speed = 1.0F;
        if (speed < -1.0F) speed = -1.0F;
        player.setWalkSpeed(speed);

        if (HealthAndSpeed.extraHealth.containsKey(name)) {
            if (extraHealth > HealthAndSpeed.extraHealth.get(name)) {
                HealthAndSpeed.extraHealth.put(player.getName(),extraHealth);
            } else {
                return;
            }
        } else {
            HealthAndSpeed.extraHealth.put(player.getName(),extraHealth);
        }

        player.setMaxHealth(oriMaxHealth+extraHealth);
        player.setHealthScale(oriMaxHealth+extraHealth);
    }

    public static double safeGetExtraHealth(String name) {
        return HealthAndSpeed.extraHealth.getOrDefault(name,0D);
    }

    public static void putExtraHealth(String name,double value) {
        HealthAndSpeed.extraHealth.put(name,value);
    }
}
