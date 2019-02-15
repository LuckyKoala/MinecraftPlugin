package tech.zuosi.minecraft.parkour.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.database.DataManager;
import tech.zuosi.minecraft.parkour.game.GameManager;
import tech.zuosi.minecraft.parkour.game.MapPath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by LuckyKoala on 18-9-18.
 */
public class FormatUtil {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("[$]([^$]+)[$]");
    private static final String STAR = "\u2730";
    private static final String ONE_STAR = stars(1);
    private static final String TWO_STARS = stars(2);
    private static final String THREE_STARS = stars(3);
    public static final String STARS_TEMPLATE = ONE_STAR+" %s  "+TWO_STARS+" %s  "+THREE_STARS+" %s";

    private static final Map<String, Hook> hooks = new HashMap<String, Hook>() {{
        DataManager dataManager = Core.getInstance().dataManager;
        GameManager gameManager = Core.getInstance().gameManager;
        put("mapUnlockedStar", (p, data) ->
                stars(dataManager.loadPlayerData(p.getName()).starsUnlocked(data)));
        put("mapUnlocked", (p, data) ->
                Integer.toString(dataManager.loadPlayerData(p.getName()).starsUnlocked(data)));
        put("mapBest", (p, data) ->
                millisecondsFormat(dataManager.loadPlayerData(p.getName()).bestTime(data)));
        put("mapAllBest", (p, data) ->
                millisecondsFormat(dataManager.loadBestData().bestTime(data)));
        put("mapAllBestName", (p, data) -> dataManager.loadAllBestName(data));
        put("sceneUnlocked", (p, data) ->
                Integer.toString(dataManager.loadPlayerData(p.getName()).starsUnlockedForScene(data)));
        put("sceneTotal", (p, data) ->
                Integer.toString(gameManager.starsForSceneTotal(data)));

        put("3S", (p, data) ->
                millisecondsFormat(gameManager.fromMapPath(data).getTimeRequiredForThreeStars()));
        put("2S", (p, data) ->
                millisecondsFormat(gameManager.fromMapPath(data).getTimeRequiredForTwoStars()));
        put("1S", (p, data) ->
                millisecondsFormat(gameManager.fromMapPath(data).getTimeRequiredForOneStar()));

        put("dN", (p, data) -> data.getDifficulty());
        put("sN", (p, data) -> data.getScene());
        put("lN", (p, data) -> data.getLevel());
    }};

    @FunctionalInterface
    interface Hook {
        String onRequest(Player p, MapPath data);
    }

    private FormatUtil() {}

    public static String setPlaceHolders(Player p, MapPath data, String text) {
        if(null == text || text.trim().isEmpty()) return "";

        Matcher m = PLACEHOLDER_PATTERN.matcher(text);
        while(m.find()) {
            String identifier = m.group(1);
            if (hooks.containsKey(identifier)) {
                String value = hooks.get(identifier).onRequest(p, data);
                if (value != null) {
                    text = text.replaceAll(Pattern.quote(m.group()), Matcher.quoteReplacement(value));
                }
            }
        }
        return PlaceholderAPI.setPlaceholders(p, text);
    }

    public static List<String> setPlaceHolders(Player p, MapPath data, List<String> textList) {
        return textList.stream()
                .map(text -> setPlaceHolders(p, data, text))
                .collect(Collectors.toList());
    }

    // 00:01:500 1.5s
    public static String millisecondsFormat(long ms) {
        long milliSeconds = ms%1000;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%d:%d:%d", minutes, seconds, milliSeconds);
    }

    public static long millisecondsFrom(String str) {
        String[] data = str.split(":");
        if(data.length!=3) return 0L;
        return TimeUnit.MINUTES.toMillis(Long.valueOf(data[0]))
                + TimeUnit.SECONDS.toMillis(Long.valueOf(data[1]))
                + Long.valueOf(data[2]);
    }

    public static String stars(int count) {
        return new String(new char[count]).replace("\0", STAR);
    }
}
