package com.envyful.economies.sponge.bridge.platform;

import com.envyful.api.player.EnvyPlayer;
import com.envyful.economies.api.Economy;
import com.envyful.economies.api.platform.PlatformController;
import com.envyful.economies.sponge.bridge.registry.ForgeCurrency;
import com.envyful.economies.sponge.bridge.registry.account.event.ForgeTransactionEvent;
import com.envyful.economies.sponge.bridge.registry.account.event.ForgeTransactionResult;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.economy.EconomyTransactionEvent;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;

import java.util.Objects;

public class SpongePlatformController implements PlatformController {
    @Override
    public void sendEconomyEvent(EnvyPlayer<?> player, Economy economy, double oldBalance, double newBalance, double change) {
        EconomyService provider = Sponge.getGame().getServiceManager().getRegistration(EconomyService.class).orElse(null).getProvider();
        UniqueAccount uniqueAccount = provider.getOrCreateAccount(player.getUuid()).orElse(null);

        if (uniqueAccount == null) {
            return;
        }

        Currency currency = this.getCurrency(provider, economy);

        if (currency == null) {
            return;
        }

        EconomyTransactionEvent event = new ForgeTransactionEvent(uniqueAccount,
                new ForgeTransactionResult(uniqueAccount, currency, ResultType.SUCCESS, TransactionTypes.TRANSFER));
        Sponge.getEventManager().post(event);
    }

    private Currency getCurrency(EconomyService service, Economy economy) {
        for (Currency currency : service.getCurrencies()) {
            if (!(currency instanceof ForgeCurrency)) {
                continue;
            }

            if (Objects.equals(((ForgeCurrency) currency).getEconomy(), economy)) {
                return currency;
            }
        }

        return null;
    }
}
