package tech.zuosi.minecraft.koalavip.constant;

/**
 * Created by luckykoala on 18-3-20.
 */
public class AssetStatus {
    public static final int OK = 0; //0 is for where return value is not used for representing amount of money
    public static final int EXPIRED = -1;
    public static final int NOT_TIME = -2;

    public static boolean isOkay(int status) {
        return status > 0;
    }
}
