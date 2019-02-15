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
public class RemoveMenu implements Listener,SlotValue {
    private VisualIntensify plugin;

    public RemoveMenu(VisualIntensify plugin) {
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
                    //摘取-REMOVE
                    if ("摘取".equals(CLICK_DISPLAYNAME)) {
                        //必要的材料是否为空
                        if (mm.isNothing(editor,MATERIAL)) {
                            p.sendMessage(ChatColor.RED+"没有放入待摘取的武器！");
                            e.setCancelled(true);
                            return;
                        } else if (mm.isNothing(editor,DOWN)) {
                            p.sendMessage(ChatColor.RED+"没有放入采集工具哦~");
                            e.setCancelled(true);
                            return;
                        }
                        ItemStack material = editor.getItem(MATERIAL);
                        ItemStack axe = editor.getItem(DOWN);
                        //检查物品是否支持镶嵌
                        if (!mm.canOperateType(material)) {
                            p.sendMessage(ChatColor.RED+"该物品无法采集");
                            e.setCancelled(true);
                            return;
                        }
                        //实例化对象，方便进行下一步操作
                        LoreManager lm = new LoreManager(plugin,material);
                        //剪刀是不是vi采集工具
                        if (Material.SHEARS.equals(axe.getType())) {
                            if (new LoreManager(plugin,axe).isITEM()) {
                                if (lm.canRemoveGem()) {
                                    //成功
                                    editor.setItem(PRODUCT_GEM,lm.getGem());
                                    lm.removeGem(p);
                                    editor.setItem(PRODUCT,material);                          //产物
                                    editor.setItem(MATERIAL,new ItemStack(Material.AIR));      //原料
                                    editor.setItem(DOWN,new ItemStack(Material.AIR));          //采集工具
                                    p.sendMessage(ChatColor.GOLD+"宝石摘取完毕");
                                    e.setCancelled(true);
                                } else {
                                    //失败
                                    p.sendMessage(ChatColor.RED+"摘取失败,请检查物品Socket");
                                    e.setCancelled(true);
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.RED+"非法的采集工具");
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
