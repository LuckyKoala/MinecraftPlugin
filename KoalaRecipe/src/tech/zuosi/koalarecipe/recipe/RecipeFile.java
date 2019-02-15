package tech.zuosi.koalarecipe.recipe;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import tech.zuosi.koalarecipe.util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by iwar on 2016/8/23.
 */
public class RecipeFile {
    private static FileManager recipeFile;
    private static FileConfiguration config;
    private String prefix = "Recipe.";

    static {
        recipeFile = new FileManager("Recipes.yml");
        config = recipeFile.getConfig();
    }

    public RecipeFile() {}

    public void writeRecipe(Recipe recipe) {
        config.set(prefix + recipe.timeStamp(),recipe.serialize());

        recipeFile.saveConfig();
    }

    @SuppressWarnings("unchecked")
    public Recipe readRecipe(String key) {
        String absoultlyKey = prefix + key;
        String disble_path = absoultlyKey + ".disable";
        if (config.contains(disble_path)) {
            if (config.getBoolean(disble_path)) return null;
        }
        ConfigurationSection cs = config.getConfigurationSection(absoultlyKey);
        Map<String,Object> map = cs.getValues(true);

        return Recipe.deserialize(key,map);
    }

    @SuppressWarnings("deprecation")
    public void readAllRecipe() {
        List<Recipe> recipeList = new ArrayList<>();
        ConfigurationSection cs = config.getConfigurationSection(prefix);
        if (cs == null) return;
        Set<String> keyList = cs.getKeys(false);
        for (String key : keyList) {
            Recipe recipe = readRecipe(key);
            if (recipe != null) recipeList.add(recipe);
        }

        RecipeData.setRecipeList(recipeList);
    }

    public void removeRecipe(String key) {
        //Clearing the map and set flag.
        key = prefix + key;
        config.set(key, null);
        config.set(key + ".disable", true);
    }

    public void updateRecipe() {
        config.set("Recipe",null);
        for (Recipe recipe : RecipeData.getRecipeList()) {
            writeRecipe(recipe);
        }
    }
}
