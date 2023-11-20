package com.example.whodid;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;
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

    @Deprecated
    public void oninstructionbtnClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("instructions.fxml"));
        Parent root = loader.load();


        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @javafx.fxml.FXML
    public void startgame(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
