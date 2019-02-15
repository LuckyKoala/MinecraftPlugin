package tech.zuosi.koalarecipe.recipe;

import me.dpohvar.powernbt.PowerNBT;
import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTManager;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalarecipe.util.ItemUtil;

import java.util.*;

/**
 * Created by iwar on 2016/8/23.
 */
public class Recipe {
    private long timeStamp;
    private ItemStack product;
    private List<String> recipeStringList = new ArrayList<>(4);
    private Map<Character,ItemStack> markMap = new HashMap<>();
    private Map<Character,NBTCompound> ymlNBT = new HashMap<>();
    private static final NBTManager nbtManager = PowerNBT.getApi();

    private final int SIZE = 28;

    /**
     * 配方表
     * XXXXXXX
     * XXXXXXX
     * XXXXXXX
     * XXXXXXX
     * 比较配方时先比较X的布局，再比较信息
     *
     * 序列化
     *   配方表 List<String> recipeStringList
     *   字符对应ItemStack Map<Character,ItemStack>
     *   字符对应NBTCompound Map<Character,NBTCompound>
     */
    public Recipe() {}

    public Recipe tempBuild(String[] recipeStringArray, Map<Character,ItemStack> markMap) {
        return this.build(Long.parseLong("0"),new ItemStack(Material.AIR),recipeStringArray,markMap);
    }

    public Recipe build(long timeStamp, ItemStack product, String[] recipeStringArray, Map<Character,ItemStack> markMap) {
        this.timeStamp = timeStamp;
        this.product = product.clone();
        this.recipeStringList = Arrays.asList(recipeStringArray);
        this.markMap = markMap;
        initRecipeNBT();

        return this;
    }

    public Recipe build(long timeStamp, ItemStack product, List<String> recipeStringList, Map<Character,ItemStack> markMap) {
        this.timeStamp = timeStamp;
        this.product = product.clone();
        this.recipeStringList = recipeStringList;
        this.markMap = markMap;
        initRecipeNBT();

        return this;
    }

    private void initRecipeNBT() {
        for (Map.Entry<Character,ItemStack> entry : markMap.entrySet()) {
            ItemStack is = entry.getValue();
            if (is.getType() == Material.AIR) {
                ymlNBT.put(entry.getKey(), null);
            } else {
                ymlNBT.put(entry.getKey(), nbtManager.read(entry.getValue()));
            }
        }
    }

    public void loadProduct(NBTCompound compound) {
        if (compound != null) {
            compound.merge(nbtManager.read(product));
            nbtManager.write(product,compound);
        }
    }

    public boolean isSame(Recipe recipe) {
        if (!this.recipeStringList.equals(recipe.recipeStringList)) return false;
        final Set<Character> marks = this.markMap.keySet();
        if (!marks.equals(recipe.markMap.keySet())) return false;
        for (Character mark : marks) {
            ItemUtil in = new ItemUtil(this.markMap.get(mark));
            ItemUtil out = new ItemUtil(recipe.markMap.get(mark));
            if (!in.equals(out)) {
                return false;
            }
        }
        return true;
    }

    //完全相同 0[不用添加];配方相同，产品不同[覆盖配方] 1;配方不同 -1[直接添加]
    public int isConfict(Recipe recipe) {
        boolean isSameRecipe = isSame(recipe);
        ItemUtil in = new ItemUtil(this.product);
        ItemUtil out = new ItemUtil(recipe.product);

        if (!isSameRecipe) return -1;
        return in.equals(out)?0:1;
    }

    public String timeStamp() {
        return Long.toString(timeStamp);
    }

    public ItemStack product() {
        return this.product;
    }

    public Map<String,Object> serialize() {
        Map<String,Object> yml = new HashMap<>();
        Map<String,Object> ymlNBTToFile = new HashMap<>();

        for (Map.Entry<Character,NBTCompound> entry : ymlNBT.entrySet()) {
            NBTCompound nbtCompound = entry.getValue();
            if (nbtCompound == null) {
                ymlNBTToFile.put(entry.getKey().toString(),null);
            } else {
                Map<String,Object> noDisplayNBTEntry = nbtCompound.toHashMap();
                noDisplayNBTEntry.remove("display",noDisplayNBTEntry.get("display"));
                ymlNBTToFile.put(entry.getKey().toString(),noDisplayNBTEntry);
            }
        }
        for (Map.Entry<Character,ItemStack> entry : markMap.entrySet()) {
            ItemStack air = new ItemStack(Material.AIR);
            if (air.equals(entry.getValue()))
            entry.setValue(air.clone());
        }

        yml.put("product", product);
        yml.put("recipe", recipeStringList);
        yml.put("markMap", markMap);
        yml.put("nbt",ymlNBTToFile);

        Map<String,Object> noDisplayNBT = nbtManager.read(product)==null?null:nbtManager.read(product).toHashMap();
        if (noDisplayNBT != null) {
            noDisplayNBT.remove("display",noDisplayNBT.get("display"));
        }
        yml.put("pNBT",noDisplayNBT);


        return yml;
    }

    @SuppressWarnings("unchecked")
    public static Recipe deserialize(String ts, Map<String,Object> data) {
        //FIXME 反序列化NBT时，存取的数据类型会不一样，例如int->byte，导致后续判断不同
        long _timeStamp = Long.parseLong(ts);
        ItemStack _itemStack = ((ItemStack) data.get("product"));
        List<String> recipeString = (List<String>)data.get("recipe");
        Map<String,Object> markMapFromFile = ((MemorySection)data.get("markMap")).getValues(true);
        Map<Character,ItemStack> _markMap = new HashMap<>();
        Map<String,Object> ymlNBTFromFile = ((MemorySection)data.get("nbt")).getValues(false);

        for (Map.Entry<String,Object> entry : markMapFromFile.entrySet()) {
            ItemStack is = (ItemStack)entry.getValue();
            String key = entry.getKey();
            MemorySection ymlNBTSection = (MemorySection)ymlNBTFromFile.get(key);

            if (ymlNBTSection != null) {
                Map<String,Object> o = ymlNBTSection.getValues(false);
                if (o != null) {
                    o = recursiveConverter(o);
                    NBTCompound nbtTagCompound = new NBTCompound();
                    for (String oS : o.keySet()) {
                        nbtTagCompound.put(oS,o.get(oS));
                    }
                    nbtTagCompound.merge(nbtManager.read(is));
                    nbtManager.write(is,nbtTagCompound);
                }
            }

            _markMap.put(key.charAt(0),is.clone());
        }

        Recipe recipe = new Recipe().build(_timeStamp,_itemStack,recipeString,_markMap);
        Map<String,Object> _pNBT;
        if (data.get("pNBT") == null) {
            _pNBT = null;
        } else {
            _pNBT = ((MemorySection)data.get("pNBT")).getValues(false);
        }
        if (_pNBT != null) {
            NBTCompound _pNBTTagCompound = new NBTCompound(recursiveConverter(_pNBT));
            recipe.loadProduct(_pNBTTagCompound);
        } else {
            recipe.loadProduct(null);
        }

        return recipe;
    }

    private static Map<String,Object> recursiveConverter(Map<String,Object> map) {
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            Object object = entry.getValue();

            if (object instanceof MemorySection) {
                Map<String,Object> objectMap = ((MemorySection) object).getValues(true);
                Set<Map.Entry<String,Object>> entrySet = objectMap.entrySet();
                Map<String,Object> newObjectMap = new HashMap<>();
                for (Map.Entry<String,Object> entryIn : entrySet) {
                    if (entryIn.getKey().split("\\.").length == 1)
                        newObjectMap.put(entryIn.getKey(),entryIn.getValue());
                }
                entry.setValue(recursiveConverter(newObjectMap));
            }
        }

        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Recipe u = (Recipe) o;
        return timeStamp().equals(u.timeStamp());
    }

    @Override
    public int hashCode() {
        return Long.hashCode(timeStamp);
    }


}
