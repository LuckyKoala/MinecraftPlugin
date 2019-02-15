package tech.zuosi.bettercloth.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.zuosi.bettercloth.BetterCloth;

import java.util.ArrayList;
import java.util.List;

public class Item {
	private static BetterCloth sr;
	public Item(BetterCloth arg){
		Item.sr=arg;
	}
    public static ItemStack getItem(String name){
    	@SuppressWarnings("deprecation")
        ItemStack item = new ItemStack(sr.getConfig().getInt("item."+name+".ItemID"));
    	ItemMeta meta = item.getItemMeta();
    	List<String> c = sr.getConfig().getStringList("item."+name+".Lore");
    	List<String> lore = new ArrayList<String>();
    	for(int i = 0;i<c.size();i++){
    		String ver = c.get(i);
    		String d = ver.replaceAll("&","¡ì");
    		lore.add(d);
    	}
    	meta.setLore(lore);
    	String a = sr.getConfig().getString("item."+name+".DisplayName");
    	String a_ = a.replaceAll("&","¡ì");
    	meta.setDisplayName(a_);
    	item.setItemMeta(meta);
    	return item;
    }
}
