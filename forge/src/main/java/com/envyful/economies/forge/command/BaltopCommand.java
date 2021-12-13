package com.envyful.economies.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.Argument;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Completable;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.impl.EconomyTabCompleter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "baltop",
        description = "§7/baltop [economy] [page]",
        aliases = {
                "ecotop"
        }
)
public class BaltopCommand {

    @CommandProcessor
    public void onCommand(@Sender EntityPlayerMP player,
                          @Completable(EconomyTabCompleter.class) @Argument(defaultValue = "default") Economy economy,
                          @Argument(defaultValue = "1") int page) {
        if (page <= 0) {
            player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes('&',
                    EconomiesForge.getInstance().getLocale().getPageMustBeGreaterThanZero())));
            return;
        }

        for (String s : economy.getLeaderboard().getPage(page - 1)) {
            player.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes('&', s)));
        }
    }
}
