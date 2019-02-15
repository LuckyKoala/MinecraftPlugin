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
                    //ת��-REBORN
                    if ("ת��".equals(CLICK_DISPLAYNAME)) {
                        if (!p.hasPermission("visualintensify.use.reborn")) {
                            p.sendMessage(ChatColor.RED+"��û��Ȩ����ô��");
                            e.setCancelled(true);
                            return;
                        }
                        boolean track_gem = false;
                        //��Ҫ�Ĳ����Ƿ�Ϊ��
                        if (mm.isNothing(editor,MATERIAL)) {
                            p.sendMessage(ChatColor.RED+"û�з����ת����������");
                            e.setCancelled(true);
                            return;
                        } else if (mm.isNothing(editor,DOWN)) {
                            p.sendMessage(ChatColor.RED+"û�з���ת��ʯŶ~");
                            e.setCancelled(true);
                            return;
                        }
                        if (!mm.isNothing(editor,UP)) {
                            track_gem = true;
                        }
                        ItemStack material = editor.getItem(MATERIAL);
                        ItemStack reborn = editor.getItem(DOWN);
                        //�����Ʒ�Ƿ�֧����Ƕ
                        if (!mm.canOperateType(material)) {
                            p.sendMessage(ChatColor.RED+"����Ʒ�޷�ת��");
                            e.setCancelled(true);
                            return;
                        }
                        //ʵ�������󣬷��������һ������
                        LoreManager lm = new LoreManager(plugin,material);
                        //�����ǲ���viת��ʯ
                        if (Material.NETHER_STAR.equals(reborn.getType())) {
                            if (lm.isRebornMax()) {
                                p.sendMessage(ChatColor.RED+"ת��ʧ��,��Ʒdeת�������Ѿ��ﵽ���");
                                e.setCancelled(true);
                                return;
                            }
                            if (new LoreManager(plugin,reborn).isITEM()) {
                                if (lm.rebornItem()) {
                                    //�ɹ�
                                    //�ǲ���vi��������
                                    if (track_gem) {
                                        if (Material.MAGMA_CREAM.equals(editor.getItem(UP).getType())
                                                || Material.EYE_OF_ENDER.equals(editor.getItem(UP).getType())) {
                                            if (new LoreManager(plugin,editor.getItem(UP)).isITEM()) {
                                                if (lm.addTrack(editor.getItem(UP))) {
                                                    editor.setItem(UP,new ItemStack(Material.AIR));
                                                    p.sendMessage(ChatColor.GOLD+"���İ�װ���");
                                                } else {
                                                    p.sendMessage(ChatColor.RED+"������������");
                                                }
                                            }
                                        }
                                    }
                                    editor.setItem(PRODUCT,material);                          //����
                                    editor.setItem(MATERIAL,new ItemStack(Material.AIR));      //ԭ��
                                    editor.setItem(DOWN,new ItemStack(Material.AIR));          //��ʯ
                                    p.sendMessage(ChatColor.GOLD+"ת���ɹ�");
                                    e.setCancelled(true);
                                } else {
                                    //ʧ��
                                    p.sendMessage(ChatColor.RED+"ת��ʧ��,������Ʒǿ���ȼ��Ƿ�ﵽʮ��");
                                    e.setCancelled(true);
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.RED+"�Ƿ���ת��ʯ");
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
