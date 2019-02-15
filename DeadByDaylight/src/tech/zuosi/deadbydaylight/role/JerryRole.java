package tech.zuosi.deadbydaylight.role;

import org.bukkit.entity.Player;

/**
 * Created by iwar on 2016/7/8.
 */
public class JerryRole extends Role {
    private int hurtCount;

    public JerryRole(Player player) {
        super(player);
    }

    public int getHurtCount() {
        return hurtCount;
    }

    public void setHurtCount(int hurtCount) {
        this.hurtCount = hurtCount;
    }
}
