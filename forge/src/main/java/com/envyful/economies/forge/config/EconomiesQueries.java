package com.envyful.economies.forge.config;

public class EconomiesQueries {

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `forge_economies_banks`(" +
            "id         INT             UNSIGNED    NOT NULL    AUTO_INCREMENT, " +
            "uuid       VARCHAR(64)     NOT NULL, " +
            "economy    VARCHAR(200)    NOT NULL, " +
            "balance    FLOAT     NOT NULL, " +
            "UNIQUE(uuid, economy), " +
            "PRIMARY KEY(id));";

    public static final String LOAD_SPECIFIC = "SELECT balance FROM `forge_economies_banks` WHERE uuid = ? AND economy = ?;";

    public static final String LOAD_USER = "SELECT economy, balance FROM `forge_economies_banks` WHERE uuid = ?;";

    public static final String CREATE_OR_UPDATE_ACCOUNT = "INSERT INTO `forge_economies_banks`(uuid, economy, balance) " +
            "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE balance = VALUES(`balance`);";

}
