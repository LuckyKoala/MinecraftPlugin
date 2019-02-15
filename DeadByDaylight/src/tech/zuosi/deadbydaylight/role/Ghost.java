package tech.zuosi.deadbydaylight.role;

import org.bukkit.entity.Player;
import tech.zuosi.deadbydaylight.item.weapon.Sickle;

/**
 * Created by iwar on 2016/7/8.
 */
public class Ghost extends TomRole {
    private boolean isInvisiable;

    public Ghost(Player player) {
        super(player,new Sickle(),1);
        this.isInvisiable = false;
    }

    public boolean isInvisiable() {
        return isInvisiable;
    }

    public void setInvisiable(boolean invisiable) {
        isInvisiable = invisiable;
    }

}
