package com.github.liamdev06.mc.nmsadvanced.utility;

import com.github.liamdev06.mc.nmsadvanced.utility.exceptions.InvalidLocationParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    /**
     * Performs an async HTTP request from the provided end point. By making it async it runs on another thread and does not
     * disturb anything happening on the main thread.
     *
     * @param endPoint the URL to the http endpoint
     * @return a {@link JsonObject} of the full json returned from the request
     */
    public static CompletableFuture<JsonObject> getJsonFromHttpRequest(String endPoint) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Open a connection to the end point
                URLConnection connection = new URL(endPoint).openConnection();

                // Read the input stream as raw json
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String rawJson = reader.lines().collect(Collectors.joining(""));

                // Return the json
                return new JsonParser().parse(rawJson).getAsJsonObject();
            } catch (Exception ignored) {
                return null;
            }
        });
    }

    /**
     * Write a bukkit location to a string to be able to save it
     *
     * @param location the location to write
     * @return a string location in the format of "X,Y,Z,YAW,PITCH" for example "23.2,-5,132,4.4,23.7"
     */
    public static String writeLocation(Location location) {
        // Location is null and cannot be parsed
        if (location == null) {
            return "";
        }

        // Return the location in the specific format
        return location.getWorld().getName() + ","  + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    /**
     * Parses a location from the location string format used in {@link Common#writeLocation(Location)} to a bukkit location
     *
     * @param inputLocation the string with the correct format to parse
     * @return a bukkit location from the {@param inputLocation} string
     */
    public static Location parseLocation(String inputLocation) throws InvalidLocationParseException {
        String[] args = inputLocation.split("\\,");
        if (args.length == 0) {
            throw new InvalidLocationParseException("The input location does not contain any values (arguments)!");
        }

        // Get the world
        World world = Bukkit.getWorld(args[0]);
        if (world == null) {
            throw new InvalidLocationParseException("The world with name " + args[0] + " is not a bukkit world!");
        }

        // Read X Y Z
        if (args.length == 4) {
            return new Location(
                    world,
                    Double.parseDouble(args[1]),
                    Double.parseDouble(args[2]),
                    Double.parseDouble(args[3])
            );
        }

        // Read X Y Z YAW PITCH
        return new Location(
                world,
                Double.parseDouble(args[1]),
                Double.parseDouble(args[2]),
                Double.parseDouble(args[3]),
                Float.parseFloat(args[4]),
                Float.parseFloat(args[5])
        );
    }
}










