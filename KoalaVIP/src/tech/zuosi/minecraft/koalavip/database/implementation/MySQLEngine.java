package tech.zuosi.minecraft.koalavip.database.implementation;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import tech.zuosi.minecraft.koalavip.Core;
import tech.zuosi.minecraft.koalavip.database.DatabaseEngine;
import tech.zuosi.minecraft.koalavip.view.BuffCard;
import tech.zuosi.minecraft.koalavip.view.Command;
import tech.zuosi.minecraft.koalavip.view.Group;
import tech.zuosi.minecraft.koalavip.view.User;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by luckykoala on 18-3-24.
 */

//TODO 提供删除长期未活动用户的方法
public class MySQLEngine implements DatabaseEngine {
    private Map<String, User> userCache;
    private ComboPooledDataSource comboPooledDataSource;
    private Tables tables = new Tables();

    public MySQLEngine() {
        this.userCache = new HashMap<>();
    }

    @Override
    public boolean init(String username, String password, String database, int port) {
        comboPooledDataSource = new ComboPooledDataSource();
        try {
            comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
            return false;
        }
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:"+port+"/"+database
                +"?useUnicode=true&characterEncoding=UTF8");
        comboPooledDataSource.setUser(username);
        comboPooledDataSource.setPassword(password);

        setupTable();
        return true;
    }

    private void setupTable() {
        boolean setup = Core.getInstance().getConfig().getBoolean("setup");
        if(setup) {
            //已经初始化过
        } else {
            //初始化表
            try(
                    Connection connection = getConnection();
                    PreparedStatement createUser = connection.prepareStatement(tables.userTableSql);
                    PreparedStatement createBuffcard = connection.prepareStatement(tables.buffcardTableSql);
                    PreparedStatement createCommand = connection.prepareStatement(tables.commandTableSql);
                    PreparedStatement createOnetime = connection.prepareStatement(tables.onetimeTableSql);
                    ) {
                createUser.executeUpdate();
                createBuffcard.executeUpdate();
                createCommand.executeUpdate();
                createOnetime.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            Core.getInstance().getConfig().set("setup", true);
        }
    }

    private Connection getConnection() {
        Connection con = null;
        try {
            con = comboPooledDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    @Override
    public void close() {
        comboPooledDataSource.close();
    }

    @Override
    public User loadUser(String username) {
        return userCache.computeIfAbsent(username, name -> {
            //缓存里没有，先去查数据库，再就是初始化用户
            Core.getInstance().debug(() -> "MySQLEngine.loadUser.初始化用户"+name);
            User user = loadUserFromDatabase(name);
            return user!=null ? user : initUser(name);
        });
    }

    @Override
    public void unloadUser(String username) {
        Core.getInstance().debug(() -> "MySQLEngine.unloadUser.从缓存中卸载用户"+username);
        userCache.remove(username);
    }

    private PreparedStatement createWithUsernameFilled(Connection connection,String sql, String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        return preparedStatement;
    }

    private User loadUserFromDatabase(String username) {
        User user = null;
        //数据库
        try(
                Connection connection = getConnection();
                PreparedStatement queryUser = createWithUsernameFilled(connection,
                        "SELECT username, lastTimePurchasing, moneySpentLocked, moneySpentTotal, downgradeTimes " +
                                "FROM " + tables.userTableName + " WHERE username=?", username
                );
                ResultSet queryUserResultSet = queryUser.executeQuery();
                PreparedStatement queryBuffCard = createWithUsernameFilled(connection,
                        "SELECT cid, templatename, remainDays, lastTimeUsed FROM " + tables.buffcardTableName + " " +
                                "WHERE owner=?", username
                );
                ResultSet queryBuffCardResultSet = queryBuffCard.executeQuery();
                PreparedStatement queryCommand = createWithUsernameFilled(connection,
                        "select cmdid, templatename, lastTimeInvoked FROM " + tables.commandTableName + " WHERE owner=?", username
                );
                ResultSet queryCommandResultSet = queryCommand.executeQuery();
        ) {
            //找不到用户 快速失败
            if(!queryUserResultSet.next()) {
                Core.getInstance().debug(() -> "数据库中找不到用户"+username);
                return null;
            }
            //载入用户
            user = new User(
                    queryUserResultSet.getString(1), null, queryUserResultSet.getLong(2),
                    queryUserResultSet.getInt(3), queryUserResultSet.getInt(4),
                    queryUserResultSet.getInt(5)
            );

            //载入BuffCard
            while(queryBuffCardResultSet.next()) {
                BuffCard buffCard = Core.getInstance().getTemplateManager()
                        .buffCardOf(queryBuffCardResultSet.getString(2),
                                queryBuffCardResultSet.getInt(3), queryBuffCardResultSet.getLong(4));
                buffCard.setId(queryBuffCardResultSet.getInt(1));
                user.addBuffCard(buffCard);
            }

            //载入用户组及其命令
            List<Command> commands = new ArrayList<>();
            while(queryCommandResultSet.next()) {
                Command command = Core.getInstance().getTemplateManager()
                        .commandOf(queryCommandResultSet.getString(2), queryCommandResultSet.getLong(3));
                command.setId(queryCommandResultSet.getInt(1));
                commands.add(command);
            }
            user.setGroup(Core.getInstance().getTemplateManager()
                    .groupOf(user.getMoneySpentTotal(), user.getDowngradeTimes(), commands));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    private User initUser(String name) {
        Core.getInstance().debug(() -> "初始化用户"+name);
        User userdata = new User(name);
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO " + tables.userTableName + "(username, lastTimePurchasing, moneySpentLocked, " +
                        "moneySpentTotal, downgradeTimes) VALUES(?,?,?,?,?)")) {
            statement.setString(1, userdata.getUsername());
            statement.setLong(2, userdata.getLastTimePurchasing());
            statement.setInt(3, userdata.getMoneySpentLocked());
            statement.setInt(4, userdata.getMoneySpentTotal());
            statement.setInt(5, userdata.getDowngradeTimes());
            return statement.executeUpdate() >= 1 ? userdata : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE " + tables.userTableName + " SET lastTimePurchasing=?, moneySpentTotal=?, " +
                        "moneySpentLocked=?, downgradeTimes=? WHERE username=?"
        )) {
            statement.setLong(1, user.getLastTimePurchasing());
            statement.setInt(2, user.getMoneySpentTotal());
            statement.setInt(3, user.getMoneySpentLocked());
            statement.setInt(4, user.getDowngradeTimes());
            statement.setString(5, user.getUsername());
            return statement.executeUpdate() >= 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean saveGroup(String username) {
        User user = loadUser(username);
        if(user == null) return false;
        Group group = user.getGroup();
        if(group == null) return false;
        List<Command> commands = group.getCommandList();
        Set<String> onetimeRecord = new HashSet<>();

        try(
                Connection connection = getConnection();
                PreparedStatement removeOldCommands = connection.prepareStatement("DELETE FROM " + tables.commandTableName + " WHERE owner=?");
                PreparedStatement insertNewCommands = connection.prepareStatement(
                        "INSERT INTO " + tables.commandTableName + "(templatename, lastTimeInvoked, owner) VALUES(?,?,?)"
                );
                PreparedStatement readOnetimeRecord = createWithUsernameFilled(connection,
                        "SELECT cmd FROM " + tables.onetimeTableName + " WHERE owner=?", username
                );
                ResultSet onetimeResultSet = readOnetimeRecord.executeQuery();
                PreparedStatement queryId = connection.prepareStatement("SELECT MAX(cmdid) FROM " + tables.commandTableName + "");
                ResultSet idResultSet = queryId.executeQuery();
        ) {
            //加载onetime记录表
            while(onetimeResultSet.next()) {
                onetimeRecord.add(onetimeResultSet.getString(1));
            }
            //从数据库中删除旧的命令
            removeOldCommands.setString(1, username);
            removeOldCommands.executeUpdate();
            //获取之前的最大id
            if(idResultSet.next()) {
                int lastId = idResultSet.getInt(1);
                //将新的命令列表写入数据库
                for(Command cmd : commands) {
                    if(cmd.getTemplate().isOnetime() && onetimeRecord.contains(cmd.getTemplate().getName()))
                        cmd.setLastTimeInvoked(1);
                    insertNewCommands.clearParameters();
                    insertNewCommands.setString(1, cmd.getTemplate().getName());
                    insertNewCommands.setLong(2, cmd.getLastTimeInvoked());
                    insertNewCommands.setString(3, username);
                    insertNewCommands.addBatch();
                    cmd.setId(++lastId);
                }
                insertNewCommands.executeBatch();
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Core.getInstance().debug(() -> "saveGroup: failed since it run through return statement in try");
        return false;
    }

    @Override
    public boolean updateCommand(String username, Command command) {
        //更新onetime记录表
        if(command.getTemplate().isOnetime() && command.getLastTimeInvoked() != 0) {
            //该一次性命令已经使用过了
            Core.getInstance().debug(() ->
                    String.format("更新onetime记录中 [User: %s, Cmd: %s]",
                            username, command.getTemplate().getCmd()));
            try(Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + tables.onetimeTableName + "(cmd, owner) VALUES(?,?)"
            )) {
                statement.setString(1, command.getTemplate().getName());
                statement.setString(2, username);
                int rowEffected = statement.executeUpdate();
                Core.getInstance().debug(() -> "onetime insert rowEffected: "+rowEffected);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        //更新命令
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE " + tables.commandTableName + " SET lastTimeInvoked=? WHERE cmdid=?"
        )) {
            statement.setLong(1, command.getLastTimeInvoked());
            statement.setInt(2, command.getId());
            int rowEffected = statement.executeUpdate();
            Core.getInstance().debug(() -> String.format("command[%d] update rowEffected: %d",
                    command.getId(), rowEffected));
            return rowEffected >= 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateBuffCard(String username, BuffCard buffCard) {
        if(buffCard.getId() == -1) {
            //插入
            try(Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + tables.buffcardTableName + "(templatename, remainDays, lastTimeUsed, owner) " +
                            "VALUE(?,?,?,?)");
                PreparedStatement queryId = connection.prepareStatement("SELECT MAX(cid) FROM " + tables.buffcardTableName + "");
                ResultSet idResultSet = queryId.executeQuery();
            ) {
                statement.setString(1, buffCard.getTemplate().getName());
                statement.setInt(2, buffCard.getRemainDays());
                statement.setLong(3, buffCard.getLastTimeUsed());
                statement.setString(4, username);
                int rowEffected = statement.executeUpdate();
                Core.getInstance().debug(() -> String.format("buffcard[%d] update rowEffected: %d",
                        buffCard.getId(), rowEffected));
                if(rowEffected < 1) return false;

                if(idResultSet.next()) {
                    buffCard.setId(idResultSet.getInt(1));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            //更新
            try(Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(
                    "UPDATE " + tables.buffcardTableName + " SET remainDays=?,lastTimeUsed=? WHERE cid=?"
            )) {
                statement.setInt(1, buffCard.getRemainDays());
                statement.setLong(2, buffCard.getLastTimeUsed());
                statement.setInt(3, buffCard.getId());
                int rowEffected = statement.executeUpdate();
                Core.getInstance().debug(() -> String.format("buffcard[%d] update rowEffected: %d",
                        buffCard.getId(), rowEffected));
                return rowEffected >= 1;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }
}
