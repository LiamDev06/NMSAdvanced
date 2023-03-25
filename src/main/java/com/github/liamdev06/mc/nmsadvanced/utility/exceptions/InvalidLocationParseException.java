package com.github.liamdev06.mc.nmsadvanced.utility.exceptions;

/**
 * Thrown when the location being parsed in {@link com.github.liamdev06.mc.nmsadvanced.utility.Common#parseLocation(String)} is
 * not valid and the method cannot return a {@link org.bukkit.Location}
 */
public class InvalidLocationParseException extends Exception {

    /**
     * @param message the error message to send when this is thrown
     */
    public InvalidLocationParseException(String message) {
        super(message);
    }

}
