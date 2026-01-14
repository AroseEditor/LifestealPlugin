package me.iusem.lifesteal.revive;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class ReviveBeaconItem {

    public static ItemStack create() {
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Revival Beacon");
        meta.setLore(Collections.singletonList(
                ChatColor.GRAY + "Revives one banned player"
        ));

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isReviveBeacon(ItemStack item) {
        if (item == null) return false;
        if (item.getType() != Material.BEACON) return false;
        if (!item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) return false;

        String expected =
                ChatColor.RED + "" + ChatColor.BOLD + "Revival Beacon";

        return expected.equals(meta.getDisplayName());
    }
}
