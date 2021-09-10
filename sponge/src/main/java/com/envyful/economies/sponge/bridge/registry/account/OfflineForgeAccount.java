package com.envyful.economies.sponge.bridge.registry.account;

import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.player.OfflinePlayerData;
import com.envyful.economies.forge.player.OfflinePlayerManager;
import com.envyful.economies.sponge.bridge.registry.ForgeCurrency;
import com.envyful.economies.sponge.bridge.registry.ForgeEconomyService;
import com.envyful.economies.sponge.bridge.registry.account.event.ForgeTransactionEvent;
import com.envyful.economies.sponge.bridge.registry.account.event.ForgeTransactionResult;
import com.google.common.collect.Maps;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.*;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class OfflineForgeAccount implements UniqueAccount {

    private final UUID uuid;
    private OfflinePlayerData offlinePlayerData;

    public OfflineForgeAccount(UUID uuid, Currency currency) {
        this.uuid = uuid;

        if (!(currency instanceof ForgeCurrency)) {
            return;
        }

        this.offlinePlayerData = OfflinePlayerManager.getPlayer(this.uuid, ((ForgeCurrency) currency).getEconomy());
    }

    @Override
    public Text getDisplayName() {
        return Text.of(this.uuid.toString());
    }

    @Override
    public BigDecimal getDefaultBalance(Currency currency) {
        if (!(currency instanceof ForgeCurrency)) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(((ForgeCurrency) currency).getEconomy().getDefaultValue());
    }

    @Override
    public boolean hasBalance(Currency currency, Set<Context> contexts) {
        return true;
    }

    @Override
    public BigDecimal getBalance(Currency currency, Set<Context> contexts) {
        if (!(currency instanceof ForgeCurrency)) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(this.offlinePlayerData.getBalance(((ForgeCurrency) currency).getEconomy()).getBalance());
    }

    @Override
    public Map<Currency, BigDecimal> getBalances(Set<Context> contexts) {
        Map<Currency, BigDecimal> currencies = Maps.newHashMap();

        for (Currency currency : ForgeEconomyService.getInstance().getCurrencies()) {
            currencies.put(currency, this.getBalance(currency));
        }

        return currencies;
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
        if (!(currency instanceof ForgeCurrency)) {
            return new ForgeTransactionResult(this, currency, ResultType.FAILED, TransactionTypes.TRANSFER);
        }

        Bank balance = this.offlinePlayerData.getBalance(((ForgeCurrency) currency).getEconomy());
        balance.setBalance(amount.doubleValue());
        return this.post(currency, amount, TransactionTypes.TRANSFER);
    }

    @Override
    public Map<Currency, TransactionResult> resetBalances(Cause cause, Set<Context> contexts) {
        Map<Currency, TransactionResult> currencies = Maps.newHashMap();

        for (Currency currency : ForgeEconomyService.getInstance().getCurrencies()) {
            currencies.put(currency, this.resetBalance(currency, cause, contexts));
        }

        return currencies;
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Cause cause, Set<Context> contexts) {
        if (!(currency instanceof ForgeCurrency)) {
            return new ForgeTransactionResult(this, currency, ResultType.FAILED, TransactionTypes.TRANSFER);
        }

        Economy economy = ((ForgeCurrency) currency).getEconomy();
        Bank balance = this.offlinePlayerData.getBalance(economy);

        balance.setBalance(economy.getDefaultValue());
        return this.post(currency, BigDecimal.valueOf(economy.getDefaultValue()), TransactionTypes.TRANSFER);
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
        if (!(currency instanceof ForgeCurrency)) {
            return new ForgeTransactionResult(this, currency, ResultType.FAILED, TransactionTypes.DEPOSIT);
        }

        Economy economy = ((ForgeCurrency) currency).getEconomy();
        Bank balance = this.offlinePlayerData.getBalance(economy);

        balance.deposit(amount.doubleValue());
        return this.post(currency, amount, TransactionTypes.DEPOSIT);
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
        if (!(currency instanceof ForgeCurrency)) {
            return new ForgeTransactionResult(this, currency, ResultType.FAILED, TransactionTypes.WITHDRAW);
        }

        Economy economy = ((ForgeCurrency) currency).getEconomy();
        Bank account = this.offlinePlayerData.getBalance(economy);

        if (!account.hasFunds(amount.doubleValue())) {
            return new ForgeTransactionResult(this, currency, ResultType.FAILED, TransactionTypes.WITHDRAW);
        }

        account.withdraw(amount.doubleValue());
        return this.post(currency, amount, TransactionTypes.WITHDRAW);
    }

    @Override
    public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
        return null;
    }

    @Override
    public String getIdentifier() {
        return this.uuid.toString();
    }

    @Override
    public Set<Context> getActiveContexts() {
        return Collections.emptySet();
    }

    private TransactionResult post(Currency currency, BigDecimal amount, TransactionType transactionType) {
        TransactionResult result = new ForgeTransactionResult(this, currency, ResultType.SUCCESS, transactionType);
        Sponge.getEventManager().post(new ForgeTransactionEvent(this, result));
        return result;
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }
}
