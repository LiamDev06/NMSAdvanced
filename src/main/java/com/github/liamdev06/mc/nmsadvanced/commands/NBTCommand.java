package com.github.liamdev06.mc.nmsadvanced.commands;

import com.github.liamdev06.mc.nmsadvanced.utility.Common;
import com.github.liamdev06.mc.nmsadvanced.utility.PlayerCommand;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegister;
import com.github.liamdev06.mc.nmsadvanced.utility.autoregistry.AutoRegistry;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * NBT command that modifies the item the player is holding by changing NBT property values
 * It has 2 versions; one with direct NMS access and one with API access using the NBT-API plugin
 *
 * @author Adjusments from course code by Liam
 */
@AutoRegister(type = AutoRegistry.Type.COMMAND)
public class NBTCommand extends PlayerCommand {

    private static final short ENCHANTMENT_LEVEL = 10;

    /**
     * Pre-defined values for the generic.attackDamage attribute modifier
     */
    private static final @NonNull String ATTACK_DAMAGE_NAME = "generic.attackDamage";
    private static final int ATTACK_DAMAGE_OPERATION = 0;
    private static final int ATTACK_DAMAGE_UUID_LEAST = 894654;
    private static final int ATTACK_DAMAGE_UUID_MOST = 2872;
    private static final int ATTACK_DAMAGE_AMOUNT = 30;

    public NBTCommand() {
        super("nbt");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Common.color("&c&lINVALID! &cProvide a valid input by using &6/nbt <nms/api>&c."));
            return;
        }

        String input = args[0].toLowerCase();
        switch (input) {
            case "nms": {
                this.nms(player);
                break;
            }
            case "api": {
                this.api(player);
                break;
            }
            default: {
                player.sendMessage(Common.color("&c'" + input + "' is invalid input for this command! Use either &6nms&c/&6api&c."));
            }
        }
    }

    /**
     * Modifies the NBT of the item the player is holding in its hand
     * using direct NMS access. Sets all enchantments to level 10 and modifies damage
     * if the item can cause it, like swords.
     *
     * @param player the player whose item in their main hand we edit NBT properties on
     */
    private void nms(Player player) {
        // Get the item and its NMS version
        ItemStack handItem = player.getItemInHand();
        if (handItem == null || handItem.getType() == Material.AIR) {
            player.sendMessage(Common.color("&cYou are not holding any item in your hand!"));
            return;
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsHandItem = CraftItemStack.asNMSCopy(handItem);

        // Get or create tag compound
        NBTTagCompound compound = nmsHandItem.hasTag() ? nmsHandItem.getTag() : new NBTTagCompound();

        // Stored enchantments example (for example: books when taking from creative)
        if (compound.hasKey("StoredEnchantments")) {
            this.performNmsEnchantmentUpdate(compound, "StoredEnchantments");
        }

        // Item enchantments example (for example: items after they've been applied with a book in an armor stand)
        if (compound.hasKey("ench")) {
            this.performNmsEnchantmentUpdate(compound, "ench");
        }

        // Damage example
        if (this.isValidAttackItem(handItem)) {
            // Get the modifiers list
            NBTTagList modifiers = compound.get("modifiers") == null ? new NBTTagList() : (NBTTagList) compound.get("modifiers");

            // Overwrite the damage attribute modifier by creating a new one
            NBTTagCompound damage = new NBTTagCompound();
            NBTTagString attackDamageString = new NBTTagString(ATTACK_DAMAGE_NAME);
            damage.set("AttributeName", attackDamageString);
            damage.set("Name", attackDamageString);
            damage.set("Amount", new NBTTagInt(ATTACK_DAMAGE_AMOUNT));
            damage.set("Operation", new NBTTagInt(ATTACK_DAMAGE_OPERATION));
            damage.set("UUIDLeast", new NBTTagInt(ATTACK_DAMAGE_UUID_LEAST));
            damage.set("UUIDMost", new NBTTagInt(ATTACK_DAMAGE_UUID_MOST));

            // Add the damage to the modifier list and update the item compound
            modifiers.add(damage);
            compound.set("AttributeModifiers", modifiers);
        }

        // Apply the edited NBT data
        nmsHandItem.setTag(compound);

        // Replace the item the player is holding with this one containing new NBT data
        player.setItemInHand(CraftItemStack.asBukkitCopy(nmsHandItem));
        player.sendMessage(Common.color("&a&lCOMPLETE! &aThe item you are holding was updating with new NBT properties."));
    }

    /**
     * Applies the enchantment level {@link NBTCommand#ENCHANTMENT_LEVEL} to the compound
     * with the key provided. This is because certain items use "StoredEnchantments" key in
     * the compound and certain use "ench" which would otherwise require duplicate code if not
     * put inside of a method.
     *
     * @param compound the compound of the item to modify enchantments on
     * @param key the key to access within the compound, in this case either "StoredEnchantments" or "ench"
     * @see NBTCommand#nms(Player) to see usage in action
     */
    private void performNmsEnchantmentUpdate(NBTTagCompound compound, String key) {
        NBTTagList list = (NBTTagList) compound.get(key);

        // Abort if the item has no enchantments
        if (list.size() == 0) {
            return;
        }

        // Loop through all enchantments and set their level to a new level
        for (int i = 0; i < list.size(); i++) {
            NBTTagCompound enchantmentTag = list.get(i);

            // Set the level property to our new level
            enchantmentTag.setShort("lvl", ENCHANTMENT_LEVEL);
        }
    }

    /**
     * Will return true or false if an item is a valid attack item
     * In this case, the valid attack items are considered all swords and all axes
     * To check this, the method returns if the item ends with _SWORD or _AXE
     *
     * @param item the item to check for
     * @return if the item is either a sword or an axe
     */
    private boolean isValidAttackItem(ItemStack item) {
        String materialName = item.getType().name();
        return materialName.endsWith("_SWORD") || materialName.endsWith("_AXE");
    }

    /**
     * Modifies the NBT of the item the player is holding in its hand
     * using the NBT-API.
     *
     * @see NBTCommand#nms(Player) for more information
     * @see <a href="https://github.com/tr7zw/Item-NBT-API">NBT API on GitHub</a>
     * @author Liam
     * @param player the player whose item in their main hand we edit NBT properties on
     */
    private void api(Player player) {
        // Get the item and its NMS version
        ItemStack handItem = player.getItemInHand();
        if (handItem == null || handItem.getType() == Material.AIR) {
            player.sendMessage(Common.color("&cYou are not holding any item in your hand!"));
            return;
        }

        // Modify the NBT using the API
        NBT.modify(handItem, nbt -> {
            // Stored enchantments example (for example: books when taking from creative)
            for (ReadWriteNBT enchantment : nbt.getCompoundList("StoredEnchantments")) {
                enchantment.setShort("lvl", ENCHANTMENT_LEVEL);
            }

            // Item enchantments example (for example: items after they've been applied with a book in an armor stand)
            for (ReadWriteNBT enchantment : nbt.getCompoundList("ench")) {
                enchantment.setShort("lvl", ENCHANTMENT_LEVEL);
            }

            // Update the item the player is holding by re-setting it to have it contain new NBT data
            player.setItemInHand(handItem);
            player.sendMessage(Common.color("&a&lCOMPLETE! &aThe item you are holding was updating with new NBT properties."));
        });
    }
}















