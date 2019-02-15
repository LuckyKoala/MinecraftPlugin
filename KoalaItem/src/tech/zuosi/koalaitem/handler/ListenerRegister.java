package tech.zuosi.koalaitem.handler;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.handler.gui.*;
import tech.zuosi.koalaitem.handler.interact.ShowMenu;
import tech.zuosi.koalaitem.handler.interact.attribute.HealthEffect;
import tech.zuosi.koalaitem.handler.interact.attribute.ManaEffect;
import tech.zuosi.koalaitem.handler.interact.event.*;

/**
 * Created by iwar on 2016/7/21.
 */
public class ListenerRegister {
    private KoalaItem instance = KoalaItem.INSTANCE;
    private PluginManager pm = instance.getServer().getPluginManager();

    private void register(Listener listener) {
        pm.registerEvents(listener,instance);
    }

    public ListenerRegister() {
        register(new Brew());
        register(new Craft());
        register(new Item());
        register(new Maker());
        register(new Menu());
        register(new Test());

        register(new HealthEffect());
        register(new ManaEffect());

        register(new EntityAttackPlayer());
        register(new EntityDeath());
        register(new PlayerAttackEntity());
        register(new PlayerInteract());
        register(new PlayerItemConsume());
        register(new PlayerItemHeld());
        register(new PlayerMove());
        register(new PlayerToggleSneak());

        register(new ShowMenu());
    }
}
