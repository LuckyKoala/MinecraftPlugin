package tech.zuosi.minecraft.koalavip.database.implementation;

import tech.zuosi.minecraft.koalavip.Core;

/**
 * Created by luckykoala on 18-4-3.
 */
public class Tables {
    private final String prefix = Core.getInstance().getConfig()
            .getConfigurationSection("Database").getString("prefix", "");
    String userTableName = prefix+"user";
    String buffcardTableName = prefix+"buffcard";
    String onetimeTableName = prefix+"onetime";
    String commandTableName = prefix+"command";

    String userTableSql = "CREATE TABLE IF NOT EXISTS `" + userTableName + "` (" +
            "  `username` varchar(40) NOT NULL," +
            "  `lastTimePurchasing` bigint(20) DEFAULT NULL," +
            "  `moneySpentTotal` int(11) DEFAULT NULL," +
            "  `moneySpentLocked` int(11) DEFAULT NULL," +
            "  `downgradeTimes` smallint(6) DEFAULT NULL," +
            "  PRIMARY KEY (`username`)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    String buffcardTableSql = "CREATE TABLE IF NOT EXISTS `" + buffcardTableName + "` (" +
            "  `cid` int(11) NOT NULL AUTO_INCREMENT," +
            "  `templatename` varchar(40) DEFAULT NULL," +
            "  `remainDays` mediumint(9) DEFAULT NULL," +
            "  `lastTimeUsed` bigint(20) DEFAULT NULL," +
            "  `owner` varchar(40) DEFAULT NULL," +
            "  PRIMARY KEY (`cid`)," +
            "  KEY `fk_buffcard_owner` (`owner`)," +
            "  CONSTRAINT `fk_buffcard_owner` FOREIGN KEY (`owner`) REFERENCES `"+ prefix +"user` (`username`) ON DELETE CASCADE" +
            ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;";

    String onetimeTableSql = "CREATE TABLE IF NOT EXISTS `" + onetimeTableName + "` (" +
            "  `oid` int(11) NOT NULL AUTO_INCREMENT," +
            "  `cmd` varchar(30) DEFAULT NULL," +
            "  `owner` varchar(40) DEFAULT NULL," +
            "  PRIMARY KEY (`oid`)," +
            "  KEY `fk_onetime_user` (`owner`)," +
            "  CONSTRAINT `fk_onetime_user` FOREIGN KEY (`owner`) REFERENCES `"+ prefix +"user` (`username`) ON DELETE CASCADE" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    String commandTableSql = "CREATE TABLE IF NOT EXISTS `" + commandTableName + "` (" +
            "  `cmdid` int(11) NOT NULL AUTO_INCREMENT," +
            "  `templatename` varchar(40) DEFAULT NULL," +
            "  `lastTimeInvoked` bigint(20) DEFAULT NULL," +
            "  `owner` varchar(40) DEFAULT NULL," +
            "  PRIMARY KEY (`cmdid`)," +
            "  KEY `fk_command_owner` (`owner`)," +
            "  CONSTRAINT `fk_command_owner` FOREIGN KEY (`owner`) REFERENCES `"+ prefix +"user` (`username`) ON DELETE CASCADE" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
}
