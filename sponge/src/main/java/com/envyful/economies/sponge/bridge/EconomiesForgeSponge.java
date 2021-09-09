package com.envyful.economies.sponge.bridge;

import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.config.EconomiesConfig;
import com.envyful.economies.sponge.bridge.registry.CurrencyRegistryModule;
import com.envyful.economies.sponge.bridge.registry.ForgeEconomyService;
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
import org.spongepowered.api.service.economy.EconomyService;
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
        this.game.getServiceManager().setProvider(this, EconomyService.class, new ForgeEconomyService());
    }

    @Listener
    public void onGameServerStarted(GameStartedServerEvent event) {
        for (String command : COMMANDS) {
            this.game.getCommandManager().get(command).ifPresent(commandMapping ->
                    this.game.getCommandManager().removeMapping(commandMapping));
        }
    }
}
