package tech.zuosi.koalaitem.type;

/**
 * Created by iwar on 2016/7/16.
 */
public enum ItemQuality {
    NORMAL("��ͨ",0),
    FINE("����",1),
    EXCELLENT("����",2),
    RARE("ϡ��",3),
    LEGEND("��˵",4),
    EPIC("ʷʫ",5),
    PEERLESS("��˫",6);

    private String name;
    private int index;

    ItemQuality(String name,int index) {
        this.name = name;
        this.index = index;
    }

    public String getTypeName(int index) {
        ItemQuality[] allType = ItemQuality.values();
        for (ItemQuality type:allType) {
            if (type.index == index) {
                return type.name();
            }
        }
        return "NORMAL";
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
