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
import java.util.EventObject;

public class Loginpage {


    @javafx.fxml.FXML
    private Button instructionsbtn;
    @javafx.fxml.FXML
    private Button pickhardbtn;
    @javafx.fxml.FXML
    private Button pickeasybtn;
    @javafx.fxml.FXML
    private Button pickmedbtn;
    @javafx.fxml.FXML
    private ToggleButton offSound;
    @javafx.fxml.FXML
    private ToggleButton offMusic;
    @javafx.fxml.FXML
    private PasswordField pin;
    @javafx.fxml.FXML
    private TextField playername;
    @javafx.fxml.FXML
    private TextField monstername;
    @javafx.fxml.FXML
    private Button startbtn;
    private EventObject actionEvent;
    private MusicPlayer musicPlayer;
    private SoundPlayer soundPlayer;




    public void initialize() {
        // Replace the path with the correct path to your button click sound file
        URL soundURL = getClass().getResource("/music and sound/Mouse ClickSOUND EFFECT HD shorts.wav");
        soundPlayer = new SoundPlayer(soundURL);

    }

    private static final String JDBC_URL = "jdbc:sqlite:credentialsdb.db";


    @Deprecated
    public void oninstructionbtnClick(ActionEvent actionEvent) throws IOException {
        soundPlayer.play();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("instructions.fxml"));
        Parent root = loader.load();
        Instructions instructionsController = loader.getController();
        instructionsController.setMusicPlayer(musicPlayer); //connects the two together
        instructionsController.setSoundPlayer(soundPlayer); // Connects the sound player


        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);

        stage.show();
    }

    @javafx.fxml.FXML
    public void startgame(Event event) throws IOException {
        soundPlayer.play();
        String enteredUsername = playername.getText();
        String enteredPin = pin.getText();
        if (!isValidLength(enteredUsername, enteredPin)) {
            showReminder();
            return;
        }

        if (!isValidCredentials(enteredUsername, enteredPin)) {
            // Show the BadLoginController with an appropriate message
            FXMLLoader badLoginLoader = new FXMLLoader(getClass().getResource("badlogin.fxml"));
            Parent badLoginRoot = badLoginLoader.load();
            Badlogin badLoginController = badLoginLoader.getController();

            // Instead, directly set the username in MainClass

            // Pass the enteredUsername and enteredPin to Badlogin
            badLoginController.initialize(enteredUsername, enteredPin);

            // Create a new stage for the bad login screen
            Stage badLoginStage = new Stage();
            badLoginStage.setTitle("Bad Login");
            badLoginStage.initModality(Modality.APPLICATION_MODAL);
            badLoginStage.setScene(new Scene(badLoginRoot));

            // Show the new stage and wait for user interaction
            badLoginStage.showAndWait();
            return;
        }

        // If credentials are valid, show playerstats.fxml content in a new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playerstats.fxml"));
        Parent root = loader.load();
        Playerstats playerstatsController = loader.getController();


        // Fetch user data from the database and set it in Playerstats controller
        Playerstats playerStatsData = DatabaseConnector.fetchPlayerStats(enteredUsername);
        if (playerStatsData != null) {
            playerstatsController.setPlayerStats(playerStatsData.getRank(), playerStatsData.getCoins());
        }

        // Create a new stage and set its content to the loaded playerstats.fxml
        Stage playerstatsStage = new Stage();
        playerstatsStage.setTitle("Player Stats");
        playerstatsStage.initModality(Modality.APPLICATION_MODAL);
        playerstatsStage.setScene(new Scene(root));

        // Show the new stage and wait for user interaction
        playerstatsStage.showAndWait();


    }



    private Playerstats fetchPlayerStats(String username) {
        String jdbcUrl = "jdbc:sqlite:credentialsdb.db";

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             PreparedStatement statement = connection.prepareStatement("SELECT goldCollected, " +
                     "(SELECT COUNT(*) + 1 FROM PlayerStats AS p2 WHERE p2.goldCollected > p1.goldCollected) AS ranking " +
                     "FROM PlayerStats AS p1 WHERE username = ?")) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int rank = resultSet.getInt("ranking");
                int coins = resultSet.getInt("goldCollected");
                return new Playerstats(rank, coins);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return a default instance if data retrieval fails
        return new Playerstats(0, 0);
    }





    private boolean isValidLength(String username, String pin) {
        boolean isValid = true;
        if (username.length() > 10 || pin.length() != 4) {
            isValid = false;
            showReminder();
        }
        return isValid;
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




    private boolean isValidCredentials(String enteredUsername, String enteredPin) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM PlayerCredentials WHERE username = ? AND pin = ?")) {

            statement.setString(1, enteredUsername);
            statement.setString(2, enteredPin);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If there is a matching user, credentials are valid

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle the exception appropriately
        }
    }

    // Method to save user credentials
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




    public void setSoundPlayer(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }


    @FXML
    public void OffSound(ActionEvent actionEvent) {
        System.out.println("OffSound method called.");
        if (soundPlayer != null) {
            if (offSound.isSelected()) {
                // ToggleButton is selected, stop the sound
                soundPlayer.stop();
            } else {
                // ToggleButton is not selected, play the sound
                soundPlayer.play();
            }
        }

    }

    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }


    @javafx.fxml.FXML
    public void OffMusic(ActionEvent actionEvent) {
            System.out.println("OffMusic method called.");
            if (musicPlayer != null) {
                if (offMusic.isSelected()) {
                    // ToggleButton is selected, stop the music
                    musicPlayer.stop();
                } else {
                    // ToggleButton is not selected, play the music
                    musicPlayer.play();
                }
            }

    }

}


