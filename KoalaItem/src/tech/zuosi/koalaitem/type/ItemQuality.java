package tech.zuosi.koalaitem.type;

/**
 * Created by iwar on 2016/7/16.
 */
public enum ItemQuality {
    NORMAL("普通",0),
    FINE("优秀",1),
    EXCELLENT("精良",2),
    RARE("稀有",3),
    LEGEND("传说",4),
    EPIC("史诗",5),
    PEERLESS("无双",6);

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
