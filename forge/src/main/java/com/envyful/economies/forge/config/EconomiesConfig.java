package com.envyful.economies.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.type.SQLDatabaseDetails;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigPath("config/EconomiesForge/config.yml")
@ConfigSerializable
public class EconomiesConfig extends AbstractYamlConfig {

    private SQLDatabaseDetails database = new SQLDatabaseDetails("EconomiesForge", "0.0.0.0", 3306,
            "admin", "password", "eco");

    public EconomiesConfig() {
        super();
    }

    public SQLDatabaseDetails getDatabase() {
        return this.database;
    }
}
