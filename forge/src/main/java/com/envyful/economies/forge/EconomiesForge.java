package com.envyful.economies.forge;

import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.player.ForgePlayerManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
        modid = "economies",
        name = "Economies Forge",
        version = EconomiesForge.VERSION,
        acceptableRemoteVersions = "*"
)
public class EconomiesForge {

    protected static final String VERSION = "0.3.0";

    private static EconomiesForge instance;

    private ForgePlayerManager playerManager = new ForgePlayerManager();
    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        instance = this;
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {

    }

    public static EconomiesForge getInstance() {
        return instance;
    }
}
