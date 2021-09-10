package com.envyful.economies.sponge.bridge.registry.account.event;

import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.economy.EconomyTransactionEvent;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

public class ForgeTransactionEvent extends AbstractEvent implements EconomyTransactionEvent {

    private final Account account;
    private final TransactionResult result;

    public ForgeTransactionEvent(Account account, TransactionResult result) {
        this.account = account;
        this.result = result;
    }

    @Override
    public TransactionResult getTransactionResult() {
        return this.result;
    }

    @Override
    public Cause getCause() {
        return null;
    }

    @Override
    public Object getSource() {
        return this.account;
    }
}
