package tech.zuosi.koalaitem.skill;

import tech.zuosi.koalaitem.type.SkillType;

/**
 * Created by iwar on 2016/7/24.
 */
public interface SkillFactory {
    long getCooldownTick();
    String getName();
    SkillType.Effect getEffectType();
    SkillType.Trigger getTriggerType();
    boolean isExist();
    String getEffectValue();
    int getChance();
    long getActivateTick();
}
