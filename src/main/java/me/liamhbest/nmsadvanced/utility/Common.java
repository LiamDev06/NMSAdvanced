package me.liamhbest.nmsadvanced.utility;

import org.bukkit.ChatColor;

public class Common {

    public static String color(String input) {
        // Regular bukkit coloring
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }
}
