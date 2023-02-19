package com.github.liamdev06.mc.nmsadvanced.pets.pathfindergoals;

import com.github.liamdev06.mc.nmsadvanced.pets.WrappedPathfinderGoal;
import com.github.liamdev06.mc.nmsadvanced.utility.metadata.Metadata;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PathfinderGoalGamePet extends WrappedPathfinderGoal {

    /**
     * How fast the pet should walk to its owner
     */
    private final double speed;

    /**
     * Maximum distance the pet should walk away from its owner
     * If the pet is further than the threshold from the owner, force teleport it to the owner
     */
    private final double navigationDistanceThreshold;

    /**
     * Pet owner
     */
    private EntityLiving owner;

    public PathfinderGoalGamePet(EntityInsentient pet, double speed, double distanceFromOwner) {
        super(pet);

        this.speed = speed;
        this.navigationDistanceThreshold = distanceFromOwner;
    }

    @Override
    protected boolean canNavigate() {
        CraftEntity bukkitEntity = this.getNavigatedEntity().getBukkitEntity();
        UUID id = bukkitEntity.getUniqueId();

        // Entity cannot navigate if it does not have an owner attached
        if (!Metadata.hasMetadata(id, "PetOwner")) {
            return false;
        }

        // Get the owner
        UUID ownerId = UUID.fromString(String.valueOf(Metadata.getMetadataValue(id, "PetOwner")));
        Player ownerPlayer = Bukkit.getPlayer(ownerId);

        // Entity cannot navigate if the owner is not available
        if (ownerPlayer == null || !ownerPlayer.isOnline()) {
            return false;
        }
        this.owner = ((CraftPlayer) ownerPlayer).getHandle();

        // If pet is out of reach from owner, teleport it to the owner
        if (!this.isWithinDistanceThreshold(this.owner, this.getNavigatedEntity(), this.navigationDistanceThreshold)) {
            this.getNavigatedEntity().setPosition(this.owner.locX, this.owner.locY, this.owner.locZ);
            return false;
        }

        // Spigot using this code to prevent mobs from being suffocated in the air
        Vec3D vector = RandomPositionGenerator.a((EntityCreature) this.getNavigatedEntity(), 16, 7, this.owner.ap());
        if (vector == null) {
            return false;
        }

        // All checks passed, navigate the pet to the owner
        return true;
    }

    @Override
    protected void onNavigationTick() {
        // Navigate to the owner on each navigation tick
        this.navigateTo(this.owner.locX, this.owner.locY, this.owner.locZ, this.speed);
    }

    @Override
    protected boolean canContinueNavigation() {
        // Navigation can continue if NavigationAbstract#m is passed (spigot using this to check) and the entity is within correct distance
        return !this.getNavigatedEntity().getNavigation().m()
                && this.isWithinDistanceThreshold(this.owner, this.getNavigatedEntity(), this.navigationDistanceThreshold);
    }

    // Nothing to perform when navigation stops
    @Override
    protected void onNavigationStop() { }

    /**
     * @param owner To compare distance to {@param entity}
     * @param entity To compare distance to {@param owner}
     * @param distanceThreshold the allowed threshold before the entity is considered not being within distance
     * @return if the distance between {@param owner} and {@param entity} do not exceed {@param distanceThreshold}
     */
    private boolean isWithinDistanceThreshold(EntityLiving owner, EntityInsentient entity, double distanceThreshold) {
        return owner.h(entity) < distanceThreshold * distanceThreshold;
    }
}











