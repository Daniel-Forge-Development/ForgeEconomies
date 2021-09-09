package com.envyful.economies.sponge.bridge;

import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.config.EconomiesConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Plugin(
        id = "economiesbridge",
        name = "Economies Forge Bridge",
        version = EconomiesForgeSponge.VERSION,
        description = "Provides sign shops for sponge"
)
public class EconomiesForgeSponge {

    private static final String[] COMMANDS = new String[] {
            "pay",
            "balance",
            "bal",
            "eco",
            "economies"
    };

    protected static final String VERSION = "0.1.0";

    @Inject private Game game;

    @Listener
    public void onGameServerStarted(GameStartedServerEvent event) {
        for (String command : COMMANDS) {
            this.game.getCommandManager().get(command).ifPresent(commandMapping ->
                    this.game.getCommandManager().removeMapping(commandMapping));
        }

        this.game.getRegistry().registerModule(Currency.class, new CatalogRegistryModule<Currency>() {

            private final Map<String, Currency> currencies = Maps.newHashMap();

            @Override
            public Optional<Currency> getById(String id) {
                EconomiesConfig.ConfigEconomy config = EconomiesForge.getInstance().getConfig().getEconomies().get(id);

                if (config == null) {
                    return Optional.empty();
                }

                return Optional.of(currencies.computeIfAbsent(id, ___ -> new CurrencyForge(config.getEconomy())));
            }

            @Override
            public Collection<Currency> getAll() {
                for (Map.Entry<String, EconomiesConfig.ConfigEconomy> entry : EconomiesForge.getInstance()
                        .getConfig().getEconomies().entrySet()) {
                    if (currencies.containsKey(entry.getKey())) {
                        continue;
                    }

                    this.currencies.put(entry.getKey(), new CurrencyForge(entry.getValue().getEconomy()));
                }

                return Lists.newArrayList(this.currencies.values());
            }
        });
    }

    public static final class CurrencyForge implements Currency {

        private final Economy economy;

        public CurrencyForge(Economy economy) {
            this.economy = economy;
        }

        @Override
        public Text getDisplayName() {
            return Text.of(this.economy.getDisplayName());
        }

        @Override
        public Text getPluralDisplayName() {
            return Text.of(this.economy.getDisplayName());
        }

        @Override
        public Text getSymbol() {
            return Text.of(this.economy.getEconomyIdentifier());
        }

        @Override
        public Text format(BigDecimal amount, int numFractionDigits) {
            return null;
        }

        @Override
        public int getDefaultFractionDigits() {
            return (int) this.economy.getDefaultValue();
        }

        @Override
        public boolean isDefault() {
            return false;
        }

        @Override
        public String getId() {
            return this.economy.getId();
        }

        @Override
        public String getName() {
            return this.economy.getId();
        }
    }
}
