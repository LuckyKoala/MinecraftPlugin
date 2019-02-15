package tech.zuosi.minecraft.koalavip.view.template;

/**
 * Created by luckykoala on 18-3-26.
 */
public class CommandTemplate {
    private final String name;
    private final boolean onetime;
    private final int period;
    private final String cmd;

    public CommandTemplate(String name, boolean onetime, int period, String cmd) {
        this.name = name;
        this.onetime = onetime;
        this.period = period;
        this.cmd = cmd;
    }

    public String getName() {
        return name;
    }

    public boolean isOnetime() {
        return onetime;
    }

    public int getPeriod() {
        return period;
    }

    public String getCmd() {
        return cmd;
    }

    public String getCmd(String username) {
        return String.format(cmd, username);
    }
}
