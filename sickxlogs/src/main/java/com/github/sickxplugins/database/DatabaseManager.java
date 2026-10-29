package com.github.sickxplugins.database;

import com.github.sickxplugins.Main;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private final Main plugin;
    private Connection connection;

    public DatabaseManager(Main plugin) {
        this.plugin = plugin;
    }

    public void connect() throws SQLException {
        String host = plugin.getConfig().getString("database.host");
        String port = plugin.getConfig().getString("database.port");
        String database = plugin.getConfig().getString("database.database_name");
        String username = plugin.getConfig().getString("database.username");
        String password = plugin.getConfig().getString("database.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true";
        connection = DriverManager.getConnection(url, username, password);
    }

    public boolean isConnected() {
        return (connection != null);
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTables() {
        if (!isConnected()) return;
        try (Statement statement = connection.createStatement()) {
            // Table for Blocks
            statement.execute("CREATE TABLE IF NOT EXISTS block_logs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "player_name VARCHAR(36) NOT NULL," +
                    "action VARCHAR(10) NOT NULL," +
                    "block_type VARCHAR(50) NOT NULL," +
                    "x INT NOT NULL," +
                    "y INT NOT NULL," +
                    "z INT NOT NULL," +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Table for Chat
            statement.execute("CREATE TABLE IF NOT EXISTS chat_logs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "player_name VARCHAR(36) NOT NULL," +
                    "message TEXT NOT NULL," +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Runs the database insert in the background asynchronously to prevent server lag
    public void logBlockActionAsync(String playerName, String action, String blockType, int x, int y, int z) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!isConnected()) return;
            try {
                String query = "INSERT INTO block_logs (player_name, action, block_type, x, y, z) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, playerName);
                ps.setString(2, action);
                ps.setString(3, blockType);
                ps.setInt(4, x);
                ps.setInt(5, y);
                ps.setInt(6, z);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void logChatAsync(String playerName, String message) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!isConnected()) return;
            try {
                String query = "INSERT INTO chat_logs (player_name, message) VALUES (?, ?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, playerName);
                ps.setString(2, message);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}