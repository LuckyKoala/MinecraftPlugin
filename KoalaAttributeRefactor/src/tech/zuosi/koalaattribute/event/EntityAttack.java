package tech.zuosi.koalaattribute.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import tech.zuosi.koalaattribute.attribute.Attribute;
import tech.zuosi.koalaattribute.attribute.AttributeUtil;
import tech.zuosi.koalaattribute.data.AttributeCache;

import java.util.Map;
import java.util.Random;

/**
 * Created by iwar on 2016/8/11.
 */
public class EntityAttack implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player damagee = (Player) event.getEntity();
        Attribute attribute = AttributeCache.INSTANCE.getAttribute(damagee.getName());
        Map<String,Double> essAttribute = AttributeUtil.convertToEssAttribute(attribute);
        double armor = essAttribute.get("armor");
        double damage = event.getDamage();

        //∑¿”˘ …¡±‹
        if (new Random().nextDouble() <= essAttribute.get("dodge")) {
            event.setCancelled(true);
            damagee.sendMessage(ChatColor.GOLD + "[KoalaAttribute] " + ChatColor.GREEN + "≥…π¶…¡±‹±æ¥Œ…À∫¶");
            return;
        }

        damagee.sendMessage(ChatColor.GOLD + "[KoalaAttribute] " + ChatColor.GREEN + "ºı√‚…À∫¶[" + damage*armor + "]");
        damage -= damage*armor;
        damage = damage<0?0:damage;

        event.setDamage(damage-damage*armor);
    }
}
