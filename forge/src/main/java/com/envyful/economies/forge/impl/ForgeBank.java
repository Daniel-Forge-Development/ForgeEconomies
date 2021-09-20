package com.envyful.economies.forge.impl;

import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;

import java.util.UUID;

public class ForgeBank implements Bank {

    private final UUID uuid;
    private final Economy economy;

    private double balance;

    public ForgeBank(UUID uuid, Economy economy, double balance) {
        this.uuid = uuid;
        this.economy = economy;
        this.balance = balance;
    }

    @Override
    public UUID getId() {
        return this.uuid;
    }

    @Override
    public Economy getEconomyId() {
        return this.economy;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public void setBalance(double balance) {
        double oldBalance = this.balance;
        this.balance = balance;
        ForgeEnvyPlayer player = EconomiesForge.getInstance().getPlayerManager().getPlayer(this.uuid);

        if (player == null) {
            return;
        }

        EconomiesForge.getInstance().getPlatformController().sendEconomyEvent(player, oldBalance, this.balance, this.balance);
    }

    @Override
    public void deposit(double value) {
        double oldBalance = this.balance;
        this.balance += value;

        ForgeEnvyPlayer player = EconomiesForge.getInstance().getPlayerManager().getPlayer(this.uuid);

        if (player == null) {
            return;
        }

        EconomiesForge.getInstance().getPlatformController().sendEconomyEvent(player, oldBalance, this.balance, value);
    }

    @Override
    public void withdraw(double value) {
        double oldBalance = this.balance;
        this.balance -= value;

        ForgeEnvyPlayer player = EconomiesForge.getInstance().getPlayerManager().getPlayer(this.uuid);

        if (player == null) {
            return;
        }

        EconomiesForge.getInstance().getPlatformController().sendEconomyEvent(player, oldBalance, this.balance, -value);
    }

    @Override
    public boolean hasFunds(double required) {
        return this.balance >= required;
    }
}
