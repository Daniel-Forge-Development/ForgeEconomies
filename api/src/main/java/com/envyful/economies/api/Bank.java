package com.envyful.economies.api;

import java.util.UUID;

/**
 *
 * Represents a store of value for the given {@link Economy}
 *
 */
public interface Bank {

    /**
     *
     * The {@link UUID} of the bank
     * Will be player's {@link UUID} if it's a player's "bank"
     *
     * @return The uuid of the bank
     */
    UUID getId();

    /**
     *
     * Gets the {@link Economy} that this {@link Bank} is in relation to
     *
     * @return The economy
     */
    Economy getEconomyId();

    /**
     *
     * Gets the balance of the account
     *
     * @return The balance
     */
    double getBalance();

    /**
     *
     * Sets the balance of the account
     *
     * @param balance The new balance
     */
    void setBalance(double balance);

    /**
     *
     * Adds money to the account
     *
     * @param value The value to add to the account
     */
    void deposit(double value);

    /**
     *
     * Takes money from the account
     *
     * @param value The value to be taken from the account
     */
    void withdraw(double value);

    /**
     *
     * If the account has more than the required amount
     *
     * @param required The amount being checked
     * @return True if the account has balance >= param
     */
    boolean hasFunds(double required);

}
