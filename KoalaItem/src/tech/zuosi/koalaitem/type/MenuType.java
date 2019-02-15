package tech.zuosi.koalaitem.type;

/**
 * Created by iwar on 2016/7/16.
 */
public enum MenuType {
    MAIN("主选单",0),
    ITEM("物品选单",1),
    IDENTIFY("鉴定选单",2),
    INTENSIFY("强化选单",3),
    GEM("镶嵌选单",4),
    REBORN("转生选单",5),
    REMOVE("摘取选单",6),
    UNKNOWN("未知",7);

    private String name;
    private int index;

    MenuType(String name,int index) {
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
