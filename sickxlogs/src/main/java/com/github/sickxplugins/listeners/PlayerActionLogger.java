package com.github.sickxplugins.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerActionLogger implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String playerName = event.getPlayer().getName();
        String blockType = event.getBlock().getType().toString();
        
        // Getting coordinates
        int x = event.getBlock().getX();
        int y = event.getBlock().getY();
        int z = event.getBlock().getZ();
        
        // Formatting our basic log message
        String logMessage = "[ACTION] " + playerName + " broke " + blockType + " at X:" + x + " Y:" + y + " Z:" + z;
        
        // For now, we will print this directly to the server console. 
        // Later we can redirect this to a file or database!
        Bukkit.getLogger().info(logMessage);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String playerName = event.getPlayer().getName();
        String blockType = event.getBlock().getType().toString();
        
        int x = event.getBlock().getX();
        int y = event.getBlock().getY();
        int z = event.getBlock().getZ();
        
        String logMessage = "[ACTION] " + playerName + " placed " + blockType + " at X:" + x + " Y:" + y + " Z:" + z;
        Bukkit.getLogger().info(logMessage);
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        String playerName = event.getPlayer().getName();
        
        // Modern Paper uses the Adventure API for text (which supports complex coloring/hover events).
        // To log it cleanly, we convert it back to plain text.
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        
        String logMessage = "[CHAT] " + playerName + ": " + message;
        Bukkit.getLogger().info(logMessage);
    }
}