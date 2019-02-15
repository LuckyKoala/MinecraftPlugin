package tech.zuosi.loadbalancing;

/**
 * Created by iwar on 2016/11/20.
 */
public class LoadStat {
    private String serverName;
    private int playerCount;

    public LoadStat(String serverName,int playerCount) {
        this.serverName = serverName;
        this.playerCount = playerCount;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public String getServerName() {
        return serverName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        LoadStat u = (LoadStat) o;
        return serverName.equals(u.serverName);
    }

    @Override
    public int hashCode() {
        return serverName.hashCode();
    }
}
