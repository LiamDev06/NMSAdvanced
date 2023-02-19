package com.github.liamdev06.mc.nmsadvanced.commands;

import com.github.liamdev06.mc.nmsadvanced.utility.PlayerCommand;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import com.github.liamdev06.mc.nmsadvanced.utility.Common;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Get the player's ping using direct NMS access
 */
@AutoRegister(type = AutoRegistry.Type.COMMAND)
public class DirectPingCommand extends PlayerCommand {

    public DirectPingCommand() {
        super("directping");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        // Get the NMS version of the player
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        // Get the ping (ping field)
        int ping = entityPlayer.ping;

        // Send the ping as a player message
        player.sendMessage(Common.color("&aYour ping is: &6" + ping));
    }
}



















