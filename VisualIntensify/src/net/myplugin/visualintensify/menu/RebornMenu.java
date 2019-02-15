package net.myplugin.visualintensify.menu;

import net.myplugin.visualintensify.VisualIntensify;
import net.myplugin.visualintensify.lore.LoreManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by iwar on 2016/7/15.
 */
public class RebornMenu implements Listener,SlotValue {
    private VisualIntensify plugin;

    public RebornMenu(VisualIntensify plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory editor = e.getInventory();
        MenuManager mm = new MenuManager(plugin);
        if (editor.getTitle() != null && editor.getName().equalsIgnoreCase("IntensifyPanel")) {
            if (null == e.getCurrentItem()) return;
            ItemStack clicked = e.getCurrentItem();
            if (clicked.getType() == Material.STAINED_GLASS_PANE || clicked.getType() == Material.SIGN) {
                e.setCancelled(true);
            } else if (clicked.hasItemMeta()) {
                if (clicked.getItemMeta().hasDisplayName()) {
                    final String CLICK_DISPLAYNAME = clicked.getItemMeta().getDisplayName();
                    //转生-REBORN
                    if ("转生".equals(CLICK_DISPLAYNAME)) {
                        if (!p.hasPermission("visualintensify.use.reborn")) {
                            p.sendMessage(ChatColor.RED+"你没有权限那么做");
                            e.setCancelled(true);
                            return;
                        }
                        boolean track_gem = false;
                        //必要的材料是否为空
                        if (mm.isNothing(editor,MATERIAL)) {
                            p.sendMessage(ChatColor.RED+"没有放入待转生的武器！");
                            e.setCancelled(true);
                            return;
                        } else if (mm.isNothing(editor,DOWN)) {
                            p.sendMessage(ChatColor.RED+"没有放入转生石哦~");
                            e.setCancelled(true);
                            return;
                        }
                        if (!mm.isNothing(editor,UP)) {
                            track_gem = true;
                        }
                        ItemStack material = editor.getItem(MATERIAL);
                        ItemStack reborn = editor.getItem(DOWN);
                        //检查物品是否支持镶嵌
                        if (!mm.canOperateType(material)) {
                            p.sendMessage(ChatColor.RED+"该物品无法转生");
                            e.setCancelled(true);
                            return;
                        }
                        //实例化对象，方便进行下一步操作
                        LoreManager lm = new LoreManager(plugin,material);
                        //金粒是不是vi转生石
                        if (Material.NETHER_STAR.equals(reborn.getType())) {
                            if (lm.isRebornMax()) {
                                p.sendMessage(ChatColor.RED+"转生失败,物品de转生级别已经达到最大");
                                e.setCancelled(true);
                                return;
                            }
                            if (new LoreManager(plugin,reborn).isITEM()) {
                                if (lm.rebornItem()) {
                                    //成功
                                    //是不是vi武器核心
                                    if (track_gem) {
                                        if (Material.MAGMA_CREAM.equals(editor.getItem(UP).getType())
                                                || Material.EYE_OF_ENDER.equals(editor.getItem(UP).getType())) {
                                            if (new LoreManager(plugin,editor.getItem(UP)).isITEM()) {
                                                if (lm.addTrack(editor.getItem(UP))) {
                                                    editor.setItem(UP,new ItemStack(Material.AIR));
                                                    p.sendMessage(ChatColor.GOLD+"核心安装完毕");
                                                } else {
                                                    p.sendMessage(ChatColor.RED+"已有武器核心");
                                                }
                                            }
                                        }
                                    }
                                    editor.setItem(PRODUCT,material);                          //产物
                                    editor.setItem(MATERIAL,new ItemStack(Material.AIR));      //原料
                                    editor.setItem(DOWN,new ItemStack(Material.AIR));          //宝石
                                    p.sendMessage(ChatColor.GOLD+"转生成功");
                                    e.setCancelled(true);
                                } else {
                                    //失败
                                    p.sendMessage(ChatColor.RED+"转生失败,请检查物品强化等级是否达到十级");
                                    e.setCancelled(true);
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.RED+"非法的转生石");
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
