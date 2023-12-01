package com.example.whodid;


import java.sql.*;

public class DatabaseConnector {
    private static final String JDBC_URL = "jdbc:sqlite:credentialsdb.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    public static Playerstats fetchPlayerStats(String username) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM PlayerStats WHERE username = ?")) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int ranking = resultSet.getInt("ranking");
                int goldCollected = resultSet.getInt("goldCollected");
                return new Playerstats(username, ranking, goldCollected);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isValidCredentials(String username, String pin) {
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM PlayerCredentials WHERE username = ? AND pin = ?")) {

            statement.setString(1, username);
            statement.setString(2, pin);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    }



