package com.github.liamdev06.mc.nmsadvanced.utility;

import org.bukkit.ChatColor;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Set of basic utilities and helpers
 *
 * @author Liam
 */
public class Common {

    /**
     * Color a string with regular built-in bukkit colors using '&' color codes
     *
     * @param input a string that includes color codes to color
     * @return the {@param input} string but colored
     */
    public static String color(@NonNull String input) {
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }
}
