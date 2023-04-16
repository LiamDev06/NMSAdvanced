package com.github.liamdev06.mc.nmsadvanced.entity;

import com.github.liamdev06.mc.nmsadvanced.entity.exceptions.GameEntitySpawnErrorException;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * Makes the custom entities persistent by checking for all game entities in a chunk on its load,
 * and updating them to be our custom entity if they have the correct metadata
 * <p>
 * For example, on server reload, all still living custom entities with custom properties would
 * get swapped to default Minecraft (NMS base) ones with default properties. By doing this, we find entities who were
 * our custom, delete them, and spawn in a new identical custom one
 */
@AutoRegister(type = AutoRegistry.Type.LISTENER)
public class EntityListener implements Listener {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {

            // Check if the entity in the chunk is marked with CustomEntity metadata, e.g. it is a custom entity
            if (!entity.hasMetadata("CustomEntity")) {
                continue;
            }

            // Get the game entity from the metadata
            GameEntity gameEntity = GameEntity.valueOf(entity.getMetadata("CustomEntity").get(0).asString());

            Location oldLocation = entity.getLocation().clone();
            LivingEntity livingEntity = (LivingEntity) entity;
            final double oldHealth = livingEntity.getHealth();
            final Entity oldPassenger = livingEntity.getPassenger();
            final Entity oldLeashHolder = livingEntity.getLeashHolder();

            // Remove the old entity
            entity.remove();

            try {
                // Swap in new custom entity to update the regular snowman with custom one
                LivingEntity transformed = (LivingEntity) gameEntity.spawn(oldLocation);
                transformed.setHealth(oldHealth);
                transformed.setPassenger(oldPassenger);
                transformed.setLeashHolder(oldLeashHolder);

            } catch (GameEntitySpawnErrorException exception) {
                exception.printStackTrace();
                continue;
            }
        }
    }
}
