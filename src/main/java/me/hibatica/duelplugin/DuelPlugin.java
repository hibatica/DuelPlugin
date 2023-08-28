package me.hibatica.duelplugin;

import me.hibatica.duelplugin.command.DuelStatsCommand;
import me.hibatica.duelplugin.controllers.DatabaseController;
import me.hibatica.duelplugin.controllers.DuelPlayerController;
import me.hibatica.duelplugin.controllers.DuelPlayerControllerListeners;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class DuelPlugin extends JavaPlugin {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.config = getConfig();

        DuelPlayerController.init(this);
        DatabaseController.init(this);

        getServer().getPluginManager().registerEvents(new DuelPlayerControllerListeners(), this);

        getCommand("duelstats").setExecutor(new DuelStatsCommand(this));
    }

    @Override
    public void onDisable() {
        try {
            DatabaseController.shutdown();
        } catch (SQLException e) {
            getLogger().severe("There was an error in shutting down the DatabaseController");
            e.printStackTrace();
        }
    }
}
