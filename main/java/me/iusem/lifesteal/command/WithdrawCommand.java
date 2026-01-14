package me.iusem.lifesteal.command;

import me.iusem.lifesteal.LifestealPlugin;
import me.iusem.lifesteal.model.PlayerData;
import me.iusem.lifesteal.util.HeartSync;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WithdrawCommand implements CommandExecutor {

    private final LifestealPlugin plugin;

    public WithdrawCommand(LifestealPlugin plugin) {
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
            player.sendMessage("Usage: /withdraw <amount>");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid number.");
            return true;
        }

        PlayerData data = plugin.getPlayerDataManager()
                .getOrCreate(player.getUniqueId());

        if (amount <= 0 || data.getHearts() <= amount) {
            player.sendMessage("Not enough hearts.");
            return true;
        }

        data.setHearts(data.getHearts() - amount);

        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(
                    new ItemStack(Material.NETHER_STAR, 1)
            );
        }

        HeartSync.apply(player, data);

        plugin.getServer().getScheduler().runTaskAsynchronously(
                plugin,
                () -> plugin.getPlayerDataManager().save()
        );

        return true;
    }
}
