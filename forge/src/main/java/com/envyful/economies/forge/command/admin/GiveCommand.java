package com.envyful.economies.forge.command.admin;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.player.EconomiesAttribute;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "give",
        description = "Admin give command"
)
@Permissible("economies.command.eco.give")
@Child
public class GiveCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSender sender, @Argument EntityPlayerMP target, @Argument Economy economy,
                          @Argument double value) {
        EnvyPlayer<EntityPlayerMP> targetPlayer = EconomiesForge.getInstance().getPlayerManager().getPlayer(target);
        EconomiesAttribute attribute = targetPlayer.getAttribute(EconomiesForge.class);

        if (attribute == null) {
            return;
        }

        if (value <= 0) {
            sender.sendMessage(new TextComponentString("You cannot give someone an amount less than 0"));
            return;
        }

        Bank account = attribute.getAccount(economy);

        if (account == null) {
            return;
        }

        account.deposit(value);

        targetPlayer.message(UtilChatColour.translateColourCodes('&', EconomiesForge.getInstance()
                .getLocale().getGivenMoney().replace("%value%",
                        (economy.isPrefix() ? economy.getEconomyIdentifier() : "") + value
                                + (!economy.isPrefix() ? economy.getEconomyIdentifier() : ""))));

        sender.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes('&',
                EconomiesForge.getInstance().getLocale().getAdminGivenMoney()
                        .replace("%player%", target.getName())
                        .replace("%value%", (economy.isPrefix() ? economy.getEconomyIdentifier() : "") + value
                                + (!economy.isPrefix() ? economy.getEconomyIdentifier() : "")))));
    }
}
