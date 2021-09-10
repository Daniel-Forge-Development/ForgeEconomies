package com.envyful.economies.spigot.bridge;

import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.player.EconomiesAttribute;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EconomiesForgeSpigot extends JavaPlugin {

    @Override
    public void onEnable() {

    }

    public static class ForgeEconomy extends AbstractEconomy {

        private final Economy economy;

        public ForgeEconomy(Economy economy) {
            this.economy = economy;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public String getName() {
            return this.economy.getDisplayName();
        }

        @Override
        public boolean hasBankSupport() {
            return false;
        }

        @Override
        public int fractionalDigits() {
            return 2;
        }

        @Override
        public String format(double amount) {
            return (this.economy.isPrefix() ? this.economy.getEconomyIdentifier() : "") +
                    String.format("%.2f", amount) +
                    (!this.economy.isPrefix() ? this.economy.getEconomyIdentifier() : "");
        }

        @Override
        public String currencyNamePlural() {
            return this.economy.getDisplayName();
        }

        @Override
        public String currencyNameSingular() {
            return this.economy.getDisplayName();
        }

        @Override
        public boolean hasAccount(String playerName) {
            return true;
        }

        @Override
        public boolean hasAccount(String playerName, String worldName) {
            return true;
        }

        @Override
        public double getBalance(String playerName) {
            ForgeEnvyPlayer onlinePlayer = EconomiesForge.getInstance().getPlayerManager().getOnlinePlayer(playerName);

            if (onlinePlayer == null) {
                return 0;
            }

            EconomiesAttribute attribute = onlinePlayer.getAttribute(EconomiesForge.class);
            return attribute.getAccount(this.economy).getBalance();
        }

        @Override
        public double getBalance(String playerName, String world) {
            return 0;
        }

        @Override
        public boolean has(String playerName, double amount) {
            ForgeEnvyPlayer onlinePlayer = EconomiesForge.getInstance().getPlayerManager().getOnlinePlayer(playerName);

            if (onlinePlayer == null) {
                return false;
            }

            EconomiesAttribute attribute = onlinePlayer.getAttribute(EconomiesForge.class);
            return attribute.getAccount(this.economy).hasFunds(amount);
        }

        @Override
        public boolean has(OfflinePlayer player, double amount) {
            return false;
        }

        @Override
        public boolean has(String playerName, String worldName, double amount) {
            return this.has(playerName, amount);
        }

        @Override
        public boolean has(OfflinePlayer player, String worldName, double amount) {
            return false;
        }

        @Override
        public EconomyResponse withdrawPlayer(String playerName, double amount) {
            ForgeEnvyPlayer onlinePlayer = EconomiesForge.getInstance().getPlayerManager().getOnlinePlayer(playerName);

            if (onlinePlayer == null) {
                return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "");
            }

            EconomiesAttribute attribute = onlinePlayer.getAttribute(EconomiesForge.class);

            if (!attribute.getAccount(this.economy).hasFunds(amount)) {
                return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "");
            }

            attribute.getAccount(this.economy).withdraw(amount);
            return new EconomyResponse(amount, attribute.getAccount(this.economy).getBalance(),
                    EconomyResponse.ResponseType.SUCCESS, "");
        }

        @Override
        public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
            return null;
        }

        @Override
        public EconomyResponse depositPlayer(String playerName, double amount) {
            return null;
        }

        @Override
        public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
            return null;
        }

        @Override
        public EconomyResponse createBank(String name, String player) {
            return null;
        }

        @Override
        public EconomyResponse deleteBank(String name) {
            return null;
        }

        @Override
        public EconomyResponse bankBalance(String name) {
            return null;
        }

        @Override
        public EconomyResponse bankHas(String name, double amount) {
            return null;
        }

        @Override
        public EconomyResponse bankWithdraw(String name, double amount) {
            return null;
        }

        @Override
        public EconomyResponse bankDeposit(String name, double amount) {
            return null;
        }

        @Override
        public EconomyResponse isBankOwner(String name, String playerName) {
            return null;
        }

        @Override
        public EconomyResponse isBankMember(String name, String playerName) {
            return null;
        }

        @Override
        public List<String> getBanks() {
            return null;
        }

        @Override
        public boolean createPlayerAccount(String playerName) {
            return false;
        }

        @Override
        public boolean createPlayerAccount(String playerName, String worldName) {
            return false;
        }
    }
}
