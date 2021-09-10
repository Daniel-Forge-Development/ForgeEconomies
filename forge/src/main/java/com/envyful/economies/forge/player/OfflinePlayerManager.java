package com.envyful.economies.forge.player;

import com.envyful.api.concurrency.AsyncTaskBuilder;
import com.envyful.economies.api.Bank;
import com.envyful.economies.api.Economy;
import com.google.common.collect.Maps;

import java.util.Iterator;
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

    public static Bank getPlayerBalance(UUID uuid, Economy economy) {
        OfflinePlayerData offlinePlayerData = OFFLINE_CACHE.get(uuid);

        if (offlinePlayerData != null) {
            return offlinePlayerData.getBalance(economy);
        }

        return new OfflinePlayerData(uuid, economy).getBalance(economy);
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
