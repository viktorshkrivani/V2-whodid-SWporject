package com.example.whodid;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static com.example.whodid.Instructions.soundPlayer;


public class Playerstats {
    private int ranking;  // Updated from "rank" to "ranking"
    private int goldCollected;

    @javafx.fxml.FXML
    private Label rankid;
    @javafx.fxml.FXML
    private Label coinid;
    @javafx.fxml.FXML
    private Button continuebtn;
    private String playerName;
    @javafx.fxml.FXML
    private TextField hidentext;

    public Playerstats() {


    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        System.out.println("Player name received: " + playerName);

    }

    public Playerstats(String username, int ranking, int goldCollected) {
        this.ranking = ranking;
        this.goldCollected = goldCollected;
    }

    public void initialize() {
        // Replace the path with the correct path to your button click sound file
        URL soundURL = getClass().getResource("/music and sound/Mouse ClickSOUND EFFECT HD shorts.wav");
        soundPlayer = new SoundPlayer(soundURL);
        System.out.println("Playerstats controller initialized");

    }




    public Playerstats(int ranking, int goldCollected) {
        this.ranking = ranking;
        this.goldCollected = goldCollected;
    }


    public void setPlayerStats(int rank, int coins) {
        ranking = rank;  // Corrected from "rank" to "ranking"
        goldCollected = coins;
        rankid.setText(String.valueOf(ranking));
        coinid.setText(String.valueOf(goldCollected));
    }

    public int getRank() {
        return ranking;
    }

    public int getCoins() {
        return goldCollected;
    }

    @javafx.fxml.FXML
    public void existingusercontinue(ActionEvent actionEvent) {

        soundPlayer.play();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();

            // If MainClass has a constructor that accepts dependencies (e.g., SoundPlayer, MusicPlayer),
            // you can pass them here before calling the controller's methods.
            MainClass mainClassController = loader.getController();


            mainClassController.setPlayerName(playerName);


            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
    }
