package com.envyful.economies.sponge.bridge.registry;

import com.envyful.economies.api.Economy;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ForgeCurrency implements Currency {

    private final Economy economy;

    public ForgeCurrency(Economy economy) {
        this.economy = economy;
    }

    public Economy getEconomy() {
        return this.economy;
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
        Text label = getPluralDisplayName();
        if (amount.equals(BigDecimal.ONE)) {
            label = getDisplayName();
        }
        return Text.of(String.format(economy.getFormat(), amount.setScale(numFractionDigits, RoundingMode.HALF_UP)) + " ", label);
    }

    @Override
    public int getDefaultFractionDigits() {
        return (int) this.economy.getDefaultValue();
    }

    @Override
    public boolean isDefault() {
        return this.economy.isDefault();
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
