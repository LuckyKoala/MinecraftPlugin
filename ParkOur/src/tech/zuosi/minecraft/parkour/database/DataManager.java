package tech.zuosi.minecraft.parkour.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.NoArgsConstructor;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.MapPath;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LuckyKoala on 18-9-16.
 */
@NoArgsConstructor
public class DataManager {
    private final Tables tables = new Tables();
    private ComboPooledDataSource comboPooledDataSource;
    private final Map<String, PlayerData> playerCache = new HashMap<>();
    private final Map<MapPath, String> allBestNameCache = new HashMap<>();
    public static final String systemBestTimeName = Core.getInstance().getConfig().getString("systemBestTimeName");

    public boolean init(String username, String password, String host, String database, int port, boolean setup) {
        comboPooledDataSource = new ComboPooledDataSource();
        try {
            comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
            return false;
        }
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://"+host+":"+port+"/"+database
                +"?useUnicode=true&characterEncoding=UTF8");
        comboPooledDataSource.setUser(username);
        comboPooledDataSource.setPassword(password);

        setupTable(setup);
        return true;
    }

    private void setupTable(boolean setup) {
        if(setup) {
            //已经初始化过
        } else {
            //初始化表
            try(
                    Connection connection = getConnection();
                    PreparedStatement createPlayer = connection.prepareStatement(tables.playerTableSql)
            ) {
                boolean success = createPlayer.executeUpdate() > 0;
                if(success) {
                    Core.getInstance().getConfig().set("Database.setup", true);
                    Core.getInstance().saveConfig();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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

    public void close() {
        comboPooledDataSource.close();
    }

    public PlayerData loadBestData() {
        return loadPlayerData(systemBestTimeName);
    }

    void updateBestTime(String username, MapPath mapPath, long bestTime, long oldBestTime) {
        if(bestTime == 0L) return;
        boolean success;

        if(0L == oldBestTime) {
            success = insertRecord(username, mapPath.toString(), bestTime);
        } else {
            success = updateRecord(username, mapPath.toString(), bestTime);
        }

        if (success) {
            if(systemBestTimeName.equals(username)) {
                //Update Cache
                allBestNameCache.put(mapPath, username);
            }
        } else {
            Core.getInstance().getLogger()
                    .warning(String.format("Updating best time of %s with %d -> %d in %s failed",
                            username, oldBestTime, bestTime, mapPath.toString()));
        }
    }

    public PlayerData loadPlayerData(String username) {
        return playerCache.computeIfAbsent(username, name -> {
            //缓存里没有，先去查数据库，再就是初始化用户
            //Core.getInstance().debug(() -> "DataManager.loadPlayer.初始化用户"+name);
            PlayerData data = loadPlayerDataFromDatabase(name);
            return data == null ? new PlayerData(new HashMap<>()) : data;
        });
    }

    public String loadAllBestName(MapPath mapPath) {
        return allBestNameCache.computeIfAbsent(mapPath, path -> {
            //缓存里没有，先去查数据库，再就是初始化用户
            //Core.getInstance().debug(() -> "DataManager.loadPlayer.初始化用户"+name);
            return loadAllBestNameFromDatabase(path);
        });
    }

    private PreparedStatement createWithUsernameFilled(Connection connection,String sql, String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        return preparedStatement;
    }

    private PlayerData loadPlayerDataFromDatabase(String username) {
        PlayerData data = null;
        //数据库
        try(
                Connection connection = getConnection();
                PreparedStatement queryData = createWithUsernameFilled(connection,
                        "SELECT mapPath, bestTime " +
                                "FROM " + tables.playerTableName + " WHERE username=?", username
                );
                ResultSet queryDataResultSet = queryData.executeQuery()
        ) {
            Map<String, Long> bestTimeRecord = new HashMap<>();
            while(queryDataResultSet.next()) {
                bestTimeRecord.put(queryDataResultSet.getString(1),
                        queryDataResultSet.getLong(2));
            }
            data = new PlayerData(bestTimeRecord);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    private boolean insertBestRecord(String mapPath, long bestTime) {
        return insertRecord(systemBestTimeName, mapPath, bestTime);
    }

    private boolean updateBestRecord(String mapPath, long bestTime) {
        return updateRecord(systemBestTimeName, mapPath, bestTime);
    }

    private boolean insertRecord(String username, String mapPath, long bestTime) {
        try(
                Connection connection = getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO " + tables.playerTableName
                        + "(username, mapPath, bestTime) VALUE (?,?,?)")
                ) {
            insertStatement.setString(1, username);
            insertStatement.setString(2, mapPath);
            insertStatement.setLong(3, bestTime);

            return insertStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updateRecord(String username, String mapPath, long bestTime) {
        try(
                Connection connection = getConnection();
                PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE " + tables.playerTableName
                        + " SET bestTime=? WHERE username=? and mapPath=?")
        ) {
            updateStatement.setLong(1, bestTime);
            updateStatement.setString(2, username);
            updateStatement.setString(3, mapPath);

            return updateStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String loadAllBestNameFromDatabase(MapPath path) {
        String name = null;
        //数据库
        try(
                Connection connection = getConnection();
                PreparedStatement queryStatement = connection.prepareStatement(
                        "select username from po_player where mapPath=? " +
                                "and bestTime in " +
                                "(select bestTime from po_player where mapPath=? and username=?) and username<>?")
        ) {
            String pathStr = path.toString();
            queryStatement.setString(1, pathStr);
            queryStatement.setString(2, pathStr);
            queryStatement.setString(3, systemBestTimeName);
            queryStatement.setString(4, systemBestTimeName);
            ResultSet resultSet = queryStatement.executeQuery();
            while(resultSet.next()) {
                name = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }
}
