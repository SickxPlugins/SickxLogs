package com.github.sickxplugins.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

public class CommandManager extends Command {

    public CommandManager() {
        // This sets the main command name, description, usage message, and aliases
        super("sickxlogs", "Main command for SickxLogs", "/sickxlogs <info|help>", Arrays.asList("sl"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        
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