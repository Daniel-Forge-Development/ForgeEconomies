package com.envyful.economies.forge.impl;

import com.envyful.api.command.injector.TabCompleter;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.EconomiesForge;
import com.envyful.economies.forge.config.EconomiesConfig;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class EconomyTabCompleter implements TabCompleter<Economy, ServerPlayerEntity> {

    private List<String> keysCache = null;

    @Override
    public Class<ServerPlayerEntity> getSenderClass() {
        return ServerPlayerEntity.class;
    }

    @Override
    public Class<Economy> getCompletedClass() {
        return Economy.class;
    }

    @Override
    public List<String> getCompletions(ServerPlayerEntity sender, String[] currentData,
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
