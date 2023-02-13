package com.github.liamdev06.mc.nmsadvanced.customentity.exceptions;

/**
 * Exception that throws if a custom game entity could not be spawned
 *
 * @author Liam
 */
public class GameEntitySpawnError extends Exception {

    /**
     * @param message the error/cause for why the entity could not be spawned
     */
    public GameEntitySpawnError(String message) {
        super(message);
    }

}
