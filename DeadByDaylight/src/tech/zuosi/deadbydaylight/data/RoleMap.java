package tech.zuosi.deadbydaylight.data;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tech.zuosi.deadbydaylight.role.Role;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/7/8.
 */
public class RoleMap {
    private Map<Player,Role> playerRole = new HashMap<>();

    public RoleMap() {}

    public void storeRole(Player player,Role role) {
        playerRole.put(player,role);
    }

    public Role queryRole(Player player) {
        if (!playerRole.containsKey(player)) {
            player.sendMessage(ChatColor.RED
                    + "Wrong,because System find a player without being a role in this game!");
            //±¨´í
            return null;
        }
        return playerRole.get(player);
    }

}
