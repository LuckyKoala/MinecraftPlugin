package tech.zuosi.minecraft.koalavip.view;

import tech.zuosi.minecraft.koalavip.constant.AssetStatus;
import tech.zuosi.minecraft.koalavip.view.template.GroupTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by luckykoala on 18-3-20.
 */
public class Group {
    private final GroupTemplate groupTemplate;
    private final List<Command> commandList;

    public Group(GroupTemplate groupTemplate) {
        this(groupTemplate, groupTemplate.getCommandTemplates().stream()
                .map(commandTemplate -> new Command(commandTemplate, 0))
                .collect(Collectors.toList()));
    }

    public Group(GroupTemplate groupTemplate, List<Command> commandList) {
        this.groupTemplate = groupTemplate;
        this.commandList = commandList;
    }

    public int getRewardFromAllCommand(String username, long now) {
        return (int)commandList.stream()
                .mapToInt(cmd -> cmd.invokeCmd(username, now))
                .filter(AssetStatus::isOkay)
                .average()
                .orElse(0d);
    }

    public String getGroupName() {
        return groupTemplate.getName();
    }

    public String getGroupIdentifier() {
        return groupTemplate.getGroupId();
    }

    public List<Command> getCommandList() {
        return commandList;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(this == obj) return true;
        if(obj instanceof Group) {
            Group anotherGroup = (Group) obj;
            return this.getGroupName().equals(anotherGroup.getGroupName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getGroupName().hashCode();
    }
}
