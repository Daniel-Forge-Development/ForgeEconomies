package com.envyful.economies.forge.player;

import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.attribute.AbstractForgeAttribute;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.config.EconomiesQueries;
import com.envyful.economies.forge.impl.ForgeBank;
import com.google.common.collect.Maps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class EconomiesAttribute extends AbstractForgeAttribute<EconomiesForge> {

    private Map<String, Bank> bankAccounts = Maps.newHashMap();

    public EconomiesAttribute(EconomiesForge manager, EnvyPlayer<?> parent) {
        super(manager, (ForgeEnvyPlayer) parent);
    }

    public Bank getAccount(Economy economy) {
        return this.bankAccounts.get(economy.getId());
    }

    @Override
    public void load() {
        try (Connection connection = this.manager.getDatabase().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EconomiesQueries.LOAD_USER)) {
            preparedStatement.setString(1, this.parent.getUuid().toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Economy economy = EconomiesForge.getInstance().getConfig().getEconomies()
                        .get(resultSet.getString("economy")).getEconomy();

                this.bankAccounts.put(economy.getId(), new ForgeBank(
                        this.parent.getUuid(),
                        economy,
                        resultSet.getDouble("balance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {

    }
}
