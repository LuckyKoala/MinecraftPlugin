package tech.zuosi.koalaitem.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/19.
 */
public class MakerUI implements GUICreator {
    private static ItemStack plate = new ItemStack(Material.STONE_PLATE);
    private static ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE);
    private static ItemStack snowball = new ItemStack(Material.SNOW_BALL);
    private static ItemStack slimeball = new ItemStack(Material.SLIME_BALL);

    static {
        plate = new NBTUtil(plate).initLore(new String[]{
                ChatColor.GOLD + "��������,װ������.",
                ChatColor.GREEN + "��������ť���ɿ�ʼ����."
        }).initData("MAKER", ItemType.MENU,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.GREEN + "���찴ť").getItemStack();
        pane = new NBTUtil(pane).initLore(new String[]{
                ChatColor.GOLD + "������̨֧�����¹���",
                ChatColor.GREEN + "������ǿ������Ƕ��ת������ף�����",
                ChatColor.RED + "ת�������ڹ��ߴ�����ת��ʯ��֮���ѡ���ڵڶ���������"
        }).initData("PANE",ItemType.INFO,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.YELLOW + "�������").getItemStack();
        snowball = new NBTUtil(snowball).initLore(new String[]{
                ChatColor.GOLD + "������ʼ����"
        }).initData("SNOWBALL",ItemType.INFO,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.BOLD + "��ʾ��").getItemStack();
        slimeball = new NBTUtil(slimeball).initLore(new String[]{
                ChatColor.GOLD + "������..."
        }).initData("SLIMEBALL",ItemType.INFO,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + "������").getItemStack();
    }

    public MakerUI() {}

    @Override
    public Inventory createGUI() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "����̨");
        inv.setItem(1,pane);
        inv.setItem(3,pane);
        inv.setItem(4,plate);

        return inv;
    }

    @SuppressWarnings("deprecation")
    public void progress(Inventory inv, ItemStack is, Player p) {
        //stage 1
        int i;
        int SIZE = inv.getSize();
        for (i=0;i<SIZE;i++) {
            inv.setItem(i,snowball);
        }
        //stage 2
        Bukkit.getScheduler().runTaskLater(KoalaItem.INSTANCE, new BukkitRunnable() {
            @Override
            public void run() {
                inv.setItem(0,slimeball);
            }
        },20L);
        Bukkit.getScheduler().runTaskLater(KoalaItem.INSTANCE, new BukkitRunnable() {
            @Override
            public void run() {
                inv.setItem(1,slimeball);
            }
        },40L);
        Bukkit.getScheduler().runTaskLater(KoalaItem.INSTANCE, new BukkitRunnable() {
            @Override
            public void run() {
                inv.setItem(2,slimeball);
            }
        },60L);
        Bukkit.getScheduler().runTaskLater(KoalaItem.INSTANCE, new BukkitRunnable() {
            @Override
            public void run() {
                inv.setItem(3,slimeball);
            }
        },80L);
        Bukkit.getScheduler().runTaskLater(KoalaItem.INSTANCE, new BukkitRunnable() {
            @Override
            public void run() {
                inv.setItem(4,slimeball);
            }
        },100L);
        //stage 3
        Bukkit.getScheduler().runTaskLater(KoalaItem.INSTANCE, new BukkitRunnable() {
            @Override
            public void run() {
                inv.setItem(0,new ItemStack(Material.AIR));
                inv.setItem(1,pane);
                inv.setItem(2,is);
                inv.setItem(3,pane);
                inv.setItem(4,plate);
            }
        },120L);
    }
}
