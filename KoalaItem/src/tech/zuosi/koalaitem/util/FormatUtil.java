package tech.zuosi.koalaitem.util;

import org.bukkit.ChatColor;

/**
 * Created by iwar on 2016/7/31.
 */
public class FormatUtil {

    public FormatUtil() {}

    public String starLevel(int level) {
        //¡ï¡î
        int big,little;
        StringBuilder format = new StringBuilder();

        little = level%2;
        big = level-little;
        if (big != 0) big /= 2;

        for (int i=0;i<big;i++) {
            format.append(ChatColor.GOLD + "¡ï");
        }
        for (int j=0;j<little;j++) {
            format.append(ChatColor.GOLD + "¡î");
        }

        return format.toString();
    }
}
