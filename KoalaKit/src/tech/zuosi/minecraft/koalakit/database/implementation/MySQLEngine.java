package tech.zuosi.minecraft.koalakit.database.implementation;

import tech.zuosi.minecraft.koalakit.Core;
import tech.zuosi.minecraft.koalakit.database.DatabaseEngine;

import java.sql.*;
import java.util.concurrent.TimeUnit;

public class MySQLEngine implements DatabaseEngine {
    private Tables tables = new Tables();
    private String url;

    @Override
    public boolean init(String host, String username, String password, String database, int port) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        this.url = "jdbc:mysql://"+host+":"+port+"/"+database
                +"?user="+username+"&password="+password+"&useUnicode=true&characterEncoding=UTF8";

        setupTable();
        return true;
    }

    private void setupTable() {
        boolean setup = Core.getInstance().getConfig().getBoolean("Database.setup");
        if (!setup) {
            //初始化表
            try(
                    Connection connection = getConnection();
                    PreparedStatement createUser = connection.prepareStatement(tables.userTableSql);
                    ) {
                createUser.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            Core.getInstance().getConfig().set("setup", true);
        }
    }

    private Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    @Override
    public boolean compareAndInc(String username, String kitname, int limit, int period) {
        if(limit == 0) return false;
        long periodInMills = TimeUnit.HOURS.toSeconds(period);
        try(
                Connection connection = getConnection();
                PreparedStatement initStatement = connection.prepareStatement(tables.initRecordSql);
                PreparedStatement updateStatement = connection.prepareStatement(tables.compareAndIncreseSql);
        ) {
            initStatement.setString(1, username);
            initStatement.setString(2, kitname);
            initStatement.executeUpdate();
            updateStatement.setString(1, username);
            updateStatement.setString(2, kitname);
            updateStatement.setLong(3, periodInMills);
            updateStatement.setInt(4, limit);
            //System.out.println(updateStatement.toString());
            return updateStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
