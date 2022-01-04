package com.envyful.economies.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.economies.forge.command.admin.*;
import net.minecraft.command.ICommandSource;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

@Command(
        value = "eco",
        description = "§7/eco [give|take|reload|reset|set]",
        aliases = {
                "economies",
                "forgeeconomies",
                "econ"
        }
)
@Permissible("economies.command.eco")
@SubCommands({GiveCommand.class, TakeCommand.class, ResetCommand.class, SetCommand.class, ReloadCommand.class})
public class EconomiesCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSource sender, String[] args) {
        sender.sendMessage(new StringTextComponent("/eco [reload|give|take|set|reset]"), Util.DUMMY_UUID);
    }
}
