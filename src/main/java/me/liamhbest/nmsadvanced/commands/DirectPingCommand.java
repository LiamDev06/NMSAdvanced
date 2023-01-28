package me.liamhbest.nmsadvanced.commands;

import me.liamhbest.nmsadvanced.utility.Common;
import me.liamhbest.nmsadvanced.utility.PlayerCommand;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class DirectPingCommand extends PlayerCommand {

    /**
     * Displays the player's ping in milliseconds using direct NMS.
     */

    public DirectPingCommand() {
        super("directping");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        // Get the NMS version of the player
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        // Get the ping
        int ping = entityPlayer.ping;

        // Send the ping
        player.sendMessage(Common.color("&aYour ping is: &6" + ping));
    }
}



















