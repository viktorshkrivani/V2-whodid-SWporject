package com.example.whodid;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class Loginpage {

    @FXML private Button instructionsbtn;
    @FXML private Button pickhardbtn;
    @FXML private Button pickeasybtn;
    @FXML private Button pickmedbtn;
    @FXML private ToggleButton offSound;
    @FXML private ToggleButton offMusic;
    @FXML private PasswordField pin;
    @FXML private TextField playername;
    @FXML private TextField monstername;
    @FXML private Button startbtn;

    private MusicPlayer musicPlayer;
    private SoundPlayer soundPlayer;

    public void initialize() {
        // Make sure the resource path is correct and exists in src/main/resources
        URL soundURL = getClass().getResource("/music and sound/Mouse ClickSOUND EFFECT HD shorts.wav");
        soundPlayer = new SoundPlayer(soundURL);

        // (Optional) Force-load DB so schema is ensured and print path once
        System.out.println("DB file: " + DatabaseConnector.dbPath());
    }

    @Deprecated
    public void oninstructionbtnClick(ActionEvent actionEvent) throws IOException {
        soundPlayer.play();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("instructions.fxml"));
        Parent root = loader.load();
        Instructions instructionsController = loader.getController();
        instructionsController.setMusicPlayer(musicPlayer);
        instructionsController.setSoundPlayer(soundPlayer);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void startgame(Event event) throws IOException {
        soundPlayer.play();
        String enteredUsername = playername.getText();
        String enteredPin = pin.getText();

        if (!isValidLength(enteredUsername, enteredPin)) {
            showReminder();
            return;
        }

        // --- Use central validator from DatabaseConnector ---
        if (!DatabaseConnector.isValidCredentials(enteredUsername, enteredPin)) {
            FXMLLoader badLoginLoader = new FXMLLoader(getClass().getResource("badlogin.fxml"));
            Parent badLoginRoot = badLoginLoader.load();
            Badlogin badLoginController = badLoginLoader.getController();

            // Pass the typed values to Badlogin
            badLoginController.initialize(enteredUsername, enteredPin);

            Stage badLoginStage = new Stage();
            badLoginStage.setTitle("Bad Login");
            badLoginStage.initModality(Modality.APPLICATION_MODAL);
            badLoginStage.setScene(new Scene(badLoginRoot));
            badLoginStage.showAndWait();
            return;
        }

        // If credentials are valid, open the Player Stats modal
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playerstats.fxml"));
        Parent root = loader.load();
        Playerstats playerstatsController = loader.getController();

        // Fetch user data using the shared connector
        Playerstats playerStatsData = DatabaseConnector.fetchPlayerStats(enteredUsername);
        if (playerStatsData != null) {
            playerstatsController.setPlayerStats(playerStatsData.getRank(), playerStatsData.getCoins());
        } else {
            // Default safe values in case of missing row
            playerstatsController.setPlayerStats(0, 0);
        }

        Stage playerstatsStage = new Stage();
        playerstatsStage.setTitle("Player Stats");
        playerstatsStage.initModality(Modality.APPLICATION_MODAL);
        playerstatsStage.setScene(new Scene(root));
        playerstatsStage.showAndWait();
    }


    private boolean isValidLength(String username, String pin) {
        if (username == null || pin == null) return false;
        boolean ok = username.length() <= 10 && pin.length() == 4;
        if (!ok) showReminder();
        return ok;
    }

    private void showReminder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("10-4Reminder.fxml"));
            Parent root = loader.load();
            Reminder reminderController = loader.getController();

            Stage reminderStage = new Stage();
            reminderStage.initModality(Modality.APPLICATION_MODAL);
            reminderStage.setTitle("Reminder");
            reminderStage.setScene(new Scene(root));
            reminderController.setStage(reminderStage);
            reminderStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save a new user if needed (kept for your flow; uses shared connector)
    private void saveUser(String username, String pin) {
        try {
            if (username == null || pin == null) return;

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

    private boolean isUsernameExists(String username) {
        String sql = "SELECT 1 FROM PlayerCredentials WHERE username = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // be conservative on errors
            return true;
        }
    }

    public void setSoundPlayer(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    @FXML
    public void OffSound(ActionEvent actionEvent) {
        System.out.println("OffSound method called.");
        if (soundPlayer != null) {
            if (offSound.isSelected()) soundPlayer.stop();
            else soundPlayer.play();
        }
    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    @FXML
    public void OffMusic(ActionEvent actionEvent) {
        System.out.println("OffMusic method called.");
        if (musicPlayer != null) {
            if (offMusic.isSelected()) musicPlayer.stop();
            else musicPlayer.play();
        }
    }
}
