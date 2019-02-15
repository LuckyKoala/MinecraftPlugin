package tech.zuosi.koalarecipe.handler.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalarecipe.recipe.RecipeLoader;

/**
 * Created by iwar on 2016/8/24.
 */
public class CreateHandler implements Listener {

    //TODO ������ý�������ṩPaperЯ��NBT-�ϳɱ���Ϣ
    @EventHandler
    public void onCreate(InventoryClickEvent ice) {
        Inventory inv = ice.getView().getTopInventory();
        ItemStack is = ice.getCurrentItem();

        if (!(ice.getWhoClicked() instanceof Player)) return;
        Player player = (Player) ice.getWhoClicked();

        if (!("�ϳ����-����".equalsIgnoreCase(inv.getTitle()))) return;
        if (!inv.equals(ice.getClickedInventory())) return;
        if (is.hasItemMeta()) {
            String name = is.getItemMeta().getDisplayName();

            if ((ChatColor.AQUA + "�߽�").equals(name)) {
                ice.setCancelled(true);
            } else if ((ChatColor.RED + "ȡ��").equals(name)) {
                ice.setCancelled(true);
            } else if ((ChatColor.GREEN + "����").equals(name)) {
                String message;

                ice.setCancelled(true);
                if (new RecipeLoader().load(inv)) {
                    //FIXME ���䷽������ƶ�ԭ���ϣ�֮����䷽��ʧЧ��֪�����¶�ȡ�����·���Ϊ��ʱ����
                    player.closeInventory();
                    message = ChatColor.GREEN +"�ɹ�����ϳ��䷽";
                } else {
                    message = ChatColor.RED + "�����䷽ʧ�ܣ�����������Ƿ�Ϊ��";
                }
                player.sendMessage(message);
            }
        }
    }
}
