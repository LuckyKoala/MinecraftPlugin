package tech.zuosi.rebelwar.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Created by iwar on 2016/10/6.
 */
public class LocationUtil {
    private Vector downV,upV;

    public LocationUtil(Location down,Location up) {
        this.downV = down.toVector();
        this.upV = up.toVector();
    }

    public boolean isInBound(Location tar) {
        return tar.toVector().isInAABB(downV,upV);
    }
}
