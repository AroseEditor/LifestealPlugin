package me.iusem.lifesteal;

import me.iusem.lifesteal.command.ReviveCommand;
import me.iusem.lifesteal.command.WithdrawCommand;
import me.iusem.lifesteal.data.PlayerDataManager;
import me.iusem.lifesteal.listener.*;
import me.iusem.lifesteal.revive.ReviveRecipe;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class LifestealPlugin extends JavaPlugin {

    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        playerDataManager = new PlayerDataManager(this);

        ReviveRecipe.register(this);

        getServer().getPluginManager().registerEvents(
                new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(
                new HeartUseListener(this), this);

        PluginCommand withdraw = getCommand("withdraw");
        if (withdraw != null)
            withdraw.setExecutor(new WithdrawCommand(this));

        PluginCommand revive = getCommand("revive");
        if (revive != null) {
            ReviveCommand cmd = new ReviveCommand(this);
            revive.setExecutor(cmd);
            revive.setTabCompleter(cmd);
        }

        getLogger().info("Lifesteal enabled");
    }

    @Override
    public void onDisable() {
        playerDataManager.save();
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}
