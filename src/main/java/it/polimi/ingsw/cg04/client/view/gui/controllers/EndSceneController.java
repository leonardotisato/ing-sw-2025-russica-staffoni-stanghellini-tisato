package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class EndSceneController extends ViewController {

    @FXML
    private TextField nick1;
    @FXML
    private TextField credits1;
    @FXML
    private Circle color1;

    @FXML
    private TextField nick2;
    @FXML
    private TextField credits2;
    @FXML
    private Circle color2;

    @FXML
    private TextField nick3;
    @FXML
    private TextField credits3;
    @FXML
    private Circle color3;

    @FXML
    private TextField nick4;
    @FXML
    private TextField credits4;
    @FXML
    private Circle color4;

    @FXML
    private ImageView backgroundImage;
    @FXML
    private Pane pane;
    @FXML
    private StackPane root;

    private GUIRoot gui;


    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        backgroundImage.setImage(
                new Image(Objects.requireNonNull(getClass().getResource("/images/background2.png")).toExternalForm())
        );
        backgroundImage.setPreserveRatio(false);
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());

        double baseWidth = 960.0;
        double baseHeight = 540.0;

        Scale scale = new Scale();
        pane.getTransforms().add(scale);

        root.widthProperty().addListener((obs, oldVal, newVal) -> updateScale(scale, root.getWidth(), root.getHeight(), baseWidth, baseHeight));
        root.heightProperty().addListener((obs, oldVal, newVal) -> updateScale(scale, root.getWidth(), root.getHeight(), baseWidth, baseHeight));
    }

    private void updateScale(Scale scale, double width, double height, double baseWidth, double baseHeight) {
        double scaleX = width / baseWidth;
        double scaleY = height / baseHeight;
        double scaleFactor = Math.min(scaleX, scaleY);

        scale.setX(scaleFactor);
        scale.setY(scaleFactor);

        double offsetX = (width - baseWidth * scaleFactor) / 2;
        double offsetY = (height - baseHeight * scaleFactor) / 2;
        pane.setTranslateX(offsetX);
        pane.setTranslateY(offsetY);
    }


    public Color mapColor(PlayerColor color) {
        switch (color) {
            case RED: return Color.RED;
            case BLUE: return Color.BLUE;
            case GREEN: return Color.GREEN;
            case YELLOW: return Color.YELLOW;
            default: return Color.GRAY;
        }
    }


    public void update(Game game) {

        List<Player> players = game.getPlayers();
        players.addAll(game.getRetiredPlayers());
        players.sort(Comparator.comparingDouble(Player::getNumCredits).reversed());

        if(players.size() >= 1) {
            nick1.setText(players.getFirst().getName());
            color1.setFill(mapColor(players.getFirst().getColor()));
            credits1.setText(Double.toString(players.getFirst().getNumCredits()));
        }
        if(players.size() >= 2) {
            nick2.setText(players.get(1).getName());
            color2.setFill(mapColor(players.get(1).getColor()));
            credits2.setText(Double.toString(players.get(1).getNumCredits()));
        }
        if(players.size() >= 3) {
            nick3.setText(players.get(2).getName());
            color3.setFill(mapColor(players.get(2).getColor()));
            credits3.setText(Double.toString(players.get(2).getNumCredits()));
        }
        if(players.size() >= 4) {
            nick4.setText(players.get(3).getName());
            color4.setFill(mapColor(players.get(3).getColor()));
            credits4.setText(Double.toString(players.get(3).getNumCredits()));
        }
    }
    public void goToEndScene(GUIRoot gui) {
        return;
    }
}

