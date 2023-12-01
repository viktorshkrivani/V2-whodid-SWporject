package com.example.whodid;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;

import static com.example.whodid.Instructions.soundPlayer;

public class Reminder {
    @javafx.fxml.FXML
    private Button reminder;

    public void initialize() {
        // Replace the path with the correct path to your button click sound file
        URL soundURL = getClass().getResource("/music and sound/Mouse ClickSOUND EFFECT HD shorts.wav");
        soundPlayer = new SoundPlayer(soundURL);

    }
    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @javafx.fxml.FXML
    public void checkcharbtn(ActionEvent actionEvent) {
        soundPlayer.play();
        stage.close();
    }
}
