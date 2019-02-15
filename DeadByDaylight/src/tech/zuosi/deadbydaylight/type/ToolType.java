package tech.zuosi.deadbydaylight.type;

/**
 * Created by iwar on 2016/7/8.
 */
public enum ToolType {
    TREAT("ҽ�ƹ���",0),
    THREAT("���幤��",1),
    EFFICIENCY("Ч�ʹ���",2),
    DANGER("���˹���",3),
    EFFECT("Ч������",4),
    OTHER("��������",5);

    private String zhName;
    private int index;

    ToolType(String zhName,int index) {
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
