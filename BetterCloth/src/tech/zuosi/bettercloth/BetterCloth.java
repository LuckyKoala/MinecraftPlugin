package tech.zuosi.bettercloth;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import tech.zuosi.bettercloth.listener.ArmorListener;
import tech.zuosi.bettercloth.listener.DamageListener;
import tech.zuosi.bettercloth.util.Item;
import tech.zuosi.bettercloth.util.Util;


public class BetterCloth extends JavaPlugin {
	public void onEnable(){
	    /*if (this.getDataFolder().exists()) {
            if (!this.getDescription().getVersion().equals(this.getConfig().getString("version"))) {
                File configFile = new File(this.getDataFolder().getPath() + "config.yml");
                if (configFile.exists()) {
                    File backup = new File(this.getDataFolder().getPath() + "config.old");
                    if (backup.exists()) {
                        System.out.println("��ɾ������.old�ļ�����");
                        backup.delete();
                    }
                    if (configFile.renameTo(backup)) System.out.println("��⵽�ɰ������ļ����Ѵ����ļ�����");
                }
            }
        }*/
        this.saveDefaultConfig();
		new Item(this);
		new Util(this);
		this.getCommand("bch").setExecutor(new Command(this));
		this.getServer().getPluginManager().registerEvents(new DamageListener(this), this);
		this.getServer().getPluginManager().registerEvents(new ArmorListener(), this);
		this.getLogger().info("�ѳ�ʼ��������Ʒ");
	    this.getLogger().info("���BetterCloth������");
	    this.getLogger().info("˽�в������й¶�������κ���������ϵQQ3122506964");
	} 
    public void onDisable(){
		this.saveConfig();
		getLogger().info("BetterCloth unload.");
		HandlerList.unregisterAll(this);
    }
}
