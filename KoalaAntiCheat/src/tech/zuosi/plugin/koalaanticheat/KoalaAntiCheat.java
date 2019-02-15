package tech.zuosi.plugin.koalaanticheat;

import org.bukkit.plugin.java.JavaPlugin;
import tech.zuosi.plugin.koalaanticheat.listener.PlayerAttackListener;

/**
 * Created by iwar on 2016/10/5.
 */
public class KoalaAntiCheat extends JavaPlugin {
    private static KoalaAntiCheat INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE=this;
        saveDefaultConfig();
        getLogger().info("[Koala反作弊]当前攻击距离阈值->"+getConfig().getDouble("AttackDistanceLimit"));
        getLogger().info("[Koala反作弊]如果以上阈值显示与配置文件中所设置的值不符，可能是数值格式问题，请尝试在数值小数点后添0");
        getServer().getPluginManager().registerEvents(new PlayerAttackListener(),INSTANCE);
    }

    @Override
    public void onDisable() {
        //
    }

    public static KoalaAntiCheat getINSTANCE() {
        return INSTANCE;
    }
}
