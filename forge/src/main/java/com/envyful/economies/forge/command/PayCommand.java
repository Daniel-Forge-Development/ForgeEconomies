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
                          @Completable(PlayerTabCompleter.class) @ExcludeSelfCompletion @Argument EntityPlayerMP target,
                          @Argument double value) {
        if (Objects.equals(player.getUniqueID(), target.getUniqueID())) {
            player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes('&',
                    EconomiesForge.getInstance().getLocale().getCannotPayYourself())));
            return;
        }

        EnvyPlayer<EntityPlayerMP> targetPlayer = EconomiesForge.getInstance().getPlayerManager().getPlayer(target);
        EconomiesAttribute targetAttribute = targetPlayer.getAttribute(EconomiesForge.class);

        if (targetAttribute == null) {
            return;
        }

        EnvyPlayer<EntityPlayerMP> envyPlayer = EconomiesForge.getInstance().getPlayerManager().getPlayer(player);
        EconomiesAttribute playerAttribute = envyPlayer.getAttribute(EconomiesForge.class);

        if (playerAttribute == null) {
            return;
        }

        if (value < economy.getMinimumPayAmount()) {
            envyPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                    .getLocale().getMinimumPayAmount().replace("%value%",
                            (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                    String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value)
                                    + (!economy.isPrefix() ? economy.getEconomyIdentifier() : ""))));
            return;
        }

        Bank playerAccount = playerAttribute.getAccount(economy);
        Bank targetAccount = targetAttribute.getAccount(economy);

        if (!playerAccount.hasFunds(value)) {
            envyPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                    .getLocale().getInsufficientFunds()
                    .replace("%economy_name%", economy.getDisplayName())));
            return;
        }

        playerAccount.withdraw(value);
        targetAccount.deposit(value);

        envyPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                .getLocale().getTakenMoney().replace("%value%",
                        (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value)
                                + (!economy.isPrefix() ? economy.getEconomyIdentifier() : ""))));

        targetPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                .getLocale().getGivenMoney().replace("%value%",
                        (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value)
                                + (!economy.isPrefix() ? economy.getEconomyIdentifier() : ""))));
    }
}
