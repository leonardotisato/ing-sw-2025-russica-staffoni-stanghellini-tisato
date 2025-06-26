package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewOthersSceneController extends ViewController{

    private final double BASE_WIDTH = 960;
    private final double BASE_HEIGHT = 540;

    @FXML
    private GridPane shipGrid1;

    @FXML
    private GridPane shipGrid2;

    @FXML
    private GridPane shipGrid3;

    @FXML
    private GridPane shipGrid4;

    @FXML
    private ImageView back1;

    @FXML
    private ImageView back2;

    @FXML
    private ImageView back3;

    @FXML
    private ImageView back4;

    @FXML
    private TextField nick1;

    @FXML
    private TextField nick2;

    @FXML
    private TextField nick3;

    @FXML
    private TextField nick4;

    @FXML
    private StackPane root;

    @FXML
    private Group scalableGroup;

    private List<TextField> nicknames;

    private List<ImageView> backgrounds;

    private List<GridPane> shipGridList;

    private GUIRoot gui;

    /**
     * Sets the GUI root reference for this view others scene controller.
     * This method establishes the connection between the controller and the main GUI application,
     * enabling the controller to communicate with other GUI components and handle navigation.
     *
     * @param gui the GUIRoot instance that manages the overall GUI application
     */
    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    /**
     * Initializes the view others scene controller after the FXML file has been loaded.
     * This method sets up responsive scaling behavior by adding listeners to the root container's
     * dimensions and creates lists for easy access to UI components (ship grids, nicknames, backgrounds).
     * The lists facilitate iterating through multiple player displays in the update methods.
     *
     * @param url the location used to resolve relative paths for the root object, or null if unknown
     * @param resourceBundle the resources used to localize the root object, or null if not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.widthProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        root.heightProperty().addListener((obs, oldVal, newVal) -> scaleUI());

        shipGridList = List.of(shipGrid1, shipGrid2, shipGrid3, shipGrid4);

        nicknames = List.of(nick1, nick2, nick3, nick4);

        backgrounds = List.of(back1, back2, back3, back4);
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
     * Updates the view with current game state, displaying all players' ships and information.
     * This method iterates through all players in the game, loads the appropriate ship background
     * image based on the game level, updates each player's ship grid with their current tiles,
     * and displays player nicknames. The method handles image loading errors gracefully and
     * ensures all player information is properly synchronized with the current game state.
     *
     * @param game the Game object containing current state and player information
     */
    @Override
    public void update(Game game) {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            String resourcePath = "/images/cardboard/ship" + game.getLevel() + ".jpg";
            try {
                Image img = new Image(
                        Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                );
                backgrounds.get(i).setImage(img);
            } catch (Exception e) {
                System.err.println("Immagine non trovata: " + resourcePath);
                e.printStackTrace();
            }

            Ship playerShip = game.getPlayers().get(i).getShip();
            updateShipGrid(playerShip, i, game.getLevel());
            nicknames.get(i).setText(game.getPlayers().get(i).getName());
        }
    }

    /**
     * Updates a specific ship grid with tiles from the given ship, handling level-specific positioning.
     * This method iterates through all grid nodes, calculates the correct matrix coordinates
     * considering level-specific offset rules (level 1 excludes columns 0 and 6), and displays
     * the appropriate tile images with correct rotation. Empty positions are cleared, while
     * occupied positions show the tile image rotated according to the tile's rotation value.
     *
     * @param ship the Ship object containing the tile matrix to display
     * @param idx the index of the player/grid to update (0-3)
     * @param level the game level affecting grid positioning and display rules
     */
    public void updateShipGrid(Ship ship, int idx, int level) {
        Tile[][] shipMatrix = ship.getTilesMatrix();

        for (Node node : shipGridList.get(idx).getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            int tempcol = colIndex == null ? 0 : colIndex;
            int row = rowIndex == null ? 0 : rowIndex;


            if(level == 1 && (tempcol == 0 || tempcol == 6)) {
                continue;
            } else if (level == 1) {
                tempcol = tempcol - 1;
            }

            int col = tempcol;

            ImageView cell = (ImageView) node;
            Tile tile = shipMatrix[row][col];

            if (tile == null) {
                cell.setImage(null);
            }
            else {
                String resourcePath = "/images/tiles/GT-new_tiles_16_for web" + tile.getId() + ".jpg";
                try {
                    Image img = new Image(
                            Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                    );
                    cell.setImage(img);
                    cell.setRotate(tile.getRotation() * 90);
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + resourcePath);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Handles the back button action to return to the home scene.
     * This method is triggered when the user clicks the back button and calls
     * the GUI root's home method to navigate back to the main game interface.
     */
    @FXML
    public void back() {
        gui.home();
    }

    /**
     * Placeholder method for navigation to the view others scene.
     * Currently returns immediately without performing any action.
     * This method appears to be intended for scene transition logic but is not implemented.
     *
     * @param gui the GUIRoot instance for potential scene management operations
     * @throws IOException if an I/O error occurs during scene transition (though not currently thrown)
     */
    @Override
    public void goToViewOthersScene(GUIRoot gui) throws IOException {
        return;
    }

}