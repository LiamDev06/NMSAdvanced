package com.github.liamdev06.mc.nmsadvanced.npc;

import com.github.liamdev06.mc.nmsadvanced.utility.Common;
import com.google.gson.*;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Type;

/**
 * Serializes the GameNPC, so it can be stored in a data file
 */
public class GameNPCSerializer implements JsonSerializer<GameNPC> {

    public static final @NonNull Gson GSON = new GsonBuilder()
            .registerTypeAdapter(GameNPC.class, new GameNPCSerializer())
            .create();

    @Override
    public JsonElement serialize(GameNPC npc, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject json = new JsonObject();
        json.addProperty("name", npc.getName());
        json.addProperty("lookingAtPlayer", npc.isLookingAtPlayer());

        String skin = npc.getSkin();
        if (skin != null) {
            json.addProperty("skin", skin);
        }

        Location location = npc.getLocation();
        if (location != null) {
            json.addProperty("spawnLocation", Common.writeLocation(location));
        }
        return json;
    }
}
