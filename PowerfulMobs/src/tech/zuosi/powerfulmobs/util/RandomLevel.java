package tech.zuosi.powerfulmobs.util;

import java.util.Random;

/**
 * Created by iwar on 2016/4/26.
 */
public class RandomLevel {
    Random random = new Random();
    public MobLevel getResult() {
        int src = random.nextInt(100);
        if (src > 20) {
            if (src <= 70) {
                if (src <= 40) {
                    return MobLevel.NORMAL;
                } else if (src <= 55) {
                    return MobLevel.HARD;
                } else {
                    return MobLevel.HELL;
                }
            } else {
                if (src <= 85) {
                    return MobLevel.LEGEND;
                } else if (src <= 95) {
                    return MobLevel.EPIC;
                } else {
                    return MobLevel.NIGHTMARE;
                }
            }
        }
        return MobLevel.EASY;
    }
}
