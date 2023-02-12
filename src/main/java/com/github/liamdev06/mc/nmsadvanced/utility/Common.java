package com.github.liamdev06.mc.nmsadvanced.utility;

import org.bukkit.ChatColor;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Set of basic utilities and helpers
 *
 * @author Liam, not from course.
 */
public class Common {

    public static String color(@NonNull String input) {
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }
}
