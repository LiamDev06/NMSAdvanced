package me.liamhbest.nmsadvanced.commands;

import me.liamhbest.nmsadvanced.utility.Common;
import me.liamhbest.nmsadvanced.utility.PlayerCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class ReflectionPingCommand extends PlayerCommand {

    public ReflectionPingCommand() {
        super("reflectionping");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        int ping = -1;

        try {
            // Get the player's handle (NMS entity player)
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);

            // Get the ping field
            Field pingField = entityPlayer.getClass().getField("ping");

            // Make the field accessible, if the field is private it will not work otherwise
            pingField.setAccessible(true);

            // Get the content of the field for the player
            ping = (int) pingField.get(entityPlayer);
        } catch (ReflectiveOperationException exception) {
            exception.printStackTrace();
        }

        if (ping != -1) {
            player.sendMessage(Common.color("&aYour ping is: &6" + ping));
        } else {
            player.sendMessage(Common.color("&cSomething went wrong fetching your ping, see the console!"));
        }
    }
}
