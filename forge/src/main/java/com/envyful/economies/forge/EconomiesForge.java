package com.envyful.economies.forge;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.database.Database;
import com.envyful.api.database.impl.SimpleHikariDatabase;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.api.forge.player.ForgePlayerManager;
import com.envyful.economies.forge.config.EconomiesConfig;
import com.envyful.economies.forge.config.EconomiesQueries;
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
    private Database database;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        instance = this;

        this.loadConfig();

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

    private void loadConfig() {
        try {
            this.config = YamlConfigFactory.getInstance(EconomiesConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {

    }

    public static EconomiesForge getInstance() {
        return instance;
    }
}
