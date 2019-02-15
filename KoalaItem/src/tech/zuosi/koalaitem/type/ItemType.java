package tech.zuosi.koalaitem.type;

/**
 * Created by iwar on 2016/7/16.
 */
public enum ItemType {
    ITEM("物品",0),
    GEM("宝石",1),
    CORE("核心",2),
    MENU("菜单",3),
    INFO("信息",4),
    PLAYERITEM("玩家物品",5),
    RESOLVENT("酿酒溶剂",6),
    WINE("酒",7),
    BOTTLE("容器",8);

    private String name;
    private int index;

    ItemType(String name,int index) {
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
