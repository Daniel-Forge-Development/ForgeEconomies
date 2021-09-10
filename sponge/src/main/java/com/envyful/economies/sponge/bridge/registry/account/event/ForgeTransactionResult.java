package com.envyful.economies.sponge.bridge.registry.account.event;

import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

public class ForgeTransactionResult implements TransactionResult {

    private final Account account;
    private final Currency currency;
    private final ResultType resultType;
    private final TransactionType transactionType;

    public ForgeTransactionResult(Account account, Currency currency, ResultType resultType,
                                  TransactionType transactionType) {
        this.account = account;
        this.currency = currency;
        this.resultType = resultType;
        this.transactionType = transactionType;
    }

    @Override
    public Account getAccount() {
        return this.account;
    }

    @Override
    public Currency getCurrency() {
        return this.currency;
    }

    @Override
    public BigDecimal getAmount() {
        return this.account.getBalance(this.currency);
    }

    @Override
    public Set<Context> getContexts() {
        return Collections.emptySet();
    }

    @Override
    public ResultType getResult() {
        return this.resultType;
    }

    @Override
    public TransactionType getType() {
        return this.transactionType;
    }
}
