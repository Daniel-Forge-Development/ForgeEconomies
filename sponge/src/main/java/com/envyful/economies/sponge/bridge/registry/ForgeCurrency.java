package com.envyful.economies.sponge.bridge.registry;

import com.envyful.economies.api.Economy;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;

public class ForgeCurrency implements Currency {

    private final Economy economy;

    public ForgeCurrency(Economy economy) {
        this.economy = economy;
    }

    @Override
    public Text getDisplayName() {
        return Text.of(this.economy.getDisplayName());
    }

    @Override
    public Text getPluralDisplayName() {
        return Text.of(this.economy.getDisplayName());
    }

    @Override
    public Text getSymbol() {
        return Text.of(this.economy.getEconomyIdentifier());
    }

    @Override
    public Text format(BigDecimal amount, int numFractionDigits) {
        return null;
    }

    @Override
    public int getDefaultFractionDigits() {
        return (int) this.economy.getDefaultValue();
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public String getId() {
        return this.economy.getId();
    }

    @Override
    public String getName() {
        return this.economy.getId();
    }
}
