package me.hibatica.duelplugin.command;

import me.hibatica.duelplugin.DuelPlugin;
import me.hibatica.duelplugin.controllers.DuelPlayerController;
import me.hibatica.duelplugin.objects.PlayerStatsData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelStatsCommand implements CommandExecutor {

    private DuelPlugin plugin;

    public DuelStatsCommand(DuelPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player sender = plugin.getServer().getPlayer(commandSender.getName());

        if(sender == null) {
            plugin.getLogger().info("The console cannot run the command /duelstats");
            return true;
        }

        if(!sender.hasPermission("duelplugin.duelstats")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        Player playerToGetStatsFor = null;

        if(args.length == 0) {
            playerToGetStatsFor = sender;
        } else {
            playerToGetStatsFor = plugin.getServer().getPlayer(args[0]);
        }

        if(playerToGetStatsFor == null) {
            sender.sendMessage(ChatColor.RED + args[0] + " is not a valid player, or is not online");
            return true;
        }

        PlayerStatsData statsData = DuelPlayerController.getDuelPlayer(playerToGetStatsFor.getName()).getStatsData();

        if(statsData == null) {
            sender.sendMessage(ChatColor.RED + "Could not find stats data for the player: " + playerToGetStatsFor.getName());
            return true;
        }

        int kDRatio;

        if(statsData.getLosses() == 0) {
            kDRatio = 0;
        } else {
            kDRatio = statsData.getWins() / statsData.getLosses();
        }

        sender.sendMessage(ChatColor.BOLD + playerToGetStatsFor.getName() + "'s Duel Stats");
        sender.sendMessage("Wins: " + statsData.getWins());
        sender.sendMessage("Losses: " + statsData.getLosses());
        sender.sendMessage("K/D: " + kDRatio);


        return true;
    }
}
