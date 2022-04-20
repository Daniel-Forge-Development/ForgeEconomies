package com.envyful.economies.sponge.bridge;

import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.sponge.bridge.platform.SpongePlatformController;
import com.envyful.economies.sponge.bridge.registry.CurrencyRegistryModule;
import com.envyful.economies.sponge.bridge.registry.ForgeEconomyService;
import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

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
            "economies",
            "baltop",
            "ecotop"
    };

    protected static final String VERSION = "1.7.5";

    @Inject private Game game;

    @Listener
    public void onGameServerStarting(GameInitializationEvent event) {
        this.game.getRegistry().registerModule(Currency.class, new CurrencyRegistryModule());
        this.game.getServiceManager().setProvider(this, EconomyService.class, new ForgeEconomyService());
        EconomiesForge.getInstance().setPlatformController(new SpongePlatformController());
    }

    @Listener
    public void onGameServerStarted(GameStartedServerEvent event) {
        for (String command : COMMANDS) {
            this.game.getCommandManager().get(command).ifPresent(commandMapping ->
                    this.game.getCommandManager().removeMapping(commandMapping));
        }
    }
}
