package tech.zuosi.minecraft.parkour.ui;

import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.GameManager;
import tech.zuosi.minecraft.parkour.game.MapPath;
import tech.zuosi.minecraft.parkour.game.format.DifficultyFormat;
import tech.zuosi.minecraft.parkour.game.format.FormatEntity;
import tech.zuosi.minecraft.parkour.game.format.SceneFormat;
import tech.zuosi.minecraft.parkour.util.NBTUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static tech.zuosi.minecraft.parkour.util.FormatUtil.setPlaceHolders;


/**
 * Created by LuckyKoala on 18-9-16.
 */
@NoArgsConstructor
public class PaneManager {

    public static final String PANE_PREFIX = "ParkOur ";
    private static final String MAIN_PANE_TITLE = PANE_PREFIX+"主菜单";

    public void showMainPaneTo(Player player) {
        player.openInventory(createMainPane(player));
    }

    public void requestToViewInventory(Player player, String target, String dataStr) {
        MapPath data = MapPath.fromString(dataStr);
        if(MapPath.LEVEL.equals(target)) {
            boolean success = Core.getInstance().gameManager.addPlayerToGame(player, data);
            if (!success) {
                player.sendMessage(ChatColor.RED + "未解锁该地图，获得上一级别的二星以上评价即可解锁");
            }
        } else if(MapPath.SCENE.equals(target)) {
            player.openInventory(createScenePane(player, data.getDifficulty(), data.getScene()));
        } else if(MapPath.DIFFICULTY.equals(target)) {
            player.openInventory(createDifficultyPane(player, data.getDifficulty()));
        }
    }

    private Inventory createMainPane(Player player) {
        GameManager gameManager = Core.getInstance().gameManager;
        Map<String, Map<String, List<String>>> unfoldedPathToMapCache = gameManager.getUnfoldedPathToMap();

        //Create mainPane
        int mainSize = unfoldedPathToMapCache.size();
        int mainReminder = mainSize%9;
        if(mainReminder != 0) mainSize += 9-mainReminder;
        Inventory mainPane = Bukkit.createInventory(null, mainSize, MAIN_PANE_TITLE);
        List<ItemStack> mainItems = new ArrayList<>(unfoldedPathToMapCache.size());
        unfoldedPathToMapCache.keySet().forEach(difficulty -> {
            MapPath data = MapPath.partialPath(difficulty);
            DifficultyFormat format = gameManager.getDifficultyFormatFor(difficulty);
            //Create item to represent difficulty and put it in mainPane
            ItemStack difficultyItem = new NBTUtil(new ItemStack(format.getBaseFormat().getModel(), 1, (short) format.getBaseFormat().getModelData()))
                    .initLore(setPlaceHolders(player, data, format.getBaseFormat().getLoreTemplate()).toArray(new String[0]))
                    .setData("target", MapPath.DIFFICULTY)
                    .setData("data", data.toString())
                    .setDisplayName(setPlaceHolders(player, data, format.getBaseFormat().getNameTemplate()))
                    .getItemStack();
            mainItems.add(difficultyItem);
        });
        mainPane.addItem(mainItems.toArray(new ItemStack[0]));
        return mainPane;
    }

    private Inventory createDifficultyPane(Player player, String difficulty) {
        GameManager gameManager = Core.getInstance().gameManager;
        Set<String> sceneStrs = gameManager.getUnfoldedPathToMap().get(difficulty).keySet();

        //Create difficultyPane
        int sceneSize = sceneStrs.size();
        int sceneReminder = sceneSize%9;
        if(sceneReminder != 0) sceneSize += 9-sceneReminder;
        Inventory difficultyPane = Bukkit.createInventory(null, sceneSize, PANE_PREFIX +difficulty);
        List<ItemStack> sceneItems = new ArrayList<>(sceneSize);
        sceneStrs.forEach(scene -> {
            MapPath data = MapPath.partialPath(difficulty, scene);
            SceneFormat sceneFormat = gameManager.getDifficultyFormatFor(difficulty).getSceneFormat(scene);
            //Create item to represent scene and put it in difficultyPane
            ItemStack sceneItem = new NBTUtil(new ItemStack(sceneFormat.getBase().getModel(), 1, (short) sceneFormat.getBase().getModelData()))
                    .initLore(setPlaceHolders(player, data, sceneFormat.getBase().getLoreTemplate()).toArray(new String[0]))
                    .setData("target", MapPath.SCENE)
                    .setData("data", data.toString())
                    .setDisplayName(setPlaceHolders(player, data, sceneFormat.getBase().getNameTemplate()))
                    .getItemStack();
            sceneItems.add(sceneItem);
        });
        difficultyPane.addItem(sceneItems.toArray(new ItemStack[0]));
        return difficultyPane;
    }

    private Inventory createScenePane(Player player, String difficulty, String scene) {
        GameManager gameManager = Core.getInstance().gameManager;
        List<String> paths = gameManager.getUnfoldedPathToMap().get(difficulty).get(scene);

        //Create scenePane
        int levelSize = paths.size();
        int levelReminder = levelSize%9;
        if(levelReminder != 0) levelSize += 9-levelReminder;
        Inventory scenePane = Bukkit.createInventory(null, levelSize, PANE_PREFIX +difficulty+" "+scene);
        List<ItemStack> levelItems = new ArrayList<>(paths.size());
        paths.sort((o1, o2) -> {
            int l1 = Integer.valueOf(MapPath.fromString(o1).getLevel());
            int l2 = Integer.valueOf(MapPath.fromString(o2).getLevel());
            return l1-l2;
        });
        paths.forEach(path -> {
            MapPath data = MapPath.fromString(path);
            SceneFormat sceneFormat = gameManager.getDifficultyFormatFor(difficulty).getSceneFormat(scene);
            FormatEntity formatEntity;
            if(gameManager.isMapUnlock(player, data)) {
                //Unlocked
                formatEntity = sceneFormat.getUnlock();
            } else {
                //Locked
                formatEntity = sceneFormat.getLock();
            }
            //Create item to represent MapPath and put it in scenePane
            ItemStack levelItem = new NBTUtil(new ItemStack(formatEntity.getModel(), 1, (short) formatEntity.getModelData()))
                    .initLore(setPlaceHolders(player, data, formatEntity.getLoreTemplate()).toArray(new String[0]))
                    .setData("target", MapPath.LEVEL)
                    .setData("data", data.toString())
                    .setDisplayName(setPlaceHolders(player, data, formatEntity.getNameTemplate()))
                    .getItemStack();
            levelItems.add(levelItem);
        });
        scenePane.addItem(levelItems.toArray(new ItemStack[0]));
        return scenePane;
    }
}
