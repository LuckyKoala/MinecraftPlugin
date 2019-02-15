package tech.zuosi.koalaitem.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/21.
 */
public class MenuUI implements GUICreator {
    private static ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE);
    private static ItemStack bookMaker = new ItemStack(Material.BOOK);
    private static ItemStack bookCraft = new ItemStack(Material.BOOK);
    private static ItemStack bookItem = new ItemStack(Material.BOOK);
    private static ItemStack bookBrew = new ItemStack(Material.BOOK);

    static {
        pane = new NBTUtil(pane).initLore(new String[]{
                ChatColor.GOLD + "支持跳转的菜单有",
                ChatColor.GREEN + "锻造，合成，物品，酿造"
        }).initData("PANE", ItemType.INFO,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.YELLOW + "锻造面板").getItemStack();
        bookMaker = new NBTUtil(bookMaker).initLore(new String[]{
                ChatColor.GOLD + "锻造包括鉴定，强化，镶嵌，转生，提炼.",
                ChatColor.GREEN + "单击本按钮即可打开锻造菜单."
        }).initData("MakerUI", ItemType.MENU,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.GREEN + "界面跳转").getItemStack();
        bookCraft = new NBTUtil(bookCraft).initLore(new String[]{
                ChatColor.GOLD + "合成包括各种工具的合成.",
                ChatColor.GREEN + "单击本按钮即可打开合成菜单."
        }).initData("CraftUI", ItemType.MENU,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.GREEN + "界面跳转").getItemStack();
        bookItem = new NBTUtil(bookItem).initLore(new String[]{
                ChatColor.GOLD + "可以直接拿取各种工具.",
                ChatColor.GREEN + "单击本按钮即可打开物品菜单."
        }).initData("ItemUI", ItemType.MENU,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.GREEN + "界面跳转").getItemStack();
        bookBrew = new NBTUtil(bookBrew).initLore(new String[]{
                ChatColor.GOLD + "酿造酒品.",
                ChatColor.GREEN + "单击本按钮即可打开酿造菜单."
        }).initData("BrewUI", ItemType.MENU,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.GREEN + "界面跳转").getItemStack();
    }

    @Override
    public Inventory createGUI() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "主菜单");
        inv.setItem(0,bookMaker);
        inv.setItem(1,bookCraft);
        inv.setItem(2,bookItem);
        inv.setItem(3,bookBrew);
        inv.setItem(4,pane);

        return inv;
    }
}
