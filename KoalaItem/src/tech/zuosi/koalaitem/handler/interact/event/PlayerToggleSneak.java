package tech.zuosi.koalaitem.handler.interact.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.skill.SkillCaller;
import tech.zuosi.koalaitem.type.SkillType;

/**
 * Created by iwar on 2016/7/30.
 */
public class PlayerToggleSneak implements Listener {
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getItemInHand();
        SkillCaller skillCaller = new SkillCaller();
        skillCaller.call(player,is,null, SkillType.Trigger.SHIFT);
    }
}
