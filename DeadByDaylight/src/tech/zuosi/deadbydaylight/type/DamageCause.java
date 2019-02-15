package tech.zuosi.deadbydaylight.type;

/**
 * Created by iwar on 2016/7/8.
 */
public enum DamageCause {
    TRAP("��������",0),
    WOUND("��������",1),
    SKILL("����Ч��",2),
    OTHER("�����˺�",3);

    private String zhName;
    private int index;

    DamageCause(String zhName,int index) {
        this.zhName = zhName;
        this.index = index;
    }

    public String getZhName() {
        return zhName;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
