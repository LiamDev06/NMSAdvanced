package com.github.liamdev06.mc.nmsadvanced.commands;

import com.github.liamdev06.mc.nmsadvanced.NMSPlugin;
import com.github.liamdev06.mc.nmsadvanced.npc.GameNPC;
import com.github.liamdev06.mc.nmsadvanced.npc.GameNPCRegistry;
import com.github.liamdev06.mc.nmsadvanced.utility.Common;
import com.github.liamdev06.mc.nmsadvanced.utility.PlayerCommand;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Spawn a custom npc with a specified name and selected if it should be displayed in the tab list or not
 * @author Liam
 */
@AutoRegister(type = AutoRegistry.Type.COMMAND)
public class GameNPCSpawnCommand extends PlayerCommand {

    public GameNPCSpawnCommand() {
        super("gamenpcspawn", "npcspawn", "spawnnpc");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (args.length <= 2) {
            player.sendMessage(Common.color("&c&lMISSING ARGUMENTS! &cProvide an entity using: &6/npcspawn <npc_name> <skin> <show_in_tab>&c."));
            return;
        }

        final String npcName = Common.color(args[0].trim()).replace("%%", " ");
        final String skinName = args[1].trim();
        boolean showInTab;

        if (npcName.length() > 16) {
            player.sendMessage(Common.color("&c&lMAX LENGTH! &cThe maximum NPC name length are 16 characters. Your name of &6" + npcName.length() + "&c chars exceeds this."));
            return;
        }

        try {
            showInTab = Boolean.parseBoolean(args[2]);
        } catch (Exception exception) {
            player.sendMessage(Common.color("&c&lINVALID BOOLEAN! &cInvalid boolean input of value &6<show_in_tab>&c."));
            return;
        }

        // Create the NPC
        GameNPC npc = new GameNPC(npcName);

        // Spawn the npc and add the spawning player as a viewer
        npc.spawn(player.getLocation());
        npc.showAll(showInTab);
        npc.setSkin(skinName, showInTab);
        this.applyLookAtPlayer(npc, player);

        GameNPCRegistry registry = NMSPlugin.getInstance().getNpcRegistry();
        if (registry != null) {
            registry.register(npc);
        }

        // Done
        player.sendMessage(Common.color("&aYou spawned the NPC '&f" + npcName + "&a' for all players."));
    }

    private void applyLookAtPlayer(final GameNPC npc, final Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                npc.setLookingAt(player.getLocation());
            }
        }.runTaskTimer(NMSPlugin.getInstance(), 0, 5);
    }
}
















