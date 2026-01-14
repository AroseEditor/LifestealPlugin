package me.iusem.lifesteal.command;

import me.iusem.lifesteal.LifestealPlugin;
import me.iusem.lifesteal.model.PlayerData;
import me.iusem.lifesteal.revive.ReviveBeaconItem;
import me.iusem.lifesteal.util.HeartSync;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReviveCommand implements CommandExecutor, TabCompleter {

    private final LifestealPlugin plugin;

    public ReviveCommand(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /revive <player>");
            return true;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (!ReviveBeaconItem.isReviveBeacon(hand)) {
            player.sendMessage(ChatColor.RED +
                    "You must hold a Revival Beacon to revive.");
            return true;
        }

        String targetName = args[0];
        if (!Bukkit.getBanList(BanList.Type.NAME).isBanned(targetName)) {
            player.sendMessage(ChatColor.RED +
                    "That player is not banned.");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

        Bukkit.getBanList(BanList.Type.NAME).pardon(targetName);

        PlayerData data = plugin.getPlayerDataManager()
                .getOrCreate(target.getUniqueId());

        data.setBanned(false);
        data.setHearts(10);

        hand.setAmount(hand.getAmount() - 1);

        Bukkit.broadcastMessage(ChatColor.GREEN +
                targetName + " has been unbanned by " +
                player.getName());
        Bukkit.broadcastMessage(ChatColor.YELLOW +
                "Say thanks to " + player.getName());

        Bukkit.getScheduler().runTaskAsynchronously(
                plugin,
                () -> plugin.getPlayerDataManager().save()
        );

        Player online = target.getPlayer();
        if (online != null) {
            HeartSync.apply(online, data);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            String input = args[0].toLowerCase();

            Set<BanEntry> bans =
                    Bukkit.getBanList(BanList.Type.NAME).getBanEntries();

            for (BanEntry entry : bans) {
                if (entry.getTarget().toLowerCase().startsWith(input)) {
                    list.add(entry.getTarget());
                }
            }
        }
        return list;
    }
}
