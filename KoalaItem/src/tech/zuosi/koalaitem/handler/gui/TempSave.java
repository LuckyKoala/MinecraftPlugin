package tech.zuosi.koalaitem.handler.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/7/21.
 */
public class TempSave {
    private Map<Player,Inventory> reopenCache = new HashMap<>();
    public static TempSave SAVER;

    static {
        SAVER = new TempSave();
    }

    public void save(Player p,Inventory inv) {
        reopenCache.put(p,inv);
    }

    public boolean reopen(Player p) {
        Inventory inv = reopenCache.get(p);
        if (inv == null) return false;
        p.openInventory(inv);
        return true;
    }
}
