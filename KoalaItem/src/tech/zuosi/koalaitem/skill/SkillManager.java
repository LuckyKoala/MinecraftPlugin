package tech.zuosi.koalaitem.skill;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.type.SkillType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iwar on 2016/7/28.
 */
public class SkillManager {
    private Player player;
    //private static Map<Player,List<SkillType.Effect>> activateSkillEffect = new HashMap<>();

    private static Map<Player,List<UniSkill>> activateSkill = new HashMap<>();
    private static Map<Player,List<UniSkill>> dodgeActivate = new HashMap<>();
    private static Map<Player,List<UniSkill>> thornActivate = new HashMap<>();
    private static Map<Player,List<UniSkill>> criticalActivate = new HashMap<>();
    private static Map<Player,List<UniSkill>> bloodsuckActivate = new HashMap<>();

    public SkillManager(Player player) {
        this.player = player;
    }

    /*@SuppressWarnings("deprecation")
    public boolean addActivateSkillEffect(SkillType.Effect skillEffect,long activateTick) {
        List<SkillType.Effect> skillEffectList = getActivateSkillEffect();
        if (skillEffectList.contains(skillEffect)) return false;
        skillEffectList.add(skillEffect);
        SkillManager.activateSkillEffect.put(player,skillEffectList);

        Bukkit.getScheduler().runTaskLater(KoalaItem.INSTANCE, new BukkitRunnable() {
            @Override
            public void run() {
                if (skillEffectList.contains(skillEffect)) {
                    skillEffectList.remove(skillEffect);
                }
            }
        },activateTick);

        return true;
    }

    public List<SkillType.Effect> getActivateSkillEffect() {
        if (SkillManager.activateSkillEffect.get(player) == null) {
            List<SkillType.Effect> l = new ArrayList<>();
            SkillManager.activateSkillEffect.put(player,l);
        }

        return SkillManager.activateSkillEffect.get(player);
    }*/

    @SuppressWarnings("deprecation")
    public void addCooldownSkill(UniSkill uniSkill, long cooldownTick) {
        List<UniSkill> l;
        List<UniSkill> skillList = getActivateSkill();
        SkillType.Effect effect = uniSkill.getSkill().getEffectType();

        if (skillList.contains(uniSkill)) return;
        skillList.add(uniSkill);
        SkillManager.activateSkill.put(player,skillList);

        Bukkit.getScheduler().runTaskLater(KoalaItem.INSTANCE, new BukkitRunnable() {
            @Override
            public void run() {
                if (skillList.contains(uniSkill)) {
                    skillList.remove(uniSkill);
                }
            }
        },cooldownTick);

        switch (effect) {
            case POTION_SELF:
            case POTION_OTHER:
            case COMMAND:
            case LIGHTNINGSTRIKE:
            case FIREBALL:
                break;
            case BLOODSUCK:
                if (SkillManager.bloodsuckActivate.get(player) == null) {
                    l = new ArrayList<>();
                } else {
                    l = SkillManager.bloodsuckActivate.get(player);
                }
                l.add(uniSkill);
                SkillManager.bloodsuckActivate.put(player,l);
                break;
            case DODGE:
                if (SkillManager.dodgeActivate.get(player) == null) {
                    l = new ArrayList<>();
                } else {
                    l = SkillManager.dodgeActivate.get(player);
                }
                l.add(uniSkill);
                SkillManager.dodgeActivate.put(player,l);
                break;
            case CRITICAL:
                if (SkillManager.criticalActivate.get(player) == null) {
                    l = new ArrayList<>();
                } else {
                    l = SkillManager.criticalActivate.get(player);
                }
                l.add(uniSkill);
                SkillManager.criticalActivate.put(player,l);
                break;
            case THORN:
                if (SkillManager.thornActivate.get(player) == null) {
                    l = new ArrayList<>();
                } else {
                    l = SkillManager.thornActivate.get(player);
                }
                l.add(uniSkill);
                SkillManager.thornActivate.put(player,l);
                break;
        }
    }

    public List<UniSkill> getActivateSkill() {
        if (SkillManager.activateSkill.get(player) == null) {
            List<UniSkill> l = new ArrayList<>();
            SkillManager.activateSkill.put(player,l);
        }

        return SkillManager.activateSkill.get(player);
    }

    public List<UniSkill> getBloodsuckSkill() {
        if (SkillManager.bloodsuckActivate.get(player) == null) {
            List<UniSkill> l = new ArrayList<>();
            SkillManager.bloodsuckActivate.put(player,l);
        }

        return SkillManager.bloodsuckActivate.get(player);
    }

    public List<UniSkill> getDodgeSkill() {
        if (SkillManager.dodgeActivate.get(player) == null) {
            List<UniSkill> l = new ArrayList<>();
            SkillManager.dodgeActivate.put(player,l);
        }

        return SkillManager.dodgeActivate.get(player);
    }

    public List<UniSkill> getcriticalSkill() {
        if (SkillManager.criticalActivate.get(player) == null) {
            List<UniSkill> l = new ArrayList<>();
            SkillManager.criticalActivate.put(player,l);
        }

        return SkillManager.criticalActivate.get(player);
    }

    public List<UniSkill> getThornSkill() {
        if (SkillManager.thornActivate.get(player) == null) {
            List<UniSkill> l = new ArrayList<>();
            SkillManager.thornActivate.put(player,l);
        }

        return SkillManager.thornActivate.get(player);
    }

}
