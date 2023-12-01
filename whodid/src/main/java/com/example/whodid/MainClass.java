package com.example.whodid;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;

public class MainClass {
    private SoundPlayer soundPlayer;
    private MusicPlayer musicPlayer;
    @javafx.fxml.FXML
    private Button drinkbtn;
    @javafx.fxml.FXML
    private Button runbtn;
    @javafx.fxml.FXML
    private Button rightbtn;
    @javafx.fxml.FXML
    private Button leftbtn;
    @javafx.fxml.FXML
    private Button upbtn;
    @javafx.fxml.FXML
    private Button downbtn;
    @javafx.fxml.FXML
    private Button fightbtn;
    @javafx.fxml.FXML
    private Button helpbtn;
    @javafx.fxml.FXML
    private Button exitbtn;
    @javafx.fxml.FXML
    private Label playernamelbl;
    @javafx.fxml.FXML
    private Label coinslbl;
    @javafx.fxml.FXML
    private Label energylbl;
    private EventObject event;
    private static String username;
    private String playerName;



    public void setPlayerName(String playerName) {
        System.out.println("Setting player name in MainClass: " + playerName);
        this.playerName = playerName;
        // Assuming playernamelbl is the Label in your Main.fxml file
        playernamelbl.setText(playerName);
    }





    public void initialize() {
        // Replace the path with the correct path to your button click sound file
        URL soundURL = getClass().getResource("/music and sound/Mouse ClickSOUND EFFECT HD shorts.wav");
        soundPlayer = new SoundPlayer(soundURL);
    }


    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }
    public void setSoundPlayer(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }



    @javafx.fxml.FXML
    public void exitgame(ActionEvent actionEvent) throws IOException {
        soundPlayer.play();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Loginpage.fxml"));
        Parent root = loader.load();
        Loginpage loginpageController = loader.getController();
        loginpageController.setMusicPlayer(musicPlayer);
        loginpageController.setSoundPlayer(soundPlayer);


        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @javafx.fxml.FXML
    public void collectenergy(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void runaway(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void rightdirection(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void leftdirection(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void updirection(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void downdirection(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void fightmonster(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void gethelp(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void username(Event event) {

    }

    @javafx.fxml.FXML
    public void goldcollected(Event event) {
    }

    @javafx.fxml.FXML
    public void energycollected(Event event) {
    }
}
