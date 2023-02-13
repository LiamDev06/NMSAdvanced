package com.github.liamdev06.mc.nmsadvanced.customentity;

import com.github.liamdev06.mc.nmsadvanced.customentity.entities.KillerSnowman;
import com.github.liamdev06.mc.nmsadvanced.customentity.exceptions.GameEntitySpawnError;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Constructor;

/**
 * Holds all custom entities and there entity class
 * @author course but adjustments by Liam
 */
public enum GameEntity {

    KILLER_SNOWMAN(KillerSnowman.class, "Killer Snowman");

    public static final GameEntity[] VALUES = GameEntity.values();
    private final Class<? extends Entity> entityClass;
    private final String displayName;

    GameEntity(Class<? extends Entity> entityClass, String displayName) {
        this.entityClass = entityClass;
        this.displayName = displayName;
    }

    /**
     * Spawns the selected entity at the location provided using reflection and NMS
     *
     * @author Liam and course (reflection code is made by me since the course was using a library for reflection)
     * @param location the location to spawn the entity at
     * @throws GameEntitySpawnError thrown if the entity could not be spawned properly
     */
    public void spawn(Location location) throws GameEntitySpawnError {
        Entity entity;

        // Initiate the constructor for the NMS entity class using reflection
        try {
            Constructor<?> constructor = this.entityClass.getConstructor(Location.class);
            entity = (Entity) constructor.newInstance(location);
        } catch (Exception exception) {
            throw new GameEntitySpawnError("The constructor could not be initialized using reflection for entity " + this.entityClass.getSimpleName());
        }

        // Set the position and add the entity to the world
        entity.setPosition(location.getX(), location.getY(), location.getZ());
        entity.getWorld().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        // Set the custom entity name
        CraftEntity craftEntity = entity.getBukkitEntity();
        craftEntity.setCustomNameVisible(true);
        craftEntity.setCustomName(this.displayName);
    }

    /**
     * @return custom set display name of the entity
     */
    public String getDisplayName() {
        return this.displayName;
    }
}





