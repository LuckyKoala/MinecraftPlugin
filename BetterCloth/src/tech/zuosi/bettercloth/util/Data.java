package tech.zuosi.bettercloth.util;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iwar on 2016/6/9.
 */
public class Data {
    //public static int restoreSpeed = 30;
    public static Map<Player,Boolean> whetherToShowMessage = new HashMap<>();

    public static List<Player> lockArmor = new ArrayList<>();
    public static Map<Player,Integer> helmetMap = new HashMap<>();
    public static Map<Player,Integer> chestplateMap = new HashMap<>();
    public static Map<Player,Integer> leggingsMap = new HashMap<>();
    public static Map<Player,Integer> bootsMap = new HashMap<>();

    public static List<Player> helmetList = new ArrayList<>();
    public static List<Player> chestplateList = new ArrayList<>();
    public static List<Player> leggingsList = new ArrayList<>();
    public static List<Player> bootsList = new ArrayList<>();

    public static List<Player> helmetCache = new ArrayList<>();
    public static List<Player> chestplateCache = new ArrayList<>();
    public static List<Player> leggingsCache = new ArrayList<>();
    public static List<Player> bootsCache = new ArrayList<>();
}
