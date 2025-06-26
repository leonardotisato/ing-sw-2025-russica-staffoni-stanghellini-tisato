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

    /**
     * Sets the GUI root reference for this build scene controller.
     * This method establishes the connection between the controller and the main GUI class,
     * enabling communication with the game client and other GUI components.
     *
     * @param gui the GUIRoot instance that manages the overall GUI state and client communication
     */
    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    /**
     * Initializes the end scene controller after the FXML file has been loaded.
     * This method sets up the background image, configures its scaling properties to fit the container,
     * and establishes responsive scaling behavior that maintains aspect ratio when the window is resized.
     * The scaling system uses a base resolution of 960x540 pixels as reference.
     *
     * @param url the location used to resolve relative paths for the root object, or null if unknown
     * @param resourceBundle the resources used to localize the root object, or null if not localized
     */
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

    /**
     * Updates the scale transformation of the UI pane to maintain proper aspect ratio during window resizing.
     * This method calculates the appropriate scale factor based on the current window dimensions
     * and the base dimensions, then centers the pane within the available space.
     * The scaling preserves the original aspect ratio by using the smaller of the two scale factors.
     *
     * @param scale the Scale transformation object to be updated
     * @param width the current width of the container
     * @param height the current height of the container
     * @param baseWidth the reference width used for scaling calculations (960.0)
     * @param baseHeight the reference height used for scaling calculations (540.0)
     */
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

    /**
     * Maps a PlayerColor enumeration value to the corresponding JavaFX Color object.
     * This method provides the visual representation color for each player color type,
     * enabling proper display of player colors in the UI components.
     *
     * @param color the PlayerColor enumeration value to be converted
     * @return the corresponding JavaFX Color object (RED, BLUE, GREEN, or YELLOW)
     */
    public Color mapColor(PlayerColor color) {
        return switch (color) {
            case RED -> Color.RED;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.GREEN;
            case YELLOW -> Color.YELLOW;
        };
    }

    /**
     * Updates the end scene display with the final game results and player rankings.
     * This method retrieves all players (including retired ones), sorts them by credits in descending order,
     * and populates the UI fields with player information (name, color, and credits) for up to 4 players.
     * The display shows the final leaderboard with the highest-scoring player first.
     *
     * @param game the Game object containing the final state with all players and their scores
     */
    public void update(Game game) {

        List<Player> players = game.getPlayers();

        for (Player retired : game.getRetiredPlayers()) {
            if (!players.contains(retired)) {
                players.add(retired);
            }
        }

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

    /**
     * Placeholder method for navigation to the end scene.
     * Currently returns immediately without performing any action.
     * This method appears to be intended for scene transition logic but is not implemented.
     *
     * @param gui the GUIRoot instance for potential scene management operations
     */
    public void goToEndScene(GUIRoot gui) {
        return;
    }
}

