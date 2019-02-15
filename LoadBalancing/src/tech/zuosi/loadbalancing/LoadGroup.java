package tech.zuosi.loadbalancing;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by iwar on 2016/11/20.
 */
public class LoadGroup {
    private String ownerName;
    private Set<LoadStat> members = new HashSet<>();

    public LoadGroup(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean addMember(LoadStat statObj) {
        return this.members.add(statObj);
    }

    public String complete() {
        int min = 0;
        String minServerName = null;
        for (LoadStat member:members) {
            if (member.getPlayerCount()<=min) {
                min = member.getPlayerCount();
                minServerName = member.getServerName();
            }
        }
        return minServerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        LoadGroup u = (LoadGroup) o;
        return ownerName.equals(u.ownerName);
    }

    @Override
    public int hashCode() {
        return ownerName.hashCode();
    }
}
