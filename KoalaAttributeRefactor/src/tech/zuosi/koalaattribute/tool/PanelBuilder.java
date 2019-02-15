package tech.zuosi.koalaattribute.tool;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tech.zuosi.koalaattribute.attribute.Attribute;
import tech.zuosi.koalaattribute.attribute.AttributeUtil;
import tech.zuosi.koalaattribute.attribute.CustomAttribute;
import tech.zuosi.koalaattribute.data.AttributeCache;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by iwar on 2016/8/11.
 */
public class PanelBuilder {

    public PanelBuilder() {}

    public static void show(Player p,int page) {
        String playerName = p.getName();
        Attribute attribute = AttributeCache.INSTANCE.getAttribute(playerName);

        p.sendMessage(new String[]{
                ChatColor.GOLD + "------[KoalaAttribute]属性信息------" + ChatColor.BOLD,
                ChatColor.GREEN + "未分配属性点: " + ChatColor.GRAY + attribute.getAvailablePoint(),
                ChatColor.GREEN + "属性:"
        });
        Map<CustomAttribute,Integer> pointMap = attribute.getPointMap();
        //属性分页
        p.sendMessage(getPage(pointMap,page));
        Map<String,Double> essAttribute = AttributeUtil.convertToEssAttribute(attribute);
        p.sendMessage(new String[]{
                ChatColor.GREEN + "经验加成: " + ChatColor.GRAY + essAttribute.get("exp") + "   |  " +
                        ChatColor.GREEN + "速度加成: " + ChatColor.GRAY + essAttribute.get("speed"),
                ChatColor.GREEN + "闪避几率: " + ChatColor.GRAY + essAttribute.get("dodge") + "   |  " +
                        ChatColor.GREEN + "暴击几率: " + ChatColor.GRAY + essAttribute.get("critical"),
                ChatColor.GREEN + "额外伤害: " + ChatColor.GRAY + essAttribute.get("damage") + "   |  " +
                        ChatColor.GREEN + "额外血量: " + ChatColor.GRAY + essAttribute.get("health"),
                ChatColor.GREEN + "吸血几率: " + ChatColor.GRAY + essAttribute.get("blood") + "   |  " +
                        ChatColor.GREEN + "护甲加成: " + ChatColor.GRAY + essAttribute.get("armor"),
                ChatColor.GOLD + "查看更多属性信息请输入/ka show <id> <page>" + ChatColor.GRAY + " | 当前页面 "
                        + page + "/" + getPageSize(pointMap)
        });





    }

    private static String getPage(Map<CustomAttribute,Integer> pointMap,int page) {
        List<String> pageList = pages(pointMap);
        if (page < 0 || page >= pageList.size()) return ChatColor.RED + "指定页面不存在内容";

        return pageList.get(page);
    }

    private static int getPageSize(Map<CustomAttribute,Integer> pointMap) {
        return pages(pointMap).size()-1;
    }

    private static List<String> pages(Map<CustomAttribute,Integer> pointMap) {
        int index = 0;
        final int pageSize = 4;
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<CustomAttribute,Integer> entry : pointMap.entrySet()) {
            index++;
            sb.append("   " + ChatColor.LIGHT_PURPLE).append(entry.getKey().getName()).append(": ")
                    .append(ChatColor.AQUA).append(entry.getValue())
                    .append("\n").append("    " + ChatColor.GRAY + entry.getKey().getDescription())
                    .append("\n");
            if (index % pageSize == 0) sb.append(";");
        }

        return Arrays.asList(sb.toString().split(";"));
    }
}
