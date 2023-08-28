package me.hibatica.duelplugin.objects;

import me.hibatica.duelplugin.controllers.DatabaseController;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerStatsData {
    private int wins;
    private int losses;

    private final UUID uuid;

    public PlayerStatsData(int wins, int losses, UUID uuid) {
        this.uuid = uuid;
        this.wins = wins;
        this.losses = losses;
        System.out.println("New PlayerStatsData +" + wins + losses + uuid);
    }

    public void incrementWins() {
        this.wins = DatabaseController.incrementPlayerWins(uuid);
    }

    public void incrementLosses() {
        this.losses = DatabaseController.incrementPlayerLosses(uuid);
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }
}
