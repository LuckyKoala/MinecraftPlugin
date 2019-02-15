package tech.zuosi.koalaitem.handler.interact.event;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.koalaitem.skill.Skill;
import tech.zuosi.koalaitem.skill.SkillHandler;
import tech.zuosi.koalaitem.skill.SkillOperator;
import tech.zuosi.koalaitem.type.SkillType;

import java.util.List;
import java.util.Random;

/**
 * Created by iwar on 2016/7/27.
 */
public class PlayerInteract implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getItemInHand();
        SkillHandler skillHandler = new SkillHandler(player,is, SkillType.Trigger.RIGHT);
        if (!skillHandler.shouldExecute()) {
            return;
        }
        List<Skill> skills = skillHandler.getSkills();

        if (Action.RIGHT_CLICK_AIR == e.getAction()) {
            for (Skill skill : skills) {
                if (new Random().nextInt(100) > skill.getChance()) return;

                SkillOperator skillOperator = new SkillOperator(player,skill,is);
                //HashSet<Material> materials = new HashSet<>();
                //materials.add(Material.AIR);

                switch (skill.getEffectType()) {
                    case POTION_SELF:
                        String[] potionValue = skill.getEffectValue().split("-");
                        if (potionValue.length != 2) return;
                        skillOperator.definePotionEffect(new PotionEffect(PotionEffectType.getByName(potionValue[0]),
                                (int)skill.getActivateTick(),Integer.parseInt(potionValue[1])-1))
                                .addPotionEffect(player);
                        break;
                    case COMMAND:
                        skillOperator.defineCommand(skill.getEffectValue()).useCommand();
                        break;
                    case LIGHTNINGSTRIKE:
                        //skillOperator.defineLocation(player.getTargetBlock(materials,16).getRelative(BlockFace.UP).getLocation()).lightningstrike();
                        skillOperator.defineLocation(player.getLocation().getBlock().getRelative(BlockFace.NORTH,5).getLocation()).lightningstrike();
                        break;
                    case FIREBALL:
                        skillOperator.defineLocation(player.getLocation().getBlock().getRelative(BlockFace.NORTH,5).getLocation()).thorwFireball();
                        break;
                    case BLOODSUCK:
                    case DODGE:
                    case CRITICAL:
                    case THORN:
                        skillOperator.activateEffect();
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onRightClickAtEntity(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getItemInHand();
        SkillHandler skillHandler = new SkillHandler(player,is, SkillType.Trigger.RIGHT);
        if (!skillHandler.shouldExecute()) return;
        List<Skill> skills = skillHandler.getSkills();
        if (!(e.getRightClicked() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) e.getRightClicked();

        for (Skill skill : skills) {
            SkillOperator skillOperator = new SkillOperator(player,skill,is);
            switch (skill.getEffectType()) {
                case POTION_OTHER:
                    String[] potionValue = skill.getEffectValue().split("-");
                    if (potionValue.length != 2) return;
                    skillOperator.definePotionEffect(new PotionEffect(PotionEffectType.getByName(potionValue[0]),
                            (int)skill.getActivateTick(),Integer.parseInt(potionValue[1])-1))
                            .addPotionEffect(entity);
                    return;
            }
        }
    }
}
