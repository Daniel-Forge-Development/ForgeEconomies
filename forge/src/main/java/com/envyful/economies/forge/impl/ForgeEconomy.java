package com.envyful.economies.forge.impl;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.database.leaderboard.Order;
import com.envyful.api.database.leaderboard.SQLLeaderboard;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;

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

    private SQLLeaderboard leaderboard;

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
                .cacheDuration(TimeUnit.MINUTES.toMillis(30))
                .formatter((resultSet, pos) -> EconomiesForge.getInstance().getLocale().getBaltopFormat()
                        .replace("%pos%", (pos + 1) + "")
                        .replace("%name%", resultSet.getString("name"))
                        .replace("%balance%", String.format("%.2f", (float) resultSet.getLong("balance")))
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
