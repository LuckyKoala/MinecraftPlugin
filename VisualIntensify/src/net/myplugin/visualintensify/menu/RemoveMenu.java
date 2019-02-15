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
                    //ժȡ-REMOVE
                    if ("ժȡ".equals(CLICK_DISPLAYNAME)) {
                        //��Ҫ�Ĳ����Ƿ�Ϊ��
                        if (mm.isNothing(editor,MATERIAL)) {
                            p.sendMessage(ChatColor.RED+"û�з����ժȡ��������");
                            e.setCancelled(true);
                            return;
                        } else if (mm.isNothing(editor,DOWN)) {
                            p.sendMessage(ChatColor.RED+"û�з���ɼ�����Ŷ~");
                            e.setCancelled(true);
                            return;
                        }
                        ItemStack material = editor.getItem(MATERIAL);
                        ItemStack axe = editor.getItem(DOWN);
                        //�����Ʒ�Ƿ�֧����Ƕ
                        if (!mm.canOperateType(material)) {
                            p.sendMessage(ChatColor.RED+"����Ʒ�޷��ɼ�");
                            e.setCancelled(true);
                            return;
                        }
                        //ʵ�������󣬷��������һ������
                        LoreManager lm = new LoreManager(plugin,material);
                        //�����ǲ���vi�ɼ�����
                        if (Material.SHEARS.equals(axe.getType())) {
                            if (new LoreManager(plugin,axe).isITEM()) {
                                if (lm.canRemoveGem()) {
                                    //�ɹ�
                                    editor.setItem(PRODUCT_GEM,lm.getGem());
                                    lm.removeGem(p);
                                    editor.setItem(PRODUCT,material);                          //����
                                    editor.setItem(MATERIAL,new ItemStack(Material.AIR));      //ԭ��
                                    editor.setItem(DOWN,new ItemStack(Material.AIR));          //�ɼ�����
                                    p.sendMessage(ChatColor.GOLD+"��ʯժȡ���");
                                    e.setCancelled(true);
                                } else {
                                    //ʧ��
                                    p.sendMessage(ChatColor.RED+"ժȡʧ��,������ƷSocket");
                                    e.setCancelled(true);
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.RED+"�Ƿ��Ĳɼ�����");
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
