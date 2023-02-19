package com.github.liamdev06.mc.nmsadvanced.commands;

import com.github.liamdev06.mc.nmsadvanced.utility.PlayerCommand;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import com.github.liamdev06.mc.nmsadvanced.utility.Common;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Get the player's ping using Reflection NMS access
 */
@AutoRegister(type = AutoRegistry.Type.COMMAND)
public class ReflectionPingCommand extends PlayerCommand {

    public ReflectionPingCommand() {
        super("reflectionping");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        int ping = -1;

        try {
            // Get the NMS entity player (player's handle)
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);

            // Get the ping field
            Field pingField = entityPlayer.getClass().getField("ping");

            // Make the field accessible, if the field is private it will not work unless acciessible is set to true
            pingField.setAccessible(true);

            // Get the content of the field for the player
            ping = (int) pingField.get(entityPlayer);
        } catch (ReflectiveOperationException exception) {
            exception.printStackTrace();
        }

        // Something went wrong when fetching the ping, the ping value is still -1
        if (ping == -1) {
            player.sendMessage(Common.color("&cSomething went wrong fetching your ping, see the console!"));
            return;
        }

        // Pinged fetched without problems, send it
        player.sendMessage(Common.color("&aYour ping is: &6" + ping));
    }
}
