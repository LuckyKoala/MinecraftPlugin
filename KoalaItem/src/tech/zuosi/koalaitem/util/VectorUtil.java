package tech.zuosi.koalaitem.util;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * Created by iwar on 2016/8/8.
 */
public class VectorUtil {
    private Entity entity;

    public VectorUtil(Entity entity) {
        this.entity = entity;
    }

    public boolean behind(Entity other) {
        Vector v1 = entity.getLocation().getDirection();
        Vector v2 = other.getLocation().getDirection();
        return v1.angle(v2) < 1.3D;
    }
}
