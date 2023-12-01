package com.example.whodid;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;

public class Instructions {
    static SoundPlayer soundPlayer;
    private static MusicPlayer musicPlayer;

    @javafx.fxml.FXML
    private Button instructionbackbtn;
    private EventObject event;

    public void initialize() {
        // Replace the path with the correct path to your button click sound file
        URL soundURL = getClass().getResource("/music and sound/Mouse ClickSOUND EFFECT HD shorts.wav");
        soundPlayer = new SoundPlayer(soundURL);
    }
    public void setMusicPlayer(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }



    @javafx.fxml.FXML
    public void oninstructionbtnClick(ActionEvent actionEvent) throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginpage.fxml"));
        Parent root = loader.load();
        Loginpage loginpageController = loader.getController();
        loginpageController.setMusicPlayer(musicPlayer);
        loginpageController.setSoundPlayer(soundPlayer); // Connects the sound player


        // Set the new scene
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void setSoundPlayer(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

}
