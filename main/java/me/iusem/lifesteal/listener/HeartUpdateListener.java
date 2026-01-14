package me.iusem.lifesteal.listener;

import me.iusem.lifesteal.LifestealPlugin;
import me.iusem.lifesteal.model.PlayerData;
import me.iusem.lifesteal.util.HeartSync;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class HeartUpdateListener implements Listener {

    private final LifestealPlugin plugin;

    public HeartUpdateListener(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    private void sync(Player player) {
        PlayerData data = plugin.getPlayerDataManager()
                .getOrCreate(player.getUniqueId());
        HeartSync.apply(player, data);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        sync(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        sync(event.getPlayer());
    }

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        if (event.getFrom().getWorld() != event.getTo().getWorld()) {
            sync(event.getPlayer());
        }
    }
}
