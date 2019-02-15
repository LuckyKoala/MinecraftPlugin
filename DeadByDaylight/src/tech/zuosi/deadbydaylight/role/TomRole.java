package tech.zuosi.deadbydaylight.role;

import org.bukkit.entity.Player;
import tech.zuosi.deadbydaylight.item.weapon.Weapon;

/**
 * Created by iwar on 2016/7/8.
 */
public class TomRole extends Role {
    private Weapon weapon;

    public TomRole(Player player, Weapon weapon) {
        super(player,false);
        this.weapon = weapon;
    }

    public TomRole(Player player, Weapon weapon,int speedAmplifier) {
        super(player,false,speedAmplifier);
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return this.weapon;
    }
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}
