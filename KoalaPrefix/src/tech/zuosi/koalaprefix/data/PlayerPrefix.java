package tech.zuosi.koalaprefix.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by iwar on 2017/1/26.
 */
public class PlayerPrefix {
    private String playerName;
    private Map<String,Prefix.PrefixState> prefixStateMap = new HashMap<>();
    private String usingPrefix;

    public PlayerPrefix(String playerName) {
        this.playerName = playerName;
        Set<Map.Entry<String,Prefix>> set = DataManager.getAllPrefixCache().entrySet();
        for(Map.Entry<String,Prefix> entry:set) {
            this.prefixStateMap.put(entry.getKey(), entry.getValue().getCurrentState());
        }
    }

    public PlayerPrefix(String playerName,Map<String,Prefix.PrefixState> prefixStateMap) {
        this.playerName = playerName;
        this.prefixStateMap = prefixStateMap;
    }

    @Override
    public int hashCode() {
        return this.playerName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o==null || !(o instanceof PlayerPrefix)) return false;
        if(this == o) return true;
        PlayerPrefix po = (PlayerPrefix)o;
        return this.playerName.equals(po.playerName);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String,Prefix.PrefixState>> set = this.prefixStateMap.entrySet();
        for(Map.Entry<String,Prefix.PrefixState> entry:set) {
            String prefixName = entry.getKey();
            Prefix.PrefixState prefixState = entry.getValue();
            builder.append(prefixName).append(':')
                    .append(prefixState.name()).append(';');
        }
        return builder.toString();
    }

    public static PlayerPrefix loadFromString(String playerName,String data) {
        String[] dataSection = data.split(";");
        Map<String,Prefix.PrefixState> map = new HashMap<>();
        for(String section:dataSection) {
            String[] entry = section.split(":");
            if(entry.length!=2) break;
            map.put(entry[0], Prefix.PrefixState.valueOf(entry[1]));
        }
        if(map.size()==0) return new PlayerPrefix(playerName);
        return new PlayerPrefix(playerName,map);
    }

    public String getUsingPrefix() {
        //if(this.usingPrefix != null) return this.usingPrefix;
        String prefixStr = "";
        Set<Map.Entry<String,Prefix.PrefixState>> set = this.prefixStateMap.entrySet();
        for(Map.Entry<String,Prefix.PrefixState> entry:set) {
            if(entry.getValue() == Prefix.PrefixState.OWNED_USING) {
                prefixStr=entry.getKey();
                break;
            }
        }
        //this.usingPrefix = prefixStr;
        return prefixStr;
    }

    public Prefix.PrefixState getPrefixState(String name) {
        Prefix.PrefixState state = this.prefixStateMap.get(name);
        System.out.println("Null? "+name+(DataManager.getAllPrefixCache().get(name)==null));
        if(state==null) this.prefixStateMap.put(name,DataManager.getAllPrefixCache().get(name).getCurrentState());
        return this.prefixStateMap.get(name);
    }

    public void setPrefixState(String name, Prefix.PrefixState state) {
        this.prefixStateMap.put(name,state);
    }
}
