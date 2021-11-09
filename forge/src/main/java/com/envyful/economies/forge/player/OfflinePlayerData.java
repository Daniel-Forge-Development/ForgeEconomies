package com.envyful.economies.forge.player;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.config.EconomiesConfig;
import com.envyful.economies.forge.config.EconomiesQueries;
import com.envyful.economies.forge.impl.ForgeBank;
import com.envyful.economies.forge.player.exception.PlayerNotFoundException;
import com.google.common.collect.Maps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OfflinePlayerData {

    private UUID uuid;
    private String name;
    private long lastUpdate = System.currentTimeMillis();
    private Map<Economy, Bank> balances = Maps.newConcurrentMap();

    public OfflinePlayerData(String name, Economy economy) throws PlayerNotFoundException {
        try (Connection connection = EconomiesForge.getInstance().getDatabase().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EconomiesQueries.LOAD_BY_NAME)) {
            preparedStatement.setString(1, name.toLowerCase());
            preparedStatement.setString(2, economy.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new PlayerNotFoundException();
            }

            this.uuid = UUID.fromString(resultSet.getString("uuid"));
            this.balances.put(economy, new ForgeBank(
                    this.uuid,
                    economy,
                    resultSet.getDouble("balance")
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        UtilConcurrency.runAsync(this::load);
    }

    public OfflinePlayerData(UUID uuid, Economy economy) {
        this.uuid = uuid;

        try (Connection connection = EconomiesForge.getInstance().getDatabase().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EconomiesQueries.LOAD_SPECIFIC)) {
            preparedStatement.setString(1, this.uuid.toString());
            preparedStatement.setString(2, economy.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                this.name = resultSet.getString("name");
                this.balances.put(economy, new ForgeBank(
                        this.uuid,
                        economy,
                        resultSet.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        UtilConcurrency.runAsync(this::load);
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public void updateLastAccess() {
        this.lastUpdate = System.currentTimeMillis();
    }

    public boolean hasTimedOut() {
        return (System.currentTimeMillis() - this.lastUpdate) >= TimeUnit.MINUTES.toMillis(30);
    }

    public Bank getBalance(Economy economy) {
        this.updateLastAccess();
        return this.balances.computeIfAbsent(economy, ___ -> new ForgeBank(this.uuid, economy, economy.getDefaultValue()));
    }

    private void load() {
        try (Connection connection = EconomiesForge.getInstance().getDatabase().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EconomiesQueries.LOAD_USER)) {
            preparedStatement.setString(1, this.uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                EconomiesConfig.ConfigEconomy ecoConfig = EconomiesForge.getInstance().getConfig().getEconomies()
                        .get(resultSet.getString("economy"));

                if (ecoConfig == null) {
                    System.out.println("DOESN'T EXIST: "+ resultSet.getString("economy"));
                    continue;
                }

                Economy economy = ecoConfig.getEconomy();

                if (this.balances.containsKey(economy)) {
                    continue;
                }

                this.balances.put(economy, new ForgeBank(
                        this.uuid,
                        economy,
                        resultSet.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (EconomiesConfig.ConfigEconomy eco : EconomiesForge.getInstance().getConfig().getEconomies().values()) {
            if (this.balances.containsKey(eco.getEconomy())) {
                continue;
            }

            this.balances.put(eco.getEconomy(), new ForgeBank(this.uuid, eco.getEconomy(), eco.getEconomy().getDefaultValue()));
        }
    }

    public void save() {
        UtilConcurrency.runAsync(() -> {
            try (Connection connection = EconomiesForge.getInstance().getDatabase().getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(EconomiesQueries.CREATE_OR_UPDATE_ACCOUNT)) {

                for (Bank value : this.balances.values()) {
                    preparedStatement.setString(1, this.uuid.toString());
                    preparedStatement.setString(2, this.name);
                    preparedStatement.setString(3, value.getEconomyId().getEconomyIdentifier());
                    preparedStatement.setDouble(4, value.getBalance());
                    preparedStatement.addBatch();
                }

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
