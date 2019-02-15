package tech.zuosi.koalarecipe.recipe;

import tech.zuosi.koalarecipe.util.AddMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2016/8/23.
 */
public class RecipeData {
    private static List<Recipe> recipeList = new ArrayList<>();
    private static RecipeFile recipeFile = new RecipeFile();

    public RecipeData() {}

    public static List<Recipe> getRecipeList() {
        return recipeList;
    }

    //Only use this method to load Recipe from file.
    @Deprecated
    public static void setRecipeList(List<Recipe> recipeList) {
        RecipeData.recipeList = recipeList;
    }

    public static void addRecipe(Recipe recipe) {
        AddMode mode = computeAddMode(recipe);

        switch (mode) {
            case ADD:
                recipeList.add(recipe);
                recipeFile.writeRecipe(recipe);
                return;
            case SKIP:
                return;
            case OVERRIDE:
                recipeList.add(recipe);
                recipeFile.writeRecipe(recipe);
                return;
        }
    }

    //True
    private static AddMode computeAddMode(Recipe newRecipe) {
        List<Recipe> RECIPELIST = new ArrayList<>();
        RECIPELIST.addAll(recipeList);
        List<Recipe> newRecipeList = recipeList;

        if (recipeList.isEmpty()) return AddMode.ADD;
        for (Recipe recipe : RECIPELIST) {
            int confictValue = recipe.isConfict(newRecipe);
            if (confictValue == 0) {
                return AddMode.SKIP;
            } else if (confictValue == 1) {
                recipeFile.removeRecipe(recipe.timeStamp());
                recipeList.remove(recipe);
                return AddMode.OVERRIDE;
            }
        }
        setRecipeList(newRecipeList);
        return AddMode.ADD;
    }
}
