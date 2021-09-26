
package com.envyful.economies.forge.command.admin;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.economies.forge.EconomiesForge;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

@Command(
        value = "reload",
        description = "§7/eco reload"
)
@Permissible("economies.command.eco.reload")
@Child
public class ReloadCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSender sender, String[] args) {
        EconomiesForge.getInstance().loadConfig();
        sender.sendMessage(new TextComponentString(UtilChatColour.translateColourCodes(
                '&',
                EconomiesForge.getInstance().getLocale().getAdminReload()
        )));
    }
}
