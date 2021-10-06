package com.envyful.economies.forge.impl;

import com.envyful.api.command.injector.TabCompleter;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.config.EconomiesConfig;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class EconomyTabCompleter implements TabCompleter<Economy, EntityPlayerMP> {

    private List<String> keysCache = null;

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
        return new ArrayList<>(this.getKeys());
    }

    private List<String> getKeys() {
        if (this.keysCache == null) {
            List<String> keys = Lists.newArrayList();

            for (EconomiesConfig.ConfigEconomy value : EconomiesForge.getInstance().getConfig().getEconomies().values()) {
                keys.add(value.getEconomy().getId());
            }
            this.keysCache = keys;
        }

        return this.keysCache;
    }
}
