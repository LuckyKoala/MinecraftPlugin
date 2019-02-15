package tech.zuosi.powerfulmobs.listener.fight;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.powerfulmobs.PowerfulMobs;
import tech.zuosi.powerfulmobs.listener.fight.util.PowerMob;
import tech.zuosi.powerfulmobs.util.MobInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/4/28.
 */
public class PMSpider implements Listener,PowerMob {
    Map<Player, Integer> limitMap = new HashMap<>();
    ArrayList<Player> limitList = new ArrayList<>();
    ArrayList<Entity> activatedSpider = new ArrayList<>();
    private PowerfulMobs plugin;

    public PMSpider(PowerfulMobs plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean validateType(Entity monster, Entity player) {
        if (monster == null || player == null) return false;
        if (monster.getType() == EntityType.SPIDER || monster.getType() == EntityType.CAVE_SPIDER)
            if (player.getType() == EntityType.PLAYER)
                return true;
        return false;
    }

    @EventHandler
    public void whenTarget(EntityTargetLivingEntityEvent var1) {
        Entity entity = var1.getEntity();
        LivingEntity target = var1.getTarget();
        if (!validateType(entity, target)) return;
        int mobLevel = new MobInfo().getMobInfo(entity).ordinal();
        if (mobLevel >= 1) {
            if (entity.getType() == EntityType.CAVE_SPIDER) {
                CaveSpider spider = (CaveSpider) entity;
                spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 0));
            } else {
                Spider spider = (Spider) entity;
                spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 0));
            }
        }
    }

    @Override
    @EventHandler
    public void addBuff(EntityDamageByEntityEvent var2) {
        Entity damager = var2.getDamager();
        Entity defender = var2.getEntity();
        if (PowerfulMobs.DEBUG) {
            if (defender instanceof Damageable) {
                Damageable damageable = (Damageable) defender;
                System.out.println("当前血量:"+damageable.getHealth());
                System.out.println("最大血量:"+damageable.getMaxHealth());
            }
            System.out.println("Damager:"+damager.getType().name());
            System.out.println("Defender:"+defender.getType().name());
            System.out.println("造成伤害:"+var2.getDamage());
        }
        if (!validateType(damager, defender)) return;
        int mobLevel = new MobInfo().getMobInfo(damager).ordinal();
        Player player = (Player) defender;
        boolean isActivate = activatedSpider.contains(damager);
        if (mobLevel == 2) {
            if (!isActivate) {
                limitMap.put(player, 2);
                activatedSpider.add(damager);
            }
        } else if (mobLevel >= 3) {
            if (!isActivate) {
                limitMap.put(player, 5);
                activatedSpider.add(damager);
            }
            if (mobLevel >= 4) {
                player.getLocation().getBlock().setType(Material.WEB);
                if (mobLevel == 6) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
                }
            }
        }
    }

    @EventHandler
    public void increaseDamage(EntityDamageByEntityEvent var3) {
        Entity damager = var3.getDamager();
        Entity defender = var3.getEntity();
        if (!validateType(damager, defender)) return;
        int mobLevel = new MobInfo().getMobInfo(damager).ordinal();
        double damage = var3.getDamage();
        switch (mobLevel) {
            case 0:
                break;
            case 1:
                damage *= 1.1;
                break;
            case 2:
                damage *= 1.3;
                break;
            case 3:
                damage *= 1.5;
                break;
            case 4:
                damage *= 1.5;
                break;
            case 5:
                damage *= 1.7;
                break;
            case 6:
                damage *= 2.0;
                break;
            default:
                break;
        }
        var3.setDamage(damage);
    }

    @EventHandler
    public void limitMovement(PlayerMoveEvent var4) {
        long delay = 20L;
        Player player = var4.getPlayer();
        if (limitMap.containsKey(player)) {
            delay *= limitMap.get(player);
            limitList.add(player);
            limitMap.remove(var4.getPlayer(), limitMap.get(var4.getPlayer()));
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () ->
                    limitList.remove(player), delay);
        }
        if (limitList.contains(player)) {
            var4.setTo(var4.getFrom());
        }
    }
}
