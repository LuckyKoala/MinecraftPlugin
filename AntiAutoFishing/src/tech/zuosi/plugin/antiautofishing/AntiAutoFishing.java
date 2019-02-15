package tech.zuosi.plugin.antiautofishing;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/6/30.
 * Updated by iwar on 2017/10/3.
 */
public class AntiAutoFishing extends JavaPlugin implements Listener {
    Map<Player,Integer> echoCounter = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("AntiAutoFishing has been loaded.");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("AntiAutoFishing has been unloaded.");
    }

    @EventHandler
    public void onFishing(PlayerFishEvent event) {
        Player player = event.getPlayer();
        PlayerFishEvent.State state = event.getState();

        int firstDirtIndex = player.getInventory().first(Material.DIRT);
        boolean foundDirt = firstDirtIndex != -1;
        if (foundDirt) {
            ItemStack dirt = player.getInventory().getItem(firstDirtIndex);
            int amount = dirt.getAmount();
            if (amount > 1) dirt.setAmount(amount-1);
            else if (amount == 1) {
                player.getInventory().clear(firstDirtIndex);
                player.updateInventory();
            }
            else player.sendMessage(ChatColor.RED + "[AAF]错误!");
        }

        ItemStack hook = player.getItemInHand();
        switch (state) {
            case FISHING:
                //Only forbid fishing when the plugin didn't find dirt
                if (!foundDirt) {
                    if (echoCounter.containsKey(player) && echoCounter.get(player) != 5) {
                        echoCounter.put(player, echoCounter.get(player) + 1);
                    } else {
                        echoCounter.put(player, 1);
                        player.sendMessage(ChatColor.RED + "[AAF]对不起，为了防止自动钓鱼，" +
                                "在你钓鱼时我们需要一些土方留备在你的背包里，如果没有将无法进行。");
                    }
                    event.setCancelled(true);
                }
                break;
            case CAUGHT_FISH:
            case CAUGHT_ENTITY:
            case IN_GROUND:
            case FAILED_ATTEMPT:
                //No matter what happens after fishing, return one dirt back to player's hand.
                player.setItemInHand(new ItemStack(Material.DIRT,1));
                //Drop hook so player must restart fishing by picking up the hook and throw it out.
                player.getWorld().dropItem(player.getLocation().add(1,0,1),hook);
                break;
            default:
                break;
        }
    }
}
