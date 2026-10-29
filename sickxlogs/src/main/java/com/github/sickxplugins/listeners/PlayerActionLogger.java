package com.github.sickxplugins.listeners;

import com.github.sickxplugins.Main;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerActionLogger implements Listener {
    
    private final Main plugin;

    // The constructor now requires the Main class to access the DatabaseManager
    public PlayerActionLogger(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String playerName = event.getPlayer().getName();
        String blockType = event.getBlock().getType().toString();
        
        int x = event.getBlock().getX();
        int y = event.getBlock().getY();
        int z = event.getBlock().getZ();
        
        plugin.getDatabaseManager().logBlockActionAsync(playerName, "BREAK", blockType, x, y, z);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String playerName = event.getPlayer().getName();
        String blockType = event.getBlock().getType().toString();
        
        int x = event.getBlock().getX();
        int y = event.getBlock().getY();
        int z = event.getBlock().getZ();
        
        plugin.getDatabaseManager().logBlockActionAsync(playerName, "PLACE", blockType, x, y, z);
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        String playerName = event.getPlayer().getName();
        
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        
        plugin.getDatabaseManager().logChatAsync(playerName, message);
    }
}