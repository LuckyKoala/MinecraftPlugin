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
        getLogger().info("[Koala������]��ǰ����������ֵ->"+getConfig().getDouble("AttackDistanceLimit"));
        getLogger().info("[Koala������]���������ֵ��ʾ�������ļ��������õ�ֵ��������������ֵ��ʽ���⣬�볢������ֵС�������0");
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
