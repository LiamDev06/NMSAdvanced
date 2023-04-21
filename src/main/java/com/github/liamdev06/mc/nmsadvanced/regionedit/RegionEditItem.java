package com.github.liamdev06.mc.nmsadvanced.regionedit;

import com.github.liamdev06.mc.nmsadvanced.utility.Common;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the region edit item stack.
 * *Absolutely NOT* the best way to store items, but for a
 * plugin mainly used for learning and showcasing, this will due
 *
 * @author Liam
 */
public class RegionEditItem {

    /**
     * Region edit item used to edit regions
     */
    public static final @NonNull ItemStack REGION_EDIT_ITEM = createRegionEditItem();

    /**
     * @return new instance of the region edit item
     */
    private static ItemStack createRegionEditItem() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();

        // Set meta
        meta.setDisplayName(Common.color("&aRegion Tool"));
        meta.setLore(createLore());

        item.setItemMeta(meta);
        return item;
    }

    /**
     * @return new instance of the lore
     */
    private static List<String> createLore() {
        List<String> lore = new ArrayList<>();
        lore.add("&7Click to points to");
        lore.add("&7select them.");
        return lore;
    }
}
