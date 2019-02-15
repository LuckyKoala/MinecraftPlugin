package tech.zuosi.koalaitem.type;

/**
 * Created by iwar on 2016/7/16.
 */
public enum CoreType {
    EMPTY("��ȱ",0),
    FLAME("����",1),
    ENDERSIGNAL("ĩӰ",2);

    private String name;
    private int index;

    CoreType(String name,int index) {
        this.name = name;
        this.index = index;
    }

    public CoreType nameToType(String NAME) {
        CoreType[] allType = CoreType.values();
        for (CoreType type:allType) {
            if (NAME.equalsIgnoreCase(type.getName())) {
                return type;
            }
        }
        return CoreType.EMPTY;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
