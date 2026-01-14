package me.iusem.lifesteal.listener;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ReviveRecipeUnlockListener implements Listener {

    private final NamespacedKey recipeKey;

    public ReviveRecipeUnlockListener(JavaPlugin plugin) {
        this.recipeKey = new NamespacedKey(plugin, "revival_beacon");
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        check(event.getItem().getItemStack());
        event.getPlayer().discoverRecipe(recipeKey);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Safety unlock if player already owns ingredients
        for (ItemStack item : event.getPlayer().getInventory().getContents()) {
            if (item == null) continue;
            if (item.getType() == Material.BEACON ||
                    item.getType() == Material.NETHERITE_INGOT) {
                event.getPlayer().discoverRecipe(recipeKey);
                break;
            }
        }
    }

    private void check(ItemStack item) {
        if (item == null) return;
        if (item.getType() == Material.BEACON ||
                item.getType() == Material.NETHERITE_INGOT) {
            // handled in caller
        }
    }
}
