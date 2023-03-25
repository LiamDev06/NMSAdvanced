package com.github.liamdev06.mc.nmsadvanced.npc;

import com.github.liamdev06.mc.nmsadvanced.NMSPlugin;
import com.github.liamdev06.mc.nmsadvanced.utility.Common;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Custom NPC using direct NMS access to spawn an NPC
 * The class holds methods for spawning, look at a player, show and hide the NPC, set skin and more.
 * @author Liam and course
 */
public final class GameNPC implements Serializable {

    public @NonNull String name;
    public @Nullable String skin;
    public Location spawnLocation;
    public boolean lookingAtPlayer;
    public @Nullable EntityPlayer npc;
    public @NonNull Set<UUID> viewers;

    public GameNPC(@NonNull String name) {
        this.name = name;
        this.viewers = new HashSet<>();
        this.lookingAtPlayer = false;
    }

    /**
     * Spawns this NPC at the provided location by creating a new NMS entity player
     *
     * @param location the location to spawn the NPC at
     */
    public void spawn(Location location) {
        // If the spawn location is not null, force override to have the NPC at the stored location
        if (this.spawnLocation != null) {
            location = this.spawnLocation;
        }

        // Values for the entity player constructor
        final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        final WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), this.name);
        final PlayerInteractManager interactManager = new PlayerInteractManager(world);

        // Create the NPC by creating an NMS entity
        EntityPlayer entity = new EntityPlayer(server, world, profile, interactManager);

        // Set the NPC location to the provided spawn location
        entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        // Set npc
        this.npc = entity;
    }

    /**
     * Shows the NPC to all players currently online
     *
     * @param showInTab if the NPC should be displayed as a "player" in the tab list or not
     */
    public void showAll(boolean showInTab) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            this.show(online, showInTab);
        }
    }

    /**
     * Shows the NPC to the player provided by sending player info and entity spawn packets
     *
     * @param player the player to show the NPC to
     * @param showInTab if the NPC should be displayed as a "player" in the tab list or not
     */
    public void show(Player player, boolean showInTab) {
        // Cancel if the NPC is null
        if (this.npc == null) {
            this.warning("NPC with name '" + this.name + "' is null and cannot be spawned!");
            return;
        }

        // Send player info packet to notify the client that we added a new "player" (NPC)
        PacketPlayOutPlayerInfo playerInfoPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.npc);
        this.sendPacket(player, playerInfoPacket);

        // Send entity spawn packet to the client to have the client render the spawned NPC
        PacketPlayOutNamedEntitySpawn entitySpawnPacket = new PacketPlayOutNamedEntitySpawn(this.npc);
        this.sendPacket(player, entitySpawnPacket);

        // Send head rotation packet to correctly rotate the NPC's head so the entity is looking in the right direction provided in the spawn location
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(this.npc, this.modifiedDirection(this.npc.yaw));
        this.sendPacket(player, headRotationPacket);

        // Send a player info packet to remove the entity again if it should not be displayed in tab
        if (!showInTab) {
            Bukkit.getScheduler().runTaskLater(NMSPlugin.getInstance(), () -> {
                PacketPlayOutPlayerInfo playerInfoToRemovePacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.npc);
                this.sendPacket(player, playerInfoToRemovePacket);
            }, 30);
        }

        // Add player as a viewer
        this.viewers.add(player.getUniqueId());
    }

    /**
     * Hides the NPC to all players that are currently viewing it
     */
    public void hideAll() {
        for (UUID onlineUuid : this.viewers) {
            // Get the online player
            Player online = Bukkit.getPlayer(onlineUuid);
            if (online == null) {
                continue;
            }

            // Hide the NPC from the online player
            this.hide(online);
        }

        this.viewers.clear();
    }

    /**
     * Hides the NPC to the player provided by sending an entity destroy packet
     *
     * @param player the player to hide the NPC for
     */
    public void hide(Player player) {
        // Cancel if the NPC is null
        if (this.npc == null) {
            this.warning("NPC with name '" + this.name + "' is null and cannot be hidden!");
            return;
        }

        // The player is not viewing the NPC, do not send a packet
        final UUID uuid = player.getUniqueId();
        if (!this.viewers.contains(uuid)) {
            return;
        }

        // Send entity destroy packet to remove the NPC entity
        PacketPlayOutEntityDestroy entityDestroyPacket = new PacketPlayOutEntityDestroy(this.npc.getId());
        this.sendPacket(player, entityDestroyPacket);

        // Remove player as view
        this.viewers.remove(uuid);
    }

    /**
     * Set the NPC to look at the location provided by sending packets to modify its head rotation
     * The method calculates the appropriate yaw and pitch depending on what location is provided
     *
     * @param targetLocation the location to have the NPC look at
     */
    public void setLookingAt(Location targetLocation) {
        // Cancel if the NPC is null
        if (this.npc == null) {
            this.warning("NPC with name '" + this.name + "' is null and cannot be set to look at a location!");
            return;
        }

        // TODO: Store the last look direction and only send new packets if it changed to minimize the amount of packets being sent

        final Location entityLoc = this.npc.getBukkitEntity().getLocation();

        // Get two points and subtract them as vector to get the point where the NPC should look at
        final Vector direction = entityLoc.toVector().subtract(targetLocation.toVector()).normalize();

        // Calculate the yaw and pitch to get values Minecraft can understand from the direction
        double yaw = 180 - Math.toDegrees(Math.atan2(direction.getX(), direction.getZ()));
        double pitch = 90 - Math.toDegrees(Math.acos(direction.getY()));

        // Send a head rotation packet with the new yaw to all viewers of the NPC
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(this.npc, this.modifiedDirection(yaw));
        this.sendPacketToViewers(headRotationPacket);

        // Send an entity look packet with the new yaw and new pitch to all viewers of the NPC
        PacketPlayOutEntity.PacketPlayOutEntityLook entityLookPacket = new PacketPlayOutEntity.PacketPlayOutEntityLook(
                this.npc.getId(),
                this.modifiedDirection(yaw),
                this.modifiedDirection(pitch),
                true);
        this.sendPacketToViewers(entityLookPacket);
    }

    /**
     * Setting to change if the NPC should look at players
     *
     * @param value boolean value if the NPC should look at players
     */
    public void setLookingAtPlayer(boolean value) {
        this.lookingAtPlayer = value;
    }

    /**
     * Sets the skin of the NPC using the skin name (player name) provided
     *
     * @param skinName the name of an existing Minecraft player who's skin to set the NPC to
     */
    public void setSkin(String skinName, boolean showInTab) {
        // Cancel if the NPC is null
        if (this.npc == null) {
            this.warning("NPC with name '" + this.name + "' is null and cannot be set a skin!");
            return;
        }

        // If the skin is not null, force override to have the NPC used the stored skin
        if (this.skin != null) {
            skinName = this.skin;
        } else {
            this.skin = skinName;
        }

        // Store all old viewers of the NPC and hide the NPC
        final Set<UUID> oldViewers = new HashSet<>(this.viewers);
        this.hideAll();

        // By using this API, you can bypass Mojang's restrictions of the amount of calls to make
        String endPoint = "https://api.ashcon.app/mojang/v2/user/" + skinName;

        // Perform an async http request and get the json from it
        Common.getJsonFromHttpRequest(endPoint).thenAccept(json -> {
            // Get the value and signature from the json request
            JsonObject rawSection = json.get("textures").getAsJsonObject().get("raw").getAsJsonObject();
            String base64Value = rawSection.get("value").getAsString();
            String base64Signature = rawSection.get("signature").getAsString();

            // Set the game profile of the NPC
            this.npc.getProfile().getProperties().put("textures", new Property(
                    "textures",
                    base64Value,
                    base64Signature
            ));

            // Show the NPC again to all old viewers. This needs to be done to refresh the NPC to have the skin updated
            for (UUID oldViewerUuid : oldViewers) {
                // Get the old viewer player
                Player oldViewer = Bukkit.getPlayer(oldViewerUuid);
                if (oldViewer == null) {
                    continue;
                }

                // Show the NPC again to the target viewer
                this.show(oldViewer, showInTab);
            }
        });
    }

    /**
     * @param player the target player to check for if they are a viewer
     * @return if the specified player is currently a registered viewer to this NPC
     */
    public boolean isViewer(Player player) {
        return this.viewers.contains(player.getUniqueId());
    }

    /**
     * Loops through all current viewers of this NPC and sends them the specified packet
     *
     * @param packet the packet to send
     */
    private void sendPacketToViewers(Packet<?> packet) {
        for (UUID viewerUuid : this.viewers) {
            // Get the player from their UUID
            Player viewer = Bukkit.getPlayer(viewerUuid);
            if (viewer == null) {
                continue;
            }

            // Send the packet to the player
            this.sendPacket(viewer, packet);
        }
    }

    public Location getLocation() {
        return this.npc == null ? null : this.npc.getBukkitEntity().getLocation();
    }

    /**
     * @return the skin the NPC is set to
     */
    public @Nullable String getSkin() {
        return this.skin;
    }

    /**
     * @return the name the NPC is set to
     */
    public @NonNull String getName() {
        return this.name;
    }

    /**
     * @return a list of UUID's that are currently viewing/registered viewers of this NPC
     */
    public @NonNull Set<UUID> getViewers() {
        return this.viewers;
    }

    /**
     * @return if this NPC is not equal to null (meaning it is spawned)
     */
    public boolean isSpawned() {
        return this.npc != null;
    }

    /**
     * @return if this NPC is trying to look at nearby players
     */
    public boolean isLookingAtPlayer() {
        return this.lookingAtPlayer;
    }

    /**
     * @return this NPC as an NMS entity player
     */
    public EntityPlayer getNpcEntity() {
        return this.npc;
    }

    /**
     * @return if the NPC has spawned, this will be equal to their spawning location
     */
    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    /**
     * Set a location the NPC should spawn at when being spawned
     * This would override the location in the {@link GameNPC#spawn(Location)} method
     *
     * @param spawnLocation the location to have the NPC spawn at
     */
    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    /**
     * Send a specific packet to a specified player
     *
     * @param player the player to send the packet to
     * @param packet the packetto send
     */
    private void sendPacket(Player player, Packet<?> packet) {
        // Get the NMS player connection and send the packet
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * Modifies the yaw or pitch in a way so Minecraft can process it.
     * For more information, read the <a href="http://wiki.vg/Protocol">Minecraft protocol wiki</a> under "Player Rotation"
     *
     * @param input the input value, either a yaw or pitch
     * @return the modified version of the {@param input}
     */
    private byte modifiedDirection(double input) {
        return (byte) (input * 256 / 360);
    }

    /**
     * Log a warning to the console
     *
     * @param warning the warning without prefix to log
     */
    private void warning(String warning) {
        NMSPlugin.getInstance().getServer().getConsoleSender().sendMessage(Common.color("&c[WARNING] " + warning));
    }

    @Override
    public String toString() {
        return "GameNPC{name=" + this.name + "}";
    }
}













