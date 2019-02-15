package tech.zuosi.koalarecipe.util;

/**
 * Created by iwar on 2016/8/28.
 */
public enum AddMode {
    ADD(-1),
    SKIP(0),
    OVERRIDE(1),
    ;

    private int value;

    AddMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
