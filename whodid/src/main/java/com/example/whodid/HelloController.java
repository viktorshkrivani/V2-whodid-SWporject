package com.example.whodid;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class HelloController {
    private static MusicPlayer musicPlayer;
    private static SoundPlayer soundPlayer;


    @FXML
    private Button startbtn;
    private EventObject event;

    public void initialize() {
        // Replace "/music and sound/SleazyWorld Go Lil Baby Sleazy Flow Remix Instrumental.wav"
        // with the correct path to your audio file
        URL resourceURL = getClass().getResource("/music and sound/SleazyWorld Go Lil Baby Sleazy Flow Remix Instrumental.wav");
        if (resourceURL != null) {
            musicPlayer = new MusicPlayer(resourceURL);
            musicPlayer.play(); // Start playing music when the controller is initialized
        } else {
            System.err.println("Error loading audio resource.");
        }
        //Replace the path with the correct path to your button click sound file
        URL soundURL = getClass().getResource("/music and sound/Mouse ClickSOUND EFFECT HD shorts.wav");
        if (soundURL != null) {
            soundPlayer = new SoundPlayer(soundURL);
            soundPlayer.play();
        } else {
            System.err.println("Error loading sound resource.");
        }
    }



    @FXML
    public void onstartbtnClick(ActionEvent actionEvent) throws IOException {
        soundPlayer.play();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginpage.fxml"));
        Parent root = loader.load();
        Loginpage loginpageController = loader.getController();
        loginpageController.setMusicPlayer(musicPlayer); //connects the two together
        loginpageController.setSoundPlayer(soundPlayer); // Connects the sound player


        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
