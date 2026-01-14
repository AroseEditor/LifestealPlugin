package me.iusem.lifesteal.gui;

import me.iusem.lifesteal.LifestealPlugin;
import me.iusem.lifesteal.model.PlayerData;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.Material;

public class ReviveGUI {

    public static final String GUI_TITLE =
            ChatColor.DARK_RED + "Revive Player";

    public static Inventory create(LifestealPlugin plugin) {
        Inventory inv = Bukkit.createInventory(null, 27, GUI_TITLE);

        Bukkit.getBanList(BanList.Type.NAME).getBanEntries().forEach(entry -> {
            OfflinePlayer offline =
                    Bukkit.getOfflinePlayer(entry.getTarget());

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(offline);
            meta.setDisplayName(ChatColor.RED + offline.getName());
            head.setItemMeta(meta);

            inv.addItem(head);
        });

        return inv;
    }

    public static void revive(
            LifestealPlugin plugin,
            String targetName,
            String reviverName
    ) {

        Bukkit.getBanList(BanList.Type.NAME).pardon(targetName);

        OfflinePlayer target =
                Bukkit.getOfflinePlayer(targetName);

        PlayerData data = plugin.getPlayerDataManager()
                .getOrCreate(target.getUniqueId());

        data.setBanned(false);
        data.setHearts(10);

        plugin.getPlayerDataManager().save();

        Bukkit.broadcastMessage(ChatColor.GREEN +
                "[" + targetName + "] has been unbanned by [" +
                reviverName + "]!");
        Bukkit.broadcastMessage(ChatColor.YELLOW +
                "Say thanks to [" + reviverName + "]");
    }
}
