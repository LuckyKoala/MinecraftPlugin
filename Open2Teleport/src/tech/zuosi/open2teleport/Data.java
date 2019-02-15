package tech.zuosi.open2teleport;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by iwar on 2016/11/8.
 */
public class Data {
    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&',"&8[&a&n系统提示&8] ");
    public static Map<String,Location> wait2back = new HashMap<>();
    public static Set<String> simulateSet = new HashSet<>();
}
