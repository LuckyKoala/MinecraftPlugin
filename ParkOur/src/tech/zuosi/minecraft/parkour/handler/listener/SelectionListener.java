package tech.zuosi.minecraft.parkour.handler.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.selection.Selection;
import tech.zuosi.minecraft.parkour.selection.SelectionManager;

import java.util.Stack;

/**
 * Created by LuckyKoala on 18-9-16.
 */
public class SelectionListener implements Listener {
    enum SelectAction {
        ADD_BLOCK(Action.LEFT_CLICK_BLOCK),
        DEL_BLOCK(Action.RIGHT_CLICK_BLOCK),
        PREVIOUS_STAGE(Action.LEFT_CLICK_AIR),
        NEXT_STAGE(Action.RIGHT_CLICK_AIR);

        final Action action;

        SelectAction(Action action) {
            this.action = action;
        }

        static SelectAction wrap(Action action) {
            for(SelectAction selectAction : values())
                if (selectAction.action == action)
                    return selectAction;

            return null;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if(action == Action.PHYSICAL) return;

        SelectionManager selectionManager = Core.getInstance().selectionManager;
        Player player = event.getPlayer();
        String playerName = player.getName();
        if(selectionManager.selectionMap.containsKey(playerName)
                && Material.STONE_AXE == event.getItem().getType()) {
            event.setCancelled(true);

            Selection selection = selectionManager.selectionMap.get(playerName);
            SelectAction selectAction = SelectAction.wrap(event.getAction());
            if(SelectAction.PREVIOUS_STAGE == selectAction) {
                toPreviousStage(selectionManager, player, playerName, selection);
            } else if(SelectAction.NEXT_STAGE == selectAction) {
                toNextStage(selectionManager, player, playerName, selection);
            } else {
                Selection.Stage stage = selection.getStage();
                Location location = event.getClickedBlock().getLocation(); //Note: y+1
                if(!selection.isFullSelection()) {
                    Stack<Vector> vectorStack = selection.getVectorStack();
                    if(SelectAction.ADD_BLOCK == selectAction) {
                        switch (stage) {
                            case START_POINT:
                                selection.getBuilder().startPoint(location);
                                player.sendMessage(String.format(ChatColor.LIGHT_PURPLE + "选中起点: %s", location.toVector()));
                                toNextStage(selectionManager, player, playerName, selection);
                                break;
                            case GAME_REGION:
                                Vector vector = location.toVector();
                                String vectorStage = selection.vectorStage();
                                player.sendMessage(String.format(ChatColor.LIGHT_PURPLE + "选中地点(%s): %s", vectorStage, vector));
                                selection.pushVector(vector);
                                break;
                            case END_POINT:
                                selection.getBuilder().endPoint(location);
                                player.sendMessage(String.format(ChatColor.LIGHT_PURPLE + "选中终点: %s", location.toVector()));
                                toNextStage(selectionManager, player, playerName, selection);
                                break;
                            case TIME_REQUIRED:
                            case FINISH:
                                break;
                        }
                    }else if(SelectAction.DEL_BLOCK == selectAction) {
                        switch (stage) {
                            case GAME_REGION:
                                if(vectorStack.empty()) {
                                    player.sendMessage(ChatColor.RED + "之前没有选择任何地点，无需回退");
                                } else {
                                    Vector vector = vectorStack.pop();
                                    player.sendMessage(String.format(ChatColor.DARK_RED + "反选地点(%s): %s", selection.vectorStage(), vector));
                                }
                                break;
                            case START_POINT:
                            case END_POINT:
                            case TIME_REQUIRED:
                            case FINISH:
                                break;
                        }
                    }

                } else {
                    Core.getInstance().getLogger().warning("Wrong path which reached to Stage.FINISH, please check");
                    Core.getInstance().getLogger().warning(" Current status:");
                    Core.getInstance().getLogger().warning("  playerName: "+playerName);
                    Core.getInstance().getLogger().warning("  stage: "+stage.name());
                    Core.getInstance().getLogger().warning("  selectAction: "+ (selectAction != null ? selectAction.name() : null));
                    Core.getInstance().getLogger().warning("  location: "+location.toString());
                }
            }
        }
    }

    private void toPreviousStage(SelectionManager selectionManager, Player player, String playerName, Selection selection) {
        if(selection.isEmptySelection()) {
            player.sendMessage(ChatColor.GREEN + "已退出选取程序");
            selectionManager.selectionMap.remove(playerName);
        } else {
            if(Selection.Stage.GAME_REGION == selection.getStage()) {
                selection.getVectorStack().clear();
            }
            Selection.Stage stage = selection.previousStage();
            player.sendMessage(stage.getDescription());
        }
    }

    private void toNextStage(SelectionManager selectionManager, Player player, String playerName, Selection selection) {
        if(selection.getStage() == Selection.Stage.GAME_REGION) {
            if(!selection.loadVectorStack()) {
                player.sendMessage(ChatColor.RED + "存在无效的游戏区域，请检查选中的地点集合中是否都是一组最低点最高点");
                return;
            }
        }

        Selection.Stage nextStage = selection.nextStage();
        if(Selection.Stage.TIME_REQUIRED == nextStage) {
            selectionManager.buildConversation(player, selection).begin();
        }
        player.sendMessage(nextStage.getDescription());
    }


}
