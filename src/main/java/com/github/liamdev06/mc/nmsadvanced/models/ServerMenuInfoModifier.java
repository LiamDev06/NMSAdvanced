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

    //Todo: does not work :/

    public ServerMenuInfoModifier(NMSPlugin plugin, ProtocolManager protocolManager) {
        // Listen for the server info packet using Protocol Lib
        protocolManager.addPacketListener(new PacketAdapter(
                plugin,
                ListenerPriority.HIGH,
                PacketType.Status.Server.OUT_SERVER_INFO) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                // Get Protocol Lib's wrapped version of the server ping by reading the first field in the array
                WrappedServerPing ping = event.getPacket().getServerPings().read(0);

                // Modify information to random values
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

    private List<WrappedGameProfile> compileHoverText(String... lines) {
        final List<WrappedGameProfile> profiles = new ArrayList<>();

        for (String line : lines) {
            WrappedGameProfile gameProfile = new WrappedGameProfile(UUID.randomUUID(), Common.color(line));
            profiles.add(gameProfile);
        }

        return profiles;
    }
}













