package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class LobbySceneController extends ViewController {

    private final double BASE_WIDTH = 960;
    private final double BASE_HEIGHT = 540;

    @FXML
    private StackPane root;
    @FXML
    private Group scalableGroup;

    private static final double DOT_RADIUS = 30;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label levelLabel;
    @FXML
    private Label maxPlayersLabel;
    @FXML
    private TextArea logs;

    @FXML
    private GridPane infoGrid;

    private GUIRoot gui;

    /**
     * Initializes the controller after the root element has been completely processed.
     * This method sets up responsive scaling behavior by adding listeners to the root container's
     * dimensions, configures the info grid layout with proper column constraints, spacing, and padding.
     * The grid is designed to display player information with circular indicators and names.
     *
     * @param url the location used to resolve relative paths for the root object, or null if not known
     * @param rb the resource bundle used to localize the root object, or null if not provided
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.widthProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        root.heightProperty().addListener((obs, oldVal, newVal) -> scaleUI());

        infoGrid.getRowConstraints().clear();

        infoGrid.setHgap(12);
        infoGrid.setVgap(24);
        infoGrid.setPadding(new Insets(12));

        ColumnConstraints c0 = new ColumnConstraints(1.8 * DOT_RADIUS, 1.8 * DOT_RADIUS, 1.8 * DOT_RADIUS);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setHgrow(Priority.ALWAYS);
        infoGrid.getColumnConstraints().setAll(c0, c1);
    }

    /**
     * Scales the UI elements to maintain proper aspect ratio and centering during window resizing.
     * This method calculates the appropriate scale factor based on the current window dimensions
     * compared to the base dimensions (960x540), applies uniform scaling to the scalable group,
     * and centers the scaled content within the available space. The scaling preserves the original
     * aspect ratio by using the smaller of the horizontal or vertical scale factors.
     */
    private void scaleUI() {
        double scaleX = root.getWidth() / BASE_WIDTH;
        double scaleY = root.getHeight() / BASE_HEIGHT;
        double scale = Math.min(scaleX, scaleY);
        scalableGroup.setScaleX(scale);
        scalableGroup.setScaleY(scale);
        double offsetX = (root.getWidth() - BASE_WIDTH * scale) / 2;
        double offsetY = (root.getHeight() - BASE_HEIGHT * scale) / 2;

        scalableGroup.setLayoutX(offsetX);
        scalableGroup.setLayoutY(offsetY);
    }

    /**
     * Sets the GUI root reference for this lobby scene controller.
     * This method establishes the connection between the controller and the main GUI application.
     * Currently used for potential future functionality such as adding a disconnect button.
     *
     * @param gui the GUIRoot instance that manages the overall GUI application
     */
    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    /**
     * Refreshes the lobby view with updated game information from the current game state.
     * This method updates the game level and maximum players display, sets the appropriate
     * background image based on the game level, clears and rebuilds the player list with
     * colored circular indicators and names, and adds placeholder spots for remaining player slots.
     * The method handles image loading errors gracefully.
     *
     * @param game the Game object containing the current lobby state and player information
     */
    @Override
    public void update(Game game) {
        // GameLevel and MaxPlayers indicators
        levelLabel.setText(String.valueOf(game.getLevel()));
        maxPlayersLabel.setText(String.valueOf(game.getMaxPlayers()));

        infoGrid.getChildren().clear();
        int level = game.getLevel();

        String backgroundPath = "/images/background" + level + ".png";
        try {
            Image backgroundImg = new Image(
                    Objects.requireNonNull(getClass().getResource(backgroundPath)).toExternalForm()
            );

            BackgroundImage backgroundImage = new BackgroundImage(
                    backgroundImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(backgroundImage));
        }
        catch (Exception e) {
            System.err.println("Immagine non trovata: " + backgroundPath);
            e.printStackTrace();
        }

        // build specific player
        int row = 0;
        for (Player p : game.getPlayers()) {
            Circle dot = new Circle(DOT_RADIUS, setColor(p.getColor()));
            dot.setStroke(Color.BLACK);
            infoGrid.add(dot, 0, row);

            Label name = new Label(p.getName());
            name.setMaxWidth(Double.MAX_VALUE);
            name.setWrapText(true);
            GridPane.setHgrow(name, Priority.ALWAYS);
            infoGrid.add(name, 1, row);

            row++;
        }

        // free spaces placeholders, as many as MaxPlayers for the specific game
        int free = game.getMaxPlayers() - game.getPlayers().size();
        for (int i = 0; i < free; i++) {
            Circle empty = new Circle(DOT_RADIUS, Color.TRANSPARENT);
            empty.setStroke(Color.GREY);
            infoGrid.add(empty, 0, row++);
        }
    }

    /**
     * Converts a PlayerColor enumeration value to the corresponding JavaFX Color object.
     * This utility method provides the visual representation color for each player color type,
     * using more visually appealing color variants (e.g., DEEPSKYBLUE instead of BLUE).
     *
     * @param pc the PlayerColor enumeration value to be converted
     * @return the corresponding JavaFX Color object with enhanced visual appearance
     */
    private Color setColor(PlayerColor pc) {
        return switch (pc) {
            case BLUE -> Color.DEEPSKYBLUE;
            case YELLOW -> Color.GOLD;
            case GREEN -> Color.LIMEGREEN;
            case RED -> Color.CRIMSON;
        };
    }

    /**
     * Updates the logs display with the provided list of log messages.
     * This method concatenates all log lines into a single string and updates the logs text area
     * on the JavaFX Application Thread to ensure thread safety. The method handles null inputs
     * gracefully by returning early if either the log lines or logs component is null.
     *
     * @param logLines the list of log message strings to be displayed, or null if no logs available
     */
    @Override
    public void showLogs(List<String> logLines) {
        if (logLines == null || logs == null) return;

        StringBuilder sb = new StringBuilder();
        for (String line : logLines) {
            sb.append(line).append("\n");
        }

        Platform.runLater(() -> logs.setText(sb.toString()));
    }

    /**
     * Placeholder method for navigation to the lobby scene.
     * Currently returns immediately without performing any action.
     * This method appears to be intended for scene transition logic but is not implemented.
     *
     * @param gui the GUIRoot instance for potential scene management operations
     */
    @Override
    public void goToLobbyScene(GUIRoot gui){
        return;
    }
}
