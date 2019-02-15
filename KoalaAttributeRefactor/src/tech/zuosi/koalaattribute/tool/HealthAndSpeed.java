package tech.zuosi.koalaattribute.tool;

import org.bukkit.entity.Player;
import tech.zuosi.koalaattribute.KoalaAttribute;
import tech.zuosi.koalaattribute.attribute.Attribute;
import tech.zuosi.koalaattribute.attribute.AttributeUtil;
import tech.zuosi.koalaattribute.data.AttributeCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/8/11.
 */
public class HealthAndSpeed {
    private static Map<String,Double> extraHealthMap = new HashMap<>();

    //TODO use attribute api or nbt
    public static void update(Player player) {
        String name = player.getName();
        Attribute attribute = AttributeCache.INSTANCE.getAttribute(name);
        Map<String,Double> essAttribute = AttributeUtil.convertToEssAttribute(attribute);
        double extraHealth = essAttribute.get("health");
        double oriMaxHealth = player.getMaxHealth();
        float speed = (float) (0.2D*(1+essAttribute.get("speed"))); //FIXME nonsense

        if (speed > 1.0F) speed = 1.0F;
        if (speed < -1.0F) speed = -1.0F;
        player.setWalkSpeed(speed);

        double previousExtraHealth = safeGetExtraHealth(name);
        double diff = extraHealth - previousExtraHealth;
        double newMaxHealth = oriMaxHealth + diff; //Offset

        KoalaAttribute.INSTANCE.logger.info(String.format("previous: %s, extra: %s, diff: %s, new: %s",
                previousExtraHealth, extraHealth, diff, newMaxHealth));

        if (diff != 0) {
            putExtraHealth(name, extraHealth); //Record
            if(diff < 0) player.setHealth(newMaxHealth); //Avoid to show animation that player was hurt
            player.setMaxHealth(newMaxHealth);
        }
    }

    public static double safeGetExtraHealth(String name) {
        return HealthAndSpeed.extraHealthMap.getOrDefault(name,0D);
    }

    public static void putExtraHealth(String name,double value) {
        HealthAndSpeed.extraHealthMap.put(name,value);
    }
}
