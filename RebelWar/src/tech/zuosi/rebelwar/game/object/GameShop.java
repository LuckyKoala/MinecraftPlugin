package tech.zuosi.rebelwar.game.object;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.*;

/**
 * Created by iwar on 2016/10/1.
 */
public class GameShop {
    private static GameShop INSTANCE;
    private GamePlayer currentPlayer;
    private Map<GamePlayer.Profession,Integer> professionPrices = new HashMap<>();
    private Map<GamePlayer.Profession,List<ItemStack>> professionItems = new HashMap<>();

    private GameShop() {
        initShop();
    }

    private void initShop() {
        List<ItemStack> ironCloth1 = new ArrayList<>();
        List<ItemStack> ironCloth2 = new ArrayList<>();
        List<ItemStack> leatherCloth5 = new ArrayList<>();
        List<ItemStack> diamondCloth1 = new ArrayList<>();
        List<ItemStack> diamondCloth3 = new ArrayList<>();
        ItemStack ironHelmet = new ItemStack(Material.IRON_HELMET);
        ItemStack ironChestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack ironLeggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
        ItemStack iron[] = new ItemStack[]{ironHelmet,ironChestplate,ironLeggings,ironBoots};
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack leather[] = new ItemStack[]{leatherHelmet,leatherChestplate,leatherLeggings,leatherBoots};
        ItemStack diamondHelmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack diamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack diamondLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemStack diamond[] = new ItemStack[]{diamondHelmet,diamondChestplate,diamondLeggings,diamondBoots};

        //步兵
        for (ItemStack i:iron) {
            i.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);
        }
        ironCloth1.addAll(Arrays.asList(iron));
        ironCloth1.add(new ItemStack(Material.STONE_SWORD));
        professionItems.put(GamePlayer.Profession.INFANTRYMAN,ironCloth1);

        //侦察兵
        for (ItemStack i:leather) {
            i.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);
        }
        leatherCloth5.addAll(Arrays.asList(leather));
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE,2);
        leatherCloth5.add(bow);
        leatherCloth5.add(new ItemStack(Material.STONE_SWORD));
        leatherCloth5.add(new ItemStack(Material.ARROW,30));
        professionItems.put(GamePlayer.Profession.SCOUT,leatherCloth5);

        //坦克
        for (ItemStack i:diamond) {
            i.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1);
        }
        diamondCloth1.addAll(Arrays.asList(diamond));
        ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
        ironSword.addEnchantment(Enchantment.DAMAGE_ALL,1);
        diamondCloth1.add(ironSword);
        professionItems.put(GamePlayer.Profession.TANK,diamondCloth1);

        //战士
        for (ItemStack i:iron) {
            i.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2);
        }
        ironCloth2.addAll(Arrays.asList(iron));
        ItemStack diamonSword = new ItemStack(Material.DIAMOND_SWORD);
        diamonSword.addEnchantment(Enchantment.DAMAGE_ALL,1);
        ironCloth2.add(diamonSword);
        professionItems.put(GamePlayer.Profession.SOLDIER,ironCloth2);

        //战神
        for (ItemStack i:diamond) {
            i.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,3);
        }
        diamondCloth3.addAll(Arrays.asList(diamond));
        diamonSword.addEnchantment(Enchantment.DAMAGE_ALL,2);
        diamondCloth3.add(diamonSword);
        professionItems.put(GamePlayer.Profession.LENGEND,diamondCloth3);

        professionPrices.put(GamePlayer.Profession.INFANTRYMAN,500);
        professionPrices.put(GamePlayer.Profession.SCOUT,800);
        professionPrices.put(GamePlayer.Profession.TANK,1000);
        professionPrices.put(GamePlayer.Profession.SOLDIER,1200);
        professionPrices.put(GamePlayer.Profession.LENGEND,2000);
    }

    public static GameShop getInstance() {
        if (INSTANCE==null) INSTANCE=new GameShop();
        return INSTANCE;
    }

    //FIXME 潜在威胁 单例模式 这个绑定必须每次更新
    public GameShop bindPlayer(GamePlayer gamePlayer) {
        this.currentPlayer=gamePlayer;
        return this;
    }

    public boolean purchaseProfession(GamePlayer.Profession profession) {
        if (currentPlayer==null) return false;
        //获取所有职业检查是否已经拥有
        if (currentPlayer.getProfessions().contains(profession)) return false;
        //添加职业
        if (currentPlayer.getGameStat().getCoinCount() < professionPrices.get(profession)) return false;
        currentPlayer.getGameStat().delCoin(professionPrices.get(profession));
        currentPlayer.addProfession(profession);
        return true;
    }

    @SuppressWarnings("deprecation")
    public enum SingleItem {
        BREAD(new ItemStack(Material.BREAD,5), 10),
        ARROW(new ItemStack(Material.ARROW,10), 10),
        GOLDENAPPLE(new ItemStack(Material.GOLDEN_APPLE), 50),
        SPEEDPOTION(new Potion(PotionType.SPEED,2,true).toItemStack(1), 100),
        ENDERPEARL(new ItemStack(Material.ENDER_PEARL,5), 200);

        private ItemStack itemStack;
        private int price;

        SingleItem(ItemStack itemStack,int price) {
            this.itemStack = itemStack;
            this.price = price;
        }

        public int getPrice() {return price;}

        public ItemStack getItemStack() {return itemStack;}

        public static SingleItem getSIById(int id) {
            id-=5;
            for (SingleItem si:SingleItem.values()) {
                if (si.ordinal() == id) {
                    return si;
                }
            }
            return null;
        }
    }

    //不是立即给予物品
    public boolean purchaseSingleItem(SingleItem singleItem) {
        if (currentPlayer==null) return false;
        if (currentPlayer.getGameStat().getCoinCount() < singleItem.getPrice()) return false;
        currentPlayer.getGameStat().delCoin(singleItem.getPrice());
        currentPlayer.addToNextItems(singleItem.getItemStack());
        return true;
    }

    public ItemStack[] getProfessionItems(GamePlayer.Profession profession) {
        return (ItemStack[]) professionItems.get(profession).toArray();
    }
}
