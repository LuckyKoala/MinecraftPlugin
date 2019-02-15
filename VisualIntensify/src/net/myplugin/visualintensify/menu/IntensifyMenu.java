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
public class IntensifyMenu implements Listener,SlotValue {
    private VisualIntensify plugin;

    public IntensifyMenu(VisualIntensify plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory editor = e.getInventory();
        MenuManager mm = new MenuManager(plugin);
        boolean debug = plugin.getConfig().getBoolean("debug");
        if (editor.getTitle() != null && editor.getName().equalsIgnoreCase("IntensifyPanel")) {
            if (null == e.getCurrentItem()) return;
            ItemStack clicked = e.getCurrentItem();
            if (clicked.getType() == Material.STAINED_GLASS_PANE || clicked.getType() == Material.SIGN) {
                e.setCancelled(true);
            } else if (clicked.hasItemMeta()) {
                if (clicked.getItemMeta().hasDisplayName()) {
                    final String CLICK_DISPLAYNAME = clicked.getItemMeta().getDisplayName();
                    //强化-INTENSIFY
                    if ("强化".equals(CLICK_DISPLAYNAME)) {
                        if (!p.hasPermission("visualintensify.use.intensify")) {
                            p.sendMessage(ChatColor.RED+"你没有权限那么做");
                            e.setCancelled(true);
                            return;
                        }
                        boolean NotToDistory = false;
                        boolean hasLuckygem = false;
                        //是否有安全护符
                        if (!mm.isNothing(editor,DOWN))
                            if (new LoreManager(plugin,editor.getItem(DOWN)).isITEM("§d安全护符"))
                                NotToDistory = true;
                        //必要的材料是否为空
                        if (mm.isNothing(editor,MATERIAL)) {
                            p.sendMessage(ChatColor.RED+"没有放入待强化武器！");
                            e.setCancelled(true);
                            return;
                        } else if (mm.isNothing(editor,UP)) {
                            p.sendMessage(ChatColor.RED+"没有放入强化石哦~");
                            e.setCancelled(true);
                            return;
                        }
                        ItemStack material = editor.getItem(MATERIAL);
                        if (debug) {
                            p.sendMessage(ChatColor.GRAY+"MaterialType:"+material.getType().name());
                        }
                        //检查物品是否支持强化
                        if (!mm.canOperateType(material)) {
                            p.sendMessage(ChatColor.RED+"该物品无法强化");
                            e.setCancelled(true);
                            return;
                        }
                        //实例化对象，方便进行下一步操作
                        LoreManager lm = new LoreManager(plugin,material);
                        if (!mm.isNothing(editor,UP)) {
                            if (new LoreManager(plugin, editor.getItem(UP)).isITEM("§d幸运系强化石")) {
                                hasLuckygem = true;
                            } else if (!new LoreManager(plugin, editor.getItem(UP)).isITEM("§d强化石")) {
                                p.sendMessage(ChatColor.RED+"非法的强化石");
                                e.setCancelled(true);
                                return;
                            }
                        }
                        //判断原材料是否已经初始化
                        if (!lm.isINIT()) {
                            //判断是否初始化成功
                            if (lm.initLore(hasLuckygem)) {
                                //初始化成功
                                editor.setItem(PRODUCT,material);                          //产物
                                editor.setItem(MATERIAL,new ItemStack(Material.AIR));      //原料
                                editor.setItem(UP,new ItemStack(Material.AIR));            //强化石
                                if (NotToDistory) {
                                    editor.setItem(DOWN,new ItemStack(Material.AIR));      //安全护符
                                }
                                p.sendMessage(ChatColor.GOLD+"初次强化成功");
                                e.setCancelled(true);
                            } else {
                                if (!NotToDistory) {
                                    editor.setItem(MATERIAL,new ItemStack(Material.AIR));
                                } else {
                                    editor.setItem(DOWN,new ItemStack(Material.AIR));
                                    editor.setItem(PRODUCT,editor.getItem(MATERIAL));
                                    editor.setItem(MATERIAL,new ItemStack(Material.AIR));
                                }
                                editor.setItem(UP,new ItemStack(Material.AIR));
                                p.sendMessage(ChatColor.RED+"初次强化失败");
                                e.setCancelled(true);
                            }
                        } else {
                            //非初始化，强化
                            if (lm.isIntensifyMax()) {
                                p.sendMessage(ChatColor.GOLD+"你的武器已经强化到十级啦，无法继续强化，尝试转生吧~");
                                e.setCancelled(true);
                                return;
                            }
                            if (!lm.intensifyItem(hasLuckygem)) {
                                if (!NotToDistory) {
                                    editor.setItem(MATERIAL,new ItemStack(Material.AIR));
                                } else {
                                    editor.setItem(DOWN,new ItemStack(Material.AIR));
                                }
                                editor.setItem(UP,new ItemStack(Material.AIR));
                                p.sendMessage(ChatColor.RED+"强化失败");
                                e.setCancelled(true);
                            } else {
                                editor.setItem(PRODUCT,material);                          //产物
                                editor.setItem(MATERIAL,new ItemStack(Material.AIR));      //原料
                                editor.setItem(UP,new ItemStack(Material.AIR));            //强化石
                                if (NotToDistory) {
                                    editor.setItem(DOWN,new ItemStack(Material.AIR));      //安全护符
                                }
                                p.sendMessage(ChatColor.GOLD+"强化成功");
                                e.setCancelled(true);
                                if (lm.isIntensifyMax()) {
                                    plugin.getServer().broadcastMessage(ChatColor.AQUA+"玩家"+p.getPlayerListName()+"刚刚得到了一把十级强化的物品！");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
