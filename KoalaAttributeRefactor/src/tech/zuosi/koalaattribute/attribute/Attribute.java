package tech.zuosi.koalaattribute.attribute;

import tech.zuosi.koalaattribute.data.AttributeCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iwar on 2016/8/11.
 */
public class Attribute {
    private int availablePoint;
    private Map<CustomAttribute,Integer> pointMap;

    public Attribute() {
        availablePoint = 0;
        pointMap = new HashMap<>();
        initCustomAttribute();
    }

    private void initCustomAttribute() {
        List<CustomAttribute> customAttributes = AttributeCache.INSTANCE.getCustomAttributeList();
        for (CustomAttribute customAttribute : customAttributes) {
            pointMap.put(customAttribute,0);
        }
    }

    public void updateCustomAttribute() {
        List<CustomAttribute> customAttributes = AttributeCache.INSTANCE.getCustomAttributeList();
        for (CustomAttribute customAttribute : customAttributes) {
            if (!pointMap.containsKey(customAttribute)) pointMap.put(customAttribute,0);
        }
    }

    public Map<CustomAttribute, Integer> getPointMap() {
        return pointMap;
    }

    public void setPointMap(Map<CustomAttribute, Integer> pointMap) {
        this.pointMap = pointMap;
    }

    public boolean setPointValue(CustomAttribute customAttribute, int amount) {
        if (!pointMap.containsKey(customAttribute)) {
            return false;
        }
        pointMap.put(customAttribute,amount);
        return true;
    }

    public int getPointValue(CustomAttribute customAttribute) {
        if (!pointMap.containsKey(customAttribute)) {
            return 0;
        }
        return pointMap.get(customAttribute);
    }

    public int getAvailablePoint() {
        return availablePoint;
    }

    public void setAvailablePoint(int availablePoint) {
        this.availablePoint = availablePoint;
    }

    public Map<String,Integer> saveToMap() {
        Map<String,Integer> newPointMap = new HashMap<>();
        for (Map.Entry<CustomAttribute,Integer> entry : pointMap.entrySet()) {
            newPointMap.put(entry.getKey().getName(),entry.getValue());
        }
        return newPointMap;
    }

    public void build(Map<String,Integer> map) {
        Map<CustomAttribute,Integer> newPointMap = new HashMap<>();
        for (Map.Entry<String,Integer> entry : map.entrySet()) {
            CustomAttribute customAttribute = AttributeCache.INSTANCE.getCAForName(entry.getKey());
            if (customAttribute == null) continue;
            newPointMap.put(customAttribute,entry.getValue());
        }
        this.setPointMap(newPointMap);
    }
}
