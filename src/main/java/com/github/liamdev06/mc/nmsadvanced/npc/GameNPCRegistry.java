package com.github.liamdev06.mc.nmsadvanced.npc;

import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Holds and stores all NPCs and loads them from the data file stored on the disk
 * @author Liam and course
 */
public class GameNPCRegistry extends YamlConfiguration {

    // TODO: Not fully complete yet meaning it does not work 100% properly. Still some fixes left

    /**
     * A list of all NPCs that exists within the storage
     * In this case, the storage is the data.yml file
     */
    private final @NonNull List<GameNPC> npcStorage;

    /**
     * A list of all NPCs that are loaded in and currently spawned
     * This is not the same as {@link #npcStorage} since the storage has all
     * NPCs regardless if they are loaded in or not
     */
    private final @NonNull List<GameNPC> loadedNpcs;

    private final @NonNull File file;

    public GameNPCRegistry(@NonNull File file) throws Exception {
        this.file = file;
        this.load(file);
        this.npcStorage = new ArrayList<>();
        this.loadedNpcs = new ArrayList<>();

        // Load in all NPCs if it exists within the data tore
        for (String serializedNpc : this.getStringList("saved_npcs")) {
            // Deserialize and create the NPC from the serialized string
            GameNPC npc = GameNPCSerializer.GSON.fromJson(serializedNpc, GameNPC.class);

            // Add the NPC
            this.npcStorage.add(npc);
        }
    }

    public void saveAll() {
        List<String> npcList = this.getStringList("saved_npcs");
        if (npcList == null) {
            npcList = new ArrayList<>();
        }

        try {
            for (GameNPC npc : this.loadedNpcs) {
                String json = GameNPCSerializer.GSON.toJson(npc, GameNPC.class);
                npcList.add(json);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        this.set("saved_npcs", npcList);
        try {
            this.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void register(GameNPC npc) {
        if (this.isLoaded(npc)) {
            return;
        }

        this.loadedNpcs.add(npc);
    }

    public boolean isLoaded(GameNPC npc) {
        return npc.isSpawned() && npc.getNpcEntity() != null && this.isLoaded(npc.getNpcEntity().getUniqueID());
    }

    public boolean isLoaded(UUID uuid) {
        for (GameNPC npc : this.loadedNpcs) {
            if (npc.isSpawned() && npc.getNpcEntity() != null && npc.getNpcEntity().getUniqueID().equals(uuid)) {
                return true;
            }
        }

        return false;
    }

    public List<GameNPC> getLoadedNpcs() {
        return Collections.unmodifiableList(this.loadedNpcs);
    }

    public List<GameNPC> getNpcStorage() {
        return Collections.unmodifiableList(this.npcStorage);
    }
}













