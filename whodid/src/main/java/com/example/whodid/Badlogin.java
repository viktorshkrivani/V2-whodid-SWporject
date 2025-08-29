package com.example.whodid;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.*;

public class Badlogin {
    @FXML private Button badlogintryagain;
    @FXML private ImageView retrybtn;
    @FXML private Button okbtn;

    private String username;
    private String pin;

    @FXML
    public void retry(ActionEvent actionEvent) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) badlogintryagain.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void addcredentials(ActionEvent actionEvent) {
        saveUser(username, pin);
        closeWindow();
    }

    private void saveUser(String username, String pin) {
        try {
            if (username == null || pin == null) return;

            // Basic constraints (match your Loginpage rules)
            if (username.length() > 10 || !username.equals(username.toLowerCase())) {
                System.out.println("Invalid username format!");
                return;
            }
            if (pin.length() != 4 || !pin.matches("\\d+")) {
                System.out.println("Invalid PIN format!");
                return;
            }
            if (isUsernameExists(username)) {
                System.out.println("Username already exists!");
                return;
            }

            try (Connection connection = DatabaseConnector.connect()) {
                connection.setAutoCommit(false);
                try (PreparedStatement insertCred = connection.prepareStatement(
                        "INSERT INTO PlayerCredentials(username, pin) VALUES (?, ?)");
                     PreparedStatement insertStats = connection.prepareStatement(
                             "INSERT INTO PlayerStats(username, goldCollected, ranking) VALUES (?, 0, 0)")
                ) {
                    insertCred.setString(1, username);
                    insertCred.setString(2, pin);
                    insertCred.executeUpdate();

                    insertStats.setString(1, username);
                    insertStats.executeUpdate();

                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                } finally {
                    connection.setAutoCommit(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertInitialPlayerStats(Connection connection, String username) throws SQLException {
        // Not used anymore; we insert PlayerStats in the transaction above.
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO PlayerStats(username, goldCollected, ranking) VALUES (?, ?, ?)")) {
            statement.setString(1, username);
            statement.setInt(2, 0);
            statement.setInt(3, 0);
            statement.executeUpdate();
        }
    }

    private boolean isUsernameExists(String username) {
        String sql = "SELECT 1 FROM PlayerCredentials WHERE username = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // be conservative on error
        }
    }

    public void initialize(String username, String pin) {
        this.username = username;
        this.pin = pin;
    }
}
