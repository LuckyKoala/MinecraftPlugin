package tech.zuosi.koalaprefix;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tech.zuosi.koalaprefix.data.DataManager;
import tech.zuosi.koalaprefix.data.Prefix;
import tech.zuosi.koalaprefix.gui.GUIHandler;
import tech.zuosi.koalaprefix.gui.PrefixMenu;

/**
 * Created by iwar on 2017/1/24.
 */
public class main extends JavaPlugin {
    private static JavaPlugin instance;
    public static boolean init;

    @Override
    public void onEnable() {
        instance = this;
        init = false;
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new GUIHandler(),this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        // allow to send to BungeeCord
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "Return", ChannelUtil.getInstance());
        // gets a Message from Bungee
        getCommand("pre").setExecutor(this);
    }

    @Override
    public void onDisable() {
        //
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if("pre".equals(label) && sender instanceof Player) {
            Player player = (Player) sender;

            // -> pre playerName prefixName prefixState
            if(args.length==3) {
                String playerName = args[0];
                String prefixName = args[1];
                String prefixSate = args[2];
                boolean result = DataManager.setState(player,playerName,prefixName,
                        Prefix.PrefixState.valueOf(prefixSate),true);

                if(result) player.sendMessage("设置成功");
                else player.sendMessage("设置失败");
                return true;
            } else if(args.length==1) {
                if(args[0].equalsIgnoreCase("open")) {
                    if(!init) ChannelUtil.getInstance().sendToBungeeCord(player,"requireupdate","");
                    else player.openInventory(new PrefixMenu(player).generateGUI());
                } else if(args[0].equalsIgnoreCase("debug")) {
                    StringBuilder builder = new StringBuilder();
                    DataManager.getAllPrefixCache().keySet().forEach(builder::append);
                    player.sendMessage(builder.toString());
                }
                return true;
            }

        }
        return false;
    }

    public static JavaPlugin getInstance() {
        return instance;
    }
}
