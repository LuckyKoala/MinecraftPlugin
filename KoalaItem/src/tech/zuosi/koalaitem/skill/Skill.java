package tech.zuosi.koalaitem.skill;

import org.bukkit.configuration.ConfigurationSection;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.type.SkillType;

/**
 * Created by iwar on 2016/7/24.
 */
public class Skill implements SkillFactory {
    private int chance;
    private String name,effectValue;
    private long activateTick,cooldownTick;
    private SkillType.Trigger triggerType;
    private SkillType.Effect effectType;
    private ConfigurationSection configurationSection;

    public Skill(String name) {
        this.name = name;
        this.configurationSection = KoalaItem.INSTANCE.getConfig()
                .getConfigurationSection("SkillList").getConfigurationSection(name);
        if (configurationSection == null) return;
        this.cooldownTick = configurationSection.getInt("cooldown")*20L;
        this.triggerType = SkillType.Trigger.valueOf(configurationSection.getString("trigger"));
        this.effectType = SkillType.Effect.valueOf(configurationSection.getString("effect.type"));
        this.chance = configurationSection.getInt("effect.chance",100);
        this.activateTick = configurationSection.getInt("effect.time",0)*20L;
        this.effectValue = configurationSection.getString("effect.value");
    }

    @Override
    public String getName() {
        return name;
    }

    public long getCooldownTick() {
        return cooldownTick;
    }

    @Override
    public SkillType.Effect getEffectType() {
        return effectType;
    }

    @Override
    public SkillType.Trigger getTriggerType() {
        return triggerType;
    }

    @Override
    public String getEffectValue() {
        return effectValue;
    }

    @Override
    public int getChance() {
        return chance;
    }

    @Override
    public long getActivateTick() {
        return activateTick;
    }

    @Override
    public boolean isExist() {
        if ("Î´¼¤»î".equalsIgnoreCase(name)) return false;
        if (configurationSection == null) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Skill u = (Skill) o;
        if (u.getName().equals(getName())) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
