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
                ChatColor.GOLD + "֧����ת�Ĳ˵���",
                ChatColor.GREEN + "���죬�ϳɣ���Ʒ������"
        }).initData("PANE", ItemType.INFO,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.YELLOW + "�������").getItemStack();
        bookMaker = new NBTUtil(bookMaker).initLore(new String[]{
                ChatColor.GOLD + "�������������ǿ������Ƕ��ת��������.",
                ChatColor.GREEN + "��������ť���ɴ򿪶���˵�."
        }).initData("MakerUI", ItemType.MENU,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.GREEN + "������ת").getItemStack();
        bookCraft = new NBTUtil(bookCraft).initLore(new String[]{
                ChatColor.GOLD + "�ϳɰ������ֹ��ߵĺϳ�.",
                ChatColor.GREEN + "��������ť���ɴ򿪺ϳɲ˵�."
        }).initData("CraftUI", ItemType.MENU,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.GREEN + "������ת").getItemStack();
        bookItem = new NBTUtil(bookItem).initLore(new String[]{
                ChatColor.GOLD + "����ֱ����ȡ���ֹ���.",
                ChatColor.GREEN + "��������ť���ɴ���Ʒ�˵�."
        }).initData("ItemUI", ItemType.MENU,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.GREEN + "������ת").getItemStack();
        bookBrew = new NBTUtil(bookBrew).initLore(new String[]{
                ChatColor.GOLD + "�����Ʒ.",
                ChatColor.GREEN + "��������ť���ɴ�����˵�."
        }).initData("BrewUI", ItemType.MENU,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.GREEN + "������ת").getItemStack();
    }

    @Override
    public Inventory createGUI() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "���˵�");
        inv.setItem(0,bookMaker);
        inv.setItem(1,bookCraft);
        inv.setItem(2,bookItem);
        inv.setItem(3,bookBrew);
        inv.setItem(4,pane);

        return inv;
    }
}
