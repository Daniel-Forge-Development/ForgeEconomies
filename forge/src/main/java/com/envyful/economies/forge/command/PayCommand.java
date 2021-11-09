package com.envyful.economies.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Completable;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.command.completion.player.ExcludeSelfCompletion;
import com.envyful.api.forge.command.completion.player.PlayerTabCompleter;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.impl.EconomyTabCompleter;
import com.envyful.economies.forge.player.EconomiesAttribute;
import com.envyful.economies.forge.player.OfflinePlayerData;
import com.envyful.economies.forge.player.OfflinePlayerManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.util.Objects;

@Command(
        value = "pay",
        description = "§7/pay <economy> <player> <amount>",
        aliases = {
                "epay",
                "ecopay"
        }
)
public class PayCommand {


    @CommandProcessor
    public void onCommand(@Sender EntityPlayerMP player,
                          @Completable(EconomyTabCompleter.class) @Argument Economy economy,
                          @Completable(PlayerTabCompleter.class) @ExcludeSelfCompletion @Argument String target,
                          @Argument double value) {
        EnvyPlayer<EntityPlayerMP> envyPlayer = EconomiesForge.getInstance().getPlayerManager().getPlayer(player);
        EnvyPlayer<EntityPlayerMP> targetPlayer = EconomiesForge.getInstance().getPlayerManager().getOnlinePlayer(target);

        if (value < economy.getMinimumPayAmount()) {
            envyPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                    .getLocale().getMinimumPayAmount().replace(
                            "%value%",
                            (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                    String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value)
                                    + (!economy.isPrefix() ? economy.getEconomyIdentifier() : "")
                    )));
            return;
        }

        EconomiesAttribute playerAttribute = envyPlayer.getAttribute(EconomiesForge.class);

        if (playerAttribute == null) {
            return;
        }

        Bank playerAccount = playerAttribute.getAccount(economy);

        if (!playerAccount.hasFunds(value)) {
            envyPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                    .getLocale().getInsufficientFunds()
                    .replace("%economy_name%", economy.getDisplayName())));
            return;
        }

        if (targetPlayer == null) {
            OfflinePlayerData playerByName = OfflinePlayerManager.getPlayerByName(target, economy);

            if (playerByName == null) {
                player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                        '&',
                        EconomiesForge.getInstance().getLocale().getPlayerNotFound()
                )));
                return;
            }

            Bank balance = playerByName.getBalance(economy);

            playerAccount.withdraw(value);
            balance.deposit(value);

            envyPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                    .getLocale().getTakenMoney().replace("%value%",
                                                         (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                                                 String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value)
                                                                 + (!economy.isPrefix() ? economy.getEconomyIdentifier() : ""))
                    .replace("%player%", target)
                    .replace("%sender%", envyPlayer.getName())));
            return;
        }

        if (Objects.equals(player.getUniqueID(), targetPlayer.getUuid())) {
            player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes('&',
                    EconomiesForge.getInstance().getLocale().getCannotPayYourself())));
            return;
        }

        EconomiesAttribute targetAttribute = targetPlayer.getAttribute(EconomiesForge.class);

        if (targetAttribute == null) {
            return;
        }

        Bank targetAccount = targetAttribute.getAccount(economy);

        playerAccount.withdraw(value);
        targetAccount.deposit(value);

        envyPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                .getLocale().getTakenMoney().replace("%value%",
                        (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value)
                                + (!economy.isPrefix() ? economy.getEconomyIdentifier() : ""))
                .replace("%player%", targetPlayer.getName())
                .replace("%sender%", envyPlayer.getName())));

        targetPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                .getLocale().getGivenMoney().replace("%value%",
                        (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value)
                                + (!economy.isPrefix() ? economy.getEconomyIdentifier() : ""))
        .replace("%player%", targetPlayer.getName())
        .replace("%sender%", envyPlayer.getName())));
    }
}
