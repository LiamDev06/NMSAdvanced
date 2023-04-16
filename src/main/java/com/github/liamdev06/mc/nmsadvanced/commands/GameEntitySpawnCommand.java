package com.github.liamdev06.mc.nmsadvanced.commands;

import com.github.liamdev06.mc.nmsadvanced.entity.GameEntity;
import com.github.liamdev06.mc.nmsadvanced.entity.exceptions.GameEntitySpawnErrorException;
import com.github.liamdev06.mc.nmsadvanced.utility.Common;
import com.github.liamdev06.mc.nmsadvanced.utility.PlayerCommand;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import org.bukkit.entity.Player;

/**
 * Spawn one of the custom entities found in the GameEntity enum
 */
@AutoRegister(type = AutoRegistry.Type.COMMAND)
public class GameEntitySpawnCommand extends PlayerCommand {

    public GameEntitySpawnCommand() {
        super("gameentity");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Common.color("&c&lMISSING ARGUMENTS! &cProvide an entity using: &6/gameentity <entity>&c."));
            return;
        }

        final String input = args[0].toUpperCase().trim();
        GameEntity gameEntity;

        try {
            // Get the game entity
            gameEntity = GameEntity.valueOf(input);

            // Spawn the entity
            gameEntity.spawn(player.getLocation());
            player.sendMessage(Common.color("&a&lENTITY SPAWNED! &aYou successfully spawned a &6" + gameEntity.getDisplayName() + "&a."));
        } catch (IllegalArgumentException enumException) {
            player.sendMessage(Common.color("&c&lINVALID ENTITY! &cThe entities available are: " + this.getValidEntities()));
        } catch (GameEntitySpawnErrorException entitySpawnError) {
            player.sendMessage(Common.color("&c[ADMIN DEBUG] Exception was thrown: &6" + entitySpawnError.getMessage()));
        }
    }

    /**
     * @param input the player input's name of the entity they are checking
     * @return if the input is a valid entity name
     * @author Liam
     */
    private boolean isEntityInvalid(String input) {
        for (GameEntity gameEntity : GameEntity.VALUES) {
            if (gameEntity.name().equals(input)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return a list of all game entities available in a nice format
     * @author Liam
     */
    private String getValidEntities() {
        StringBuilder builder = new StringBuilder();

        for (GameEntity entity : GameEntity.VALUES) {
            builder.append(String.format("%s (%s), ", entity.getDisplayName(), entity.name()));
        }

        return builder.substring(0, builder.length() - 2);
    }
}



















