package com.github.liamdev06.mc.nmsadvanced.utility;

import org.bukkit.ChatColor;

/**
 * Set of basic utilities and helpers
 *
 * @author Liam, not from course.
 */
public class Common {

    public static String color(String input) {
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }
}
