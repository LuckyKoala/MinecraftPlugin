package tech.zuosi.koalaattribute.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import tech.zuosi.koalaattribute.attribute.Attribute;
import tech.zuosi.koalaattribute.attribute.AttributeUtil;
import tech.zuosi.koalaattribute.data.AttributeCache;

import java.util.Map;

/**
 * Created by iwar on 2016/8/11.
 */
public class EntityDeath implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() == null) return;
        LivingEntity entity = event.getEntity();
        if (entity.getKiller() == null) return;
        Player player = (Player) entity.getKiller();
        int exp = event.getDroppedExp();
        Attribute attribute = AttributeCache.INSTANCE.getAttribute(player.getName());
        Map<String,Double> essAttribute = AttributeUtil.convertToEssAttribute(attribute);

        //经验加成
        exp *= 1+essAttribute.get("exp");
        exp = exp>60?60:exp;
        event.setDroppedExp(exp);
    }
}
