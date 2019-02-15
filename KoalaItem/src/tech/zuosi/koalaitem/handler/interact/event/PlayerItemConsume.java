package tech.zuosi.koalaitem.handler.interact.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.item.ItemWine;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/27.
 */
public class PlayerItemConsume implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrink(PlayerItemConsumeEvent pice) {
        Player player = pice.getPlayer();
        ItemStack is = pice.getItem();
        if (!shouldExecute(is)) return;
        if (pice.isCancelled()) return;
        NBTUtil util = new NBTUtil(is);
        player.addPotionEffects(util.getWineEffect());
    }

    public boolean shouldExecute(ItemStack is) {
        return new ItemWine().validate(is);
    }
}
