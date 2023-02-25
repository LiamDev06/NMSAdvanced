package com.github.liamdev06.mc.nmsadvanced.utility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.ChatColor;
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
                URLConnection connection = new URL(endPoint).openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String rawJson = reader.lines().collect(Collectors.joining(""));

                // Return the json
                return new JsonParser().parse(rawJson).getAsJsonObject();
            } catch (Exception ignored) {
                return null;
            }
        });
    }
}










