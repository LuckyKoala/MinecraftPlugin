package tech.zuosi.koalaitem.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.item.*;
import tech.zuosi.koalaitem.type.GemType;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/19.
 */
public class ItemUI implements GUICreator {
    private static ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE);

    static {
        pane = new NBTUtil(pane).initLore(new String[]{
                ChatColor.GOLD + "本物品菜单支持以下功能物品",
                ChatColor.GREEN + "鉴定，强化，镶嵌，转生，打孔，熔炼"
        }).initData("PANE", ItemType.INFO,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.YELLOW + "锻造面板").getItemStack();
    }

    @Override
    public Inventory createGUI() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, "物品菜单");
        int size = inv.getSize();
        int i;
        for (i=0;i<size;i++) {
            inv.setItem(i, pane);
        }
        inv.setItem(0,new ItemCore(0).defaultItem());
        inv.setItem(1,new ItemCore(1).defaultItem());
        inv.setItem(2,new ItemReborn().defaultItem());
        inv.setItem(4,new ItemDrill().defaultItem());
        inv.setItem(6,new ItemGem(GemType.BLOOD).defaultItem());
        inv.setItem(7,new ItemGem(GemType.CRITICALCHANCE).defaultItem());
        inv.setItem(8,new ItemGem(GemType.CRITICALDAMAGE).defaultItem());
        inv.setItem(9,new ItemGem(GemType.DODGE).defaultItem());
        inv.setItem(10,new ItemGem(GemType.ANTICRITICAL).defaultItem());
        inv.setItem(11,new ItemGem(GemType.SPEED).defaultItem());
        inv.setItem(12,new ItemGem(GemType.LUCKY).defaultItem());
        inv.setItem(14,new ItemIdentify().defaultItem());
        inv.setItem(16,new ItemIntensify().defaultItem());
        inv.setItem(17,new ItemLuckyIntensify().defaultItem());
        inv.setItem(18,new ItemSafetyIntensify().defaultItem());
        inv.setItem(20,new ItemBottle().defaultItem());

        return inv;
    }
}
