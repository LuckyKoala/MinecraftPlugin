package tech.zuosi.deadbydaylight.item.tool;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.deadbydaylight.role.Role;
import tech.zuosi.deadbydaylight.role.TomRole;
import tech.zuosi.deadbydaylight.type.ToolType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by iwar on 2016/7/8.
 */
public class Flashlight extends Tool {
    private int chance;
    private boolean isOn;

    public Flashlight() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a手电筒"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a一定几率致盲屠夫"));
        super.setItem(new ItemStack(Material.REDSTONE_TORCH_OFF),"Flashlight", lore);
        this.isOn = false;
        this.setToolType(ToolType.EFFECT);
        this.chance = 10;
    }

    @Override
    public boolean effect(Role role) {
        if (!(role instanceof TomRole)) {
            return false;
        }
        TomRole tom = (TomRole) role;
        if (new Random().nextInt(100) < this.chance) {
            tom.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,20,0));
            tom.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,10,0));
            tom.echo(ChatColor.RED + "你被手电筒致盲，持续一秒。");
            torchOn();
            return true;
        }
        return false;
    }

    public void torchOn() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a手电筒"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a一定几率致盲屠夫"));
        super.setItem(new ItemStack(Material.REDSTONE_TORCH_ON),"Flashlight", lore);
        this.isOn = true;
    }

    public void torchOff() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a手电筒"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a一定几率致盲屠夫"));
        super.setItem(new ItemStack(Material.REDSTONE_TORCH_OFF),"Flashlight", lore);
        this.isOn = false;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }
}
