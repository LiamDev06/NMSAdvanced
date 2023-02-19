package com.github.liamdev06.mc.nmsadvanced.models;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.github.liamdev06.mc.nmsadvanced.NMSPlugin;
import com.github.liamdev06.mc.nmsadvanced.utility.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Modify the server menu info by intercepting the PacketStatusOutServerInfo packet
 * This example in particular uses ProtocolLib to listen for the packet
 */
public class ServerMenuInfoModifier {

    public ServerMenuInfoModifier(NMSPlugin plugin, ProtocolManager protocolManager) {
        // Listen using ProtocolLib for when the server sends the packet 'PacketStatusOutServerInfo' to the client
        protocolManager.addPacketListener(new PacketAdapter(
                plugin,
                ListenerPriority.HIGH,
                PacketType.Status.Server.OUT_SERVER_INFO) { // the deprecated method is needed for 1.8 compatibility... :/
            @Override
            public void onPacketSending(PacketEvent event) {
                // Get ProtocolLib's wrapped version of the server ping by reading the first field in the array
                WrappedServerPing ping = event.getPacket().getServerPings().read(0);

                // Modify information by setting other values (just random values for the example)
                ping.setMotD(Common.color("&cI have control of this menu!"));
                ping.setPlayersOnline(20403);
                ping.setPlayersMaximum(200000);
                ping.setPlayers(compileHoverText(
                        "&aWelcome to this server!",
                        "&eWow... this is another line",
                        "&cI am cool, so I'm adding a third!"
                ));
            }
        });
    }

    /**
     * Takes the string input lines and makes them into a game profile with the same content as the string line
     * The {@link WrappedServerPing} only accepts {@link WrappedGameProfile} when setting the players list hence why this is necessary
     *
     * @param lines the string input lines
     * @return {@param lines} as {@link WrappedGameProfile}s instead
     */
    private List<WrappedGameProfile> compileHoverText(String... lines) {
        final List<WrappedGameProfile> profiles = new ArrayList<>();

        // Loop through all lines and create a WrappedGameProfile object for each with a random UUID and the provided colored string line
        for (String line : lines) {
            WrappedGameProfile gameProfile = new WrappedGameProfile(UUID.randomUUID(), Common.color(line));
            profiles.add(gameProfile);
        }

        return profiles;
    }
}













