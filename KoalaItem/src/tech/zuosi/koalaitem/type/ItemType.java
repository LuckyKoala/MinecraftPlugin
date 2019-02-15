package tech.zuosi.koalaitem.type;

/**
 * Created by iwar on 2016/7/16.
 */
public enum ItemType {
    ITEM("��Ʒ",0),
    GEM("��ʯ",1),
    CORE("����",2),
    MENU("�˵�",3),
    INFO("��Ϣ",4),
    PLAYERITEM("�����Ʒ",5),
    RESOLVENT("����ܼ�",6),
    WINE("��",7),
    BOTTLE("����",8);

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
