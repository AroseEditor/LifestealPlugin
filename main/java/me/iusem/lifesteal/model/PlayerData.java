package me.iusem.lifesteal.model;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private int hearts;
    private boolean banned;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.hearts = 10;
        this.banned = false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = Math.max(0, Math.min(hearts, 20));
    }

    public void addHeart() {
        setHearts(this.hearts + 1);
    }

    public void removeHeart() {
        setHearts(this.hearts - 1);
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
