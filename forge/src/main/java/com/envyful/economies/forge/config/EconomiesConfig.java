package com.envyful.economies.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.SQLDatabaseDetails;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.impl.ForgeEconomy;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Map;

@ConfigPath("config/EconomiesForge/config.yml")
@ConfigSerializable
public class EconomiesConfig extends AbstractYamlConfig {

    private SQLDatabaseDetails database = new SQLDatabaseDetails("EconomiesForge", "0.0.0.0", 3306,
            "admin", "password", "eco");

    private boolean balanceShowsAll = false;

    private Map<String, ConfigEconomy> economies = Maps.newHashMap(ImmutableMap.of(
            "one", new ConfigEconomy("one", "dollar", "dollars", "$",
                    true, true, 250.0, 1.0)
    ));

    public EconomiesConfig() {
        super();
    }

    public boolean isBalanceShowsAll() {
        return this.balanceShowsAll;
    }

    public SQLDatabaseDetails getDatabase() {
        return this.database;
    }

    public Map<String, ConfigEconomy> getEconomies() {
        return this.economies;
    }

    @ConfigSerializable
    public static class ConfigEconomy {

        private String id;
        private String displayName;
        private String displayNamePlural;
        private String identifier;
        private boolean prefix;
        private boolean isDefault;
        private double defaultValue;
        private double minimumPayAmount;

        private transient Economy economy = null;

        public ConfigEconomy(String id, String displayName, String displayNamePlural, String identifier, boolean prefix,
                             boolean isDefault, double defaultValue, double minimumPayAmount) {
            this.id = id;
            this.displayName = displayName;
            this.displayNamePlural = displayNamePlural;
            this.identifier = identifier;
            this.prefix = prefix;
            this.isDefault = isDefault;
            this.defaultValue = defaultValue;
            this.minimumPayAmount = minimumPayAmount;
        }

        public ConfigEconomy() {
        }

        public Economy getEconomy() {
            if (this.economy == null) {
                this.economy = new ForgeEconomy(this.id, this.displayName, this.displayNamePlural, this.identifier,
                        this.prefix, this.isDefault, this.defaultValue, minimumPayAmount);
            }

            return this.economy;
        }
    }
}
