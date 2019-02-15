package tech.zuosi.deadbydaylight.listener.rule;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.deadbydaylight.data.RoleMap;
import tech.zuosi.deadbydaylight.role.JerryRole;
import tech.zuosi.deadbydaylight.role.Role;
import tech.zuosi.deadbydaylight.util.MediaUtil;

/**
 * Created by iwar on 2016/7/8.
 */
public class RoleMoveListener implements Listener {
    private RoleMap roleMap = new RoleMap();

    @EventHandler
    public void onRoleMove(PlayerMoveEvent pme) {
        Player player = pme.getPlayer();
        Role role = roleMap.queryRole(player);
        if (role instanceof JerryRole) {
            JerryRole jerry = (JerryRole) role;
            if (player.getWalkSpeed() > 0.1) {
                MediaUtil.showFootstep(player,6);
            } else {
                MediaUtil.showFootstep(player,3);
            }
        }
        int speedAmplifier = role.getSpeedAmplifier();
        if (speedAmplifier > 0) {
            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                return;
            }
            role.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,speedAmplifier));
        }
    }
}
