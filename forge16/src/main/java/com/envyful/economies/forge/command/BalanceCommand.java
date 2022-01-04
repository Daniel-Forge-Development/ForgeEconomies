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
import com.envyful.economies.forge.config.EconomiesConfig;
import com.envyful.economies.forge.impl.EconomyTabCompleter;
import com.envyful.economies.forge.player.EconomiesAttribute;
import com.envyful.economies.forge.player.OfflinePlayerData;
import com.envyful.economies.forge.player.OfflinePlayerManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

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
    public void onCommand(@Sender ServerPlayerEntity player,
                          @Completable(EconomyTabCompleter.class) @Argument(defaultValue = "default") Economy economy,
                          String[] args) {
        if (args.length < 1) {
            EnvyPlayer<ServerPlayerEntity> envyPlayer = EconomiesForge.getInstance().getPlayerManager().getPlayer(player);
            EconomiesAttribute playerAttribute = envyPlayer.getAttribute(EconomiesForge.class);

            if (playerAttribute == null) {
                return;
            }

            if (EconomiesForge.getInstance().getConfig().isBalanceShowsAll()) {
                for (String s : EconomiesForge.getInstance().getLocale().getAllBalanceFormat()) {
                    envyPlayer.message(UtilChatColour.translateColourCodes('&',
                                                                           this.handleAllPlaceholders(envyPlayer.getName(),
                                                                                                      playerAttribute, s)));
                }

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

        EnvyPlayer<ServerPlayerEntity> target = EconomiesForge.getInstance().getPlayerManager().getOnlinePlayerCaseInsensitive(args[0]);

        if (target == null) {
            OfflinePlayerData playerByName = OfflinePlayerManager.getPlayerByName(args[0], economy);

            if (playerByName == null) {
                player.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes(
                        '&',
                        EconomiesForge.getInstance().getLocale().getPlayerNotFound()
                )), Util.DUMMY_UUID);
                return;
            }

            if (EconomiesForge.getInstance().getConfig().isBalanceShowsAll()) {
                for (String s : EconomiesForge.getInstance().getLocale().getAllBalanceFormat()) {
                    player.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes(
                            '&',
                            this.handleAllPlaceholders(
                                    args[0],
                                    playerByName,
                                    s
                            )
                    )), Util.DUMMY_UUID);
                }

                return;
            }

            Bank account = playerByName.getBalance(economy);
            player.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes(
                    '&',
                    EconomiesForge.getInstance().getLocale().getTargetBalance()
                            .replace("%target%", args[0])
                            .replace("%balance%", String.format(economy.getFormat(), account.getBalance()))
            )), Util.DUMMY_UUID);
            return;
        }

        EconomiesAttribute targetAttribute = target.getAttribute(EconomiesForge.class);

        if (targetAttribute == null) {
            player.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes(
                    '&',
                    EconomiesForge.getInstance().getLocale().getPlayerNotOnline()
            )), Util.DUMMY_UUID);
            return;
        }

        if (EconomiesForge.getInstance().getConfig().isBalanceShowsAll()) {
            for (String s : EconomiesForge.getInstance().getLocale().getAllBalanceFormat()) {
                player.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes('&', this.handleAllPlaceholders(args[0], targetAttribute, s))), Util.DUMMY_UUID);
            }

            return;
        }

        Bank account = targetAttribute.getAccount(economy);
        player.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes(
                '&',
                EconomiesForge.getInstance().getLocale().getTargetBalance()
                .replace("%target%", target.getName())
                .replace("%balance%", String.format(economy.getFormat(), account.getBalance()))
        )), Util.DUMMY_UUID);
    }

    private String handleAllPlaceholders(String name, EconomiesAttribute attribute, String s) {
        s = s.replace("%player%", name);

        for (EconomiesConfig.ConfigEconomy value :
                EconomiesForge.getInstance().getConfig().getEconomies().values()) {
            s = s.replace(
                    "%player_balance_" + value.getEconomy().getId() + "%",
                    String.format(value.getEconomyFormat(), attribute.getAccount(value.getEconomy()).getBalance())
            );
        }

        return s;
    }

    private String handleAllPlaceholders(String name, OfflinePlayerData attribute, String s) {
        s = s.replace("%player%", name);

        for (EconomiesConfig.ConfigEconomy value :
                EconomiesForge.getInstance().getConfig().getEconomies().values()) {
            s = s.replace(
                    "%player_balance_" + value.getEconomy().getId() + "%",
                    String.format(value.getEconomyFormat(), attribute.getBalance(value.getEconomy()).getBalance())
            );
        }

        return s;
    }
}
