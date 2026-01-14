package me.iusem.lifesteal.util;

import me.iusem.lifesteal.model.PlayerData;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public final class HeartSync {

    private static final double HEALTH_PER_HEART = 2.0;

    private HeartSync() {}

    public static void apply(Player player, PlayerData data) {
        if (player == null || data == null) return;

        double maxHealth = data.getHearts() * HEALTH_PER_HEART;

        if (maxHealth < 2.0) maxHealth = 2.0;

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                .setBaseValue(maxHealth);

        // Clamp current health if needed
        if (player.getHealth() > maxHealth) {
            player.setHealth(maxHealth);
        }
    }
}
