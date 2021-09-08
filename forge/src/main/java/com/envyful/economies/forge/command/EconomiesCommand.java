package com.envyful.economies.forge.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.SubCommands;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.economies.forge.command.admin.GiveCommand;
import com.envyful.economies.forge.command.admin.TakeCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "eco",
        description = "Economies admin command",
        aliases = {
                "economies",
                "forgeeconomies",
                "econ"
        }
)
@Permissible("economies.command.eco")
@SubCommands({GiveCommand.class, TakeCommand.class})
public class EconomiesCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSender sender, String[] args) {
        sender.sendMessage(new TextComponentString("/eco [reload|give|take|set|reset]"));
    }
}
