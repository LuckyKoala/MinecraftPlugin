package tech.zuosi.koalarecipe;

import org.bukkit.plugin.java.JavaPlugin;
import tech.zuosi.koalarecipe.handler.RecipeCommand;
import tech.zuosi.koalarecipe.handler.listener.CloseHandler;
import tech.zuosi.koalarecipe.handler.listener.CraftHandler;
import tech.zuosi.koalarecipe.handler.listener.CreateHandler;
import tech.zuosi.koalarecipe.recipe.RecipeFile;

/**
 * Created by iwar on 2016/8/23.
 */
public class KoalaRecipe extends JavaPlugin {
    private static KoalaRecipe instance;
    public static boolean onDebug;

    public void onEnable() {
        instance = this;
        onDebug = false;

        getCommand("recipe").setExecutor(new RecipeCommand());
        getServer().getPluginManager().registerEvents(new CreateHandler(),instance);
        getServer().getPluginManager().registerEvents(new CraftHandler(),instance);
        getServer().getPluginManager().registerEvents(new CloseHandler(),instance);
        new RecipeFile().readAllRecipe();
        getLogger().info("KoalaRecipe load.");
    }

    public void onDisable() {
        getLogger().info("KoalaRecipe unload.");
    }

    public static KoalaRecipe getInstance() {
        return instance;
    }

    public static boolean switchDebugMode() {
        onDebug = !onDebug;
        return onDebug;
    }
}
