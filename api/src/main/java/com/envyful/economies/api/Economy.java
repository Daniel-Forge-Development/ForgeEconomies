package com.envyful.economies.api;

/**
 *
 * Represents an economy on the server
 *
 */
public interface Economy {

    /**
     *
     * The identifier of the economy
     *
     * @return The identifier
     */
    String getId();

    /**
     *
     * Gets the display name of the Economy
     *
     * @return the display name
     */
    String getDisplayName();

    /**
     *
     * Gets the identifier for the economy
     *
     * @return The identifier
     */
    String getEconomyIdentifier();

    /**
     *
     * Determines if the identifier is a prefix or suffix
     *
     * @return True = prefix
     */
    boolean isPrefix();

}
