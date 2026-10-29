package com.github.sickxplugins.commands;

import com.github.sickxplugins.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

public class CommandManager extends Command {

    private final Main plugin;

    public CommandManager(Main plugin) {
        // Updated the usage message in the super constructor to include 'db'
        super("sickxlogs", "Main command for SickxLogs", "/sickxlogs <info|help|db>", Arrays.asList("sl"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /sl <info|help|db>");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "info":
                sender.sendMessage("§a[SickxLogs] §fVersion: §e1.0");
                sender.sendMessage("§a[SickxLogs] §fCurrently logging: §eBlocks (Break/Place) and Chat");
                break;
            
            case "help":
                sender.sendMessage("§a[SickxLogs] §fHelp Menu: §cUnder Development.");
                break;

            case "database":
            case "db":
                // Check if the database manager exists and is connected
                if (plugin.getDatabaseManager() != null && plugin.getDatabaseManager().isConnected()) {
                    sender.sendMessage("§a[SickxLogs] §fDatabase Status: §aConnected");
                } else {
                    sender.sendMessage("§a[SickxLogs] §fDatabase Status: §cDisconnected §7(Check console/config)");
                }
                break;
            
            default:
                sender.sendMessage("§cUnknown subcommand. Usage: /sl <info|help|db>");
                break;
        }

        return true;
    }
}