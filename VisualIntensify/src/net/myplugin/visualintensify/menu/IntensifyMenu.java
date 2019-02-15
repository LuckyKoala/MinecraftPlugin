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
                    //ǿ��-INTENSIFY
                    if ("ǿ��".equals(CLICK_DISPLAYNAME)) {
                        if (!p.hasPermission("visualintensify.use.intensify")) {
                            p.sendMessage(ChatColor.RED+"��û��Ȩ����ô��");
                            e.setCancelled(true);
                            return;
                        }
                        boolean NotToDistory = false;
                        boolean hasLuckygem = false;
                        //�Ƿ��а�ȫ����
                        if (!mm.isNothing(editor,DOWN))
                            if (new LoreManager(plugin,editor.getItem(DOWN)).isITEM("��d��ȫ����"))
                                NotToDistory = true;
                        //��Ҫ�Ĳ����Ƿ�Ϊ��
                        if (mm.isNothing(editor,MATERIAL)) {
                            p.sendMessage(ChatColor.RED+"û�з����ǿ��������");
                            e.setCancelled(true);
                            return;
                        } else if (mm.isNothing(editor,UP)) {
                            p.sendMessage(ChatColor.RED+"û�з���ǿ��ʯŶ~");
                            e.setCancelled(true);
                            return;
                        }
                        ItemStack material = editor.getItem(MATERIAL);
                        if (debug) {
                            p.sendMessage(ChatColor.GRAY+"MaterialType:"+material.getType().name());
                        }
                        //�����Ʒ�Ƿ�֧��ǿ��
                        if (!mm.canOperateType(material)) {
                            p.sendMessage(ChatColor.RED+"����Ʒ�޷�ǿ��");
                            e.setCancelled(true);
                            return;
                        }
                        //ʵ�������󣬷��������һ������
                        LoreManager lm = new LoreManager(plugin,material);
                        if (!mm.isNothing(editor,UP)) {
                            if (new LoreManager(plugin, editor.getItem(UP)).isITEM("��d����ϵǿ��ʯ")) {
                                hasLuckygem = true;
                            } else if (!new LoreManager(plugin, editor.getItem(UP)).isITEM("��dǿ��ʯ")) {
                                p.sendMessage(ChatColor.RED+"�Ƿ���ǿ��ʯ");
                                e.setCancelled(true);
                                return;
                            }
                        }
                        //�ж�ԭ�����Ƿ��Ѿ���ʼ��
                        if (!lm.isINIT()) {
                            //�ж��Ƿ��ʼ���ɹ�
                            if (lm.initLore(hasLuckygem)) {
                                //��ʼ���ɹ�
                                editor.setItem(PRODUCT,material);                          //����
                                editor.setItem(MATERIAL,new ItemStack(Material.AIR));      //ԭ��
                                editor.setItem(UP,new ItemStack(Material.AIR));            //ǿ��ʯ
                                if (NotToDistory) {
                                    editor.setItem(DOWN,new ItemStack(Material.AIR));      //��ȫ����
                                }
                                p.sendMessage(ChatColor.GOLD+"����ǿ���ɹ�");
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
                                p.sendMessage(ChatColor.RED+"����ǿ��ʧ��");
                                e.setCancelled(true);
                            }
                        } else {
                            //�ǳ�ʼ����ǿ��
                            if (lm.isIntensifyMax()) {
                                p.sendMessage(ChatColor.GOLD+"��������Ѿ�ǿ����ʮ�������޷�����ǿ��������ת����~");
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
                                p.sendMessage(ChatColor.RED+"ǿ��ʧ��");
                                e.setCancelled(true);
                            } else {
                                editor.setItem(PRODUCT,material);                          //����
                                editor.setItem(MATERIAL,new ItemStack(Material.AIR));      //ԭ��
                                editor.setItem(UP,new ItemStack(Material.AIR));            //ǿ��ʯ
                                if (NotToDistory) {
                                    editor.setItem(DOWN,new ItemStack(Material.AIR));      //��ȫ����
                                }
                                p.sendMessage(ChatColor.GOLD+"ǿ���ɹ�");
                                e.setCancelled(true);
                                if (lm.isIntensifyMax()) {
                                    plugin.getServer().broadcastMessage(ChatColor.AQUA+"���"+p.getPlayerListName()+"�ոյõ���һ��ʮ��ǿ������Ʒ��");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
