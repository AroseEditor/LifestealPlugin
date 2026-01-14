package me.iusem.lifesteal.listener;

import me.iusem.lifesteal.revive.ReviveBeaconBlock;
import me.iusem.lifesteal.revive.ReviveBeaconItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ReviveBeaconListener implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent event) {

        ItemStack item = event.getItem();
        if (!ReviveBeaconItem.isReviveBeacon(item)) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        Location placeLoc = player.getLocation().getBlock().getLocation();

        // Place the beacon block
        placeLoc.getBlock().setType(Material.BEACON);

        // Mark this block as a revival beacon
        ReviveBeaconBlock.mark(placeLoc.getBlock());

        // Consume the item
        item.setAmount(item.getAmount() - 1);
    }
}
