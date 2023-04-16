package com.github.liamdev06.mc.nmsadvanced.entity.entities;

import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

/**
 * Custom-made entity using direct NMS access
 * This is a snowman that has been customized to follow a player and shoot snowballs at them
 *
 * @author adjustments by Liam
 */
@AutoRegister(type = AutoRegistry.Type.CUSTOM_ENTITY, entityId = 97)
public class KillerSnowman extends EntitySnowman {

    private static final double HIT_DAMAGE = 1.3;

    public KillerSnowman(Location location) {
        super(((CraftWorld) location.getWorld()).getHandle());

        // Set the entities goal to perform a melee attack
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0, true));

        // Set the target selector to target players
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));

        // Make the entity de-spawn when it is far away
        this.persistent = false;
    }

    /**
     * Code taken from skeleton shooting implemented to this snowman to make the snowman shoot snowball with the same logic
     */
    @Override
    public void a(EntityLiving entityliving, float f) {
        EntitySnowball entitySnowball = new EntitySnowball(this.world, this);

        // Calculate x, y, z
        double posX = entityliving.locX - this.locX; // Calculate relative X
        double posY = (entityliving.locY + entityliving.getHeadHeight() - 1.100000023841858) - entitySnowball.locY;
        double posZ = entityliving.locZ - this.locZ; // Calculate relative Z
        float f2 = MathHelper.sqrt(posX * posX + posZ * posZ) * 0.2f;

        // Shoot the snowball
        entitySnowball.shoot(posX, posY + f2, posZ, 1.6f, 12.0f);

        // Spawn the snowball
        this.world.addEntity(entitySnowball);

        // If the entity hit was a player, make it take some damage by subtracting the HIT_DAMAGE from the current damage
        if (entityliving instanceof EntityPlayer) {
            Player player = ((EntityPlayer) entityliving).getBukkitEntity();
            double newHealth = player.getHealth() - HIT_DAMAGE;

            // Check if the new health would mean that the player is dead
            if (newHealth <= 0) {
                player.setHealth(0);
                return;
            }

            // Modify the player's health to the new damaged value
            player.setHealth(newHealth);
        }
    }
}














