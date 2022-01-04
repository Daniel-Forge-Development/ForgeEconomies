package com.envyful.economies.forge.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

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
    private String adminInsufficientFunds = "&c&l(!) &cThe target has insufficient funds!";

    private String economyDoesntExist = "&c&l(!) &cCannot find that economy";
    private String cannotPayYourself = "&c&l(!) &cYou cannot pay yourself!";
    private String minimumPayAmount = "&c&l(!) &cYou cannot pay less than %value%";
    private String pageMustBeGreaterThanZero = "&c&l(!) &cThe page number must be greater than 0";
    private String cannotSetLessThanZero = "&c&l(!) &cYou cannot set an amount less than 0";
    private String cannotTakeLessThanZero = "&c&l(!) &cYou cannot take an amount less than 0";
    private String cannotGiveLessThanZero = "&c&l(!) &cYou cannot give an amount less than 0";
    private String playerNotOnline = "&c&l(!) &cCannot find that player as they are not online";
    private String playerNotFound = "&c&l(!) &cCannot find that player";

    private String baltopFormat = "&e%pos%. &b%name% $%balance%";
    private String balanceFormat = "%.2f";
    private String targetBalance = "&e&l(!) &e%target% has $%balance%";

    private List<String> allBalanceFormat = Lists.newArrayList(
            "&8&m-------- &e%player%'s balance &8&m--------",
            "&eDollar: %player_balance_one%",
            "&8&m--------------------"
    );

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

    public String getAdminInsufficientFunds() {
        return this.adminInsufficientFunds;
    }

    public String getCannotTakeLessThanZero() {
        return this.cannotTakeLessThanZero;
    }

    public String getCannotGiveLessThanZero() {
        return this.cannotGiveLessThanZero;
    }

    public String getBalanceFormat() {
        return this.balanceFormat;
    }

    public String getBaltopFormat() {
        return this.baltopFormat;
    }

    public String getPlayerNotOnline() {
        return this.playerNotOnline;
    }

    public String getTargetBalance() {
        return this.targetBalance;
    }

    public String getPlayerNotFound() {
        return this.playerNotFound;
    }

    public List<String> getAllBalanceFormat() {
        return this.allBalanceFormat;
    }
}
