package com.envyful.economies.api.platform;

import com.envyful.api.player.EnvyPlayer;

/**
 *
 * Used for when the Forge mod needs to send events on the platform's side
 *
 */
public interface PlatformController {

    /**
     *
     * An interface to send the platform event
     *
     * @param player The player who's money is changing
     * @param oldBalance The player's old balance
     * @param newBalance The player's new balance
     * @param change The raw change in balance
     */
    void sendEconomyEvent(EnvyPlayer<?> player, double oldBalance, double newBalance, double change);

}
