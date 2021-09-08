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

@ConfigPath("config/EconomiesForge/locale.yml")
@ConfigSerializable
public class EconomiesLocale extends AbstractYamlConfig {

    private String balance = "&a&l(!) &a%value%";
    private String givenMoney = "&a&l(!) &a+%value%";
    private String takenMoney = "&c&l(!) &c-%value%";
    private String insufficientFunds = "&c&l(!) &cYou don't have enough %economy_name%.";

    private String adminGivenMoney = "&a&l(!) &a%player% was given %value%";
    private String adminTakenMoney = "&a&l(!) &aYou have taken %value% from %player%";
    private String adminSetMoney = "&a&l(!) &aYou have set %player%'s balance to %value%";
    private String adminResetMoney = "&a&l(!) &aYou have reset %player%'s balance to %value%";

    public EconomiesLocale() {
        super();
    }

    public String getBalance() {
        return this.balance;
    }

    public String getInsufficientFunds() {
        return this.insufficientFunds;
    }

    public String getGivenMoney() {
        return this.givenMoney;
    }

    public String getTakenMoney() {
        return this.takenMoney;
    }

    public String getAdminGivenMoney() {
        return this.adminGivenMoney;
    }

    public String getAdminTakenMoney() {
        return this.adminTakenMoney;
    }

    public String getAdminSetMoney() {
        return this.adminSetMoney;
    }

    public String getAdminResetMoney() {
        return this.adminResetMoney;
    }
}
