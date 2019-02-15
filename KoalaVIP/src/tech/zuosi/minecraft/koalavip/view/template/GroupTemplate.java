package tech.zuosi.minecraft.koalavip.view.template;

import java.util.List;

/**
 * Created by luckykoala on 18-3-26.
 */
public class GroupTemplate {
    private final String name, groupId;
    private final List<CommandTemplate> commandTemplates;

    public GroupTemplate(String name, String groupId, List<CommandTemplate> commandTemplates) {
        this.name = name;
        this.groupId = groupId;
        this.commandTemplates = commandTemplates;
    }

    public String getName() {
        return name;
    }

    public String getGroupId() {
        return groupId;
    }

    public List<CommandTemplate> getCommandTemplates() {
        return commandTemplates;
    }
}
