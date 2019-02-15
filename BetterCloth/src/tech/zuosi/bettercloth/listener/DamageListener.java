package tech.zuosi.bettercloth.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.zuosi.bettercloth.BetterCloth;
import tech.zuosi.bettercloth.util.ActionBar;
import tech.zuosi.bettercloth.util.Data;
import tech.zuosi.bettercloth.util.Util;

import java.util.List;
import java.util.Map;

public class DamageListener implements Listener{
	private static BetterCloth sr;
	public DamageListener(BetterCloth args){
		this.sr = args;
	}

    @EventHandler(priority = EventPriority.HIGH)
    public void onHurt(EntityDamageEvent event) {
        double finalDamage;
        if (event.getEntity() instanceof Player) {
            EntityDamageEvent.DamageCause damageCause = event.getCause();
            if (damageCause == EntityDamageEvent.DamageCause.CUSTOM || damageCause == EntityDamageEvent.DamageCause.SUICIDE
                    || damageCause == EntityDamageEvent.DamageCause.MELTING || damageCause == EntityDamageEvent.DamageCause.VOID) {
                return;
            }
            Player p = (Player) event.getEntity();
            if (Data.helmetList.contains(p)) Data.helmetList.remove(p);
            if (Data.chestplateList.contains(p)) Data.chestplateList.remove(p);
            if (Data.leggingsList.contains(p)) Data.leggingsList.remove(p);
            if (Data.bootsList.contains(p)) Data.bootsList.remove(p);
            finalDamage = Util.deDamage(p, event.getDamage());
            if (Data.whetherToShowMessage.get(p)) {
                p.sendMessage(ChatColor.GOLD + "[BetterCloth]减伤完成,伤害类型为"
                        + damageCause.name() + ",初始伤害" + event.getDamage() + ",减免伤害" + (event.getDamage()-finalDamage));
            }
            event.setDamage(finalDamage);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ItemStack helmet = p.getInventory().getHelmet();
        ItemStack chestplate = p.getInventory().getChestplate();
        ItemStack leggings = p.getInventory().getLeggings();
        ItemStack boots = p.getInventory().getBoots();

        restoreArmor(helmet,p,Data.helmetMap,Data.helmetList,Data.helmetCache);
        restoreArmor(chestplate,p,Data.chestplateMap,Data.chestplateList,Data.chestplateCache);
        restoreArmor(leggings,p,Data.leggingsMap,Data.leggingsList,Data.leggingsCache);
        restoreArmor(boots,p,Data.bootsMap,Data.bootsList,Data.bootsCache);
    }

    void restoreArmor(ItemStack armor,Player p, Map<Player,Integer> map, List<Player> list, List<Player> cache) {
        long delay;
        if (map.containsKey(p) && !cache.contains(p)) {
            int var = map.get(p);
            delay = var*20;
            map.remove(p,var);
            cache.add(p);
            if (var > 0) {
                ActionBar.sendAction(p, var + "秒后开始恢复装甲护盾值.");
                Bukkit.getScheduler().scheduleSyncDelayedTask(sr, () -> {
                    if (!list.contains(p)) {
                        if (!p.isDead()) {
                            list.add(p);
                            cache.remove(p);
                        }
                    }
                }, delay);
            }
        } else if (!map.containsKey(p) && !cache.contains(p)) {
            if (isCloth(armor)) {
                ItemMeta meta = armor.getItemMeta();
                List<String> lore = meta.getLore();
                String displayName = meta.getDisplayName();
                int SIZE = lore.size();
                if (SIZE>=4 && lore.get(SIZE-Util.DISPLAYNAME).equalsIgnoreCase(displayName)) {
                    int prePoint = Util.getInt(lore.get(SIZE-Util.POINT));
                    if (prePoint < Util.getInt(lore.get(SIZE-Util.MAXPOINT)))
                        map.put(p,Util.getInt(lore.get(SIZE-Util.RESTORESPEED)));
                }
            }
        }
        if (list.contains(p)) {
            if (isCloth(armor)) {
                ItemMeta meta = armor.getItemMeta();
                List<String> lore = meta.getLore();
                String displayName = meta.getDisplayName();
                int SIZE = lore.size();
                if (SIZE>=4 && lore.get(SIZE-Util.DISPLAYNAME).equalsIgnoreCase(displayName)) {
                    int prePoint = Util.getInt(lore.get(SIZE-Util.POINT));
                    if (prePoint < Util.getInt(lore.get(SIZE-Util.MAXPOINT))) {
                        prePoint++;
                        lore.set(SIZE-Util.POINT,"§a防御点数:§e"+prePoint);
                        meta.setLore(lore);
                        armor.setItemMeta(meta);
                        ActionBar.sendAction(p,ChatColor.GOLD +"恢复装甲护盾值1.");
                        list.remove(p);
                        if (prePoint < Util.getInt(lore.get(SIZE-Util.MAXPOINT))) {
                            map.put(p,Util.getInt(lore.get(SIZE-Util.RESTORESPEED)));
                        }
                    } else {
                        list.remove(p);
                    }
                }
            }
        }
    }

    boolean isCloth(ItemStack is) {
        if (is==null || is.getType() == Material.AIR) return false;
        if (!is.hasItemMeta() || !is.getItemMeta().hasLore()) return false;
        if (!is.getItemMeta().hasDisplayName()) return false;
        return true;
    }
}
