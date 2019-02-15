package tech.zuosi.minecraft.parkour.handler.command;

import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.util.tellraw.ClickAction;
import tech.zuosi.minecraft.parkour.util.tellraw.HoverAction;
import tech.zuosi.minecraft.parkour.util.tellraw.TellRawBuilder;
import tech.zuosi.minecraft.parkour.util.tellraw.TextColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

import static tech.zuosi.minecraft.parkour.handler.command.CommandDispatcher.TOP_COMMAND_LABEL;
import static tech.zuosi.minecraft.parkour.handler.command.ListCommand.LIST_COMMAND_LABEL;

/**
 * Created by LuckyKoala on 18-9-18.
 */
class MapEditorHelper {
    private static final String SPACE = " ";
    private static final int STEP = 4; //Display 4 item in one line at most

    private MapEditorHelper() {}

    public static void showDifficulty(String playerName) {
        Set<String> difficultySet = Core.getInstance().gameManager.getUnfoldedPathToMap().keySet();
        sendClickableInfo(playerName, new ArrayList<>(difficultySet), ClickAction.run_command,
                str -> TOP_COMMAND_LABEL + SPACE + LIST_COMMAND_LABEL + SPACE + str, "显示指定难度的场景列表");
    }

    public static void showScene(String playerName, String difficulty) {
        Set<String> sceneSet = Core.getInstance().gameManager.getUnfoldedPathToMap()
                .getOrDefault(difficulty, Collections.emptyMap())
                .keySet();
        sendClickableInfo(playerName, new ArrayList<>(sceneSet), ClickAction.run_command,
                str -> TOP_COMMAND_LABEL + SPACE + LIST_COMMAND_LABEL + SPACE + difficulty + SPACE + str,
                "显示指定场景的地图列表");
    }

    public static void showMap(String playerName, String difficulty, String scene) {
        List<String> sceneList = Core.getInstance().gameManager.getUnfoldedPathToMap()
                .getOrDefault(difficulty, Collections.emptyMap())
                .getOrDefault(scene, Collections.emptyList());
        sendClickableInfo(playerName, sceneList, ClickAction.suggest_command,
                Function.identity(),
                "填充到命令输入框");
    }

    private static void sendClickableInfo(String playerName, List<String> list, ClickAction clickAction,
                                   Function<String, String> strMapper, String text) {
        int subListCount = list.size() / STEP;
        if(list.size() % STEP != 0) subListCount++;
        IntStream.range(0, subListCount)
                .map(i -> i*4)
                .mapToObj(i -> list.subList(i, i+STEP > list.size() ? list.size() : i+STEP))
                .forEach(subList -> {
                    TellRawBuilder builder = new TellRawBuilder();
                    subList.forEach(str -> builder.text("  "+str).color(TextColor.green)
                            .onClick(clickAction, strMapper.apply(str))
                            .onHover(HoverAction.show_text, text).endItem());
                    builder.endAll().send(playerName);
                });
    }
}
