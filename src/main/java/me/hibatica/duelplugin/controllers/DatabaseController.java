package me.hibatica.duelplugin.controllers;

import me.hibatica.duelplugin.DuelPlugin;
import me.hibatica.duelplugin.objects.PlayerStatsData;

import java.sql.*;
import java.util.UUID;

public class DatabaseController {

    private static DuelPlugin plugin;

    private static Connection connection;

    public static void init(DuelPlugin plugin) {
        DatabaseController.plugin = plugin;

        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/data.db");

            plugin.getLogger().info("DuelPlugin: Connected to SQLite database");

        } catch (ClassNotFoundException | SQLException e) {
            plugin.getLogger().severe("DuelPlugin: Failed to connect to SQLite database.");
            e.printStackTrace();
        }

        String sql = "CREATE TABLE IF NOT EXISTS playerstats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "uuid TEXT NOT NULL," +
                "wins INTEGER NOT NULL," +
                "losses INTEGER NOT NULL)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("DuelsPlugin: Failed to create 'playerstats' table.");
            throw new RuntimeException(e);
        }
    }

    public static void shutdown() throws SQLException {
        if (connection == null) {
            return;
        }
        connection.close();
    }

    public static PlayerStatsData getPlayerData(UUID playerUuid) {
        plugin.getLogger().info("Getting player stats data for " + playerUuid);

        String sql = "SELECT uuid, wins, losses FROM playerstats WHERE uuid = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUuid.toString());
            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    String fetchedUuid = results.getString("uuid");
                    int wins = results.getInt("wins");
                    int losses = results.getInt("losses");

                    plugin.getLogger().info("Success!");
                    return new PlayerStatsData(wins, losses, UUID.fromString(fetchedUuid));
                } else {
                    System.out.println("Player not found, registering");
                    registerNewPlayer(playerUuid);
                    return getPlayerData(playerUuid);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().info("DuelPlugin: Couldn't get player data for " + playerUuid);
            e.printStackTrace();
        }

        return null;
    }

    public static int incrementPlayerWins(UUID playerUuid) {
        String sql = "UPDATE playerstats SET wins = wins + 1 WHERE uuid = ?";
        int newWins = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUuid.toString());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                newWins = getPlayerWins(playerUuid);
            }
        } catch (SQLException e) {
            return 1234567;
        }

        return newWins;
    }

    public static int incrementPlayerLosses(UUID playerUuid) {
        String sql = "UPDATE playerstats SET losses = losses + 1 WHERE uuid = ?";
        int newLosses = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUuid.toString());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                newLosses = getPlayerLosses(playerUuid); // Fetch the updated losses value
            }
        } catch (SQLException e) {
            return 1234567;
        }

        return newLosses;
    }

    private static int getPlayerWins(UUID playerUuid) throws SQLException {
        String sql = "SELECT wins FROM playerstats WHERE uuid = ?";
        int wins = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    wins = resultSet.getInt("wins");
                }
            }
        }

        return wins;
    }

    private static int getPlayerLosses(UUID playerUuid) throws SQLException {
        String sql = "SELECT losses FROM playerstats WHERE uuid = ?";
        int losses = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    losses = resultSet.getInt("losses");
                }
            }
        }

        return losses;
    }


    private static void registerNewPlayer(UUID playerUuid) {
        plugin.getLogger().info("Registering new player: " + playerUuid);
        String sql = "INSERT OR IGNORE INTO playerstats (uuid, wins, losses) VALUES (?, 0, 0)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUuid.toString());
            statement.executeUpdate();

            plugin.getLogger().info("DuelPlugin: Added player profile for " + playerUuid);
        } catch (SQLException e) {
            plugin.getLogger().severe("DuelPlugin: Failed to add player: " + playerUuid);
            e.printStackTrace();
        }
    }
}
