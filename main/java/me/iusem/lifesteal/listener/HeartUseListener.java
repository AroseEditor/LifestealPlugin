package me.iusem.lifesteal.listener;

import me.iusem.lifesteal.LifestealPlugin;
import me.iusem.lifesteal.model.PlayerData;
import me.iusem.lifesteal.util.HeartSync;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HeartUseListener implements Listener {

    private final LifestealPlugin plugin;

    public HeartUseListener(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.NETHER_STAR) return;

        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager()
                .getOrCreate(player.getUniqueId());

        if (data.getHearts() >= 20) return;

        data.setHearts(data.getHearts() + 1);
        item.setAmount(item.getAmount() - 1);

        HeartSync.apply(player, data);

        plugin.getServer().getScheduler().runTaskAsynchronously(
                plugin,
                () -> plugin.getPlayerDataManager().save()
        );
    }
}
