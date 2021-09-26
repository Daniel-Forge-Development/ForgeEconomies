
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
        value = "reset",
        description = "§7/eco reset <player> <economy>"
)
@Permissible("economies.command.eco.reset")
@Child
public class ResetCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSender sender, @Argument EntityPlayerMP target, @Argument Economy economy) {
        EnvyPlayer<EntityPlayerMP> targetPlayer = EconomiesForge.getInstance().getPlayerManager().getPlayer(target);
        EconomiesAttribute attribute = targetPlayer.getAttribute(EconomiesForge.class);

        if (attribute == null) {
            return;
        }

        Bank account = attribute.getAccount(economy);

        if (account == null) {
            return;
        }

        account.setBalance(economy.getDefaultValue());

        sender.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes('&',
                EconomiesForge.getInstance().getLocale().getAdminResetMoney()
                        .replace("%player%", target.getName())
                        .replace("%value%", (economy.isPrefix() ? economy.getEconomyIdentifier() : "") + economy.getDefaultValue()
                                + (!economy.isPrefix() ? economy.getEconomyIdentifier() : "")))));
    }
}
