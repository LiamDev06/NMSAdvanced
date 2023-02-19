package com.github.liamdev06.mc.nmsadvanced.commands;

import com.github.liamdev06.mc.nmsadvanced.NMSPlugin;
import com.github.liamdev06.mc.nmsadvanced.utility.Common;
import com.github.liamdev06.mc.nmsadvanced.utility.PlayerCommand;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

/**
 * Spawns particles behind a player every tick whenever they walk by accessing NMS directly
 * This is only sent to the specified player, not to everyone
 * The command /particles toggles on and off the feature
 *
 * @author Liam and course (course was using a library for certain things that I want to avoid. I do not learn from using an API that does everytihng for me...)
 */
@AutoRegister(type = AutoRegistry.Type.COMMAND)
public class ParticlesBehindCommand extends PlayerCommand {

    private final @NonNull Map<UUID, BukkitTask> sendingParticles;

    public ParticlesBehindCommand() {
        super("particlesbehind");
        this.sendingParticles = new HashMap<>();
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        UUID uuid = player.getUniqueId();

        // Cancel the task if the player is already sending particles
        if (this.sendingParticles.containsKey(uuid)) {
            this.sendingParticles.get(uuid).cancel();
            this.sendingParticles.remove(uuid);

            // Message
            player.sendMessage(Common.color("&eStopped spawning particles!"));
            return;
        }

        // Start sending particles
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(NMSPlugin.getInstance(), () -> {
            this.spawnParticlesBehindPlayer(player);
        }, 0, 1);

        // Store that the player is sending particles
        this.sendingParticles.put(uuid, task);

        // Message
        player.sendMessage(Common.color("&aYou will now have particles spawned behind you"));
    }

    /**
     * Sends a client-bound packet (server -> client) to the target player
     * The packet is a WorldParticles packet and specifies things like which particle, location to spawn, amount and more
     *
     * @param player the player to spawn the particles for and behind
     */
    private void spawnParticlesBehindPlayer(Player player) {
        // Calculate location to spawn particles behind the player
        Vector direction = player.getLocation().getDirection(); // get the player's direction vector
        direction.setY(0).normalize() // get rid of the Y component as we do not want to modify it and normalize after
            .multiply(-0.5); // make the vector's length -0.5 to make X and Z offset be just behind the player

        // Add the updated vector direction to the location to spawn particles at
        Location location = player.getLocation().add(direction);

        // Create the packet
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                EnumParticle.VILLAGER_HAPPY, // particle type
                false, // long distance or not (if the particle is spawned from more than 256 blocks from the player)
                (float) location.getX(), // x position of particle spawn
                (float) (location.getY() + 1), // y position of particle spawn
                (float) location.getZ(), // z position of particle spawn
                0, // x offset (for "randomness")
                0, // y offset (for "randomness")
                0, // z offset (for "randomness")
                0, // max speed
                3 // particle amount/count
        );

        // Send the packet via the entity player's player connection
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.playerConnection.sendPacket(packet);
    }

}

















