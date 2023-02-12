package com.github.liamdev06.mc.nmsadvanced.commands;

import com.github.liamdev06.mc.nmsadvanced.utility.Common;
import com.github.liamdev06.mc.nmsadvanced.utility.PlayerCommand;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Send action bars and titles by accessing NMS directly
 *
 * @author Liam, not from course.
 */
@AutoRegister(type = AutoRegistry.Type.COMMAND)
public class DisplayCommand extends PlayerCommand {

    public DisplayCommand() {
        super("display");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Common.color("&c&lMISSING ARGS! &cUse: &6/display <type> <message...>&c." + "\n" +
                    "&cThe available types are: &6actionbar&c, &6title&c."));
            return;
        }

        final String type = args[0].toLowerCase();
        final String message = Common.color(this.getMessage(args));

        switch (type) {
            case "actionbar": {
                this.sendActionBar(player, message);
                break;
            }
            case "title": {
                this.sendTitle(player, message);
                break;
            }
            default: {
                player.sendMessage(Common.color("&cInvalid display type of '&6" + type + "&c'."));
            }
        }
    }

    /**
     * Sends a client-bound packet (server -> client) to the target player
     * Packet is a chat packet with data type 2 which sets the chat type to action bar
     *
     * @param player the player to send the action bar to
     * @param message the message to display on the action bar
     */
    private void sendActionBar(Player player, String message) {
        // Create the packet
        IChatBaseComponent component = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(component, (byte) 2);

        // Send the packet
        this.sendPacket(player, packet);
    }

    /**
     * Sends 2 a client-bound packets (server -> client) to the target player.
     * One packet sends the title and the other sends a subtitle (if a subtitle was provided in the message).
     * <p>
     * Packet 1: title action enum set to TITLE
     * Packet 2: title action enum set to SUBTITLE
     *
     * @param player the player to send the title to
     * @param message the message to display on the title and subtitle (split by the char "|")
     */
    private void sendTitle(Player player, String message) {
        // Split into title and subtitle
        String[] split = message.split("\\|");
        String title = split[0];
        String subtitle = split.length == 1 ? "" : split[1];
        final int[] ticks = { 20, 40, 20 };

        // Create the title packet and send it
        IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleComponent, ticks[0], ticks[1], ticks[2]);
        this.sendPacket(player, titlePacket);

        // Create the subtitle packet and send it
        if (!subtitle.equals("")) {
            IChatBaseComponent subtitleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleComponent, ticks[0], ticks[1], ticks[2]);
            this.sendPacket(player, subtitlePacket);
        }
    }

    /**
     * Takes all command arguments and puts them together in one string
     * It also excludes the first argument since that is the type of display
     *
     * @param args all command arguments
     * @return message to display from arguments
     */
    private String getMessage(String[] args) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            if (i >= 1) {
                builder.append(args[i]).append(" ");
            }
        }

        return builder.toString().trim();
    }

    /**
     * Send a packet using direct NMS.
     * Get the NMS player (EntityPlayer) by getting the CraftPlayer's handle
     * Use the player connection and send a packet
     *
     * @param player the player to send the packet to
     * @param packet the packet to send
     */
    private void sendPacket(Player player, Packet<?> packet) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.playerConnection.sendPacket(packet);
    }
}
















