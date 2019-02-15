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
import tech.zuosi.koalaitem.util.VectorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by iwar on 2016/7/28.
 */
public class PlayerAttackEntity implements Listener {
    //属性、技能、宝石
    //吸血 攻击力加成 抗暴 暴击
    @EventHandler(priority = EventPriority.HIGH)
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player player = (Player) e.getDamager();
        ItemStack is = player.getItemInHand();
        GemUtil gemUtil = new GemUtil();
        if (!gemUtil.shouldExecute(is)) return;
        NBTUtil util = new NBTUtil(is);
        SkillManager skillManager = new SkillManager(player);
        SkillCaller skillCaller = new SkillCaller();
        int bloodLevel = gemUtil.getGemLevel(util, GemType.BLOOD);
        int criticalDamageLevel = gemUtil.getGemLevel(util, GemType.CRITICALDAMAGE);
        int criticalChanceLevel = gemUtil.getGemLevel(util, GemType.CRITICALCHANCE);
        int DEX = (int)util.getAttribute("DEX");
        double damage = e.getDamage();

        int chance = 0;
        int extraPercent = 0;
        double extraDamage = 0D;
        int decChance = 0;

        //技能触发-攻击
        skillCaller.call(player,is,e.getEntity(), SkillType.Trigger.ATTACK);
        //技能触发-背刺
        if (e.getEntity() instanceof LivingEntity) {
            VectorUtil vectorUtil = new VectorUtil(player);
            if (vectorUtil.behind(e.getEntity())) {
                skillCaller.call(player,is,e.getEntity(), SkillType.Trigger.BACKSTAB);
            }
        }

        chance += DEX * KoalaItem.INSTANCE.getConfig().getInt("Attribute.DEX.criticalChance");
        //攻击力加成
        damage += getIntensifyLevel(util)*KoalaItem.INSTANCE.getConfig().getDouble("IntensifyDamage");
        if ((criticalChanceLevel+criticalDamageLevel) > -2) {
            //暴击几率和伤害预处理
            if (criticalChanceLevel > -1) {
                chance += criticalChanceLevel * KoalaItem.INSTANCE.getConfig().getInt("Gem.Critical.incPercent");
            }
            if (criticalDamageLevel > -1) {
                extraPercent += criticalDamageLevel * KoalaItem.INSTANCE.getConfig().getInt("Gem.Critical.incChance");
            }
        }

        //抗暴
        if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            ItemStack victimIs = victim.getItemInHand();
            if (!gemUtil.shouldExecute(victimIs));
            NBTUtil vUtil = new NBTUtil(victimIs);
            int AGL = (int)vUtil.getAttribute("AGL");
            int antiCriticalLevel = gemUtil.getGemLevel(vUtil,GemType.ANTICRITICAL);

            if (antiCriticalLevel > -1)
                decChance += antiCriticalLevel * KoalaItem.INSTANCE.getConfig().getInt("Gem.AntiCritical.decChance");
            decChance += AGL * KoalaItem.INSTANCE.getConfig().getInt("Attribute.AGL.antiCriticalChance");
        }

        //暴击
        chance -= decChance;
        if (new Random().nextInt(100) <= chance) {
            //技能触发-暴击
            skillCaller.call(player,is,e.getEntity(), SkillType.Trigger.CRITICAL);

            damage += extraDamage;
            //技能
            if (!skillManager.getcriticalSkill().isEmpty()) {
                List<UniSkill> skillList = skillManager.getcriticalSkill();
                List<UniSkill> criticalSkillList = new ArrayList<>();
                for (UniSkill uniSkill : skillList) {
                    if (SkillType.Effect.CRITICAL == uniSkill.getSkill().getEffectType())
                        criticalSkillList.add(uniSkill);
                }
                for (UniSkill criticalSkill:criticalSkillList) {
                    extraPercent += Integer.parseInt(criticalSkill.getSkill().getEffectValue());
                }
            }
            damage *= (extraPercent/100)+1;
            player.sendMessage(ChatColor.GREEN + "成功触发暴击");
        }

        //吸血
        if (bloodLevel > -1) {
            double health = player.getHealth();
            int bloodChance = KoalaItem.INSTANCE.getConfig().getInt("Gem.Blood.essChance");
            int bloodPercent = KoalaItem.INSTANCE.getConfig().getInt("Gem.Blood.essPercent");

            bloodChance += bloodLevel * KoalaItem.INSTANCE.getConfig().getInt("Gem.Blood.incChance");
            bloodPercent += bloodLevel * KoalaItem.INSTANCE.getConfig().getInt("Gem.Blood.incPercent");

            //技能
            if (!skillManager.getBloodsuckSkill().isEmpty()) {
                List<UniSkill> skillList = skillManager.getBloodsuckSkill();
                List<Skill> bloodsuckSkillList = new ArrayList<>();
                for (UniSkill uniSkill:skillList) {
                    if (SkillType.Effect.BLOODSUCK == uniSkill.getSkill().getEffectType())
                        bloodsuckSkillList.add(uniSkill.getSkill());
                }
                for (Skill bloodsuckSkill:bloodsuckSkillList) {
                    bloodPercent += Integer.parseInt(bloodsuckSkill.getEffectValue());
                }
            }

            if (new Random().nextInt(100) <= bloodChance) {
                health += damage * bloodPercent;
                health = health>player.getMaxHealth()?player.getMaxHealth():health;
                player.setHealth(health);
                player.sendMessage(ChatColor.GREEN + "成功触发吸血效果");
            }
        }

        e.setDamage(damage);
    }

    private int getIntensifyLevel(NBTUtil util) {
        return (int)util.getItemData("intensifyLevel")+10*(int)util.getItemData("rebornLevel");
    }
}
