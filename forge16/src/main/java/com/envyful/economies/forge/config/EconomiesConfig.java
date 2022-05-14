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
import java.util.concurrent.TimeUnit;

@ConfigPath("config/EconomiesForge/config.yml")
@ConfigSerializable
public class EconomiesConfig extends AbstractYamlConfig {

    private SQLDatabaseDetails database = new SQLDatabaseDetails("EconomiesForge", "0.0.0.0", 3306,
            "admin", "password", "eco");

    private boolean balanceShowsAll = false;

    private Map<String, ConfigEconomy> economies = Maps.newHashMap(ImmutableMap.of(
            "one", new ConfigEconomy("one", "dollar", "dollars", "$",
                    true, true, 250.0, 1.0, "%.2f", 120)
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
        private String economyFormat = "%.2f";
        private long cacheDurationSeconds = 120;

        private transient Economy economy = null;

        public ConfigEconomy(String id, String displayName, String displayNamePlural, String identifier, boolean prefix,
                             boolean isDefault, double defaultValue, double minimumPayAmount, String economyFormat, long cacheDurationSeconds) {
            this.id = id;
            this.displayName = displayName;
            this.displayNamePlural = displayNamePlural;
            this.identifier = identifier;
            this.prefix = prefix;
            this.isDefault = isDefault;
            this.defaultValue = defaultValue;
            this.minimumPayAmount = minimumPayAmount;
            this.economyFormat = economyFormat;
            this.cacheDurationSeconds = cacheDurationSeconds;
        }

        public ConfigEconomy() {
        }

        public Economy getEconomy() {
            if (this.economy == null) {
                this.economy = new ForgeEconomy(this.id, this.displayName, this.displayNamePlural, this.identifier,
                        this.prefix, this.isDefault, this.defaultValue, minimumPayAmount,
                        this.economyFormat, TimeUnit.SECONDS.toMillis(this.cacheDurationSeconds)
                );
            }

            return this.economy;
        }

        public String getEconomyFormat() {
            return this.economyFormat;
        }
    }
}
