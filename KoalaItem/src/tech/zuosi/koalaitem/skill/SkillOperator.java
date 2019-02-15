package tech.zuosi.koalaitem.skill;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/27.
 */
public class SkillOperator {
    private Location location;
    private Player player;
    private String command;
    private Skill skill;
    private PotionEffect potionEffect;
    private SkillManager skillManager;
    private UniSkill uniSkill;

    public SkillOperator(Player player, Skill skill, ItemStack is) {
        this.player = player;
        this.skill = skill;
        this.skillManager = new SkillManager(player);
        this.uniSkill = new UniSkill(new NBTUtil(is).getItemData("timeStamp").toString(),skill);
    }

    public SkillOperator defineLocation(Location location) {
        this.location = location;
        return this;
    }

    public SkillOperator defineCommand(String command) {
        this.command = command;
        return this;
    }

    public SkillOperator definePotionEffect(PotionEffect potionEffect) {
        this.potionEffect = potionEffect;
        return this;
    }

    public void useCommand() {
        if (command == null) return;
        if (isActivate()) return;
        activate();
        KoalaItem.INSTANCE.getLogger().info("[KICommandLogger]PlayerName:" + player.getName()
                + "Command:" + command);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);

    }

    public void lightningstrike() {
        if (location == null) return;
        if (isActivate()) return;
        activate();
        player.getWorld().strikeLightning(location);
    }

    public void thorwFireball() {
        if (location == null) return;
        if (isActivate()) return;
        activate();
        player.getWorld().spawnEntity(location, EntityType.FIREBALL);
    }

    public void addPotionEffect(LivingEntity livingEntity) {
        if (potionEffect == null) return;
        if (isActivate()) return;
        activate();
        livingEntity.addPotionEffect(potionEffect);
    }

    public void activateEffect() {
        if (isActivate()) return;
        activate();
    }

    private boolean isActivate() {
        if (skillManager.getActivateSkill().contains(uniSkill)) {
            player.sendMessage(ChatColor.RED + "技能[" + skill.getName() + "]正在冷却中...");
            return true;
        }
        return false;
    }

    private void activate() {
        SkillManager skillManager = new SkillManager(player);
        skillManager.addCooldownSkill(uniSkill,skill.getCooldownTick());
        player.sendMessage(ChatColor.AQUA + "技能[" + skill.getName() + "]已触发并进入冷却");
    }
}
