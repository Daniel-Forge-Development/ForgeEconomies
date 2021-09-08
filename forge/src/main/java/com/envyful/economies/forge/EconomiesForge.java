package com.envyful.economies.forge;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.database.Database;
import com.envyful.api.database.impl.SimpleHikariDatabase;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.command.BalanceCommand;
import com.envyful.economies.forge.command.EconomiesCommand;
import com.envyful.economies.forge.command.PayCommand;
import com.envyful.economies.forge.config.EconomiesConfig;
import com.envyful.economies.forge.config.EconomiesLocale;
import com.envyful.economies.forge.config.EconomiesQueries;
import com.envyful.economies.forge.player.EconomiesAttribute;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Mod(
        modid = "economies",
        name = "Economies Forge",
        version = EconomiesForge.VERSION,
        acceptableRemoteVersions = "*"
)
public class EconomiesForge {

    protected static final String VERSION = "0.1.0";

    private static EconomiesForge instance;

    private ForgePlayerManager playerManager = new ForgePlayerManager();
    private ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    private EconomiesConfig config;
    private EconomiesLocale locale;
    private Database database;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        instance = this;

        this.loadConfig();

        this.playerManager.registerAttribute(this, EconomiesAttribute.class);

        this.commandFactory.registerInjector(Economy.class, (sender, args) -> {
            EconomiesConfig.ConfigEconomy config = this.config.getEconomies().get(args[0]);

            if (config == null) {
                sender.sendMessage(new TextComponentString(
                        UtilChatColour.translateColourCodes('&', this.locale.getEconomyDoesntExist())));
                return null;
            }

            return config.getEconomy();
        });

        UtilConcurrency.runAsync(() -> {
            this.database = new SimpleHikariDatabase(this.config.getDatabase());

            try (Connection connection = this.database.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(EconomiesQueries.CREATE_TABLE)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(EconomiesConfig.class);
            this.locale = YamlConfigFactory.getInstance(EconomiesLocale.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        this.commandFactory.registerCommand(event.getServer(), new EconomiesCommand());
        this.commandFactory.registerCommand(event.getServer(), new PayCommand());
        this.commandFactory.registerCommand(event.getServer(), new BalanceCommand());
    }

    public static EconomiesForge getInstance() {
        return instance;
    }

    public EconomiesConfig getConfig() {
        return this.config;
    }

    public Database getDatabase() {
        return this.database;
    }

    public ForgePlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public EconomiesLocale getLocale() {
        return this.locale;
    }
}
