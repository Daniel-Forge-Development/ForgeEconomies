package com.envyful.economies.forge.player;

import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.UUID;

public class OfflinePlayerManager {

    private static final Map<UUID, OfflinePlayerData> OFFLINE_CACHE = Maps.newConcurrentMap();

    public static Bank getPlayerBalance(UUID uuid, Economy economy) {
        OfflinePlayerData offlinePlayerData = OFFLINE_CACHE.get(uuid);

        if (offlinePlayerData != null) {
            return offlinePlayerData.getBalance(economy);
        }

        return new OfflinePlayerData(uuid, economy).getBalance(economy);
    }


}
