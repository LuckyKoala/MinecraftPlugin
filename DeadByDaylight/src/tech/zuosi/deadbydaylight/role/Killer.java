package tech.zuosi.deadbydaylight.role;

import org.bukkit.entity.Player;
import tech.zuosi.deadbydaylight.item.tool.Tool;
import tech.zuosi.deadbydaylight.item.tool.Trap;
import tech.zuosi.deadbydaylight.item.weapon.Knife;

/**
 * Created by iwar on 2016/7/8.
 */
public class Killer extends TomRole implements Generalist {
    private boolean isInvisiable;
    private Tool trap = new Trap();

    public Killer(Player player) {
        super(player,new Knife());
    }


}
