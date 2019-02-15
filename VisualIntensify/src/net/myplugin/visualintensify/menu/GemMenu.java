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
                    //��Ƕ-GEM
                    if ("��Ƕ".equals(CLICK_DISPLAYNAME)) {
                        if (!p.hasPermission("visualintensify.use.gem")) {
                            p.sendMessage(ChatColor.RED+"��û��Ȩ����ô��");
                            e.setCancelled(true);
                            return;
                        }
                        //��Ҫ�Ĳ����Ƿ�Ϊ��
                        if (mm.isNothing(editor,MATERIAL)) {
                            p.sendMessage(ChatColor.RED+"û�з������Ƕ��������");
                            e.setCancelled(true);
                            return;
                        } else if (mm.isNothing(editor,UP)) {
                            p.sendMessage(ChatColor.RED+"û�з��뱦ʯŶ~");
                            e.setCancelled(true);
                            return;
                        }
                        ItemStack material = editor.getItem(MATERIAL);
                        ItemStack gem = editor.getItem(UP);
                        //�����Ʒ�Ƿ�֧����Ƕ
                        if (!mm.canOperateType(material)) {
                            p.sendMessage(ChatColor.RED+"����Ʒ�޷���Ƕ");
                            e.setCancelled(true);
                            return;
                        }
                        //ʵ�������󣬷��������һ������
                        LoreManager lm = new LoreManager(plugin,material);
                        //�̱�ʯ�ǲ���vi��ʯ
                        if (Material.EMERALD.equals(gem.getType())) {
                            if (new LoreManager(plugin,gem).isITEM()) {
                                if (lm.addGem(gem,p)) {
                                    //�ɹ�
                                    editor.setItem(PRODUCT,material);                          //����
                                    editor.setItem(MATERIAL,new ItemStack(Material.AIR));      //ԭ��
                                    editor.setItem(UP,new ItemStack(Material.AIR));            //��ʯ
                                    p.sendMessage(ChatColor.GOLD+"��Ƕ�ɹ�");
                                    e.setCancelled(true);
                                } else {
                                    //ʧ��
                                    p.sendMessage(ChatColor.RED+"��Ƕʧ��,������Ʒǿ���ȼ��Ƿ�ﵽ�߼��������б�ʯ");
                                    e.setCancelled(true);
                                }
                            } else {
                                e.setCancelled(true);
                            }
                        } else {
                            p.sendMessage(ChatColor.RED+"�Ƿ��ı�ʯ");
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
