package net.myplugin.visualintensify;

import net.myplugin.visualintensify.lore.LoreManager;
import net.myplugin.visualintensify.menu.MenuManager;
import net.myplugin.visualintensify.util.Probability;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/2/3.
 */
public class EventListener implements Listener {
    private VisualIntensify plugin;
    public static Map<Player,String> gemOnAffect = new HashMap<>();
    public static Map<Player,Boolean> canAffect = new HashMap<>();    //一次性标签
    private boolean debug;

    public EventListener(VisualIntensify plugin) {
        this.plugin = plugin;
        debug = plugin.getConfig().getBoolean("debug");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasItem()) {
            if (!Material.WATCH.equals(e.getItem().getType())) return;
            if (!e.getItem().hasItemMeta()) return;
            if (!e.getItem().getItemMeta().hasDisplayName()) return;
            if (!e.getItem().getItemMeta().hasLore()) return;
            if ("强化面板".equals(e.getItem().getItemMeta().getDisplayName())) {
                if (!e.getPlayer().hasPermission("visualintensify.use")) {
                    e.getPlayer().sendMessage(ChatColor.RED+"没有权限打开面板哦~");
                }
                if (!e.getItem().getItemMeta().getLore().get(0).equals("vi:强化面板")) return;
                if (Action.RIGHT_CLICK_AIR.equals(e.getAction())) {
                    //打开GUI
                    e.getPlayer().closeInventory();
                    e.getPlayer().openInventory(new MenuManager(plugin).createGUI(MenuManager.MenuType.MAIN));
                    e.getPlayer().sendMessage(ChatColor.GOLD+"强化面板已开启~");
                } else if (Action.LEFT_CLICK_AIR.equals(e.getAction())) {
                    //可加音效？
                    e.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+"你愤怒的打了一下空气娘~0w0");
                    e.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+"若是要开启强化面板，请右键抚摸空气娘~0w0");
                    e.getPlayer().sendMessage(ChatColor.YELLOW+"请不要拿着面板娘欺负怪物们哦~QAQ");
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (EntityType.PLAYER.equals(e.getDamager().getType())) {
            Player player = (Player)e.getDamager();
            //判断有无Gem
            if (null == gemOnAffect.get(player)) return;
            String s = gemOnAffect.get(player);
            if (hasItemInHandANDhasItemMeta(player)) {
                if ("暴击伤害提升".equals(s)) {
                    LoreManager lm = new LoreManager(plugin,player.getItemInHand());
                    if (lm.hasGem() > 0) {
                        if ("暴击伤害提升".equals(lm.getGemName())) {
                            if (new Probability(plugin).getResult("gem.GreatAttack.chance")) {
                                double extraAttack = plugin.getConfig().getDouble("gem.GreatAttack.percent")
                                        + plugin.getConfig().getDouble("gem.GreatAttack.percent_up");
                                double finalDamage = e.getDamage() * extraAttack / 100;
                                if (debug) {
                                    player.sendMessage(ChatColor.GRAY+"");
                                    player.sendMessage(ChatColor.GRAY+"FinalDamage:"+finalDamage);
                                }
                                e.setDamage(finalDamage);
                            }
                        }
                    }
                    return;
                } else if ("暴击概率提升".equals(s)) {
                    LoreManager lm = new LoreManager(plugin,player.getItemInHand());
                    if (lm.hasGem() > 0) {
                        if ("暴击概率提升".equals(lm.getGemName())) {
                            int chanceAttack = plugin.getConfig().getInt("gem.GreatAttack.chance") + plugin.getConfig().getInt("gem.GreatAttack.chance_up");
                            if (new Probability(plugin).getResult(chanceAttack)) {
                                double extraAttack = plugin.getConfig().getDouble("gem.GreatAttack.percent");
                                double finalDamage = e.getDamage() * extraAttack / 100;
                                e.setDamage(finalDamage);
                            }
                        }
                    }
                    return;
                } else if ("吸血".equals(s)) {
                    if (hasItemInHandANDhasItemMeta(player)) {
                        LoreManager lm = new LoreManager(plugin,player.getItemInHand());
                        if (lm.hasGem() > 0) {
                            if ("吸血".equals(lm.getGemName())) {
                                if (new Probability(plugin).getResult("gem.BloodSuck.chance")) {
                                    player.setHealth(player.getHealth() + e.getDamage() * plugin.getConfig().getDouble("gem.BloodSuck.percent") / 100);
                                }
                            }
                        }
                    }
                    return;
                } else {
                }
            }
        }
        if (EntityType.PLAYER.equals(e.getEntity().getType())) {
            Player player = (Player) e.getEntity();
            if ("闪避".equals(gemOnAffect.get(player))) {
                if (hasItemInHandANDhasItemMeta(player)) {
                    LoreManager lm = new LoreManager(plugin,player.getItemInHand());
                    if (lm.hasGem() > 0) {
                        if ("吸血".equals(lm.getGemName())) {
                            if (new Probability(plugin).getResult("gem.Evade.chance")) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        //判断有无相关宝石
        if (null != e.getEntity().getKiller()) {
            if ("经验加成".equals(gemOnAffect.get(e.getEntity().getKiller()))) {
                if (new Probability(plugin).getResult("gem.MoreExp.chance")) {
                    int droppedExp = e.getDroppedExp();
                    droppedExp *= 1+plugin.getConfig().getDouble("gem.MoreExp.percent")/100;
                    e.setDroppedExp(droppedExp);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (hasItemInHandANDhasItemMeta(player)) {
            LoreManager lm = new LoreManager(plugin,player.getItemInHand());
            if (lm.hasGem() > 0) {
                if ("快速移动".equals(lm.getGemName())) {
                    //判断有无相关宝石
                    int amplifier = plugin.getConfig().getInt("gem.Speed.amplifier");
                    if ("快速移动".equals(gemOnAffect.get(player))) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20,amplifier),true);
                    }
                } else {
                    //武器不在手上，恢复正常速度
                    player.removePotionEffect(PotionEffectType.SPEED);
                }
            }
        }

    }

    //末影
    @EventHandler
    public void onTrackMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (hasItemInHandANDhasItemMeta(player)) {
            ItemStack itemInHand = player.getItemInHand();
            LoreManager lm = new LoreManager(plugin, itemInHand);
            if (lm.hasTrack()) {
                if (!player.hasPermission("visualintensify.use.zhuiying")) {
                    return;
                }
                if ("末影".equals(lm.getTrackName())) {
                    Location to = e.getTo();
                    Location from = e.getFrom();
                    Location loc = player.getLocation();
                    if(to.getX() == (double)from.getBlockX() && to.getY() == from.getY() && to.getZ() == from.getZ()) {
                        e.setCancelled(false);
                    } else {
                        loc.setY(loc.getY());
                        player.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1, 100);
                    }
                } else if ("烈焰".equals(lm.getTrackName())) {
                    Location to = e.getTo();
                    Location from = e.getFrom();
                    Location loc = player.getLocation();
                    if(to.getX() == (double)from.getBlockX() && to.getY() == from.getY() && to.getZ() == from.getZ()) {
                        e.setCancelled(false);
                    } else {
                        loc.setY(loc.getY() - 1.0D);
                        player.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1, 100);
                    }
                }
            }
        }

    }

    public boolean hasItemInHandANDhasItemMeta(Player _player) {
        if (null != _player.getItemInHand() || !Material.AIR.equals(_player.getItemInHand().getType())) {
            final ItemStack ITEMINHAND = _player.getItemInHand();
            if (ITEMINHAND.hasItemMeta()) {
                return true;
            }
        }
        return false;
    }
}
