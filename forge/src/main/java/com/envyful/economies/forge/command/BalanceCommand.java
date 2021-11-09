package com.envyful.economies.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Completable;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
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

@Command(
        value = "balance",
        description = "§7/balance [economy] (player)",
        aliases = {
                "ebalance",
                "ebal",
                "bal"
        }
)
public class BalanceCommand {

    @CommandProcessor
    public void onCommand(@Sender EntityPlayerMP player,
                          @Completable(EconomyTabCompleter.class) @Argument Economy economy,
                          String[] args) {
        if (args.length == 0) {
            player.sendMessage(new TextComponentString("§7/balance [economy] (player)"));
            return;
        }

        if (args.length == 1) {
            EnvyPlayer<EntityPlayerMP> envyPlayer = EconomiesForge.getInstance().getPlayerManager().getPlayer(player);
            EconomiesAttribute playerAttribute = envyPlayer.getAttribute(EconomiesForge.class);

            if (playerAttribute == null) {
                return;
            }

            Bank playerAccount = playerAttribute.getAccount(economy);

            envyPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                    .getLocale().getBalance().replace(
                            "%value%",
                            (economy.isPrefix() ? economy.getEconomyIdentifier() : "") +
                                    String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), playerAccount.getBalance())
                                    + (!economy.isPrefix() ? economy.getEconomyIdentifier() : "")
                    )));
            return;
        }

        EnvyPlayer<?> target = EconomiesForge.getInstance().getPlayerManager().getOnlinePlayer(args[1]);

        if (target == null) {
            OfflinePlayerData playerByName = OfflinePlayerManager.getPlayerByName(args[1], economy);

            if (playerByName == null) {
                player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                        '&',
                        EconomiesForge.getInstance().getLocale().getPlayerNotFound()
                )));
                return;
            }

            Bank account = playerByName.getBalance(economy);
            player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                    '&',
                    EconomiesForge.getInstance().getLocale().getTargetBalance()
                            .replace("%target%", target.getName())
                            .replace("%balance%", String.format("%.2f", account.getBalance()))
            )));
            return;
        }

        EconomiesAttribute targetAttribute = target.getAttribute(EconomiesForge.class);

        if (targetAttribute == null) {
            player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                    '&',
                    EconomiesForge.getInstance().getLocale().getPlayerNotOnline()
            )));
            return;
        }

        Bank account = targetAttribute.getAccount(economy);
        player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                '&',
                EconomiesForge.getInstance().getLocale().getTargetBalance()
                .replace("%target%", target.getName())
                .replace("%balance%", String.format("%.2f", account.getBalance()))
        )));
    }
}
