package com.envyful.economies.sponge.bridge.registry;

import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.config.EconomiesConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ForgeEconomyService implements EconomyService {

    private static ForgeEconomyService instance;

    private Currency defaultCurrency = null;

    public ForgeEconomyService() {
        instance = this;
    }

    public static ForgeEconomyService getInstance() {
        return instance;
    }

    @Override
    public Currency getDefaultCurrency() {
        if (this.defaultCurrency == null) {
            for (Map.Entry<String, EconomiesConfig.ConfigEconomy> entry : EconomiesForge.getInstance()
                    .getConfig().getEconomies().entrySet()) {
                if (entry.getValue().getEconomy().isDefault()) {
                    this.defaultCurrency = new ForgeCurrency(entry.getValue().getEconomy());
                    return this.defaultCurrency;
                }
            }
        }

        return this.defaultCurrency;
    }

    @Override
    public Set<Currency> getCurrencies() {
        for (Map.Entry<String, EconomiesConfig.ConfigEconomy> entry : EconomiesForge.getInstance()
                .getConfig().getEconomies().entrySet()) {
            if (CurrencyRegistryModule.CURRENCIES.containsKey(entry.getKey())) {
                continue;
            }

            CurrencyRegistryModule.CURRENCIES.put(entry.getKey(), new ForgeCurrency(entry.getValue().getEconomy()));
        }

        return Sets.newHashSet(CurrencyRegistryModule.CURRENCIES.values());
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return true;
    }

    @Override
    public boolean hasAccount(String identifier) {
        return true;
    }

    @Override
    public Optional<UniqueAccount> getOrCreateAccount(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> getOrCreateAccount(String identifier) {
        return Optional.empty();
    }

    @Override
    public void registerContextCalculator(ContextCalculator<Account> calculator) {}
}
