package com.envyful.economies.forge.impl;

import com.envyful.api.command.injector.TabCompleter;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import net.minecraft.entity.player.EntityPlayerMP;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class EconomyTabCompleter implements TabCompleter<Economy, EntityPlayerMP> {
    @Override
    public Class<EntityPlayerMP> getSenderClass() {
        return EntityPlayerMP.class;
    }

    @Override
    public Class<Economy> getCompletedClass() {
        return Economy.class;
    }

    @Override
    public List<String> getCompletions(EntityPlayerMP sender, String[] currentData,
                                       Annotation... completionData) {
        return new ArrayList<>(EconomiesForge.getInstance().getConfig().getEconomies().keySet());
    }
}
