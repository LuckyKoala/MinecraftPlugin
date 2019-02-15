package tech.zuosi.rebelwar.game.manager;

import tech.zuosi.rebelwar.game.object.Arena;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/9/30.
 * |-ArenaManager 场地管理
 |-Set<Arena> 存储所有Arena实例
 |-addArena(Arena) 添加Arena实例
 |-需通知QueueManger增加队列
 |-delArena(Arena) 移除Arena实例
 */
public class ArenaManager {
    private static Map<String,Arena> arenaMap = new HashMap<>();
    private static ArenaManager INSTANCE;

    public ArenaManager() {}

    public boolean isExist(String arenaName) {
        return arenaMap.containsKey(arenaName);
    }

    public boolean addArena(String arenaName,Arena arena) {
        if (isExist(arenaName)) return false;
        arenaMap.put(arenaName,arena);
        QueueManager.getINSTANCE().addToQueue(arenaName,arena);
        return true;
    }

    public boolean delArena(String arenaName) {
        if (!isExist(arenaName)) return false;
        arenaMap.remove(arenaName);
        QueueManager.getINSTANCE().removeFromQueue(arenaName);
        return true;
    }

    public void addAllArena(Map<String,Arena> map) {
        for (Map.Entry<String,Arena> entry:map.entrySet()) {
            addArena(entry.getKey(),entry.getValue());
        }
    }

    public Arena getArena(String arenaName) {
        if (!isExist(arenaName)) return null;
        return arenaMap.get(arenaName);
    }

    public Map<String, Arena> getArenaMap() {
        return arenaMap;
    }

    public static ArenaManager getInstance() {
        if (INSTANCE==null) INSTANCE=new ArenaManager();
        return INSTANCE;
    }
}
