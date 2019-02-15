package tech.zuosi.powerfulmobs.listener.fight;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.powerfulmobs.PowerfulMobs;
import tech.zuosi.powerfulmobs.listener.fight.util.DistanceUtil;
import tech.zuosi.powerfulmobs.util.ActionBar;
import tech.zuosi.powerfulmobs.util.ArmorUtil;
import tech.zuosi.powerfulmobs.util.MobInfo;
import tech.zuosi.powerfulmobs.util.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iwar on 2016/5/10.
 */
public class PMDistanceAttack implements Listener {
    Map<Entity,Player> targetPair = new HashMap<>();
    ArrayList<Entity> waitToResetTarget = new ArrayList<>();
    public static final long delay = 100L;
    private PowerfulMobs plugin;

    public PMDistanceAttack(PowerfulMobs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void resetTarget(PlayerMoveEvent var1) {
        Location location;
        List<Entity> entityList;
        location = var1.getPlayer().getLocation();
        entityList = var1.getPlayer().getNearbyEntities(location.getX(),location.getY(),location.getZ());
        entityList.stream().filter(entity -> waitToResetTarget.contains(entity)).forEach(entity -> {
            Monster monster = (Monster) entity;
            monster.setTarget(null);
            waitToResetTarget.remove(entity);
        });
    }

    @EventHandler
    public void whenTarget(EntityTargetEvent var) {
        Entity entity,target;
        entity = var.getEntity();
        target = var.getTarget();

        if (entity == null || !Validate.isMonster(entity.getType())) {
            return;
        }
        if (target == null) {
            if (targetPair.containsKey(entity)) {
                targetPair.remove(entity,targetPair.get(entity));
                waitToResetTarget.add(entity);
            }
            return;
        }
        if (target.getType() != EntityType.PLAYER) {
            if (targetPair.containsKey(entity)) {
                targetPair.remove(entity,targetPair.get(entity));
                waitToResetTarget.add(entity);
            }
            return;
        }
        Player player = (Player) target;
        if (!targetPair.containsKey(entity)) {
            double MaxEffectiveDistance = DistanceUtil.getMaxEffectiveDistance(entity);
            if (entity.getLocation().distance(player.getLocation()) >= MaxEffectiveDistance) {
                System.out.println(entity.getLocation().distance(player.getLocation()));
                System.out.println(MaxEffectiveDistance);
                waitToResetTarget.add(entity);
                return;
            }
            targetPair.put(entity,player);
            ActionBar.sendAction(player,ChatColor.RED + ((Monster)entity).getCustomName() + "即将于五秒后对您造成伤害，请做好准备.");
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
                if (targetPair.containsKey(entity)) {
                    if (entity.isDead()) {
                        targetPair.remove(entity,targetPair.get(entity));
                    } else {
                        if (funtionFakeAttack(entity,player)) {
                            System.out.println("3-1-1-2-1");
                        }
                        targetPair.remove(entity,targetPair.get(entity));
                        waitToResetTarget.add(entity);
                    }
                }
            }, delay);
        }
    }

    boolean funtionFakeAttack(Entity entity,Player player) {
        double damage;
        int difficulty,mobLevel;
        EntityType entityType;
        entityType = entity.getType();
        mobLevel = new MobInfo().getMobInfo(entity).ordinal();
        difficulty = entity.getWorld().getDifficulty().ordinal();
        if (!Validate.isMonster(entityType)) return false;
        if (entityType == EntityType.ZOMBIE) {
            //Essential Damage
            damage = (difficulty+2);
            //Increase Damage
            switch (mobLevel) {
                case 1:
                    damage *= PMZombie.zombie1Ratio;
                    break;
                case 2:
                    damage *= PMZombie.zombie2Ratio;
                    break;
                case 3:
                    damage *= PMZombie.zombie3Ratio;
                    break;
                case 4:
                    damage *= PMZombie.zombie4Ratio;
                    break;
            }
            damage *= (1-ArmorUtil.getDamageReducePercent(player.getInventory()));
            if (player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,(int)(damage*25),0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,400,0));
            } else {
                if (player.getHealth() - damage <= 0) {
                    player.setHealth(0);
                } else {
                    player.setHealth(player.getHealth() - damage);
                }
            }
            if (mobLevel >= 2) {
                player.setFireTicks(200);
                if (mobLevel >= 3) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,400,0));
                    if (mobLevel >= 4) {
                        player.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS,400,0)));
                    }
                }
            }
            ActionBar.sendAction(player, ChatColor.RED + ((Zombie)entity).getCustomName() + "对您造成伤害中...");
        } else if (entityType == EntityType.CAVE_SPIDER || entityType == EntityType.SPIDER) {
            //Essential Damage
            damage = 2.1;
            if (entityType == EntityType.CAVE_SPIDER) player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,30,0));
            //Increase Damage
            switch (mobLevel) {
                case 1:
                    damage *= PMOtherMobs.other1Ratio;
                    break;
                case 2:
                    damage *= PMOtherMobs.other2Ratio;
                    break;
                case 3:
                    damage *= PMOtherMobs.other3Ratio;
                    break;
                case 4:
                    damage *= PMOtherMobs.other4Ratio;
                    break;
                case 5:
                    damage *= PMOtherMobs.other5Ratio;
                    break;
                case 6:
                    damage *= PMOtherMobs.other6Ratio;
                    break;
            }
            damage *= (1-ArmorUtil.getDamageReducePercent(player.getInventory()));
            if (player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,(int)(damage*25),0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,400,0));
            } else {
                if (player.getHealth() - damage <= 0) {
                    player.setHealth(0);
                } else {
                    player.setHealth(player.getHealth() - damage);
                }
            }
            if (mobLevel >= 4) {
                player.getLocation().getBlock().setType(Material.WEB);
                if (mobLevel == 6) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
                }
            }
        } else if (entityType == EntityType.PIG_ZOMBIE) {
            //Essential Damage
            damage = (4*difficulty + 1);
            //Increase Damage
            switch (mobLevel) {
                case 1:
                    damage *= PMOtherMobs.other1Ratio;
                    break;
                case 2:
                    damage *= PMOtherMobs.other2Ratio;
                    break;
                case 3:
                    damage *= PMOtherMobs.other3Ratio;
                    break;
                case 4:
                    damage *= PMOtherMobs.other4Ratio;
                    break;
                case 5:
                    damage *= PMOtherMobs.other5Ratio;
                    break;
                case 6:
                    damage *= PMOtherMobs.other6Ratio;
                    break;
            }
            damage *= (1-ArmorUtil.getDamageReducePercent(player.getInventory()));
            if (player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,(int)(damage*25),0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,400,0));
            } else {
                if (player.getHealth() - damage <= 0) {
                    player.setHealth(0);
                } else {
                    player.setHealth(player.getHealth() - damage);
                }
            }
            if (mobLevel == 2) {
                player.setFireTicks(100);
            } else if (mobLevel >= 3) {
                player.setFireTicks(300);
                if (mobLevel >= 4) {
                    player.addPotionEffect((new PotionEffect(PotionEffectType.BLINDNESS,200,0)));
                }
                if (mobLevel >= 5) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,1200,0));
                    if (mobLevel == 6) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,100,0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,100,0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,100,0));
                    }
                }
            }
        } else {
            //Essential Damage
            damage = 0;
            switch (entityType) {
                case BLAZE:
                    break;
                case ENDERMAN:
                    damage += (3*difficulty + 1);
                    break;
                case ENDERMITE:
                    damage += 2;
                    break;
                case GIANT:
                    damage += (25*difficulty);
                    break;
                case SILVERFISH:
                    damage += 1;
                    break;
                default:
                    break;
            }
            //Increase Damage
            switch (mobLevel) {
                case 1:
                    damage *= PMOtherMobs.other1Ratio;
                    break;
                case 2:
                    damage *= PMOtherMobs.other2Ratio;
                    break;
                case 3:
                    damage *= PMOtherMobs.other3Ratio;
                    break;
                case 4:
                    damage *= PMOtherMobs.other4Ratio;
                    break;
                case 5:
                    damage *= PMOtherMobs.other5Ratio;
                    break;
                case 6:
                    damage *= PMOtherMobs.other6Ratio;
                    break;
            }
            damage *= (1-ArmorUtil.getDamageReducePercent(player.getInventory()));
            if (player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,(int)(damage*25),0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,400,0));
            } else {
                if (player.getHealth() - damage <= 0) {
                    player.setHealth(0);
                } else {
                    player.setHealth(player.getHealth() - damage);
                }
            }
        }
        return true;
    }
}
