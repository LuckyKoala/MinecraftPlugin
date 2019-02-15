package tech.zuosi.minecraft.parkour.database;

import tech.zuosi.minecraft.parkour.Core;

/**
 * Created by luckykoala on 18-4-3.
 */
class Tables {
    private final String prefix = Core.getInstance().getConfig()
            .getConfigurationSection("Database").getString("prefix", "");
    final String playerTableName = prefix+"player";

    final String playerTableSql = "CREATE TABLE IF NOT EXISTS `" + playerTableName + "` (" +
            "  `username` varchar(40) NOT NULL," +
            "  `mapPath` varchar(80) NOT NULL," +
            "  `bestTime` bigint(20) DEFAULT NULL," +
            "  PRIMARY KEY (`username`, `mapPath`)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
}
