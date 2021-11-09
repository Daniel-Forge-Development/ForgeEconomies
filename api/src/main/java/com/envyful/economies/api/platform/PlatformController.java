package com.envyful.economies.api.platform;

import com.envyful.api.player.EnvyPlayer;
import com.envyful.economies.api.Economy;

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
     * @param economy The economy being used
     * @param oldBalance The player's old balance
     * @param newBalance The player's new balance
     * @param change The raw change in balance
     */
    void sendEconomyEvent(EnvyPlayer<?> player, Economy economy, double oldBalance, double newBalance, double change);

}
