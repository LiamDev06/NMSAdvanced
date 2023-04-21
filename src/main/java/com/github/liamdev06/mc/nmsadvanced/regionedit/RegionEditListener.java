package com.github.liamdev06.mc.nmsadvanced.regionedit;

import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 */
@AutoRegister(type = AutoRegistry.Type.LISTENER)
public class RegionEditListener implements Listener {

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        // Player clicked left (primary location)
        if (action == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        // Player clicked right (secondary location)
        if (action == Action.RIGHT_CLICK_BLOCK) {

        }
    }
}













