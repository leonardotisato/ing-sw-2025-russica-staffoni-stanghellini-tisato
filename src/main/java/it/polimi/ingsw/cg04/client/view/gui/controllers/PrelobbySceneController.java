package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.shape.StrokeType;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

public class PrelobbySceneController extends ViewController {

    private final double BASE_WIDTH = 960;
    private final double BASE_HEIGHT = 540;

    @FXML
    private StackPane root;
    @FXML
    private Group scalableGroup;


    @FXML
    public AnchorPane anchorPane;
    /**
     * circles, on click set player color
     */
    @FXML
    private Circle blueCircle;
    @FXML
    private Circle yellowCircle;
    @FXML
    private Circle greenCircle;
    @FXML
    private Circle redCircle;

    /**
     * game level buttons, on click set game level 1 or 2
     */
    @FXML
    private ToggleButton level1Button;
    @FXML
    private ToggleButton level2Button;

    private ToggleGroup levelGroup;

    /**
     * number of player buttons, on click set number of players 2,3 or 4
     */
    @FXML
    private ToggleButton player2Button;
    @FXML
    private ToggleButton player3Button;
    @FXML
    private ToggleButton player4Button;

    private ToggleGroup playerCountGroup;

    /**
     * create game button, on click create a game with selected options and send action
     */
    @FXML
    private Button createGameButton;

    /**
     * existing games pane, on click join an existing game
     */
    @FXML
    private ScrollPane gamesScrollPane;

    private VBox gamesListContainer;

    /**
     * selected options fromn client
     * can join game only if color is set
     * can create game only if all 3 of them are set
     */
    private String selectedColor;
    private int selectedLevel;
    private int selectedPlayerCount;

    private List<String> allColors = List.of("BLUE", "YELLOW", "GREEN", "RED");

    private GUIRoot gui;

    /**
     * Sets the GUI root reference for this pre-lobby scene controller.
     * This method establishes the connection between the controller and the main GUI application,
     * enabling the controller to communicate with other GUI components and handle game operations.
     *
     * @param gui the GUIRoot instance that manages the overall GUI application
     */
    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    /**
     * Initializes the pre-lobby scene controller after the FXML file has been loaded.
     * This method sets up responsive scaling behavior, creates toggle groups for game level
     * and player count selection, configures default selections (level 1, 2 players),
     * adds listeners for visual feedback on toggle changes with golden drop shadow effects,
     * sets up the games list container, and binds mouse click events to color selection circles
     * and the create game button.
     *
     * @param location the location used to resolve relative paths for the root object, or null if unknown
     * @param resources the resources used to localize the root object, or null if not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        root.widthProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        root.heightProperty().addListener((obs, oldVal, newVal) -> scaleUI());

        levelGroup = new ToggleGroup();
        level1Button.setToggleGroup(levelGroup);
        level2Button.setToggleGroup(levelGroup);

        playerCountGroup = new ToggleGroup();
        player2Button.setToggleGroup(playerCountGroup);
        player3Button.setToggleGroup(playerCountGroup);
        player4Button.setToggleGroup(playerCountGroup);

        level1Button.setSelected(true);
        player2Button.setSelected(true);
        selectedLevel = 1;
        selectedPlayerCount = 2;

        levelGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == level1Button) {
                level1Button.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
                level2Button.setStyle("");
                selectedLevel = 1;
                System.out.println("selected level 1");
            } else if (newToggle == level2Button) {
                level2Button.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
                level1Button.setStyle("");
                selectedLevel = 2;
                System.out.println("selected level 2");
            }
        });

        playerCountGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == player2Button) {
                player2Button.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
                player3Button.setStyle("");
                player4Button.setStyle("");
                selectedPlayerCount = 2;
                System.out.println("selected 2 players");
            } else if (newToggle == player3Button) {
                player3Button.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
                player2Button.setStyle("");
                player4Button.setStyle("");
                selectedPlayerCount = 3;
                System.out.println("selected 3 players");
            } else if (newToggle == player4Button) {
                player4Button.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
                player2Button.setStyle("");
                player3Button.setStyle("");
                selectedPlayerCount = 4;
                System.out.println("selected 4 players");
            }
        });

        gamesListContainer = new VBox(10);
        gamesScrollPane.setContent(gamesListContainer);

        blueCircle.setOnMouseClicked(e -> setColor("BLUE"));
        yellowCircle.setOnMouseClicked(e -> setColor("YELLOW"));
        greenCircle.setOnMouseClicked(e -> setColor("GREEN"));
        redCircle.setOnMouseClicked(e -> setColor("RED"));

        createGameButton.setOnAction(e -> {
            try {
                onCreateGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // load existing games somehow
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
     * Sets the selected player color and updates the visual feedback for color selection circles.
     * This method stores the selected color, resets all circle styles to remove previous highlights,
     * and applies a golden drop shadow effect to the selected color circle to provide visual
     * confirmation of the user's choice.
     *
     * @param color the color string to be selected ("BLUE", "YELLOW", "GREEN", or "RED")
     */
    private void setColor(String color) {
        this.selectedColor = color;
        System.out.println("Selected color: " + selectedColor);

        resetCircleStyle(blueCircle);
        resetCircleStyle(yellowCircle);
        resetCircleStyle(greenCircle);
        resetCircleStyle(redCircle);

        switch (color) {
            case "BLUE" -> applyCircleHighlight(blueCircle);
            case "YELLOW" -> applyCircleHighlight(yellowCircle);
            case "GREEN" -> applyCircleHighlight(greenCircle);
            case "RED" -> applyCircleHighlight(redCircle);
        }
    }

    /**
     * Handles the creation of a new game with the selected configuration options.
     * This method validates that all required selections (color, level, and player count) are made,
     * then calls the GUI root to create a game with the specified parameters. If any required
     * selection is missing, the method logs an error and returns without creating the game.
     *
     * @throws IOException if an I/O error occurs during the game creation process
     */
    private void onCreateGame() throws IOException {
        if (selectedColor == null || selectedLevel == 0 || selectedPlayerCount == 0) {
            System.out.println("could not create game, check if color level and numPlayers are set");
            return;
        }
        gui.createGame(selectedColor, selectedLevel, selectedPlayerCount);
        System.out.println("created game: level -> " + selectedLevel + " numPlayers -> " + selectedPlayerCount + " color -> " + selectedColor);

    }

    /**
     * Handles joining an existing game with the selected player color.
     * This method validates that a color has been selected, then calls the GUI root to join
     * the specified game with the chosen color. If no color is selected, the method logs
     * an error and returns without attempting to join the game.
     *
     * @param gameId the string representation of the game ID to join
     * @throws IOException if an I/O error occurs during the game joining process
     */
    private void onJoinGame(String gameId) throws IOException {
        if (selectedColor == null) {
            System.out.println("No color selected, could not join game " + gameId);
            return;
        }

        gui.joinGame(Integer.parseInt(gameId), selectedColor);
        System.out.println("Joining game " + gameId);
    }

    /**
     * Refreshes the list of joinable games displayed in the scroll pane.
     * This method clears the existing games list, filters out full games, and creates
     * buttons for each available game showing game information (ID, level, current/max players)
     * and available colors. Each button is configured with proper styling and click handlers
     * to enable joining the corresponding game.
     *
     * @param games the list of GameInfo objects representing available games to join
     */
    public void refreshJoinableGames(List<Game.GameInfo> games) {
        gamesListContainer.getChildren().clear();

        for (Game.GameInfo g : games) {

            // if game is full don't show it
            if (g.maxPlayers() == g.playerWithColor().size()) break;

            // create button
            int totPlayers = g.playerWithColor().size();

            Collection<String> usedColors = g.playerWithColor().values();
            List<String> availableColors = allColors.stream()
                    .filter(c -> !usedColors.contains(c))
                    .toList();

            StringBuilder colorIcons = new StringBuilder();
            for (String color : availableColors) {
                colorIcons.append(color + "  ");
            }

            String label = "Game: " + g.id() + "    Level: " + g.gameLevel() + "    "
                    + totPlayers + "/" + g.maxPlayers()
                    + " Players\n" + "available colors: " + colorIcons;

            Button btn = new Button(label);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.getStyleClass().add("joinable-game-button");

            btn.setAlignment(javafx.geometry.Pos.CENTER);

            btn.setOnAction(e -> {
                try {
                    onJoinGame(String.valueOf(g.id()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            gamesListContainer.getChildren().add(btn);
        }
    }

    /**
     * Removes all visual styling effects from the specified circle.
     * This utility method resets a color selection circle to its default appearance
     * by clearing any applied CSS styles, typically used before applying highlighting
     * to a newly selected circle.
     *
     * @param circle the Circle object to reset to default styling
     */
    private void resetCircleStyle(Circle circle) {
        circle.setStyle("");
    }

    /**
     * Applies a golden drop shadow highlight effect to the specified circle.
     * This utility method provides visual feedback for color selection by applying
     * a golden drop shadow effect to indicate the currently selected color circle.
     *
     * @param circle the Circle object to highlight with the golden drop shadow effect
     */
    private void applyCircleHighlight(Circle circle) {
        circle.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
    }

    /**
     * Placeholder method for navigation to the pre-lobby scene.
     * Currently returns immediately without performing any action.
     * This method appears to be intended for scene transition logic but is not implemented.
     *
     * @param gui the GUIRoot instance for potential scene management operations
     * @throws IOException if an I/O error occurs during scene transition (though not currently thrown)
     */
    @Override
    public void goToPrelobbyScene(GUIRoot gui) throws IOException {
        return;
    }
}
