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
                        System.out.println("已删除过期.old文件备份");
                        backup.delete();
                    }
                    if (configFile.renameTo(backup)) System.out.println("检测到旧版配置文件，已创建文件备份");
                }
            }
        }*/
        this.saveDefaultConfig();
		new Item(this);
		new Util(this);
		this.getCommand("bch").setExecutor(new Command(this));
		this.getServer().getPluginManager().registerEvents(new DamageListener(this), this);
		this.getServer().getPluginManager().registerEvents(new ArmorListener(), this);
		this.getLogger().info("已初始化所有物品");
	    this.getLogger().info("插件BetterCloth已启动");
	    this.getLogger().info("私有插件请勿泄露，如有任何问题请联系QQ3122506964");
	} 
    public void onDisable(){
		this.saveConfig();
		getLogger().info("BetterCloth unload.");
		HandlerList.unregisterAll(this);
    }
}
