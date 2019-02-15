package tech.zuosi.koalaattribute.attribute;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tech.zuosi.koalaattribute.data.AttributeCache;
import tech.zuosi.koalaattribute.tool.HealthAndSpeed;
import tech.zuosi.koalaattribute.tool.Permissions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/8/11.
 */
public class AttributeUtil {
    private static final int ERR_VAL = -1;

    private static int valRemovalCheck(Player operator, int baseVal, int changedVal) {
        int finalVal = baseVal+changedVal;
        if(changedVal < 0) {
            if(Permissions.canRemove(operator)) {
                finalVal = finalVal>=0 ? finalVal : 0;
            } else {
                finalVal = ERR_VAL;
            }
        }
        return finalVal;
    }

    public static void adjustAvailableAttributePoint(Player player, int amount, String playerName) {
        Attribute attribute = AttributeCache.INSTANCE.getAttribute(playerName);
        int checkedVal = valRemovalCheck(player, attribute.getAvailablePoint(), amount);
        if(checkedVal==ERR_VAL) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "ȱ��Ȩ�޽ڵ�koalaattribute.remove" );
            return;
        }
        attribute.setAvailablePoint(checkedVal);
        AttributeCache.INSTANCE.putAttribute(playerName,attribute);
        player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.GREEN + "�Ѹ���[" + playerName
                + "]" + amount + "δ�������Ե�");
    }

    public static void adjustAttributePoint(Player player, String attributeName, int amount) {
        String playerName = player.getName();
        Attribute attribute = AttributeCache.INSTANCE.getAttribute(playerName);
        if (amount > attribute.getAvailablePoint()) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "��ǰδ�������Ե㲻��");
            return;
        }
        CustomAttribute ca = AttributeCache.INSTANCE.getCAForName(attributeName);
        int oriPoint = attribute.getPointValue(ca);
        int checkedVal = valRemovalCheck(player, oriPoint, amount);
        if(checkedVal==ERR_VAL) {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "ȱ��Ȩ�޽ڵ�koalaattribute.remove" );
            return;
        }
        if (attribute.setPointValue(ca,checkedVal)) {
            if(amount>=0) {
                attribute.setAvailablePoint(attribute.getAvailablePoint()-amount);
                player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.GREEN + "�ѷ����[" + attributeName
                        + "]" + amount + "���Ե�");
            }
            //�Ƴ����Ե㲻���Ӵ��������Ե�
            if(amount<0)
                player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "���Ƴ�[" + attributeName
                    + "]" + (checkedVal-oriPoint) + "���Ե�");
            HealthAndSpeed.update(player);

            AttributeCache.INSTANCE.putAttribute(playerName,attribute);
        } else {
            player.sendMessage(ChatColor.GOLD + "[KoalaAttribute]" + ChatColor.RED + "ָ�������Բ�����");
        }
    }

    public static Map<String,Double> convertToEssAttribute(Attribute attribute) {
        Map<String,Double> essAttribute = new HashMap<>();
        double damage,dodge,health,critical,armor,exp,speed,blood;
        Map<CustomAttribute,Integer> pointMap = attribute.getPointMap();

        damage = dodge = health = critical = armor = exp = speed = blood = 0D;
        for (Map.Entry<CustomAttribute,Integer> entry : pointMap.entrySet()) {
            CustomAttribute customAttribute = entry.getKey();
            int point = entry.getValue();

            damage += point*customAttribute.getDamage();
            dodge += point*customAttribute.getDodge();
            health += point*customAttribute.getHealth();
            critical += point*customAttribute.getCritical();
            armor += point*customAttribute.getArmor();
            exp += point*customAttribute.getExp();
            speed += point*customAttribute.getSpeed();
            blood += point*customAttribute.getBlood();
        }
        essAttribute.put("damage",damage);
        essAttribute.put("dodge",dodge);
        essAttribute.put("health",health);
        essAttribute.put("critical",critical);
        essAttribute.put("armor",armor);
        essAttribute.put("exp",exp);
        essAttribute.put("speed",speed);
        essAttribute.put("blood",blood);

        return essAttribute;
    }
}
