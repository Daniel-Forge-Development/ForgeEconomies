package com.envyful.economies.sponge.bridge.registry;

import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.config.EconomiesConfig;
import com.envyful.economies.sponge.bridge.EconomiesForgeSponge;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.service.economy.Currency;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class CurrencyRegistryModule implements CatalogRegistryModule<Currency> {
    
    private static final Map<String, Currency> CURRENCIES = Maps.newHashMap();

    @Override
    public Optional<Currency> getById(String id) {
        EconomiesConfig.ConfigEconomy config = EconomiesForge.getInstance().getConfig().getEconomies().get(id);

        if (config == null) {
            return Optional.empty();
        }

        return Optional.of(CURRENCIES.computeIfAbsent(id, ___ -> new EconomiesForgeSponge.CurrencyForge(config.getEconomy())));
    }

    @Override
    public Collection<Currency> getAll() {
        for (Map.Entry<String, EconomiesConfig.ConfigEconomy> entry : EconomiesForge.getInstance()
                .getConfig().getEconomies().entrySet()) {
            if (CURRENCIES.containsKey(entry.getKey())) {
                continue;
            }

            CURRENCIES.put(entry.getKey(), new EconomiesForgeSponge.CurrencyForge(entry.getValue().getEconomy()));
        }

        return Lists.newArrayList(CURRENCIES.values());
    }
}
