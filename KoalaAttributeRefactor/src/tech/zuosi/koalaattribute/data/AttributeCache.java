package tech.zuosi.koalaattribute.data;

import tech.zuosi.koalaattribute.attribute.Attribute;
import tech.zuosi.koalaattribute.attribute.CustomAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iwar on 2016/8/11.
 */
public class AttributeCache {
    private static Map<String,Attribute> playerAttribute = new HashMap<>();
    private static List<CustomAttribute> customAttributeList = new ArrayList<>();
    public static AttributeCache INSTANCE = new AttributeCache();

    private AttributeCache() {}

    public List<CustomAttribute> getCustomAttributeList() {
        return AttributeCache.customAttributeList;
    }

    public void setCustomAttributeList(List<CustomAttribute> list) {
        AttributeCache.customAttributeList = list;
    }

    public Attribute getAttribute(String name) {
        if (AttributeCache.playerAttribute.get(name) == null) {
            Attribute defaultValue = new Attribute();
            AttributeCache.playerAttribute.put(name,defaultValue);
        }
        return AttributeCache.playerAttribute.get(name);
    }

    public void putAttribute(String name, Attribute attribute) {
        AttributeCache.playerAttribute.put(name,attribute);
    }

    public CustomAttribute getCAForName(String name) {
        for (CustomAttribute customAttribute : AttributeCache.customAttributeList) {
            if (name.equals(customAttribute.getName())) return customAttribute;
        }
        return null;
    }
}
