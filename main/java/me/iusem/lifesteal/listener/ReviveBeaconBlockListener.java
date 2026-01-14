package me.iusem.lifesteal.listener;

import me.iusem.lifesteal.LifestealPlugin;
import me.iusem.lifesteal.gui.ReviveGUI;
import me.iusem.lifesteal.revive.ReviveBeaconBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ReviveBeaconBlockListener implements Listener {

    private final LifestealPlugin plugin;

    public ReviveBeaconBlockListener(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockUse(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null) return;
        if (block.getType() != Material.BEACON) return;

        if (!ReviveBeaconBlock.isReviveBeacon(block)) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        player.openInventory(ReviveGUI.create(plugin));
    }
}
