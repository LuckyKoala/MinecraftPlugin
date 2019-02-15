package net.myplugin.visualintensify.menu;

import net.myplugin.visualintensify.VisualIntensify;
import net.myplugin.visualintensify.lore.LoreManager;
import net.myplugin.visualintensify.util.NBTUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

/**
 * Created by iwar on 2016/1/29.
 */

public class MainMenu implements Listener,SlotValue {
    private VisualIntensify plugin;

    public MainMenu(VisualIntensify plugin) {
        this.plugin = plugin;
    }

    //TODO 安全检查，关闭inventory时检查是否有物品没有拿出，提示
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
                    //通用操作-检测是否放入物品，避免丢失
                    if ("返回".equals(CLICK_DISPLAYNAME)) {
                        if (mm.isNothing(editor, MATERIAL)
                                && mm.isNothing(editor, UP)
                                && mm.isNothing(editor, DOWN)
                                && mm.isNothing(editor, PRODUCT)
                                && mm.isNothing(editor, PRODUCT_GEM)) {
                            p.closeInventory();
                            p.openInventory(mm.createGUI(MenuManager.MenuType.MAIN));
                        } else {
                            p.closeInventory();
                        }
                        e.setCancelled(true);
                        return;
                    }
                    //菜单跳转-MAIN
                    if (NBTUtil.isMenu(clicked)) {
                        if ("物品菜单".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            p.openInventory(mm.createGUI(MenuManager.MenuType.ITEM));
                            e.setCancelled(true);
                            return;
                        } else if ("强化菜单".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            p.openInventory(mm.createGUI(MenuManager.MenuType.INTENSIFY));
                            e.setCancelled(true);
                            return;
                        } else if ("镶嵌菜单".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            p.openInventory(mm.createGUI(MenuManager.MenuType.GEM));
                            e.setCancelled(true);
                            return;
                        } else if ("转生菜单".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            p.openInventory(mm.createGUI(MenuManager.MenuType.REBORN));
                            e.setCancelled(true);
                            return;
                        } else if ("摘取菜单".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            p.openInventory(mm.createGUI(MenuManager.MenuType.REMOVE));
                            e.setCancelled(true);
                            return;
                        } else if ("关闭".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            e.setCancelled(true);
                            return;
                        }
                    }
                    //拿取物品-ITEM
                    if (new LoreManager(plugin,clicked).isITEM()) {
                        if (!p.hasPermission("visualintensify.getitem")) {
                            p.sendMessage(ChatColor.RED+"坏东西！你可没有权限获得这个物品~");
                            e.setCancelled(true);
                        } else {
                            //进行到这里的话，是物品，且玩家有权限拿取物品呢~
                            //FIXME 影响原版正常的中键复制物品机制
                            if (e.getClick().equals(ClickType.MIDDLE)) {
                                ItemStack MAXSTACK_ITEM = clicked.clone();
                                MAXSTACK_ITEM.setAmount(clicked.getMaxStackSize());
                                p.getWorld().dropItem(p.getLocation(),MAXSTACK_ITEM);
                                plugin.getLogger().log(Level.INFO,"dropItem-"+p.getPlayerListName()+"-"+clicked.getType().name());
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}