package tech.zuosi.deadbydaylight.type;

/**
 * Created by iwar on 2016/7/8.
 */
public enum ToolType {
    TREAT("医疗工具",0),
    THREAT("威慑工具",1),
    EFFICIENCY("效率工具",2),
    DANGER("致伤工具",3),
    EFFECT("效果工具",4),
    OTHER("其他工具",5);

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
