package com.github.sickxplugins;

import org.bukkit.plugin.java.JavaPlugin;
import com.github.sickxplugins.listeners.PlayerActionLogger;
import com.github.sickxplugins.commands.CommandManager;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // This copies the config.yml from our resources folder to the server's plugins/SickxLogs folder
        saveDefaultConfig();

        getLogger().info("SickxLogs has been successfully enabled!");
        
        // Register the PlayerActionLogger so the server fires the events
        getServer().getPluginManager().registerEvents(new PlayerActionLogger(), this);

        // Register the main command
        if (getCommand("sickxlogs") != null) {
            getCommand("sickxlogs").setExecutor(new CommandManager());
        } else {
            getLogger().severe("Failed to register the command 'sickxlogs'. Is it in the paper-plugin.yml?");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("SickxLogs has been disabled.");
    }
}