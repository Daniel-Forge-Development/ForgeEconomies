package com.envyful.economies.forge.impl;

import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;

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
        this.balance = balance;
    }

    @Override
    public void deposit(double value) {
        this.balance += value;
    }

    @Override
    public void withdraw(double value) {
        this.balance -= value;
    }

    @Override
    public boolean hasFunds(double required) {
        return this.balance >= required;
    }
}
