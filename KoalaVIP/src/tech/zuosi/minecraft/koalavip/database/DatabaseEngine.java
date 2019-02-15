package tech.zuosi.minecraft.koalavip.database;

import tech.zuosi.minecraft.koalavip.view.BuffCard;
import tech.zuosi.minecraft.koalavip.view.Command;
import tech.zuosi.minecraft.koalavip.view.User;

/**
 * Created by luckykoala on 18-3-24.
 */
public interface DatabaseEngine {
    boolean init(String username, String password, String database, int port);
    void close();

    User loadUser(String username);
    void unloadUser(String username);
    boolean updateUser(User user);
    boolean saveGroup(String username);
    boolean updateCommand(String username, Command command);
    boolean updateBuffCard(String username, BuffCard buffCard);
}
