package com.example.whodid;

import java.io.File;
import java.sql.*;

public class DatabaseConnector {
    // Stable, writable location: C:\Users\<you>\.whodid\credentialsdb.db (on Windows)
    private static final String DB_DIR  = System.getProperty("user.home") + File.separator + ".whodid";
    private static final String DB_PATH = DB_DIR + File.separator + "credentialsdb.db";
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH;

    // Ensure folder + schema exist once on class load
    static {
        ensureDb();
    }

    private static void ensureDb() {
        try {
            new File(DB_DIR).mkdirs(); // create ~/.whodid if missing
            try (Connection c = DriverManager.getConnection(JDBC_URL);
                 Statement st = c.createStatement()) {

                st.execute("PRAGMA foreign_keys = ON");

                st.execute("""
                    CREATE TABLE IF NOT EXISTS PlayerCredentials(
                        username TEXT PRIMARY KEY,
                        pin      TEXT NOT NULL CHECK(length(pin)=4 AND pin GLOB '[0-9][0-9][0-9][0-9]')
                    )
                """);

                st.execute("""
                    CREATE TABLE IF NOT EXISTS PlayerStats(
                        username      TEXT PRIMARY KEY,
                        goldCollected INTEGER NOT NULL DEFAULT 0,
                        ranking       INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY(username) REFERENCES PlayerCredentials(username) ON DELETE CASCADE
                    )
                """);
            }
            System.out.println("[DB] Using: " + new File(DB_PATH).getAbsolutePath());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to init DB schema", e);
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    // For debugging / printing in initialize()
    public static String dbPath() {
        return DB_PATH;
    }

    public static Playerstats fetchPlayerStats(String username) {
        String sql = "SELECT ranking, goldCollected FROM PlayerStats WHERE username = ?";
        try (Connection connection = connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int ranking = rs.getInt("ranking");
                    int goldCollected = rs.getInt("goldCollected");
                    return new Playerstats(username, ranking, goldCollected);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // not found
    }

    public static boolean isValidCredentials(String username, String pin) {
        String sql = "SELECT 1 FROM PlayerCredentials WHERE username = ? AND pin = ?";
        try (Connection connection = connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, pin);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateGold(String username, int updatedGold) {

    }
}
