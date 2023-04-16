package com.github.liamdev06.mc.nmsadvanced.entity.exceptions;

/**
 * Exception that throws if a custom game entity could not be spawned
 *
 * @author Liam
 */
public class GameEntitySpawnErrorException extends Exception {

    /**
     * @param message the error/cause for why the entity could not be spawned
     */
    public GameEntitySpawnErrorException(String message) {
        super(message);
    }

}
