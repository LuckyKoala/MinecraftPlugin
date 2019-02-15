package tech.zuosi.koalaitem.handler.interact.attribute;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/7/25.
 */
public class HealthEffect implements Listener {
    private static Map<Player,Double> extraValue = new HashMap<>();

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent erhe) {
        if (!(erhe.getEntity() instanceof Player)) return;
        Player player = (Player) erhe.getEntity();
        double amount = erhe.getAmount();
        ItemStack is = player.getItemInHand();
        if (!shouldExecute(is)) return;
        NBTUtil util = new NBTUtil(is);
        double health = player.getHealth();
        double maxHealth = player.getMaxHealth();
        int ATK = (int)util.getAttribute("ATK");
        double factor = KoalaItem.INSTANCE.getConfig().getDouble("Attribute.ATK.regenFactor");

        amount += amount * ATK * factor;
        if ((health + amount) > maxHealth) amount = maxHealth-health;
        erhe.setAmount(amount);
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent pihe) {
        Player player = pihe.getPlayer();
        ItemStack is = player.getInventory().getItem(pihe.getNewSlot());
        if (!extraValue.containsKey(player)) {
            //FIXME 智障你得兼容其它插件啊
            if (player.getMaxHealth() > 20D) player.setMaxHealth(20D);
            extraValue.put(player,0D);
        }
        if (extraValue.get(player) > 0) {
            //是否取消
            double value = extraValue.get(player);
            if (!shouldExecute(is)) {
                extraValue.remove(player,value);
                if ((player.getHealth() - value) <= 0) {
                    player.setHealth(1D);
                } else {
                    player.setHealth(player.getHealth() - value);
                }
                player.setMaxHealth(player.getMaxHealth() - value);
            } else {
                NBTUtil util = new NBTUtil(is);

                int ATK = (int)util.getAttribute("ATK");
                double extraHealth = KoalaItem.INSTANCE.getConfig().getDouble("Attribute.ATK.extraHealth");

                extraValue.put(player,ATK*extraHealth);
                player.setMaxHealth(20D);
                player.setMaxHealth(player.getMaxHealth() + ATK*extraHealth);
                player.setHealth(player.getHealth() + ATK*extraHealth);
            }
        } else {
            if (!shouldExecute(is)) return;
            NBTUtil util = new NBTUtil(is);

            int ATK = (int)util.getAttribute("ATK");
            double extraHealth = KoalaItem.INSTANCE.getConfig().getDouble("Attribute.ATK.extraHealth");

            extraValue.put(player,ATK*extraHealth);
            player.setMaxHealth(player.getMaxHealth() + ATK*extraHealth);
            player.setHealth(player.getHealth() + ATK*extraHealth);
        }
    }

    public boolean shouldExecute(ItemStack is) {
        is = is==null?new ItemStack(Material.AIR):is;
        if (Material.AIR == is.getType()) return false;
        NBTUtil util = new NBTUtil(is);
        Object o = util.getData("type");
        if (o == null) return false;
        return ItemType.PLAYERITEM == ItemType.valueOf((String) o);
    }

    public static Map<Player, Double> getExtraValue() {
        return extraValue;
    }

    public static void setExtraValue(Map<Player,Double> newMap) {
        extraValue = newMap;
    }
}
