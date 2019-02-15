package tech.zuosi.koalaitem.handler.interact.event;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.type.GemType;
import tech.zuosi.koalaitem.util.GemUtil;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/23.
 */
public class EntityDeath implements Listener {

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onEntityDeath(EntityDeathEvent ede) {
        if (ede.getEntity().getKiller() != null) {
            Player player = ede.getEntity().getKiller();
            ItemStack is = player.getItemInHand();
            GemUtil gemUtil = new GemUtil();
            if (!gemUtil.shouldExecute(is)) return;
            NBTUtil util = new NBTUtil(is);
            int level = KoalaItem.INSTANCE.getConfig().getInt("Gem.Lucky.level");
            int luckyLevel = gemUtil.getGemLevel(util,GemType.LUCKY);

            if (luckyLevel > -1) {
                level *= luckyLevel;
                if (is.containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {
                    int oriLevel = is.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
                    if (level <= oriLevel) return;
                    is.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
                    is.addEnchantment(Enchantment.LOOT_BONUS_MOBS, level);
                    Bukkit.getScheduler().runTaskLater(KoalaItem.INSTANCE, new BukkitRunnable() {
                        @Override
                        public void run() {
                            is.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
                            is.addEnchantment(Enchantment.LOOT_BONUS_MOBS, oriLevel);
                        }
                    }, 30L);
                } else {
                    is.addEnchantment(Enchantment.LOOT_BONUS_MOBS, level);
                    Bukkit.getScheduler().runTaskLater(KoalaItem.INSTANCE, new BukkitRunnable() {
                        @Override
                        public void run() {
                            is.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
                        }
                    }, 30L);
                }
            }
        }
    }
}
