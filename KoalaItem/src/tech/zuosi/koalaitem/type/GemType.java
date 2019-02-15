package tech.zuosi.koalaitem.type;

/**
 * Created by iwar on 2016/7/16.
 */
public enum GemType {
    VOID("δ���",0),
    EMPTY("��ȱ",1),
    BLOOD("��Ѫ",2),
    DODGE("����",3),
    SPEED("�����ƶ�",4),
    ANTICRITICAL("��������",5),
    CRITICALDAMAGE("�����˺�����",6),
    CRITICALCHANCE("������������",7),
    LUCKY("����",8);

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
