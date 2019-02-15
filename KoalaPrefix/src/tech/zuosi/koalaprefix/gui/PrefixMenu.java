package tech.zuosi.koalaprefix.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.zuosi.koalaprefix.data.DataManager;
import tech.zuosi.koalaprefix.data.PlayerPrefix;
import tech.zuosi.koalaprefix.data.Prefix;
import tech.zuosi.koalaprefix.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by iwar on 2017/1/25.
 */
public class PrefixMenu {
    private static String title;
    private static int row = 0;
    private static ItemStack infoItem = new ItemStack(Material.PAPER);
    private static ItemStack exitItem = new ItemStack(Material.FENCE_GATE);
    private List<ItemStack> items = new ArrayList<>();
    private HashMap<ItemStack,Prefix> temp = new HashMap<>();
    private static Map<Inventory,Map<Integer,Prefix>> cacheItems = new HashMap<>();

    public PrefixMenu(Player player) {
        if(title==null||row==0) initStatic();
        initDynamic(player);
    }

    public void initStatic() {
        ConfigurationSection section = main.getInstance().getConfig().getConfigurationSection("Menu");
        title = ChatColor.translateAlternateColorCodes('&', section.getString("title", "&3Test"));
        row = section.getInt("row", 4);
        if(row<1||row>6) row=4;
        infoItem = writeLore(infoItem, section.getStringList("infoLore")
                .stream().map(x->ChatColor.translateAlternateColorCodes('&',x)).collect(Collectors.toList()));
        exitItem = writeLore(exitItem, section.getStringList("exitLore")
                .stream().map(x->ChatColor.translateAlternateColorCodes('&',x)).collect(Collectors.toList()));
    }

    public void initDynamic(Player player) {
        PlayerPrefix playerPrefix = DataManager.getCachedPlayerPrefix(player,false);
        Map<String,Prefix> allPrefix = DataManager.getAllPrefixCache();

        for(Map.Entry<String,Prefix> prefixEntry:allPrefix.entrySet()) {
            String prefixName = prefixEntry.getKey();
            Prefix prefix = prefixEntry.getValue();
            ItemStack item = new ItemStack(Material.getMaterial(prefix.getModel()));
            item = writeLore(item, prefix.generateLore(playerPrefix.getPrefixState(prefixName)));
            this.items.add(item);
            temp.put(item,prefix);
        }
    }

    public Inventory generateGUI() {
        System.out.println("Row:"+row);
        int slots = 9*row;
        Inventory inv = Bukkit.createInventory(null, slots, title);
        int size = this.items.size();
        System.out.println("Size:"+size);
        Map<Integer,Prefix> map = new HashMap<>();
        int itemCount = 0;

        inv.setItem(slots-1, exitItem);
        inv.setItem(slots-9, infoItem);
        for(int i=0;i<row;i++) {
            System.out.println("Slot->"+(9*i+1)+" Limit->"+(9*(i+1)-1)+" Boolean->"+((9*i+1)<(9*(i+1)-1)));
            for(int slot=9*i+1;slot<9*(i+1)-1;slot++) {
                if(itemCount>=size) break;
                ItemStack item = this.items.get(itemCount++);
                System.out.println("Item: "+item.getType());
                inv.setItem(slot, item);
                map.put(slot, temp.get(item));
            }
        }
        cacheItems.put(inv,map);

        return inv;
    }

    private static ItemStack writeLore(ItemStack orinal, List<String> lore) {
        ItemMeta meta = orinal.getItemMeta();
        meta.setLore(lore);
        orinal.setItemMeta(meta);
        return orinal;
    }

    public static String getGUITitle() {
        return title;
    }

    public static int getRow() {
        return row;
    }

    public static Map<Integer,Prefix> getCacheItems(Inventory inventory) {
        return cacheItems.get(inventory);
    }

    public static void clearCacheItems(Inventory inventory) {
        cacheItems.remove(inventory);
    }
}
