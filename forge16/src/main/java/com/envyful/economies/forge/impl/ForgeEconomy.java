package com.envyful.economies.forge.impl;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.database.leaderboard.Order;
import com.envyful.api.database.leaderboard.SQLLeaderboard;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;

public class ForgeEconomy implements Economy {

    private final String id;
    private final String displayname;
    private final String displaynamePlural;
    private final String identifier;
    private final boolean prefix;
    private final boolean isDefault;
    private final double defaultValue;
    private final double minimumPayAmount;
    private final String economyFormat;
    private final long cacheDuration;

    private SQLLeaderboard leaderboard;

    public ForgeEconomy(String id, String displayname, String displaynamePlural, String identifier, boolean prefix, boolean isDefault, double defaultValue,
                        double minimumPayAmount, String economyFormat, long cacheDuration) {
        this.id = id;
        this.displayname = displayname;
        this.displaynamePlural = displaynamePlural;
        this.identifier = identifier;
        this.prefix = prefix;
        this.isDefault = isDefault;
        this.defaultValue = defaultValue;
        this.minimumPayAmount = minimumPayAmount;
        this.economyFormat = economyFormat;
        this.cacheDuration = cacheDuration;

        if (EconomiesForge.getInstance().getDatabase() == null) {
            UtilConcurrency.runLater(this::initLeaderboard, 40L);
        } else {
            this.initLeaderboard();
        }
    }

    private void initLeaderboard() {
        this.leaderboard = SQLLeaderboard.builder()
                .order(Order.DESCENDING)
                .pageSize(10)
                .cacheDuration(this.cacheDuration)
                .formatter((resultSet, pos) -> EconomiesForge.getInstance().getLocale().getBaltopFormat()
                        .replace("%pos%", (pos + 1) + "")
                        .replace("%name%", resultSet.getString("name"))
                        .replace("%balance%", String.format(this.economyFormat, (float) resultSet.getLong("balance")))
                )
                .table("forge_economies_banks")
                .column("balance")
                .extraClauses("economy = '" + this.getEconomyIdentifier() + "'")
                .database(EconomiesForge.getInstance().getDatabase())
                .build();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getDisplayName() {
        return this.displayname;
    }

    @Override
    public String getEconomyIdentifier() {
        return this.identifier;
    }

    @Override
    public String getFormat() {
        return this.economyFormat;
    }

    @Override
    public boolean isPrefix() {
        return this.prefix;
    }

    @Override
    public double getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public double getMinimumPayAmount() {
        return this.minimumPayAmount;
    }

    @Override
    public String getDisplayNamePlural() {
        return this.displaynamePlural;
    }

    @Override
    public boolean isDefault() {
        return this.isDefault;
    }

    @Override
    public SQLLeaderboard getLeaderboard() {
        return this.leaderboard;
    }
}
