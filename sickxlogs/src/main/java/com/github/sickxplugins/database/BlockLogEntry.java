package com.github.sickxplugins.database;

import java.sql.Timestamp;

public record BlockLogEntry(String playerName, String action, String blockType, int x, int y, int z, Timestamp timestamp) {}