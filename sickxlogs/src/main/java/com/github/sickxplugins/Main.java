package com.github.sickxplugins;

import org.bukkit.plugin.java.JavaPlugin;
import com.github.sickxplugins.listeners.PlayerActionLogger;
import com.github.sickxplugins.commands.CommandManager;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Copies config.yml from resources folder to the server's plugins/SickxLogs folder
        saveDefaultConfig();

        getLogger().info("SickxLogs has been successfully enabled!");
        
        // Register the PlayerActionLogger so the server fires the events
        getServer().getPluginManager().registerEvents(new PlayerActionLogger(), this);

        // Register the command directly to the server's CommandMap
        getServer().getCommandMap().register("sickxlogs", new CommandManager());
    }

    @Override
    public void onDisable() {
        getLogger().info("SickxLogs has been disabled.");
    }
}