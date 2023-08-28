package me.hibatica.duelplugin.objects;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nullable;

public class DuelPlayer {
    private final Player bukkitPlayer;
    private Duel currentDuel;
    private final PlayerStatsData statsData;

    public DuelPlayer(@NonNull Player player, @NonNull PlayerStatsData statsData, @Nullable Duel currentDuel) {
        this.bukkitPlayer = player;
        this.statsData = statsData;
        this.currentDuel = currentDuel;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public PlayerStatsData getStatsData() {
        return statsData;
    }

    public Duel getCurrentDuel() {
        return currentDuel;
    }

    public void registerNewDuel(Duel duel) {
        this.currentDuel = duel;
    }

    public void deleteCurerntDuel() {
        this.currentDuel = null;
    }
}
