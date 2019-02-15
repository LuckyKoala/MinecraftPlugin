package tech.zuosi.koalarecipe.handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalarecipe.recipe.RecipeLoader;
import tech.zuosi.koalarecipe.util.ItemLore;

/**
 * Created by iwar on 2016/8/23.
 */
public class ChestPanel {
    private static ItemStack glass, left, right;
    private static ItemLore itemLore = new ItemLore();
    private static final int[] paneSlot = new int[]{
            0,8,
            9,17,
            18,26,
            27,35,
            36,37,38,39,40,41,42,43,44,
            45,48,50,53
    };
    private static final int[] cancelSlot = new int[]{
            46,47
    };
    private static final int[] saveSlot = new int[]{
            51,52
    };

    static {
        glass = new ItemStack(Material.STAINED_GLASS_PANE);
        right = new ItemStack(Material.STAINED_GLASS_PANE);
        left = new ItemStack(Material.STAINED_GLASS_PANE);

        glass.setDurability((short)15);
        right.setDurability((short)13);
        left.setDurability((short)14);

        glass = itemLore.item(glass).name(ChatColor.AQUA + "边界").build();
        left = itemLore.item(left).name(ChatColor.RED + "取消").build();
    }

    public ChestPanel() {}

    public Inventory panel(boolean setting) {
        String title = setting?"合成面板-设置":"合成面板";
        Inventory inv = Bukkit.createInventory(null, 54, title);

        right = setting? itemLore.item(right).name(ChatColor.GREEN + "保存").build():itemLore.name(ChatColor.GREEN + "合成").build();
        for (int slot : paneSlot) {
            inv.setItem(slot, glass);
        }
        for (int slot : cancelSlot) {
            inv.setItem(slot, right);
        }
        for (int slot : saveSlot) {
            inv.setItem(slot, right);
        }

        return inv;
    }

    public static void clearMaterial(Inventory inv) {
        for (int slot : RecipeLoader.MATERIALSLOT) {
            inv.clear(slot);
        }
    }
}
