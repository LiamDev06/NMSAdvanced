package com.github.liamdev06.mc.nmsadvanced.utility.metadata;

/**
 * Stores a metadata value using a pair of key-value
 */
public class MetadataValue {

    private final String key;
    private final Object value;

    public MetadataValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return key of this metadata value
     */
    public String getKey() {
        return this.key;
    }

    /**
     * @return value of this metadata value
     */
    public Object getValue() {
        return this.value;
    }
}
