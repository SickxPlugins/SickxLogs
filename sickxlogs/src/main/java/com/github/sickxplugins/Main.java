package com.github.sickxplugins;

import org.bukkit.plugin.java.JavaPlugin;
import com.github.sickxplugins.listeners.PlayerActionLogger;
import com.github.sickxplugins.commands.CommandManager;
import com.github.sickxplugins.database.DatabaseManager;

import java.sql.SQLException;

public class Main extends JavaPlugin {

    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Initialize Database
        databaseManager = new DatabaseManager(this);
        try {
            databaseManager.connect();
            databaseManager.createTables();
            getLogger().info("Successfully connected to MySQL database!");
        } catch (SQLException e) {
            getLogger().severe("Could not connect to MySQL database! Check your config.yml credentials.");
            e.printStackTrace();
        }

        getLogger().info("SickxLogs has been successfully enabled!");
        
        // Register the PlayerActionLogger and pass this plugin instance to it
        getServer().getPluginManager().registerEvents(new PlayerActionLogger(this), this);

        // Register the command and pass 'this' (the Main plugin instance) so it can access the database
        getServer().getCommandMap().register("sickxlogs", new CommandManager(this));
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
        getLogger().info("SickxLogs has been disabled.");
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}