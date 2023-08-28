package me.hibatica.duelplugin.controllers;

import me.hibatica.duelplugin.DuelPlugin;
import me.hibatica.duelplugin.objects.Duel;
import me.hibatica.duelplugin.objects.DuelPlayer;
import me.hibatica.duelplugin.objects.PlayerStatsData;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DuelPlayerController {

    private static DuelPlugin plugin;

    private static HashMap<String, DuelPlayer> duelPlayerMap;

    public static void init(DuelPlugin plugin) {
        DuelPlayerController.plugin = plugin;
        DuelPlayerController.duelPlayerMap = new HashMap<>();
    }

    public static DuelPlayer getDuelPlayer(String playerName) {
        return duelPlayerMap.get(playerName);

    }

    public static void registerPlayersDuel(String playerName, Duel duel) {
        DuelPlayer duelPlayer = duelPlayerMap.get(playerName);

        if(duelPlayer != null) {
            duelPlayer.registerNewDuel(duel);
        }
    }

    public static void registerJoinedPlayer(Player player) {

        PlayerStatsData data = DatabaseController.getPlayerData(player.getUniqueId());
        DuelPlayer duelPlayer = new DuelPlayer(player, data, null);


        duelPlayerMap.put(player.getName(), duelPlayer);

    }

    public static void removeQuitingPlayer(Player player) {
        duelPlayerMap.remove(player.getName());
    }

}
