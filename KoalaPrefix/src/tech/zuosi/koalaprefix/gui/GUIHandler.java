package tech.zuosi.koalaprefix.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import tech.zuosi.koalaprefix.ChannelUtil;
import tech.zuosi.koalaprefix.data.DataManager;
import tech.zuosi.koalaprefix.data.PlayerPrefix;
import tech.zuosi.koalaprefix.data.Prefix;

import java.util.Map;

/**
 * Created by iwar on 2017/1/25.
 */
public class GUIHandler implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ChannelUtil.getInstance().getPlayerPrefix(event.getPlayer(),event.getPlayer().getName());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getView().getTopInventory();
        int rawSlot = event.getRawSlot();
        Player player = (Player)event.getWhoClicked();

        if(!PrefixMenu.getGUITitle().equals(inv.getTitle())) return;
        event.setCancelled(true);
        if(rawSlot==9*PrefixMenu.getRow()-9) {
            //Info
        } else if(rawSlot==9*PrefixMenu.getRow()-1) {
            //Exit
        } else {
            //PrefixItem
            //Click->Try change using-prefix->close
            Map<Integer,Prefix> itemMap = PrefixMenu.getCacheItems(inv);
            Prefix prefix = itemMap.get(rawSlot);
            if(prefix!=null) {
                PlayerPrefix playerPrefix = DataManager.getCachedPlayerPrefix(player,false);
                Prefix.PrefixState state = playerPrefix.getPrefixState(prefix.getName());

                switch (state) {
                    case UNOWNED:
                        player.sendMessage(ChatColor.RED+"该称号无法直接购买");
                        break;
                    case UNOWNED_CANBUY:
                        boolean result = DataManager.setState(player,prefix.getName(), Prefix.PrefixState.OWNED,false);
                        if(result) player.sendMessage(ChatColor.GREEN+"称号购买成功，可在菜单切换");
                        else player.sendMessage(ChatColor.RED+"称号购买失败");
                        break;
                    case OWNED:
                        DataManager.setState(player,prefix.getName(), Prefix.PrefixState.OWNED_USING,false);
                        player.sendMessage(ChatColor.GOLD+"已切换当前称号为 ["
                                + ChatColor.translateAlternateColorCodes('&',playerPrefix.getUsingPrefix()) + "]");
                        break;
                    case OWNED_USING:
                        break;
                }
            }
        }
        safeClose(player,inv);
    }

    private void safeClose(Player player,Inventory inv) {
        PrefixMenu.clearCacheItems(inv);
        player.closeInventory();
    }
}
