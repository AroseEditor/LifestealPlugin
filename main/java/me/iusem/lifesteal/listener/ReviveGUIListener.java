package me.iusem.lifesteal.listener;

import me.iusem.lifesteal.LifestealPlugin;
import me.iusem.lifesteal.gui.ReviveGUI;
import me.iusem.lifesteal.model.PlayerData;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ReviveGUIListener implements Listener {

    private final LifestealPlugin plugin;

    public ReviveGUIListener(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (!event.getView().getTitle().equals(ReviveGUI.GUI_TITLE)) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() != Material.PLAYER_HEAD) return;
        if (!(item.getItemMeta() instanceof SkullMeta)) return;

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        OfflinePlayer target = meta.getOwningPlayer();
        if (target == null) return;

        // Unban + restore
        Bukkit.getBanList(BanList.Type.NAME).pardon(target.getName());
        PlayerData data = plugin.getPlayerDataManager()
                .getOrCreate(target.getUniqueId());
        data.setBanned(false);
        data.setHearts(10);
        plugin.getPlayerDataManager().save();

        World world = player.getWorld();
        Location loc = player.getLocation();

        world.spawnParticle(Particle.HAPPY_VILLAGER, loc, 50);
        world.playSound(loc, Sound.ITEM_TOTEM_USE, 1f, 1f);

        Bukkit.broadcastMessage(
                ChatColor.GREEN + "[" + target.getName() + "] has been unbanned by [" +
                        player.getName() + "]!");
        Bukkit.broadcastMessage(
                ChatColor.YELLOW + "Say thanks to [" + player.getName() + "]");

        // Destroy nearby revive beacon block
        loc.getBlock().setType(Material.AIR);

        player.closeInventory();
    }
}
