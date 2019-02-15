package tech.zuosi.minecraft.koalavip.manager;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import tech.zuosi.minecraft.koalavip.view.BuffCard;
import tech.zuosi.minecraft.koalavip.view.Command;
import tech.zuosi.minecraft.koalavip.view.Group;
import tech.zuosi.minecraft.koalavip.view.template.BuffCardTemplate;
import tech.zuosi.minecraft.koalavip.view.template.CommandTemplate;
import tech.zuosi.minecraft.koalavip.view.template.GroupTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by luckykoala on 18-3-26.
 */
public class TemplateManager {
    private final Map<String, BuffCardTemplate> buffCardTemplateMap;
    private final Map<String, CommandTemplate> commandTemplateMap;
    private final RangeMap<Integer, Integer> rangeToLevel;
    private final List<GroupTemplate> groupList; //以等级为索引

    public TemplateManager(Map<String, BuffCardTemplate> buffCardTemplateMap,
                           Map<String, CommandTemplate> commandTemplateMap,
                           Map<Integer,GroupTemplate> groupData) {
        this.buffCardTemplateMap = buffCardTemplateMap;
        this.commandTemplateMap = commandTemplateMap;

        List<Integer> endpoints = new ArrayList<>(groupData.keySet());
        endpoints.sort(Comparator.naturalOrder());
        this.groupList = endpoints.stream()
                .map(groupData::get)
                .collect(Collectors.toList());
        this.rangeToLevel = TreeRangeMap.create();

        int levelCounter = 0;
        int size = endpoints.size();
        for(int i=0; i < size; i++) {
            int lower = endpoints.get(i);
            if(i+1 < size) {
                //还有上界
                int upper = endpoints.get(i+1);
                this.rangeToLevel.put(Range.closedOpen(lower, upper), levelCounter);
                levelCounter++;
            } else {
                //没有上界
                this.rangeToLevel.put(Range.atLeast(lower), levelCounter);
                break;
            }
        }
    }

    public Map<String, BuffCardTemplate> getBuffCardTemplateMap() {
        return buffCardTemplateMap;
    }

    //For cli
    public BuffCard newBuffCard(String name) {
        BuffCardTemplate template = buffCardTemplateMap.get(name);
        if(template == null) return null;
        return new BuffCard(template, template.getDays(), 0L);
    }

    //For database
    public BuffCard buffCardOf(String name, int remainDays, long lastTimeUsed) {
        return Optional.ofNullable(buffCardTemplateMap.get(name))
                .map(buffCardTemplate -> new BuffCard(buffCardTemplate, remainDays, lastTimeUsed))
                .orElseThrow(() -> new RuntimeException(String.format("Undefined buffcard of name %s", name)));
    }

    public Command commandOf(String name, long lastTimeInvoked) {
        return Optional.ofNullable(commandTemplateMap.get(name))
                .map(commandTemplate -> new Command(commandTemplate, lastTimeInvoked))
                .orElseThrow(() -> new RuntimeException(String.format("Undefined command of name %s", name)));
    }

    public Group groupOf(int moneySpentTotal, int downgradeTimes) {
        return Optional.ofNullable(rangeToLevel.get(moneySpentTotal))
                .map(level -> level-downgradeTimes < 0 ? 0 : level-downgradeTimes)
                .map(groupList::get)
                .map(Group::new)
                .orElseThrow(() -> new RuntimeException("找不到符合条件的VIP组，请确保所有VIP的区间中没有遗落的范围"));
    }

    public Group groupOf(int moneySpentTotal, int downgradeTimes, List<Command> commands) {
        return Optional.ofNullable(rangeToLevel.get(moneySpentTotal))
                .map(level -> level-downgradeTimes < 0 ? 0 : level-downgradeTimes)
                .map(groupList::get)
                .map(template -> new Group(template, commands))
                .orElseThrow(() -> new RuntimeException("找不到符合条件的VIP组，请确保所有VIP的区间中没有遗落的范围"));
    }
}
