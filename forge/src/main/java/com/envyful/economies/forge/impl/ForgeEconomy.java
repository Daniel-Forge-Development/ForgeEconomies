package com.envyful.economies.forge.impl;

import com.envyful.economies.api.Economy;

public class ForgeEconomy implements Economy {

    private final String id;
    private final String displayname;
    private final String identifier;
    private final boolean prefix;
    private final double defaultValue;

    public ForgeEconomy(String id, String displayname, String identifier, boolean prefix, double defaultValue) {
        this.id = id;
        this.displayname = displayname;
        this.identifier = identifier;
        this.prefix = prefix;
        this.defaultValue = defaultValue;
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
}
