package com.github.liamdev06.mc.nmsadvanced.utility.metadata;

import java.util.*;

/**
 * Metadata class to store temporary local data and values on any object with a {@link java.util.UUID}
 *
 * @author Liam
 */
public class Metadata {

    private static final Map<UUID, List<MetadataValue>> STORE = new HashMap<>();

    /**
     * Set the metadata using key-value for any object.
     * If the object already has metadata, it will override it with the new
     *
     * @param uuid the uuid of the object (player, mob, and so on)
     * @param key metadata key
     * @param value metadata value
     */
    public static void setMetadata(UUID uuid, String key, Object value) {
        STORE.putIfAbsent(uuid, new ArrayList<>());

        // Update list with new metadata value
        List<MetadataValue> list = STORE.get(uuid);
        list.add(new MetadataValue(key, value));

        STORE.put(uuid, list);
    }

    /**
     * Remove the metadata with the specified key from an object
     *
     * @param uuid the uuid of the object (player, mob, and so on)
     * @param key the metadata key to remove
     */
    public static void removeMetadata(UUID uuid, String key) {
        if (!STORE.containsKey(uuid)) {
            return;
        }

        // Loop through list and remove metadata value
        List<MetadataValue> list = STORE.get(uuid);
        for (int i = 0; i < list.size(); i++) {
            MetadataValue meta = list.get(i);

            if (meta.getKey().equals(key)) {
                list.remove(i);
                break;
            }
        }

        STORE.put(uuid, list);
    }

    /**
     * @param uuid the uuid of the object (player, mob, and so on)
     * @param key the key to check the metadata for
     * @return if the target object has a metadata value with the key provided
     */
    public static boolean hasMetadata(UUID uuid, String key) {
        if (!STORE.containsKey(uuid)) {
            return false;
        }

        // Find metadata value
        for (MetadataValue meta : STORE.get(uuid)) {
            if (meta.getKey().equals(key)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param uuid the uuid of the object (player, mob, and so on)
     * @param key the key to return the value for since its paired key-value
     * @return the metadata value for the object provided with the specified key
     */
    public static Object getMetadataValue(UUID uuid, String key) {
        if (!hasMetadata(uuid, key)) {
            return null;
        }

        // Find and return metadata value
        for (MetadataValue meta : STORE.get(uuid)) {
            if (meta.getKey().equals(key)) {
                return meta.getValue();
            }
        }

        return null;
    }
}
