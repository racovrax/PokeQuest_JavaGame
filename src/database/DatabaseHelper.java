package database;

import entity.Player;

import java.sql.*;
import entity.Entity;

public class DatabaseHelper {
    private static final String DATABASE_URL = "jdbc:sqlite:res/Database/game.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createTables() {
        String playersTable = "CREATE TABLE IF NOT EXISTS players (" +
                "id INTEGER PRIMARY KEY," +
                "worldX INTEGER," +
                "worldY INTEGER," +
                "direction TEXT" +
                ");";

        String entitiesTable = "CREATE TABLE IF NOT EXISTS entities (" +
                "id INTEGER PRIMARY KEY," +
                "worldX INTEGER," +
                "worldY INTEGER," +
                "type TEXT" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(playersTable);
            stmt.execute(entitiesTable);
            System.out.println("Tables created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void savePlayer(int id, double worldX, double worldY, String direction) {
        String sql = "INSERT OR REPLACE INTO players(id, worldX, worldY, direction) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setDouble(2, worldX);
            pstmt.setDouble(3, worldY);
            pstmt.setString(4, direction);
            pstmt.executeUpdate();
            System.out.println("Player state saved.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadPlayer(int id, Player player) {
        String sql = "SELECT worldX, worldY, direction FROM players WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    player.worldX = (int) rs.getDouble("worldX");
                    player.worldY = (int) rs.getDouble("worldY");
                    player.direction = rs.getString("direction");
                    System.out.println("Player loaded: WorldX - " + player.worldX + ", WorldY - " + player.worldY + ", Direction - " + player.direction);
                } else {
                    System.out.println("No saved player state found.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void saveEntity(int id, double worldX, double worldY, String type) {
        String sql = "INSERT OR REPLACE INTO entities(id, worldX, worldY, type) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setDouble(2, worldX);
            pstmt.setDouble(3, worldY);
            pstmt.setString(4, type);
            pstmt.executeUpdate();
            System.out.println("Entity state saved.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadEntity(int id, Entity entity) {
        String sql = "SELECT worldX, worldY, type FROM entities WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    entity.worldX = (int) rs.getDouble("worldX");
                    entity.worldY = (int) rs.getDouble("worldY");
                    System.out.println("Entity loaded: WorldX - " + entity.worldX + ", WorldY - " + entity.worldY);
                } else {
                    System.out.println("No saved entity state found.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
