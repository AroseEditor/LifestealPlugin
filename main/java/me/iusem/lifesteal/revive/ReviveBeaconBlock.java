package me.iusem.lifesteal.revive;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ReviveBeaconBlock {

    private static NamespacedKey KEY;

    public static void init(NamespacedKey key) {
        KEY = key;
    }

    public static void mark(Block block) {
        if (KEY == null) return;

        PersistentDataContainer container =
                block.getChunk().getPersistentDataContainer();

        container.set(KEY, PersistentDataType.STRING,
                block.getX() + "," + block.getY() + "," + block.getZ());
    }

    public static boolean isReviveBeacon(Block block) {
        if (KEY == null) return false;

        PersistentDataContainer container =
                block.getChunk().getPersistentDataContainer();

        String value = container.get(KEY, PersistentDataType.STRING);
        if (value == null) return false;

        return value.equals(
                block.getX() + "," + block.getY() + "," + block.getZ());
    }
}
