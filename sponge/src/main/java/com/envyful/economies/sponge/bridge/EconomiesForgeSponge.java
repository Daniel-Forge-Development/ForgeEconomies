package com.envyful.economies.sponge.bridge;

import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.config.EconomiesConfig;
import com.envyful.economies.sponge.bridge.registry.CurrencyRegistryModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
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
    public void onGameServerStarting(GamePreInitializationEvent event) {
        this.game.getRegistry().registerModule(Currency.class, new CurrencyRegistryModule());
    }

    @Listener
    public void onGameServerStarted(GameStartedServerEvent event) {
        for (String command : COMMANDS) {
            this.game.getCommandManager().get(command).ifPresent(commandMapping ->
                    this.game.getCommandManager().removeMapping(commandMapping));
        }
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
