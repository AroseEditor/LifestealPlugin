package me.iusem.lifesteal.revive;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class ReviveRecipe {

    public static void register(JavaPlugin plugin) {

        NamespacedKey key =
                new NamespacedKey(plugin, "revival_beacon");

        ShapedRecipe recipe =
                new ShapedRecipe(key, ReviveBeaconItem.create());

        recipe.shape("NNN", "NBN", " N ");
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('B', Material.BEACON);

        Bukkit.addRecipe(recipe);
    }
}
