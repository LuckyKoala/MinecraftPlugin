package tech.zuosi.koalaitem.type;

/**
 * Created by iwar on 2016/7/16.
 */
public enum GemType {
    VOID("未打孔",0),
    EMPTY("空缺",1),
    BLOOD("吸血",2),
    DODGE("闪避",3),
    SPEED("快速移动",4),
    ANTICRITICAL("暴击抗性",5),
    CRITICALDAMAGE("暴击伤害提升",6),
    CRITICALCHANCE("暴击概率提升",7),
    LUCKY("幸运",8);

    private String name;
    private int index;

    GemType(String name,int index) {
        this.name = name;
        this.index = index;
    }

    public GemType nameToType(String NAME) {
        GemType[] allType = GemType.values();
        for (GemType type:allType) {
            if (NAME.equalsIgnoreCase(type.getName())) {
                return type;
            }
        }
        return GemType.VOID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
