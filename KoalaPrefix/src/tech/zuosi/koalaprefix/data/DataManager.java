package tech.zuosi.koalaprefix.data;

import org.bukkit.entity.Player;
import tech.zuosi.koalaprefix.ChannelUtil;
import tech.zuosi.koalaprefix.PlayerPointUtil;
import tech.zuosi.koalaprefix.main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2017/1/26.
 */
public class DataManager {
    private static Map<String,Prefix> prefixCache = new HashMap<>();
    private static Map<String,PlayerPrefix> playerCache = new HashMap<>();

    public static void loadAllPrefix(String data) {
        System.out.println("Data --> \n"+data+"\n <--");
        String[] essData = data.split("<");
        for(String ess:essData) {
            System.out.println("EssData --> \n"+ess+"\n <--");
            String[] essData2 = ess.split(">");
            String prefixName = essData2[0];
            Prefix prefix = Prefix.loadFromString(essData2[1]);
            prefixCache.put(prefixName,prefix);
        }
        main.init = true;
    }

    public static PlayerPrefix getCachedPlayerPrefix(Player player, boolean forceUpdate) {
        return getCachedPlayerPrefix(player,player.getName(),forceUpdate);
    }

    public static void cachePlayerPrefix(String name,PlayerPrefix playerPrefix) {
        playerCache.put(name,playerPrefix);
    }

    public static PlayerPrefix getCachedPlayerPrefix(Player player,String overrideName,boolean forceUpdate) {
        PlayerPrefix playerPrefix;
        if(forceUpdate) ChannelUtil.getInstance().getPlayerPrefix(player,overrideName);
        playerPrefix = playerCache.get(overrideName);
        if(playerPrefix==null) {
            ChannelUtil.getInstance().getPlayerPrefix(player,overrideName);
            playerPrefix = new PlayerPrefix(overrideName);
        }
        return playerPrefix;
    }

    public static Map<String,Prefix> getAllPrefixCache() {
        return prefixCache;
    }

    //give true mean direct give,so just check command permission
    public static boolean setState(Player p, String prefixName, Prefix.PrefixState state, boolean command) {
        return setState(p,p.getName(),prefixName,state,command);
    }

    public static boolean setState(Player p,String overrideName, String prefixName, Prefix.PrefixState state, boolean command) {
        PlayerPrefix playerPrefix = getCachedPlayerPrefix(p,overrideName,false);
        Prefix prefix = prefixCache.get(prefixName);

        //Restrict not to change aquire_mode
        if(prefix==null || (state != Prefix.PrefixState.OWNED && state != Prefix.PrefixState.OWNED_USING)) return false;
        if(command || state == Prefix.PrefixState.OWNED_USING) {
            playerPrefix.setPrefixState(prefixName,state);
            ChannelUtil.getInstance().setState(p,overrideName,prefixName,state);
            return true;
        }
        Prefix.Aquire_Mode aquire_mode = prefix.getAquire_mode();
        int mode_value = prefix.getMode_value();

        if(aquire_mode == Prefix.Aquire_Mode.BUY_POINT) {
            if(PlayerPointUtil.getInstance().consumePoints(overrideName,mode_value)) {
                playerPrefix.setPrefixState(prefixName,state);
                ChannelUtil.getInstance().setState(p,overrideName,prefixName,state);
                return true;
            }
            return false;
        } else {
            //TODO check op
            return false;
        }
    }
}
