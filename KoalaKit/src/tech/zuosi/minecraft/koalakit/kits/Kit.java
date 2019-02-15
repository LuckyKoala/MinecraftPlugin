package tech.zuosi.minecraft.koalakit.kits;

import java.util.List;

public class Kit {
    public static final String NO_PERMISSION_REQUIRED = "NO_PERMISSION_REQUIRED";
    private int limit;
    private int period;
    private String permission;
    private List<String> commands;

    public Kit(int limit, int period, String permission, List<String> commands) {
        this.limit = limit;
        this.period = period;
        this.permission = permission;
        this.commands = commands;
    }

    public int getLimit() {
        return limit;
    }

    public int getPeriod() {
        return period;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getCommands() {
        return commands;
    }
}
