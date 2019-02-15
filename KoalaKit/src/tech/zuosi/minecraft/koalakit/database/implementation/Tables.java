package tech.zuosi.minecraft.koalakit.database.implementation;

import tech.zuosi.minecraft.koalakit.Core;

public class Tables {
    private final String prefix = Core.getInstance().getConfig()
            .getConfigurationSection("Database").getString("prefix", "");
    private String userTableName = prefix+"record";

    String userTableSql = "CREATE TABLE IF NOT EXISTS `" + userTableName + "` (" +
            "  `username` varchar(40) NOT NULL," +
            "  `kitname` varchar(40) NOT NULL," +
            "  `count` smallint(6) DEFAULT 0," +
            "  `timestamp` int(11)," +
            "  PRIMARY KEY (`username`, `kitname`)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    String initRecordSql = "INSERT IGNORE INTO " + userTableName + "(username,kitname,count,timestamp) " +
            "VALUE (?,?,0,0);";

    String compareAndIncreseSql = "UPDATE " + userTableName + " SET timestamp=UNIX_TIMESTAMP(), count=count+1 " +
            "where username=? and kitname=? and UNIX_TIMESTAMP()-timestamp>? and count<?;";
}

/*//不可维护代码 -。-ll
    //?代表的分别是username, kitname, period, limit, period, period, limit
    String compareAndSetSql = "INSERT INTO " + userTableName + "(username,kitname,count,timestamp) " +
            "VALUE (?,?,1,UNIX_TIMESTAMP()) " +
            "ON DUPLICATE KEY UPDATE " +
            "count=(case when (UNIX_TIMESTAMP()-timestamp>?) then LEAST(count+1,?) else count end)," +
            "timestamp=(case when (UNIX_TIMESTAMP()-timestamp>?) " +
            "then (case when (?=0) then timestamp else (case when (LEAST(count+1,?)-count=0) then timestamp else UNIX_TIMESTAMP() end) end) " +
            "else timestamp end);";*/
