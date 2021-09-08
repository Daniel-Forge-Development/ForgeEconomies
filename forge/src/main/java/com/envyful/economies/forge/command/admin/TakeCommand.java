package com.envyful.economies.forge.command.admin;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.player.EconomiesAttribute;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "take",
        description = "Admin take command"
)
@Permissible("economies.command.eco.take")
@Child
public class TakeCommand {

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

        account.withdraw(value);
        targetPlayer.message("");
        sender.sendMessage(new TextComponentString("")); //TODO: add messages
    }
}
