
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
import com.envyful.api.player.EnvyPlayer;
import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.impl.EconomyTabCompleter;
import com.envyful.economies.forge.player.EconomiesAttribute;
import com.envyful.economies.forge.player.OfflinePlayerData;
import com.envyful.economies.forge.player.OfflinePlayerManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "set",
        description = "§7/eco set <player> <economy> <amount>"
)
@Permissible("economies.command.eco.set")
@Child
public class SetCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSender sender,
                          @Completable(PlayerTabCompleter.class) @Argument String target,
                          @Completable(EconomyTabCompleter.class) @Argument Economy economy,
                          @Completable(IntegerTabCompleter.class) @IntCompletionData(min = 1, max = 20) @Argument double value) {
        EnvyPlayer<EntityPlayerMP> targetPlayer = EconomiesForge.getInstance().getPlayerManager().getOnlinePlayer(target);

        if (value <= 0) {
            sender.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                    '&',
                    EconomiesForge.getInstance().getLocale().getCannotSetLessThanZero()
            )));
            return;
        }

        if (targetPlayer == null) {
            OfflinePlayerData playerByName = OfflinePlayerManager.getPlayerByName(target, economy);

            if (playerByName == null) {
                sender.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                        '&',
                        EconomiesForge.getInstance().getLocale().getPlayerNotFound()
                )));
                return;
            }

            Bank balance = playerByName.getBalance(economy);

            balance.setBalance(value);
            sender.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                    '&',
                    EconomiesForge.getInstance().getLocale().getAdminSetMoney()
                            .replace("%player%", target)
                            .replace("%value%",
                                     String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value))
                            .replace("%sender%", sender.getName())
            )));

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

        account.setBalance(value);
        sender.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                '&',
                EconomiesForge.getInstance().getLocale().getAdminSetMoney()
                        .replace("%player%", targetPlayer.getName())
                        .replace("%value%",
                                 String.format(EconomiesForge.getInstance().getLocale().getBalanceFormat(), value))
                        .replace("%sender%", sender.getName())
        )));
    }
}
