package me.iusem.lifesteal.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.iusem.lifesteal.model.PlayerData;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final File dataFile;
    private final Gson gson;
    private Map<UUID, PlayerData> playerDataMap;

    public PlayerDataManager(JavaPlugin plugin) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.dataFile = new File(plugin.getDataFolder(), "players.json");
        this.playerDataMap = new HashMap<>();
        load();
    }

    public PlayerData getOrCreate(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, PlayerData::new);
    }

    public void save() {
        try {
            dataFile.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(dataFile);
            gson.toJson(playerDataMap, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try {
            if (!dataFile.exists()) {
                save();
                return;
            }

            Type type = new TypeToken<Map<UUID, PlayerData>>() {}.getType();
            FileReader reader = new FileReader(dataFile);
            playerDataMap = gson.fromJson(reader, type);
            reader.close();

            if (playerDataMap == null) {
                playerDataMap = new HashMap<>();
            }

        } catch (Exception e) {
            e.printStackTrace();
            playerDataMap = new HashMap<>();
        }
    }
}
