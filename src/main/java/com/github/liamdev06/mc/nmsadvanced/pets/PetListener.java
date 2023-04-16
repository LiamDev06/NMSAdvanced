package com.github.liamdev06.mc.nmsadvanced.pets;

import com.github.liamdev06.mc.nmsadvanced.pets.pathfindergoals.PathfinderGoalGamePet;
import com.github.liamdev06.mc.nmsadvanced.utility.Common;
import com.github.liamdev06.mc.nmsadvanced.utility.SoundHelper;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import com.github.liamdev06.mc.nmsadvanced.utility.metadata.Metadata;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.UUID;

/**
 * Regular bukkit listener to listen for events related to pets
 * @author Liam and course, mixed
 */
@AutoRegister(type = AutoRegistry.Type.LISTENER)
public class PetListener implements Listener {

    @EventHandler
    public void onEntityRightClick(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity rightClicked = event.getRightClicked();
        if (rightClicked == null) {
            return;
        }
        UUID id = rightClicked.getUniqueId();

        for (GamePet pet : GamePet.VALUES) {
            // Entity clicked is not equal to the current game pet in the loop, continue
            if (!rightClicked.getClass().equals(pet.getEntityClass())) {
                continue;
            }

            // Cancel pet claiming if the target pet already has an owner
            String displayName = pet.getColoredDisplayName();
            if (Metadata.hasMetadata(id, "PetOwner")) {
                player.sendMessage(Common.color("&c&lCANNOT CLAIM! &cThis " + displayName+ "&c is already owned by someone else!"));
                continue;
            }

            // Cancel pet claiming if the entity is not an animal by checking instanceof
            net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) rightClicked).getHandle();
            if (!(nmsEntity instanceof EntityAnimal)) {
                player.sendMessage(Common.color("&c&lNOT ANIMAL! &cThe mob you have clicked is not an animal and can therefor not be made into your pet."));
                return;
            }

            // Make the clicked entity into a pet
            this.makeEntityIntoPet(pet, player, nmsEntity);

            // Send messages and play claim sound
            player.sendMessage(Common.color("&a&lPET! &aYou now have the " + displayName + "&a as your pet!"));
            SoundHelper.playSound(player, Sound.LEVEL_UP);
        }
    }

    private void makeEntityIntoPet(GamePet pet, Player owner, net.minecraft.server.v1_8_R3.Entity rightClicked) {
        // Cast the NMS entity into an animal
        EntityAnimal animal = (EntityAnimal) rightClicked;

        // Set custom name
        animal.setCustomName(pet.getPlayerIncludedDisplayName(owner.getName()));
        animal.setCustomNameVisible(true);

        // Add pathfinder goals in priority order using the obfuscated PathfinderGoalSelector#a method
        animal.goalSelector.a(0, new PathfinderGoalFloat(animal)); // will float on water/stay on top of water
        animal.goalSelector.a(1, new PathfinderGoalLookAtPlayer(animal, EntityPlayer.class, 15)); // looks at a player with a distance of how far away the player can be
        animal.goalSelector.a(2, new PathfinderGoalGamePet(animal, pet.getSpeed(), pet.getDistanceThreshold())); // custom pet goal

        // Set metadata to keep track of who the owner is
        Metadata.setMetadata(animal.getBukkitEntity().getUniqueId(), "PetOwner", owner.getUniqueId().toString());
    }
}



















