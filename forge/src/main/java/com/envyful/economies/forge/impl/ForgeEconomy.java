package com.envyful.economies.forge.impl;

import com.envyful.api.database.leaderboard.Order;
import com.envyful.api.database.leaderboard.SQLLeaderboard;
import com.envyful.economies.api.Economy;

import java.util.concurrent.TimeUnit;

public class ForgeEconomy implements Economy {

    private final String id;
    private final String displayname;
    private final String displaynamePlural;
    private final String identifier;
    private final boolean prefix;
    private final boolean isDefault;
    private final double defaultValue;
    private final double minimumPayAmount;
    private final SQLLeaderboard leaderboard;

    public ForgeEconomy(String id, String displayname, String displaynamePlural, String identifier, boolean prefix, boolean isDefault, double defaultValue,
                        double minimumPayAmount) {
        this.id = id;
        this.displayname = displayname;
        this.displaynamePlural = displaynamePlural;
        this.identifier = identifier;
        this.prefix = prefix;
        this.isDefault = isDefault;
        this.defaultValue = defaultValue;
        this.minimumPayAmount = minimumPayAmount;
        this.leaderboard = SQLLeaderboard.builder()
                .order(Order.ASCENDING)
                .pageSize(10)
                .cacheDuration(TimeUnit.MINUTES.toMillis(30))
                .formatter(resultSet -> "") //TODO
                .table("forge_economies_banks")
                .column("balance")
                .extraClauses("economy = '" + this.id + "'")
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
