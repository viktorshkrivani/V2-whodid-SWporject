package com.example.whodid;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Random;

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
    @javafx.fxml.FXML
    private ImageView img1;
    @javafx.fxml.FXML
    private ImageView img2;
    @javafx.fxml.FXML
    private ImageView img3;
    @javafx.fxml.FXML
    private ImageView collectEnergyButton;
    @javafx.fxml.FXML
    private Button helpButton;


    public void setPlayerName(String playerName) {
        System.out.println("Setting player name in MainClass: " + playerName);
        this.playerName = playerName;
        // Assuming playernamelbl is the Label in your Main.fxml file
        playernamelbl.setText(playerName);

        // Set initial values for energy and coins
        energylbl.setText("50");
        coinslbl.setText("0");
    }

    private enum ImageType {
        MONSTER("/images/maschineMonster.png"),
        COIN("/images/Machinecoin.png"),
        ENERGY("/images/MachineEnergy.png");

        private final String path;

        ImageType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    private ImageType[] slotImageTypes = {
            ImageType.MONSTER,
            ImageType.COIN,
            ImageType.ENERGY
            // Add more types as needed
    };
    private ImageType[] displayedImages = new ImageType[3];
    private boolean canCollect = true;
    private boolean isMonsterDisplayed = false;


    private void spinImages() {
        if (!isMonsterDisplayed) {
            Random random = new Random();

            // Set random images to the ImageViews
            int monsterIndex = random.nextInt(slotImageTypes.length);
            int coinIndex = random.nextInt(slotImageTypes.length);
            int energyIndex = random.nextInt(slotImageTypes.length);

            displayedImages[0] = slotImageTypes[monsterIndex];
            displayedImages[1] = slotImageTypes[coinIndex];
            displayedImages[2] = slotImageTypes[energyIndex];

            if (displayedImages[0] == ImageType.MONSTER ||
                    displayedImages[1] == ImageType.MONSTER ||
                    displayedImages[2] == ImageType.MONSTER) {
                isMonsterDisplayed = true;
            }

            img1.setImage(new Image(slotImageTypes[monsterIndex].getPath()));
            img2.setImage(new Image(slotImageTypes[coinIndex].getPath()));
            img3.setImage(new Image(slotImageTypes[energyIndex].getPath()));

            canCollect = true;
        }
    }

    private void handleImage(ImageType imageType) {
        Random random = new Random();
        int minCoinsToAdd = 5;
        int maxCoinsToAdd = 5;

        switch (imageType) {
            case MONSTER:
                // Add logic for monster image
                int energyToReduce = random.nextInt(1) + 3;
                decreaseEnergy(energyToReduce);
                break;
            case COIN:
                increaseCoins(1);
                break;
            case ENERGY:
                // Add logic for energy image
                int energyToAdd = random.nextInt(1) + 3;
                increaseEnergy(energyToAdd);
                break;
            // Add more cases as needed
        }
    }


    private void decreaseEnergy(int value) {
        int currentEnergy = Integer.parseInt(energylbl.getText());
        int newEnergy = Math.max(0, currentEnergy - value);
        energylbl.setText(String.valueOf(newEnergy));

        if (newEnergy <= 0) {
            try {
                showTryAgainScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showTryAgainScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tryagain.fxml"));
        Parent root = loader.load();
        Tryagainpg tryagainpg = loader.getController();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();


        Stage currentStage = (Stage) drinkbtn.getScene().getWindow();
        currentStage.close();

        int collectedGold = 0;
        try {
            collectedGold = Integer.parseInt(coinslbl.getText().trim()); // Get the collected gold
        } catch (NumberFormatException e) {
            System.err.println("Error parsing collected gold: " + e.getMessage());
        }


        if (collectedGold > 0) {
            DatabaseConnector.updateGold(username, collectedGold);
        }
    }


    private void increaseCoins(int value) {
        int currentCoins = Integer.parseInt(coinslbl.getText());
        int newCoins = currentCoins + value;
        coinslbl.setText(String.valueOf(newCoins));


        int goldEquivalent = value * 1; // Replace '1' with your conversion rate
        DatabaseConnector.updateGold(username, goldEquivalent);
    }

    private void increaseEnergy(int value) {
        int currentEnergy = Integer.parseInt(energylbl.getText());
        energylbl.setText(String.valueOf(currentEnergy + value));
    }


    public void initialize() {
        URL soundURL = getClass().getResource("/music and sound/Mouse ClickSOUND EFFECT HD shorts.wav");
        soundPlayer = new SoundPlayer(soundURL);

        coinslbl.textProperty().addListener((observable, oldValue, newValue) -> {
            goldCollectedChanged();
        });
    }

    private void goldCollectedChanged() {
        int updatedGold = Integer.parseInt(coinslbl.getText());
        DatabaseConnector.updateGold(username, updatedGold);
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

        if (canCollect && !isMonsterDisplayed) {
            // Retrieve the values of the displayed images when collectenergy button is pressed
            for (ImageType image : displayedImages) {
                handleImage(image); // Apply effects of displayed images
            }
            // Clear the displayed images array after collecting values
            Arrays.fill(displayedImages, null);

            // Disable collectenergy button after collecting values
            canCollect = false;
        }
    }


    @javafx.fxml.FXML
    public void runaway(ActionEvent actionEvent) {
        if (isMonsterDisplayed) {
            int energyToDeduct = 7;
            decreaseEnergy(energyToDeduct);
            // Reset isMonsterDisplayed flag to allow spinning again
            isMonsterDisplayed = false;
            // Add any other logic related to running away
        }
    }
    @javafx.fxml.FXML
    public void rightdirection(ActionEvent actionEvent) {
        spinImages();
    }

    @javafx.fxml.FXML
    public void leftdirection(ActionEvent actionEvent) {
        spinImages();
    }

    @javafx.fxml.FXML
    public void updirection(ActionEvent actionEvent) {
        spinImages();
    }

    @javafx.fxml.FXML
    public void downdirection(ActionEvent actionEvent) {
        spinImages();
    }

    @javafx.fxml.FXML
    public void fightmonster(ActionEvent actionEvent) {
        if (isMonsterDisplayed) {
            Random random = new Random();
            int playerSuccess = random.nextInt(100);

            if (playerSuccess < 35) {

                int energyToAdd = random.nextInt(6) + 5;
                increaseEnergy(energyToAdd);
            } else {

                int energyToReduce = 10; // Lose 10 energy
                decreaseEnergy(energyToReduce);
            }


            collectenergy(actionEvent);


            isMonsterDisplayed = false;
        }
    }

    private int helpCount = 2;
    @javafx.fxml.FXML
    public void gethelp(ActionEvent actionEvent) {
        if (helpCount > 0) {
            Random random = new Random();
            int energyToAdd = random.nextInt(21) + 10;

            increaseEnergy(energyToAdd);
            helpCount--;

            if (helpCount == 0) {
                helpButton.setDisable(true);
            }
        }
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
