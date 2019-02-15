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
    public static Map<Player,Boolean> canAffect = new HashMap<>();    //һ���Ա�ǩ
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
            if ("ǿ�����".equals(e.getItem().getItemMeta().getDisplayName())) {
                if (!e.getPlayer().hasPermission("visualintensify.use")) {
                    e.getPlayer().sendMessage(ChatColor.RED+"û��Ȩ�޴����Ŷ~");
                }
                if (!e.getItem().getItemMeta().getLore().get(0).equals("vi:ǿ�����")) return;
                if (Action.RIGHT_CLICK_AIR.equals(e.getAction())) {
                    //��GUI
                    e.getPlayer().closeInventory();
                    e.getPlayer().openInventory(new MenuManager(plugin).createGUI(MenuManager.MenuType.MAIN));
                    e.getPlayer().sendMessage(ChatColor.GOLD+"ǿ������ѿ���~");
                } else if (Action.LEFT_CLICK_AIR.equals(e.getAction())) {
                    //�ɼ���Ч��
                    e.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+"���ŭ�Ĵ���һ�¿�����~0w0");
                    e.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+"����Ҫ����ǿ����壬���Ҽ�����������~0w0");
                    e.getPlayer().sendMessage(ChatColor.YELLOW+"�벻Ҫ����������۸�������Ŷ~QAQ");
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (EntityType.PLAYER.equals(e.getDamager().getType())) {
            Player player = (Player)e.getDamager();
            //�ж�����Gem
            if (null == gemOnAffect.get(player)) return;
            String s = gemOnAffect.get(player);
            if (hasItemInHandANDhasItemMeta(player)) {
                if ("�����˺�����".equals(s)) {
                    LoreManager lm = new LoreManager(plugin,player.getItemInHand());
                    if (lm.hasGem() > 0) {
                        if ("�����˺�����".equals(lm.getGemName())) {
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
                } else if ("������������".equals(s)) {
                    LoreManager lm = new LoreManager(plugin,player.getItemInHand());
                    if (lm.hasGem() > 0) {
                        if ("������������".equals(lm.getGemName())) {
                            int chanceAttack = plugin.getConfig().getInt("gem.GreatAttack.chance") + plugin.getConfig().getInt("gem.GreatAttack.chance_up");
                            if (new Probability(plugin).getResult(chanceAttack)) {
                                double extraAttack = plugin.getConfig().getDouble("gem.GreatAttack.percent");
                                double finalDamage = e.getDamage() * extraAttack / 100;
                                e.setDamage(finalDamage);
                            }
                        }
                    }
                    return;
                } else if ("��Ѫ".equals(s)) {
                    if (hasItemInHandANDhasItemMeta(player)) {
                        LoreManager lm = new LoreManager(plugin,player.getItemInHand());
                        if (lm.hasGem() > 0) {
                            if ("��Ѫ".equals(lm.getGemName())) {
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
            if ("����".equals(gemOnAffect.get(player))) {
                if (hasItemInHandANDhasItemMeta(player)) {
                    LoreManager lm = new LoreManager(plugin,player.getItemInHand());
                    if (lm.hasGem() > 0) {
                        if ("��Ѫ".equals(lm.getGemName())) {
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
        //�ж�������ر�ʯ
        if (null != e.getEntity().getKiller()) {
            if ("����ӳ�".equals(gemOnAffect.get(e.getEntity().getKiller()))) {
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
                if ("�����ƶ�".equals(lm.getGemName())) {
                    //�ж�������ر�ʯ
                    int amplifier = plugin.getConfig().getInt("gem.Speed.amplifier");
                    if ("�����ƶ�".equals(gemOnAffect.get(player))) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20,amplifier),true);
                    }
                } else {
                    //�����������ϣ��ָ������ٶ�
                    player.removePotionEffect(PotionEffectType.SPEED);
                }
            }
        }

    }

    //ĩӰ
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
                if ("ĩӰ".equals(lm.getTrackName())) {
                    Location to = e.getTo();
                    Location from = e.getFrom();
                    Location loc = player.getLocation();
                    if(to.getX() == (double)from.getBlockX() && to.getY() == from.getY() && to.getZ() == from.getZ()) {
                        e.setCancelled(false);
                    } else {
                        loc.setY(loc.getY());
                        player.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1, 100);
                    }
                } else if ("����".equals(lm.getTrackName())) {
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
