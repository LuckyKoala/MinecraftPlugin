package tech.zuosi.koalarecipe.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalarecipe.handler.listener.CraftHandler;

import java.util.*;

/**
 * Created by iwar on 2016/8/26.
 */
public class RecipeLoader {
    public static final int[] MATERIALSLOT = {
            1,2,3,4,5,6,7,
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34
    };
    private static final int PRODUCTSLOT = 49;
    private static final ItemStack AIR = new ItemStack(Material.AIR);
    private static final char LOWER_A = 'a';
    private static final char UPPER_A = 'A';

    public RecipeLoader() {}

    public boolean load(Inventory inv) {
        Recipe recipe = loadAsRecipe(inv,true);

        if (recipe == null) return false;
        //RecipeData.I_addRecipe(recipe);
        RecipeData.addRecipe(recipe);

        return true;
    }

    public ItemStack craft(Inventory inv) {
        final List<Recipe> recipeList = RecipeData.getRecipeList();
        List<Integer> lockSlot = new ArrayList<>();
        Recipe craftRecipe = loadAsRecipe(inv,false);

        for (Recipe recipe : recipeList) {
            boolean compareResult = recipe.isSame(craftRecipe);
            if (compareResult) {
                for (int slot : MATERIALSLOT) {
                    if (!AIR.equals(safeItemStack(inv.getItem(slot)))) {
                        lockSlot.add(slot);
                    }
                }
                CraftHandler.setLockedSlot(inv,lockSlot);
                return recipe.product();
            }
        }

        return AIR;
    }

    private Recipe loadAsRecipe(Inventory inv,boolean hasProduct) {
        ItemStack product = safeItemStack(inv.getItem(PRODUCTSLOT));
        if (hasProduct && product.equals(AIR)) return null;

        long timeStamp = System.currentTimeMillis();
        Map<Character,ItemStack> markMap = new HashMap<>();
        Map<ItemStack,Character> reverseMarkMap = new HashMap<>();
        String[] strings;
        StringBuilder sb = new StringBuilder();
        Recipe recipe = new Recipe();
        int count = 0;

        for (int slot : MATERIALSLOT) {
            ItemStack is = safeItemStack(inv.getItem(slot));
            char mark;

            count++;
            if (markMap.containsValue(is)) {
                mark = reverseMarkMap.get(is);
            } else {
                mark = generateNewMark(markMap.keySet());
            }
            sb.append(mark);
            if (count % 7 == 0 && count != 28) sb.append("-");
            markMap.put(mark,is);
            reverseMarkMap.put(is,mark);
        }
        strings = sb.toString().split("-");

        return hasProduct ?
                recipe.build(timeStamp,product,strings,markMap) :
                recipe.tempBuild(strings,markMap);
    }

    public static ItemStack safeItemStack(ItemStack is) {
        return is==null?AIR:is;
    }

    private Character generateNewMark(Collection<Character> markList) {
        if (markList == null) markList = new ArrayList<>();

        for (int i=0;i<26;i++) {
            char mark = (char) (LOWER_A + i);
            if (markList.contains(mark)) continue;

            return mark;
        }
        for (int i=0;i<26;i++) {
            char mark = (char) (UPPER_A + i);
            if (markList.contains(mark)) continue;

            return mark;
        }

        return '0';
    }
}
