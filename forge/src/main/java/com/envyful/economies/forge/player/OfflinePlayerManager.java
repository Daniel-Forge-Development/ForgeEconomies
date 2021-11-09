package com.envyful.economies.forge.player;

import com.envyful.api.concurrency.AsyncTaskBuilder;
import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.envyful.economies.forge.player.exception.PlayerNotFoundException;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.UUID;

public class OfflinePlayerManager {

    private static final Map<UUID, OfflinePlayerData> OFFLINE_CACHE = Maps.newConcurrentMap();

    static {
        new AsyncTaskBuilder()
                .delay(1000L)
                .interval(1000L)
                .task(new CacheTask())
                .start();
    }

    public static OfflinePlayerData getPlayerByName(String name, Economy economy) {
        OfflinePlayerData cachedData = searchByName(name);

        if (cachedData != null) {
            return cachedData;
        }

        System.out.println("FAILED TO FIND CACHE");

        try {
            OfflinePlayerData offlineData = new OfflinePlayerData(name, economy);
            OFFLINE_CACHE.put(offlineData.getUniqueId(), offlineData);
            return offlineData;
        } catch (PlayerNotFoundException e) {
            return null;
        }
    }

    private static OfflinePlayerData searchByName(String name) {
        for (OfflinePlayerData value : OFFLINE_CACHE.values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }

        return null;
    }

    public static boolean isCached(UUID uuid) {
        return OFFLINE_CACHE.containsKey(uuid);
    }

    public static OfflinePlayerData removeCache(UUID uuid) {
        return OFFLINE_CACHE.remove(uuid);
    }

    public static OfflinePlayerData getPlayer(UUID uuid, Economy economy) {
        OfflinePlayerData offlinePlayerData = OFFLINE_CACHE.get(uuid);

        if (offlinePlayerData != null) {
            offlinePlayerData.updateLastAccess();
            return offlinePlayerData;
        }

        OfflinePlayerData offlineData = new OfflinePlayerData(uuid, economy);
        OFFLINE_CACHE.put(uuid, offlineData);
        return offlineData;
    }

    public static Bank getPlayerBalance(UUID uuid, Economy economy) {
        OfflinePlayerData offlinePlayerData = OFFLINE_CACHE.get(uuid);

        if (offlinePlayerData != null) {
            return offlinePlayerData.getBalance(economy);
        }

        OfflinePlayerData offlineData = new OfflinePlayerData(uuid, economy);
        OFFLINE_CACHE.put(uuid, offlineData);
        return offlineData.getBalance(economy);
    }

    public static final class CacheTask implements Runnable {

        @Override
        public void run() {
            OFFLINE_CACHE.values().removeIf(next -> {
                if (next != null && next.hasTimedOut()) {
                    next.save();
                    return true;
                }

                return false;
            });
        }
    }
}
