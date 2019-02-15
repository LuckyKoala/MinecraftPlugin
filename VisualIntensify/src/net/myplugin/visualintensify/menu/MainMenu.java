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

    //TODO ��ȫ��飬�ر�inventoryʱ����Ƿ�����Ʒû���ó�����ʾ
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
                    //ͨ�ò���-����Ƿ������Ʒ�����ⶪʧ
                    if ("����".equals(CLICK_DISPLAYNAME)) {
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
                    //�˵���ת-MAIN
                    if (NBTUtil.isMenu(clicked)) {
                        if ("��Ʒ�˵�".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            p.openInventory(mm.createGUI(MenuManager.MenuType.ITEM));
                            e.setCancelled(true);
                            return;
                        } else if ("ǿ���˵�".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            p.openInventory(mm.createGUI(MenuManager.MenuType.INTENSIFY));
                            e.setCancelled(true);
                            return;
                        } else if ("��Ƕ�˵�".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            p.openInventory(mm.createGUI(MenuManager.MenuType.GEM));
                            e.setCancelled(true);
                            return;
                        } else if ("ת���˵�".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            p.openInventory(mm.createGUI(MenuManager.MenuType.REBORN));
                            e.setCancelled(true);
                            return;
                        } else if ("ժȡ�˵�".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            p.openInventory(mm.createGUI(MenuManager.MenuType.REMOVE));
                            e.setCancelled(true);
                            return;
                        } else if ("�ر�".equals(CLICK_DISPLAYNAME)) {
                            p.closeInventory();
                            e.setCancelled(true);
                            return;
                        }
                    }
                    //��ȡ��Ʒ-ITEM
                    if (new LoreManager(plugin,clicked).isITEM()) {
                        if (!p.hasPermission("visualintensify.getitem")) {
                            p.sendMessage(ChatColor.RED+"�����������û��Ȩ�޻�������Ʒ~");
                            e.setCancelled(true);
                        } else {
                            //���е�����Ļ�������Ʒ���������Ȩ����ȡ��Ʒ��~
                            //FIXME Ӱ��ԭ���������м�������Ʒ����
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