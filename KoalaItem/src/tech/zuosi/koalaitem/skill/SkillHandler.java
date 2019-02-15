package tech.zuosi.koalaitem.skill;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.type.SkillType;
import tech.zuosi.koalaitem.util.NBTUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2016/7/27.
 */
public class SkillHandler {
    //FIXME 涉及SkillHandler的技能触发似乎都不行

    private ItemStack is;
    private Player player;
    private SkillType.Trigger triggerType;
    private List<Skill> skills = new ArrayList<>();

    public SkillHandler(Player player, ItemStack is, SkillType.Trigger triggerType) {
        this.is = is;
        this.player = player;
        this.triggerType = triggerType;
    }


    public boolean shouldExecute() {
        is = is==null?new ItemStack(Material.AIR):is;
        if (Material.AIR == is.getType()) return false;
        NBTUtil util = new NBTUtil(is);
        Object o = util.getData("type");
        if (o == null) return false;
        if (ItemType.PLAYERITEM != ItemType.valueOf((String) o)) return false;
        saveSkills();
        return skills.size() > 0;
    }

    private void saveSkills() {
        NBTUtil util = new NBTUtil(is);
        Skill skillOne = new Skill(util.getSkill("skillOne"));
        Skill skillTwo = new Skill(util.getSkill("skillTwo"));

        if (skillOne.isExist()) {
            if (triggerType == skillOne.getTriggerType()) {
                skills.add(skillOne);
            }
        }
        if (skillTwo.isExist()) {
            if (triggerType == skillTwo.getTriggerType()) {
                skills.add(skillTwo);
            }
        }
    }

    public List<Skill> getSkills() {
        return skills;
    }
}
