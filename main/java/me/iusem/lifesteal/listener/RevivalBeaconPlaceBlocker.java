package me.iusem.lifesteal.listener;

import me.iusem.lifesteal.revive.ReviveBeaconItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class RevivalBeaconPlaceBlocker implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();

        if (!ReviveBeaconItem.isReviveBeacon(item)) return;

        // Prevent placing the beacon
        event.setCancelled(true);

        // Send action bar message (middle of screen)
        player.sendActionBar(
                ChatColor.RED +
                        "Can't place to revive — use /revive <player>"
        );
    }
}
