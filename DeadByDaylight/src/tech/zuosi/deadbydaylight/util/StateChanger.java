package tech.zuosi.deadbydaylight.util;

import tech.zuosi.deadbydaylight.role.JerryRole;

/**
 * Created by iwar on 2016/7/8.
 */
public class StateChanger {
    private JerryRole jerry;

    public StateChanger() {}

    public StateChanger(JerryRole jerry) {
        this.jerry = jerry;
    }

    public void fire() {
        int count = jerry.getHurtCount();
        switch (count) {
            case 0:
                //
                break;
            case 1:
                //
                break;
            case 2:
                //
                break;
            default:
                break;
        }
    }
}
