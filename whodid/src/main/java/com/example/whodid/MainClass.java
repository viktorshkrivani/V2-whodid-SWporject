package com.example.whodid;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventObject;

public class MainClass {
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

    @javafx.fxml.FXML
    public void exitgame(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Loginpage.fxml"));
        Parent root = loader.load();


        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
