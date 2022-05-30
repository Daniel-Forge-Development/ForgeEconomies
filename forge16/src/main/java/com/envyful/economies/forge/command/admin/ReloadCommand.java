
package com.envyful.economies.forge.command.admin;

import com.envyful.api.command.annotate.Child;
import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.economies.forge.EconomiesForge;
import net.minecraft.command.ICommandSource;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

@Command(
        value = "reload",
        description = "§7/eco reload"
)
@Permissible("economies.command.eco.reload")
@Child
public class ReloadCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSource sender, String[] args) {
        EconomiesForge.getInstance().loadConfig();
        sender.sendMessage(new StringTextComponent(UtilChatColour.translateColourCodes(
                '&',
                EconomiesForge.getInstance().getLocale().getAdminReload()
        )), Util.NIL_UUID);
    }
}
