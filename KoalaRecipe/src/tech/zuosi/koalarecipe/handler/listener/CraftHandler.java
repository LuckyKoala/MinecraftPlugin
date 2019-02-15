package tech.zuosi.koalarecipe.handler.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalarecipe.KoalaRecipe;
import tech.zuosi.koalarecipe.handler.ChestPanel;
import tech.zuosi.koalarecipe.recipe.RecipeLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iwar on 2016/8/26.
 */
public class CraftHandler implements Listener {
    private static final ItemStack AIR = new ItemStack(Material.AIR);
    private static final int PRODUCTSLOT = 49;
    private static Map<Inventory,List<Integer>> lockedSlot= new HashMap<>();

    public static void setLockedSlot(Inventory inv,List<Integer> slot) {
        lockedSlot.put(inv,slot);
    }

    private void clearLockedSlot(Inventory inv) {
        List<Integer> newLockList = new ArrayList<>();
        lockedSlot.put(inv,newLockList);
    }

    private boolean isAfterCrafting(Inventory inv) {
        if (lockedSlot.get(inv) == null) return false;
        final List<Integer> lock = lockedSlot.get(inv);
        return !lock.isEmpty();
    }

    //��ֹ���������������䶯ԭ��-������ʧ��
    @EventHandler
    public void onCraft(InventoryClickEvent ice) {
        Inventory inv = ice.getView().getTopInventory();
        ItemStack is = ice.getCurrentItem();

        if (!(ice.getWhoClicked() instanceof Player)) return;
        Player player = (Player) ice.getWhoClicked();
        ItemStack product = AIR;

        if (!("�ϳ����".equalsIgnoreCase(inv.getTitle()))) return;
        //����Action
        InventoryAction action = ice.getAction();

        if (KoalaRecipe.onDebug)
            System.out.println("[InventoryAction] " + action.name());

        if (InventoryAction.MOVE_TO_OTHER_INVENTORY == action
                || InventoryAction.COLLECT_TO_CURSOR == action) {
            ice.setCancelled(true);
            return;
        }

        if (!inv.equals(ice.getClickedInventory())) return;
        if (is.hasItemMeta()) {
            String name = is.getItemMeta().getDisplayName();

            if ((ChatColor.AQUA + "�߽�").equals(name)) {
                ice.setCancelled(true);

                return;
            } else if ((ChatColor.RED + "ȡ��").equals(name)) {
                ice.setCancelled(true);

                return;
            } else if ((ChatColor.GREEN + "�ϳ�").equals(name)) {
                product = new RecipeLoader().craft(inv);
                String message;

                ice.setCancelled(true);
                if (product.equals(AIR)) {
                    message = ChatColor.RED + "�䷽������,������Ʒ�ڷ�˳�������";
                } else {
                    message = ChatColor.GREEN + "0.0�ϳ����";
                }
                inv.setItem(PRODUCTSLOT,product);
                player.sendMessage(message);

                return;
            }
        }

        if (PRODUCTSLOT == ice.getSlot()) {
            switch (action) {
                case NOTHING:
                    break;
                case PICKUP_ALL:
                case PICKUP_SOME:
                case PICKUP_HALF:
                case PICKUP_ONE:
                    //Insert Shift Left Feature Here
                    product = ice.getCursor();
                    break;
                default:
                    ice.setCancelled(true);
                    break;
            }

            product = product==null?AIR:product;

            if (!product.equals(AIR)) {
                boolean hasCraft = isAfterCrafting(inv);
                if (!hasCraft) return;

                ChestPanel.clearMaterial(inv);
                clearLockedSlot(inv);
                player.updateInventory();
                //ǿ�Ƹ���ʹ���Ӿ��������
            }
        } else {
            int clickSlot = ice.getSlot();
            if (lockedSlot.get(inv) != null) {
                if (!isAfterCrafting(inv)) return;
                if (!lockedSlot.get(inv).contains(clickSlot)) return;
                product = inv.getItem(PRODUCTSLOT);
                product = product==null?AIR:product;
                if (!AIR.equals(product)) {
                    inv.setItem(PRODUCTSLOT,AIR);
                    player.updateInventory();
                }
            }
        }
    }
}
