package tech.zuosi.koalaitem.handler.interact.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.skill.Skill;
import tech.zuosi.koalaitem.skill.SkillCaller;
import tech.zuosi.koalaitem.skill.SkillManager;
import tech.zuosi.koalaitem.skill.UniSkill;
import tech.zuosi.koalaitem.type.GemType;
import tech.zuosi.koalaitem.type.SkillType;
import tech.zuosi.koalaitem.util.GemUtil;
import tech.zuosi.koalaitem.util.NBTUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by iwar on 2016/7/28.
 */
public class EntityAttackPlayer implements Listener {
    //闪避，技能+属性+宝石
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMonsterHurt(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        ItemStack is = player.getItemInHand();
        GemUtil gemUtil = new GemUtil();
        if (!gemUtil.shouldExecute(is)) return;
        NBTUtil util = new NBTUtil(is);
        SkillManager skillManager = new SkillManager(player);
        SkillCaller skillCaller = new SkillCaller();
        int gemLevel = gemUtil.getGemLevel(util, GemType.DODGE);
        int chance = 0;
        int AGL = (int)util.getAttribute("AGL");
        int dodgeChance = KoalaItem.INSTANCE.getConfig().getInt("Attribute.AGL.dodgeChance");
        int thornPercent = 0;
        int thornChance = -1;

        //技能触发
        skillCaller.call(player,is,e.getEntity(), SkillType.Trigger.HURT);

        if (gemLevel > -1) {
            chance += KoalaItem.INSTANCE.getConfig().getInt("Gem.Dodge.essChance")*gemLevel;
        }
        chance += AGL*dodgeChance;

        //技能判定
        if (!skillManager.getDodgeSkill().isEmpty()) {
            List<UniSkill> skillList = skillManager.getDodgeSkill();
            List<Skill> dodgeSkillList = new ArrayList<>();
            for (UniSkill uniSkill:skillList) {
                if (SkillType.Effect.DODGE == uniSkill.getSkill().getEffectType()) dodgeSkillList.add(uniSkill.getSkill());
            }
            for (Skill dodgeSkill:dodgeSkillList) {
                chance += Integer.parseInt(dodgeSkill.getEffectValue());
            }
        }
        if (!skillManager.getThornSkill().isEmpty()) {
            List<UniSkill> skillList = skillManager.getThornSkill();
            List<Skill> thornSkillList = new ArrayList<>();
            for (UniSkill uniSkill:skillList) {
                if (SkillType.Effect.DODGE == uniSkill.getSkill().getEffectType()) thornSkillList.add(uniSkill.getSkill());
            }
            for (Skill thornSkill:thornSkillList) {
                thornPercent += Integer.parseInt(thornSkill.getEffectValue());
                thornChance = thornSkill.getChance();
            }
        }

        if (new Random().nextInt(100) <= chance) {
            e.setDamage(0);
            player.sendMessage(ChatColor.GREEN + "成功闪避一次攻击");
        }
        if (new Random().nextInt(100) <= thornChance) {
            if (e.getDamager() instanceof LivingEntity) {
                LivingEntity damager = (LivingEntity) e.getDamager();
                damager.setHealth(damager.getHealth() - e.getFinalDamage()*thornPercent);
                player.sendMessage(ChatColor.GREEN + "成功触发荆棘效果");
            }
        }
    }
}
