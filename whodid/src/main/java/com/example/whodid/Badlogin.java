package com.example.whodid;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import static com.example.whodid.DatabaseConnector.fetchPlayerStats;


public class Badlogin {
    @javafx.fxml.FXML
    private Button badlogintryagain;
    @javafx.fxml.FXML
    private ImageView retrybtn;
    @javafx.fxml.FXML
    private Button okbtn;

    private String username;
    private String pin;
    private static final String JDBC_URL = "jdbc:sqlite:credentialsdb.db";


    @javafx.fxml.FXML
    public void retry(ActionEvent actionEvent) {
        closeWindow();
    }

    private void closeWindow() {
        // Get the stage (window) from any UI element in the scene
        Stage stage = (Stage) badlogintryagain.getScene().getWindow();

        // Close the stage
        stage.close();
    }

    @javafx.fxml.FXML
    public void addcredentials(ActionEvent actionEvent) {
        saveUser(username, pin);
        closeWindow();
    }

    private void saveUser(String username, String pin) {
        try {
            // Check constraints before saving
            if (username.length() > 10 || !username.equals(username.toLowerCase())) {
                System.out.println("Invalid username format!");
                return;
            }

            if (pin.length() != 4 || !pin.matches("\\d+")) {
                System.out.println("Invalid PIN format!");
                return;
            }

            // Check if the username already exists
            if (isUsernameExists(username)) {
                System.out.println("Username already exists!");
                return;
            }

            try (Connection connection = DriverManager.getConnection(JDBC_URL);
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO PlayerCredentials(username, pin) VALUES (?, ?)")) {

                statement.setString(1, username);
                statement.setString(2, pin);

                statement.executeUpdate();

                // After saving user credentials, insert a record into PlayerStats with initial values
                insertInitialPlayerStats(connection, username);

            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void insertInitialPlayerStats(Connection connection, String username) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO PlayerStats(username, goldCollected, ranking) VALUES (?, ?, ?)")) {

            // Set the values for username, initial gold, and initial rank
            statement.setString(1, username);
            statement.setInt(2, 0);  // Initial gold (you can set it to whatever default gold value you want)
            statement.setInt(3, 0);  // Initial rank (you can set it to whatever default rank you want)

            statement.executeUpdate();
        }
    }

    private boolean isUsernameExists(String username) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM PlayerCredentials WHERE username = ?")) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Returns true if the username exists

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
            return true; // Assume username exists in case of an error
        }
    }

    public void initialize(String username, String pin) {
        this.username = username;
        this.pin = pin;
    }

}