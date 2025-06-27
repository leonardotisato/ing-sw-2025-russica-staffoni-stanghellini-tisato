package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class FaceUpSceneController extends ViewController {

    @FXML
    private GridPane gridPane;

    @FXML
    private StackPane root;

    @FXML
    private ImageView background;

    private final List<ImageView> cellViews = new ArrayList<>();
    private GUIRoot gui;

    private int size = 0;

    /**
     * Sets the GUI root reference for this face-up scene controller.
     * This method establishes the connection between the controller and the main GUI application,
     * enabling the controller to communicate with other GUI components and handle user interactions.
     *
     * @param gui the GUIRoot instance that manages the overall GUI application
     */
    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    /**
     * Initializes the face-up scene controller after the FXML file has been loaded.
     * This method sets up the background image, creates a 10x16 grid of interactive cells for displaying tiles,
     * and configures mouse event handlers for tile selection and hover effects. Each cell is bound to
     * the grid's dimensions for responsive scaling and includes visual feedback for user interactions.
     *
     * @param url the location used to resolve relative paths for the root object, or null if unknown
     * @param resourceBundle the resources used to localize the root object, or null if not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.setImage(
                new Image(Objects.requireNonNull(getClass().getResource("/images/background2.png")).toExternalForm())
        );
        background.setPreserveRatio(false);
        background.fitWidthProperty().bind(root.widthProperty());
        background.fitHeightProperty().bind(root.heightProperty());
        background.setMouseTransparent(true);

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 16; col++) {
                final int r = row;
                final int c = col;

                StackPane stackPane = new StackPane();
                stackPane.setPadding(new Insets(5, 5, 5, 5));
                stackPane.prefWidthProperty().bind(gridPane.widthProperty().divide(16));
                stackPane.prefHeightProperty().bind(gridPane.heightProperty().divide(10));

                ImageView imageView = new ImageView();
                imageView.setPreserveRatio(true);
                imageView.fitWidthProperty().bind(stackPane.widthProperty().divide(1.2));
                imageView.fitHeightProperty().bind(stackPane.heightProperty().divide(1.2));
                imageView.setMouseTransparent(true);

                stackPane.setOnMouseClicked(event -> {
                    System.out.println("Click detected at row: " + r + ", col: " + c);
                    handleChoice(r, c);
                });

                stackPane.setOnMouseEntered(event -> {
                    stackPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);");
                });

                stackPane.setOnMouseExited(event -> {
                    stackPane.setStyle("-fx-background-color: transparent;");
                });

                stackPane.getChildren().add(imageView);
                gridPane.add(stackPane, col, row);
                cellViews.add(imageView);
            }
        }
    }


    /**
     * Closes the face-up scene and returns to the previous view.
     * This method is triggered when the user wants to exit the face-up tiles selection screen
     * and notifies the GUI root to handle the scene transition.
     */
    @FXML
    public void close() {
        System.out.println("Faceup scene closed.");
        gui.closeFaceUp();
    }

    /**
     * Handles the selection of a face-up tile at the specified grid position.
     * This method converts the row and column coordinates to a linear index and validates
     * that the selection is within the bounds of available tiles before notifying the GUI
     * root of the user's choice.
     *
     * @param row the row position in the grid (0-based)
     * @param col the column position in the grid (0-based)
     */
    @FXML
    public void handleChoice(int row, int col) {
        int idx = row * 16 + col;
        if(idx < size) {
            System.out.println("Faceup tile selected at row: " + row + ", col: " + col + ", index: " + idx);
            gui.chooseFaceUp(idx);
        }
    }

    /**
     * Updates the face-up tiles display with the current game state.
     * This method retrieves the list of available face-up tiles from the game,
     * clears all existing tile images, and loads new tile images based on their IDs.
     * The method handles image loading errors gracefully and updates the internal
     * size counter to track the number of available tiles.
     *
     * @param game the Game object containing the current state of face-up tiles
     */
    @Override
    public void update(Game game) {
        List<Integer> faceUpTiles = game.getFaceUpTiles();
        size = faceUpTiles.size();

        cellViews.forEach(iv -> iv.setImage(null));

        for (int i = 0; i < faceUpTiles.size() && i < cellViews.size(); i++) {
            int id = faceUpTiles.get(i);

            System.out.println("Loading faceup tile with id: " + id);
            String resourcePath = "/images/tiles/GT-new_tiles_16_for web" + id + ".jpg";

            try {
                Image img = new Image(
                        Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                );
                cellViews.get(i).setImage(img);
            } catch (Exception e) {
                System.err.println("Failed to load image: " + resourcePath);
                e.printStackTrace();
            }
        }
    }

    /**
     * Placeholder method for navigation to the face-up scene.
     * Currently returns immediately without performing any action.
     * This method appears to be intended for scene transition logic but is not implemented.
     *
     * @param gui the GUIRoot instance for potential scene management operations
     */
    @Override
    public void goToFaceUpScene(GUIRoot gui) {
        return;
    }

}