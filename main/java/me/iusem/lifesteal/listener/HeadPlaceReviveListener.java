package me.iusem.lifesteal.listener;

import me.iusem.lifesteal.LifestealPlugin;
import me.iusem.lifesteal.model.PlayerData;
import me.iusem.lifesteal.revive.ReviveBeaconItem;
import me.iusem.lifesteal.revive.ReviveSessionManager;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class HeadPlaceReviveListener implements Listener {

    private final LifestealPlugin plugin;

    public HeadPlaceReviveListener(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHeadUse(PlayerInteractEvent event) {

        // Must right-click a block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();

        // Must have an active revive session
        if (!ReviveSessionManager.hasSession(player.getUniqueId())) return;

        ItemStack item = event.getItem();
        if (item == null) return;
        if (item.getType() != Material.PLAYER_HEAD) return;
        if (!(item.getItemMeta() instanceof SkullMeta)) return;

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta.getOwningPlayer() == null) return;

        String expectedTarget =
                ReviveSessionManager.getTarget(player.getUniqueId());

        if (!meta.getOwningPlayer().getName()
                .equalsIgnoreCase(expectedTarget)) {
            player.sendMessage("This is not the correct head.");
            event.setCancelled(true);
            return;
        }

        // Cancel normal interaction (prevents bedrock rollback)
        event.setCancelled(true);

        // Consume ONE Revival Beacon
        for (ItemStack invItem : player.getInventory().getContents()) {
            if (ReviveBeaconItem.isReviveBeacon(invItem)) {
                invItem.setAmount(invItem.getAmount() - 1);
                break;
            }
        }

        // Consume the head
        item.setAmount(item.getAmount() - 1);

        // Unban & restore player
        OfflinePlayer target =
                Bukkit.getOfflinePlayer(expectedTarget);

        Bukkit.getBanList(BanList.Type.NAME)
                .pardon(expectedTarget);

        PlayerData data = plugin.getPlayerDataManager()
                .getOrCreate(target.getUniqueId());

        data.setBanned(false);
        data.setHearts(10);
        plugin.getPlayerDataManager().save();

        // Effects & sound
        World world = event.getClickedBlock().getWorld();
        Location loc = event.getClickedBlock().getLocation().add(0.5, 1, 0.5);

        world.spawnParticle(Particle.HAPPY_VILLAGER, loc, 40, 0.3, 0.5, 0.3);
        world.playSound(loc, Sound.ITEM_TOTEM_USE, 1f, 1f);

        Bukkit.broadcastMessage(
                expectedTarget + " has been unbanned by " +
                        player.getName() + "!");
        Bukkit.broadcastMessage(
                "Say thanks to " + player.getName());

        // End revive session
        ReviveSessionManager.end(player.getUniqueId());
    }
}
