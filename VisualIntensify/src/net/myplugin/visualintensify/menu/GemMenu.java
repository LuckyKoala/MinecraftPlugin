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
public class GemMenu implements Listener,SlotValue {
    private VisualIntensify plugin;

    public GemMenu(VisualIntensify plugin) {
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
                    //镶嵌-GEM
                    if ("镶嵌".equals(CLICK_DISPLAYNAME)) {
                        if (!p.hasPermission("visualintensify.use.gem")) {
                            p.sendMessage(ChatColor.RED+"你没有权限那么做");
                            e.setCancelled(true);
                            return;
                        }
                        //必要的材料是否为空
                        if (mm.isNothing(editor,MATERIAL)) {
                            p.sendMessage(ChatColor.RED+"没有放入待镶嵌的武器！");
                            e.setCancelled(true);
                            return;
                        } else if (mm.isNothing(editor,UP)) {
                            p.sendMessage(ChatColor.RED+"没有放入宝石哦~");
                            e.setCancelled(true);
                            return;
                        }
                        ItemStack material = editor.getItem(MATERIAL);
                        ItemStack gem = editor.getItem(UP);
                        //检查物品是否支持镶嵌
                        if (!mm.canOperateType(material)) {
                            p.sendMessage(ChatColor.RED+"该物品无法镶嵌");
                            e.setCancelled(true);
                            return;
                        }
                        //实例化对象，方便进行下一步操作
                        LoreManager lm = new LoreManager(plugin,material);
                        //绿宝石是不是vi宝石
                        if (Material.EMERALD.equals(gem.getType())) {
                            if (new LoreManager(plugin,gem).isITEM()) {
                                if (lm.addGem(gem,p)) {
                                    //成功
                                    editor.setItem(PRODUCT,material);                          //产物
                                    editor.setItem(MATERIAL,new ItemStack(Material.AIR));      //原料
                                    editor.setItem(UP,new ItemStack(Material.AIR));            //宝石
                                    p.sendMessage(ChatColor.GOLD+"镶嵌成功");
                                    e.setCancelled(true);
                                } else {
                                    //失败
                                    p.sendMessage(ChatColor.RED+"镶嵌失败,请检查物品强化等级是否达到七级或者已有宝石");
                                    e.setCancelled(true);
                                }
                            } else {
                                e.setCancelled(true);
                            }
                        } else {
                            p.sendMessage(ChatColor.RED+"非法的宝石");
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
