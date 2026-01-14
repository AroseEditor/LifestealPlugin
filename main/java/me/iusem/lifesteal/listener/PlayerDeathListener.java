package me.iusem.lifesteal.listener;

import me.iusem.lifesteal.LifestealPlugin;
import me.iusem.lifesteal.model.PlayerData;
import me.iusem.lifesteal.util.HeartSync;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final LifestealPlugin plugin;

    public PlayerDeathListener(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        PlayerData victimData =
                plugin.getPlayerDataManager()
                        .getOrCreate(victim.getUniqueId());

        victimData.setHearts(victimData.getHearts() - 1);

        if (victimData.getHearts() <= 0) {
            victimData.setBanned(true);
            Bukkit.getBanList(BanList.Type.NAME)
                    .addBan(victim.getName(),
                            "Lost all hearts",
                            null,
                            null);
            victim.kickPlayer("You lost all hearts.");
        }

        HeartSync.apply(victim, victimData);

        if (killer != null) {
            PlayerData killerData =
                    plugin.getPlayerDataManager()
                            .getOrCreate(killer.getUniqueId());

            if (killerData.getHearts() < 20) {
                killerData.setHearts(killerData.getHearts() + 1);
                HeartSync.apply(killer, killerData);
            }
        }

        Bukkit.getScheduler().runTaskAsynchronously(
                plugin,
                () -> plugin.getPlayerDataManager().save()
        );
    }
}
