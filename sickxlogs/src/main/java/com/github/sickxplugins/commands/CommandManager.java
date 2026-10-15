package com.github.sickxplugins.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        // If the player just types /sl or /sickxlogs with no arguments
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /sl <info|help>");
            return true;
        }

        // Get the first argument (e.g., "info" or "help") and make it lowercase
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "info":
                sender.sendMessage("§a[SickxLogs] §fVersion: §e1.0");
                sender.sendMessage("§a[SickxLogs] §fCurrently logging: §eBlocks (Break/Place) and Chat");
                break;
            
            case "help":
                sender.sendMessage("§a[SickxLogs] §fHelp Menu: §cUnder Development.");
                break;
            
            default:
                sender.sendMessage("§cUnknown subcommand. Usage: /sl <info|help>");
                break;
        }

        return true;
    }
}