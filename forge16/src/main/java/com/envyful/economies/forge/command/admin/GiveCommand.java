package com.envyful.economies.forge.command.admin;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Completable;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.command.completion.number.IntCompletionData;
import com.envyful.api.forge.command.completion.number.IntegerTabCompleter;
import com.envyful.api.forge.command.completion.player.PlayerTabCompleter;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.api.forge.player.util.UtilPlayer;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.impl.EconomyTabCompleter;
import com.envyful.economies.forge.player.EconomiesAttribute;
import com.envyful.economies.forge.player.OfflinePlayerData;
import com.envyful.economies.forge.player.OfflinePlayerManager;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

@Command(
        value = "give",
        description = "§7/eco give <player> <economy> <amount>",
        aliases = {
                "add"
        }
)
@Permissible("economies.command.eco.give")
@Child
public class GiveCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSource sender,
                          @Completable(PlayerTabCompleter.class) @Argument String target,
                          @Completable(EconomyTabCompleter.class) @Argument(defaultValue = "default") Economy economy,
                          @Completable(IntegerTabCompleter.class) @IntCompletionData(min = 1, max = 20) @Argument double value) {
        if (target.equalsIgnoreCase("@a")) {
            for (ForgeEnvyPlayer onlinePlayer : EconomiesForge.getInstance().getPlayerManager().getOnlinePlayers()) {
                EconomiesAttribute attribute = onlinePlayer.getAttribute(EconomiesForge.class);

                if (attribute == null) {
                    continue;
                }

                Bank account = attribute.getAccount(economy);

                if (account == null) {
                    return;
                }

                account.deposit(value);

                onlinePlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                        .getLocale().getGivenMoney().replace("%value%",
                                (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                        String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value)
                                        + (!economy.isPrefix() ? economy.getEconomyIdentifier() : ""))
                        .replace("%sender%", UtilPlayer.getName(sender))
                        .replace("%player%", onlinePlayer.getName())));
            }

            sender.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes('&', "&a&l(!) &aAdded $" + String.format(economy.getFormat(), value) + " to all online players!")), Util.DUMMY_UUID);
            return;
        }

        EnvyPlayer<ServerPlayerEntity> targetPlayer = EconomiesForge.getInstance().getPlayerManager().getOnlinePlayerCaseInsensitive(target);

        if (value <= 0) {
            sender.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes(
                    '&',
                    EconomiesForge.getInstance().getLocale().getCannotGiveLessThanZero()
            )), Util.DUMMY_UUID);
            return;
        }

        if (targetPlayer == null) {
            OfflinePlayerData playerByName = OfflinePlayerManager.getPlayerByName(target, economy);

            if (playerByName == null) {
                sender.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes(
                        '&',
                        EconomiesForge.getInstance().getLocale().getPlayerNotFound()
                )), Util.DUMMY_UUID);
                return;
            }

            Bank balance = playerByName.getBalance(economy);

            balance.deposit(value);

            sender.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes(
                    '&',
                    EconomiesForge.getInstance().getLocale().getAdminGivenMoney()
                            .replace("%player%", target)
                            .replace("%value%", (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                    String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value)
                                    + (!economy.isPrefix() ? economy.getEconomyIdentifier() : ""))
                            .replace("%sender%", UtilPlayer.getName(sender))
            )), Util.DUMMY_UUID);

            return;
        }

        EconomiesAttribute attribute = targetPlayer.getAttribute(EconomiesForge.class);

        if (attribute == null) {
            return;
        }

        Bank account = attribute.getAccount(economy);

        if (account == null) {
            return;
        }

        account.deposit(value);

        targetPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                .getLocale().getGivenMoney().replace("%value%",
                        (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value)
                                + (!economy.isPrefix() ? economy.getEconomyIdentifier() : ""))
        .replace("%sender%", UtilPlayer.getName(sender))
        .replace("%player%", targetPlayer.getName())));

        sender.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes('&',
                EconomiesForge.getInstance().getLocale().getAdminGivenMoney()
                        .replace("%player%", targetPlayer.getName())
                        .replace("%value%", (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value)
                                + (!economy.isPrefix() ? economy.getEconomyIdentifier() : ""))
                        .replace("%sender%", UtilPlayer.getName(sender))
        )), Util.DUMMY_UUID);
    }
}
