package com.github.liamdev06.mc.nmsadvanced.utility;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SoundHelper {

    /**
     * Play a sound to a player with default volume and pitch
     *
     * @param player the player to play the sound to
     * @param sound the sound to play
     */
    public static void playSound(Player player, Sound sound) {
        if (player == null) {
            return;
        }

        player.playSound(player.getLocation(), sound, 1, 1);
    }

    /**
     * Play a sound to which player's UUID was specified with default volume and pitch
     *
     * @param uuid the uuid of the player to play the sound to
     * @param sound the sound to play
     */
    public static void playSound(UUID uuid, Sound sound) {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            player.playSound(player.getLocation(), sound, 1, 1);
        }
    }

    /**
     * Play a default sound by selecting a sound from the {@link DefaultSound} enum
     * Depending on which the enum value is, a different sound with different volume and pitch is played
     *
     * @param player the player to play the sound for
     * @param sound the default sound to play
     */
    public static void playDefaultSound(Player player, DefaultSound sound) {
        if (player == null) {
            return;
        }

        // Default values
        Sound bukkitSound = null;
        double volume = 1;
        double pitch = 1;

        // Implement default sounds
        switch (sound) {
            case CLICK:
                bukkitSound = Sound.CLICK;
                break;
            case ERROR:
                bukkitSound = Sound.ENDERMAN_TELEPORT;
                volume = 11;
                pitch = -2;
                break;
        }

        // Play the sound
        if (bukkitSound != null) {
            player.playSound(player.getLocation(), bukkitSound, (float) volume, (float) pitch);
        }
    }

    /**
     * Enum with different default sounds used in {@link SoundHelper#playDefaultSound(Player, DefaultSound)}
     */
    public enum DefaultSound {
        CLICK,
        ERROR
    }
}
