package com.github.sickxplugins.commands;

import com.github.sickxplugins.Main;
import com.github.sickxplugins.database.BlockLogEntry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class CommandManager extends Command {

    private final Main plugin;
    private final LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();
    
    // Split the date format into two separate formatters
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CommandManager(Main plugin) {
        super("sickxlogs", "Main command for SickxLogs", "/sickxlogs <info|help|db|block>", Arrays.asList("sl"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /sl <info|help|db|block>");
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
                if (plugin.getDatabaseManager() != null && plugin.getDatabaseManager().isConnected()) {
                    sender.sendMessage("§a[SickxLogs] §fDatabase Status: §aConnected");
                } else {
                    sender.sendMessage("§a[SickxLogs] §fDatabase Status: §cDisconnected §7(Check console/config)");
                }
                break;

            case "block":
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /sl block <player> [e <limit>] [page]");
                    return true;
                }
                String targetPlayer = args[1];
                
                int limit = 30; // Default limit
                int page = 1;   // Default page
                int argIndex = 2;

                if (args.length > argIndex && args[argIndex].equalsIgnoreCase("e")) {
                    if (args.length > argIndex + 1) {
                        try { 
                            limit = Integer.parseInt(args[argIndex + 1]); 
                        } catch (NumberFormatException ignored) {}
                        argIndex += 2;
                    } else {
                        sender.sendMessage("§cUsage: /sl block <player> [e <limit>] [page]");
                        return true;
                    }
                }

                if (args.length > argIndex) {
                    try { 
                        page = Integer.parseInt(args[argIndex]); 
                    } catch (NumberFormatException ignored) {}
                }

                final int finalLimit = limit;
                final int finalPage = page;

                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    List<BlockLogEntry> logs = plugin.getDatabaseManager().getBlockLogs(targetPlayer, finalLimit);
                    
                    if (logs.isEmpty()) {
                        sender.sendMessage("§cNo block logs found for " + targetPlayer + ".");
                        return;
                    }

                    int totalPages = (int) Math.ceil(logs.size() / 10.0);
                    if (totalPages == 0) totalPages = 1;
                    
                    int currentPage = finalPage;
                    if (currentPage < 1) currentPage = 1;
                    if (currentPage > totalPages) currentPage = totalPages;

                    int startIndex = (currentPage - 1) * 10;
                    int endIndex = Math.min(startIndex + 10, logs.size());

                    sender.sendMessage(legacy.deserialize("&6--------[ &3Block Log of " + targetPlayer + " &6]--------"));

                    for (int i = startIndex; i < endIndex; i++) {
                        BlockLogEntry entry = logs.get(i);
                        
                        // Parse time and date separately
                        String timeStr = timeFormat.format(entry.timestamp());
                        String dateStr = dateFormat.format(entry.timestamp());
                        
                        // Build the timestamp component with hover text for the date
                        Component timestampComponent = legacy.deserialize("&8[" + timeStr + "] ")
                            .hoverEvent(HoverEvent.showText(Component.text(dateStr, NamedTextColor.GRAY)));
                        
                        // Build the middle section text
                        Component baseText = legacy.deserialize("&f" + entry.playerName() + " >>> &e" + entry.action() + " &7" + entry.blockType() + " at ");
                        
                        // Build the coordinates component
                        Component coords = Component.text(entry.x() + " " + entry.y() + " " + entry.z())
                            .color(NamedTextColor.YELLOW)
                            .hoverEvent(HoverEvent.showText(Component.text("click to teleport to the location")))
                            .clickEvent(ClickEvent.runCommand("/sl _internal_tp " + entry.x() + " " + entry.y() + " " + entry.z()));
                        
                        // Stitch all three components together and send
                        sender.sendMessage(timestampComponent.append(baseText).append(coords));
                    }

                    String baseCommand = "/sl block " + targetPlayer + (finalLimit != 30 ? " e " + finalLimit : "");
                    
                    Component footer = Component.empty();
                    
                    if (currentPage > 1) {
                        footer = footer.append(legacy.deserialize("&e<------")
                            .clickEvent(ClickEvent.runCommand(baseCommand + " " + (currentPage - 1)))
                            .hoverEvent(HoverEvent.showText(Component.text("Previous Page"))));
                    } else {
                        footer = footer.append(legacy.deserialize("&8<------"));
                    }

                    footer = footer.append(legacy.deserialize("&6&l[ &3Page " + currentPage + "&7/&3" + totalPages + " &6&l]"));

                    if (currentPage < totalPages) {
                        footer = footer.append(legacy.deserialize("&e------->")
                            .clickEvent(ClickEvent.runCommand(baseCommand + " " + (currentPage + 1)))
                            .hoverEvent(HoverEvent.showText(Component.text("Next Page"))));
                    } else {
                        footer = footer.append(legacy.deserialize("&8------->"));
                    }
                    
                    sender.sendMessage(footer);
                });
                break;

            case "_internal_tp":
                if (sender instanceof Player player) {
                    if (args.length == 4) {
                        try {
                            int x = Integer.parseInt(args[1]);
                            int y = Integer.parseInt(args[2]);
                            int z = Integer.parseInt(args[3]);
                            
                            player.teleportAsync(new Location(player.getWorld(), x, y, z));
                            player.sendMessage("§aTeleported to logged location.");
                        } catch (NumberFormatException ignored) {}
                    }
                } else {
                    sender.sendMessage("§cOnly players can teleport.");
                }
                break;
            
            default:
                sender.sendMessage("§cUnknown subcommand. Usage: /sl <info|help|db|block>");
                break;
        }

        return true;
    }
}