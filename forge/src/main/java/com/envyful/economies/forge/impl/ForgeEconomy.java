package com.envyful.economies.forge.impl;

import com.envyful.economies.api.Economy;

public class ForgeEconomy implements Economy {

    private final String id;
    private final String displayname;
    private final String identifier;
    private final boolean prefix;

    public ForgeEconomy(String id, String displayname, String identifier, boolean prefix) {
        this.id = id;
        this.displayname = displayname;
        this.identifier = identifier;
        this.prefix = prefix;
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
}
