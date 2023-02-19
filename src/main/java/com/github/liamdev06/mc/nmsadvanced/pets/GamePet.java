package com.github.liamdev06.mc.nmsadvanced.pets;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftWolf;
import org.bukkit.entity.Entity;

/**
 * Holds all custom pets, their entity class and pet specific data
 * @author Liam
 */
public enum GamePet {

    FRIENDLY_WOLF(CraftWolf.class, "Friendly Wolf", ChatColor.LIGHT_PURPLE, 1.1, 20);

    private final Class<? extends Entity> entityClass;
    private final String displayName;
    private final ChatColor nameColor;
    private final double speed, distanceThreshold;

    public static final GamePet[] VALUES = GamePet.values();

    GamePet(Class<? extends CraftEntity> entityClass, String displayName, ChatColor nameColor, double speed, double distanceThreshold) {
        this.entityClass = entityClass;
        this.displayName = displayName;
        this.nameColor = nameColor;
        this.speed = speed;
        this.distanceThreshold = distanceThreshold;
    }

    /**
     * @return the class of the bukkit pet entity
     */
    public Class<? extends Entity> getEntityClass() {
        return this.entityClass;
    }

    /**
     * @return display name/friendly name of the pet
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * @return display name/friendly name with its bukkit chat color
     */
    public String getColoredDisplayName() {
        return this.nameColor + this.displayName;
    }

    /**
     * @param playerName the name of the player to include in the display name
     * @return display name/friendly name adopted with the player's name first which results in: "PlayerName's Epic Pet"
     */
    public String getPlayerIncludedDisplayName(String playerName) {
        return this.nameColor + playerName + "'s " + this.displayName;
    }

    /**
     * @return the bukkit chat color used in the display name
     */
    public ChatColor getNameColor() {
        return this.nameColor;
    }

    /**
     * @return the speed the pathfinder goal will use to navigate the entity
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * @return the maximum allowed distance between the pet and its owner before it is force teleported to its owner
     */
    public double getDistanceThreshold() {
        return this.distanceThreshold;
    }
}
