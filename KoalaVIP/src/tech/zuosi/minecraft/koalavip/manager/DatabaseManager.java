package tech.zuosi.minecraft.koalavip.manager;

import tech.zuosi.minecraft.koalavip.Core;
import tech.zuosi.minecraft.koalavip.database.DatabaseEngine;
import tech.zuosi.minecraft.koalavip.view.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by luckykoala on 18-3-23.
 */
public class DatabaseManager {
    private Map<UUID, String> uuidToName;
    private DatabaseEngine engine;

    public DatabaseManager(DatabaseEngine engine) {
        this.engine = engine;
        this.uuidToName = new HashMap<>();
    }

    public User get(String username) {
        uuidToName.putIfAbsent(Core.getInstance().translateNameToUUID(username), username);
        return engine.loadUser(username);
    }

    public User get(UUID uuid) {
        return Optional.ofNullable(uuidToName.get(uuid))
                .map(this::get)
                .orElseThrow(() -> new RuntimeException("无法识别的UUID，可能正在操作离线玩家（暂不支持）"));
    }

    public DatabaseEngine getEngine() {
        return engine;
    }

    public void tickAll(long now) {
        Core.getInstance().debug(() -> "DatabaseManager.tickAll."+now);
        Core.getInstance().getServer().getOnlinePlayers()
                .stream()
                .map(player -> get(player.getName()))
                .forEach(user -> user.tick(now));
    }
}
