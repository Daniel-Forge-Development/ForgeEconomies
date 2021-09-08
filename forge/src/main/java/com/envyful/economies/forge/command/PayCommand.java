package com.envyful.economies.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.player.EnvyPlayer;
import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.player.EconomiesAttribute;
import net.minecraft.entity.player.EntityPlayerMP;

@Command(
        value = "pay",
        description = "Pay another player",
        aliases = {
                "epay",
                "ecopay"
        }
)
public class PayCommand {


    @CommandProcessor
    public void onCommand(@Sender EntityPlayerMP player, @Argument Economy economy, @Argument EntityPlayerMP target,
                          @Argument double value) {
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

        Bank playerAccount = playerAttribute.getAccount(economy);
        Bank targetAccount = targetAttribute.getAccount(economy);

        if (!playerAccount.hasFunds(value)) {
            //TODO: error message
            return;
        }

        playerAccount.withdraw(value);
        targetAccount.withdraw(value);
    }
}
