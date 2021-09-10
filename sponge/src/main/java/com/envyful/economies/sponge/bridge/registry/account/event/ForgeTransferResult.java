package com.envyful.economies.sponge.bridge.registry.account.event;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransferResult;

public class ForgeTransferResult extends ForgeTransactionResult implements TransferResult {

    private final Account to;

    public ForgeTransferResult(Account to, Account account, Currency currency, ResultType resultType,
                               TransactionType transactionType) {
        super(account, currency, resultType, transactionType);

        this.to = to;
    }

    @Override
    public Account getAccountTo() {
        return this.to;
    }
}
