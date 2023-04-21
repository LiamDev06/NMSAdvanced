package com.github.liamdev06.mc.nmsadvanced.utility;

import org.bukkit.Location;

/**
 * Hold two locations and together, they make up a region
 *
 * @author Liam
 */
public class LocationRegion {

    private final Location primaryLocation, secondaryLocation;

    public LocationRegion(Location primaryLocation, Location secondaryLocation) {
        this.primaryLocation = primaryLocation;
        this.secondaryLocation = secondaryLocation;
    }

    public Location getPrimaryLocation() {
        return this.primaryLocation;
    }

    public Location getSecondaryLocation() {
        return this.secondaryLocation;
    }
}
