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
                ChatColor.GOLD + "------[KoalaAttribute]������Ϣ------" + ChatColor.BOLD,
                ChatColor.GREEN + "δ�������Ե�: " + ChatColor.GRAY + attribute.getAvailablePoint(),
                ChatColor.GREEN + "����:"
        });
        Map<CustomAttribute,Integer> pointMap = attribute.getPointMap();
        //���Է�ҳ
        p.sendMessage(getPage(pointMap,page));
        Map<String,Double> essAttribute = AttributeUtil.convertToEssAttribute(attribute);
        p.sendMessage(new String[]{
                ChatColor.GREEN + "����ӳ�: " + ChatColor.GRAY + essAttribute.get("exp") + "   |  " +
                        ChatColor.GREEN + "�ٶȼӳ�: " + ChatColor.GRAY + essAttribute.get("speed"),
                ChatColor.GREEN + "���ܼ���: " + ChatColor.GRAY + essAttribute.get("dodge") + "   |  " +
                        ChatColor.GREEN + "��������: " + ChatColor.GRAY + essAttribute.get("critical"),
                ChatColor.GREEN + "�����˺�: " + ChatColor.GRAY + essAttribute.get("damage") + "   |  " +
                        ChatColor.GREEN + "����Ѫ��: " + ChatColor.GRAY + essAttribute.get("health"),
                ChatColor.GREEN + "��Ѫ����: " + ChatColor.GRAY + essAttribute.get("blood") + "   |  " +
                        ChatColor.GREEN + "���׼ӳ�: " + ChatColor.GRAY + essAttribute.get("armor"),
                ChatColor.GOLD + "�鿴����������Ϣ������/ka show <id> <page>" + ChatColor.GRAY + " | ��ǰҳ�� "
                        + page + "/" + getPageSize(pointMap)
        });





    }

    private static String getPage(Map<CustomAttribute,Integer> pointMap,int page) {
        List<String> pageList = pages(pointMap);
        if (page < 0 || page >= pageList.size()) return ChatColor.RED + "ָ��ҳ�治��������";

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
