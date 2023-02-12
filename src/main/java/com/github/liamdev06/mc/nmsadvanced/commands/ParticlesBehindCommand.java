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
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

/**
 * Spawns particles behind a player wherever they walk by accessing NMS directly
 * This is only sent to the specified player, not to everyone
 * The command /particles toggles on and off the feature
 *
 * @author Liam and course (course was using a library for certain things that I want to avoid. I do not learn from using an API that does everytihng for me...)
 */
@AutoRegister(type = AutoRegistry.Type.COMMAND)
public class SpawnParticlesCommand extends PlayerCommand {

    private static final Random RANDOM = new Random();
    private final @NonNull Map<UUID, BukkitTask> sendingParticles;

    public SpawnParticlesCommand() {
        super("spawnparticles");
        this.sendingParticles = new HashMap<>();
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        UUID uuid = player.getUniqueId();

        // Cancel the task if the player is sending particles
        if (this.sendingParticles.containsKey(uuid)) {
            BukkitTask task = this.sendingParticles.get(uuid);
            task.cancel();

            this.sendingParticles.remove(uuid);
            player.sendMessage(Common.color("&eStopped spawning particles!"));
            return;
        }

        // Start sending particles
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(NMSPlugin.getInstance(), () -> {
            this.spawnParticles(player, player.getLocation());
        }, 0, 1);

        this.sendingParticles.put(uuid, task);
        player.sendMessage(Common.color("&aYou will now have particles spawned behind you"));
    }

    /**
     * Sends a client-bound packet (server -> client) to the target player
     * The packet is a WorldParticles packet and specifies things like which particle, location to spawn, amount and more
     *
     * @param player the player to spawn the particles for
     * @param location the location to spawn the particle at
     */
    private void spawnParticles(Player player, Location location) {
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

        // Send the packet
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.playerConnection.sendPacket(packet);
    }

}

















