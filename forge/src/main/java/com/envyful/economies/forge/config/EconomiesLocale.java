package com.envyful.economies.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

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
    private String adminReload = "&a&l(!) &aReloaded config";

    private String economyDoesntExist = "&c&l(!) &cCannot find that economy";
    private String cannotPayYourself = "&c&l(!) &cYou cannot pay yourself!";
    private String minimumPayAmount = "&c&l(!) &cYou cannot pay less than %value%";
    private String pageMustBeGreaterThanZero = "&c&l(!) &cThe page number must be greater than 0";
    private String cannotSetLessThanZero = "&c&l(!) &cYou cannot set an amount less than 0";

    public EconomiesLocale() {
        super();
    }

    public String getPageMustBeGreaterThanZero() {
        return this.pageMustBeGreaterThanZero;
    }

    public String getMinimumPayAmount() {
        return this.minimumPayAmount;
    }

    public String getCannotPayYourself() {
        return this.cannotPayYourself;
    }

    public String getBalance() {
        return this.balance;
    }

    public String getEconomyDoesntExist() {
        return this.economyDoesntExist;
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

    public String getAdminReload() {
        return this.adminReload;
    }

    public String getCannotSetLessThanZero() {
        return this.cannotSetLessThanZero;
    }
}
