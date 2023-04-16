package com.github.liamdev06.mc.nmsadvanced.entity;

import com.github.liamdev06.mc.nmsadvanced.NMSPlugin;
import com.github.liamdev06.mc.nmsadvanced.entity.entities.KillerSnowman;
import com.github.liamdev06.mc.nmsadvanced.entity.exceptions.GameEntitySpawnErrorException;
import com.github.liamdev06.mc.nmsadvanced.utility.metadata.Metadata;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Constructor;

/**
 * Holds all custom entities and there entity class
 *
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
     * @throws GameEntitySpawnErrorException thrown if the entity could not be spawned properly
     */
    public CraftEntity spawn(Location location) throws GameEntitySpawnErrorException {
        Entity entity;

        try {
            // Initialize the constructor for the NMS entity class using reflection
            Constructor<?> constructor = this.entityClass.getConstructor(Location.class);
            entity = (Entity) constructor.newInstance(location);
        } catch (Exception exception) {
            throw new GameEntitySpawnErrorException("The constructor could not be initialized using reflection for entity " + this.entityClass.getSimpleName());
        }

        // Set the position and add the entity to the world
        entity.setPosition(location.getX(), location.getY(), location.getZ());
        entity.getWorld().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        // Set the custom entity name
        CraftEntity craftEntity = entity.getBukkitEntity();
        craftEntity.setCustomNameVisible(true);
        craftEntity.setCustomName(this.displayName);

        // Add metadata to know this is a custom mob
        craftEntity.setMetadata("CustomEntity", new FixedMetadataValue(NMSPlugin.getInstance(), this.name()));

        return entity.getBukkitEntity();
    }

    /**
     * @return custom set display name of the entity
     */
    public String getDisplayName() {
        return this.displayName;
    }
}





