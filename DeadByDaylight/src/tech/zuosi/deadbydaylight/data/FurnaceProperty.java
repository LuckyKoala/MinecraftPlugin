package tech.zuosi.deadbydaylight.data;

/**
 * Created by iwar on 2016/7/9.
 */
public class FurnaceProperty {
    private int BURN_TIME;
    private int COOK_TIME;

    public FurnaceProperty() {}

    public int getBURN_TIME() {
        return BURN_TIME;
    }

    public FurnaceProperty BURN_TIME(int BURN_TIME) {
        this.BURN_TIME = BURN_TIME;
        return this;
    }

    public int getCOOK_TIME() {
        return COOK_TIME;
    }

    public FurnaceProperty COOK_TIME(int COOK_TIME) {
        this.COOK_TIME = COOK_TIME;
        return this;
    }
}
