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

    //TODO 左侧设置将会额外提供Paper携带NBT-合成表信息
    @EventHandler
    public void onCreate(InventoryClickEvent ice) {
        Inventory inv = ice.getView().getTopInventory();
        ItemStack is = ice.getCurrentItem();

        if (!(ice.getWhoClicked() instanceof Player)) return;
        Player player = (Player) ice.getWhoClicked();

        if (!("合成面板-设置".equalsIgnoreCase(inv.getTitle()))) return;
        if (!inv.equals(ice.getClickedInventory())) return;
        if (is.hasItemMeta()) {
            String name = is.getItemMeta().getDisplayName();

            if ((ChatColor.AQUA + "边界").equals(name)) {
                ice.setCancelled(true);
            } else if ((ChatColor.RED + "取消").equals(name)) {
                ice.setCancelled(true);
            } else if ((ChatColor.GREEN + "保存").equals(name)) {
                String message;

                ice.setCancelled(true);
                if (new RecipeLoader().load(inv)) {
                    //FIXME 当配方保存后，移动原材料，之后该配方会失效，知道重新读取。以下方案为临时方案
                    player.closeInventory();
                    message = ChatColor.GREEN +"成功保存合成配方";
                } else {
                    message = ChatColor.RED + "保存配方失败，请检查产物栏是否为空";
                }
                player.sendMessage(message);
            }
        }
    }
}
