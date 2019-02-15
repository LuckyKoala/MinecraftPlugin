package tech.zuosi.koalaitem.handler.interact.event;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.type.CoreType;
import tech.zuosi.koalaitem.type.GemType;
import tech.zuosi.koalaitem.util.CoreUtil;
import tech.zuosi.koalaitem.util.GemUtil;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/29.
 */
public class PlayerMove implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getItemInHand();
        CoreUtil coreUtil = new CoreUtil();
        if (!coreUtil.shouldExecute(is)) return;
        NBTUtil util = new NBTUtil(is);
        CoreType coreType = coreUtil.getCoreType(util);
        Location to = e.getTo();
        Location from = e.getFrom();
        Location loc = player.getLocation();

        if (to.distance(from) == 0) return;
        if (CoreType.FLAME == coreType) {
            player.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1, 30);
        } else if (CoreType.ENDERSIGNAL == coreType) {
            player.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1, 30);
        }

        GemUtil gemUtil = new GemUtil();
        if (!gemUtil.shouldExecute(is)) return;
        int speedLevel = gemUtil.getGemLevel(util,GemType.SPEED);

        if (player.hasPotionEffect(PotionEffectType.SPEED)) return;
        if (speedLevel > -1) {
            int amplifier = speedLevel * KoalaItem.INSTANCE.getConfig().getInt("Gem.Speed.amplifier");
            amplifier--;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                    60,
                    amplifier<0?0:amplifier));
        }
    }
}
