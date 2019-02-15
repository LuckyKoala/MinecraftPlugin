package tech.zuosi.koalaitem.skill;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.koalaitem.type.SkillType;

import java.util.List;
import java.util.Random;

/**
 * Created by iwar on 2016/7/30.
 */
public class SkillCaller {

    public SkillCaller() {}

    public void call(Player player, ItemStack is, Entity entity, SkillType.Trigger trigger) {
        //¼¼ÄÜ´¥·¢
        SkillHandler skillHandler = new SkillHandler(player,is, trigger);
        if (!skillHandler.shouldExecute()) return;
        List<Skill> skills = skillHandler.getSkills();
        String[] potionValue;

        for (Skill skill : skills) {
            if (new Random().nextInt(100) > skill.getChance()) return;

            SkillOperator skillOperator = new SkillOperator(player,skill,is);
            if (skill.getEffectValue() == null) {
                potionValue = new String[]{};
            } else {
                potionValue = skill.getEffectValue().split("-");
            }

            switch (skill.getEffectType()) {
                case POTION_SELF:
                    if (potionValue.length != 2) return;
                    skillOperator.definePotionEffect(new PotionEffect(PotionEffectType.getByName(potionValue[0]),
                            (int)skill.getActivateTick(),Integer.parseInt(potionValue[1])-1))
                            .addPotionEffect(player);
                    break;
                case POTION_OTHER:
                    if (potionValue.length != 2) return;
                    if (!(entity instanceof LivingEntity)) return;
                    LivingEntity livingEntity = (LivingEntity) entity;
                    skillOperator.definePotionEffect(new PotionEffect(PotionEffectType.getByName(potionValue[0]),
                            (int)skill.getActivateTick(),Integer.parseInt(potionValue[1])-1))
                            .addPotionEffect(livingEntity);
                    return;
                case COMMAND:
                    skillOperator.defineCommand(skill.getEffectValue()).useCommand();
                    break;
                case LIGHTNINGSTRIKE:
                    if (entity == null) return;
                    skillOperator.defineLocation(entity.getLocation()).lightningstrike();
                    break;
                case FIREBALL:
                    if (entity == null) return;
                    skillOperator.defineLocation(entity.getLocation()).thorwFireball();
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
