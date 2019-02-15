package tech.zuosi.koalaitem.type;

/**
 * Created by iwar on 2016/7/16.
 */
public enum MenuType {
    MAIN("��ѡ��",0),
    ITEM("��Ʒѡ��",1),
    IDENTIFY("����ѡ��",2),
    INTENSIFY("ǿ��ѡ��",3),
    GEM("��Ƕѡ��",4),
    REBORN("ת��ѡ��",5),
    REMOVE("ժȡѡ��",6),
    UNKNOWN("δ֪",7);

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
