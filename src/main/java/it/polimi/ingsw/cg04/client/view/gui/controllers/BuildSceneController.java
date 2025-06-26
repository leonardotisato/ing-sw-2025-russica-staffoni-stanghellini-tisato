package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.FlightBoard;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.BuildState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class BuildSceneController extends ViewController {
    private final double BASE_WIDTH = 600;
    private final double BASE_HEIGHT = 400;

    @FXML
    private ImageView heldTile;

    @FXML
    private Button drawButton;

    @FXML
    private Button shoowFaceUpButton;

    @FXML
    private GridPane shipGrid;

    @FXML
    private GridPane pilesGrid;

    @FXML
    private GridPane faceUpGrid;

    @FXML
    private StackPane root;

    @FXML
    private AnchorPane buildScene;

    @FXML
    private AnchorPane endBuildingScene;

    @FXML
    private AnchorPane cheatsPopup;

    @FXML
    private Group scalableGroup;

    @FXML
    private GridPane bufferGrid;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private TextArea logs;

    @FXML
    private Button timerButton;

    @FXML
    private Button endButton;

    @FXML
    private Button showOtherShipsButton;

    @FXML
    private Button fixButton;

    @FXML
    private Button cheatsButton;


    @FXML
    private Polygon pos1, pos2, pos3, pos4;

    @FXML
    private Polygon pos1_1, pos2_1, pos3_1, pos4_1;

    @FXML
    private ImageView shipImage, flightboardImg;

    @FXML
    private Pane pilesPane, drawPane;

    @FXML
    private Label title;

    @FXML
    private ImageView pinkAlien, brownAlien;

    private final Map<Polygon, Integer> trianglePositionMap = new HashMap<>();

    private List<Coordinates> tilesToBreak = new ArrayList<>();

    private final Map<Coordinates, ImageView> highlightedCells = new HashMap<>();

    private Map<CrewType, Coordinates> crewAliens = new HashMap<>();

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


//
//    @FXML
//    private void handleShowOtherShips() {
//        gui.viewOtherShips();
//    }
//
//    @FXML
//    private void handleEnd() {
//        gui.endTurn(); // o endPhase() o simile
//    }

    /**
     * Initializes the build scene controller and sets up all UI components and event handlers.
     * JavaFX automatically calls this method after loading the FXML file and configures:
     * - Responsive UI scaling based on window size changes
     * - Drag and drop functionality for tiles and crew members
     * - Event handlers for tile rotation and selection
     * - Initial state of UI components
     *
     * @param url            the location used to resolve relative paths for the root object, or null if unknown
     * @param resourceBundle the resources used to localize the root object, or null if not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.widthProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        root.heightProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        initDragAndDrop();
        logs.setEditable(false);
        drawButton.setOnAction(event -> handleDraw());
        heldTile.setVisible(false);
        title.setPrefWidth(BASE_WIDTH);
        title.setAlignment(Pos.CENTER);
        title.setFont(new Font(48));
        heldTile.setOnMouseClicked(event -> {
            if (heldTile.getImage() != null) {
                selectHeldTile(true);
                event.consume();
            }
        });
        heldTile.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT -> {
                    gui.rotateTile("LEFT");
                    event.consume();
                }
                case RIGHT -> {
                    gui.rotateTile("RIGHT");
                    event.consume();
                }
            }
            Platform.runLater(() -> heldTile.requestFocus());
        });
        setDragAndDrop(pinkAlien, CrewType.PINK_ALIEN);
        setDragAndDrop(brownAlien, CrewType.BROWN_ALIEN);
        crewAliens.put(CrewType.PINK_ALIEN, null);
        crewAliens.put(CrewType.BROWN_ALIEN, null);
    }

    /**
     * Scales and centers the UI components to maintain aspect ratio when the window is resized.
     * Calculates the appropriate scale factor based on the current window dimensions compared
     * to the base dimensions, then applies uniform scaling and centering to the scalable group.
     * This ensures the game interface remains properly proportioned and centered regardless
     * of window size.
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
     * Initializes drag and drop functionality for the held tile component.
     * Sets up the drag detection event handler that creates a visual representation
     * of the tile being dragged, including proper scaling and rotation to match
     * the current tile state. The drag image is created with transparent background
     * and centered cursor positioning for better user experience.
     */
    private void initDragAndDrop() {
        heldTile.setOnDragDetected(event -> {
            if (heldTile.getImage() == null) return;

            Dragboard db = heldTile.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(heldTile.getImage());
            db.setContent(content);
            double currentScale = scalableGroup.getScaleX();

            double scaledWidth = heldTile.getFitWidth() * currentScale;
            double scaledHeight = heldTile.getFitHeight() * currentScale;

            ImageView tempView = new ImageView(heldTile.getImage());
            tempView.setFitWidth(heldTile.getFitWidth());
            tempView.setFitHeight(heldTile.getFitHeight());
            tempView.setRotate(heldTile.getRotate());

            SnapshotParameters params = new SnapshotParameters();
            params.setFill(javafx.scene.paint.Color.TRANSPARENT); // Per evitare sfondo nero
            javafx.scene.image.Image rotatedImage = tempView.snapshot(params, null);


            db.setDragView(
                    rotatedImage,
                    scaledWidth / 2,
                    scaledHeight / 2
            );

            event.consume();
        });
    }

    /**
     * Sets up drag and drop functionality for crew alien ImageViews.
     * Configures the specified alien ImageView to be draggable, creating a visual
     * drag representation with fixed dimensions and including the crew type information
     * in the drag data for proper drop validation.
     *
     * @param alien    the ImageView representing the crew alien to make draggable
     * @param crewType the type of crew member (PINK_ALIEN or BROWN_ALIEN) for identification during drop operations
     */
    public void setDragAndDrop(ImageView alien, CrewType crewType) {
        alien.setOnDragDetected(event -> {
            if (alien.getImage() == null) return;

            Dragboard db = alien.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putImage(alien.getImage());
            content.putString(crewType.name());
            db.setContent(content);

            double dragWidth = 30;
            double dragHeight = 30;

            ImageView dragView = new ImageView(alien.getImage());
            dragView.setFitWidth(dragWidth);
            dragView.setFitHeight(dragHeight);
            dragView.setPreserveRatio(true);

            SnapshotParameters params = new SnapshotParameters();
            params.setFill(javafx.scene.paint.Color.TRANSPARENT);
            Image scaledDragImage = dragView.snapshot(params, null);

            db.setDragView(scaledDragImage, dragWidth / 2, dragHeight / 2);

            event.consume();
        });


    }

    /**
     * Clears all drag and drop event handlers from ship grid and buffer grid components.
     * This method removes existing drag event listeners to prevent interference when
     * switching between different game states or UI modes.
     */
    private void clearAllDragAndDropEvents() {
        for (Node node : shipGrid.getChildren()) {
            StackPane stack = (StackPane) node;
            stack.setOnDragEntered(null);
            stack.setOnDragExited(null);
            stack.setOnDragOver(null);
            stack.setOnDragDropped(null);
        }

        for (Node node : bufferGrid.getChildren()) {
            StackPane stack = (StackPane) node;
            stack.setOnDragEntered(null);
            stack.setOnDragExited(null);
            stack.setOnDragOver(null);
            stack.setOnDragDropped(null);
        }
    }


    /**
     * Handles the draw button click event by requesting a face-down tile from the server.
     * Sends a draw request to the GUI controller to obtain a new tile from the deck.
     */
    @FXML
    private void handleDraw() {
        System.out.println("Draw button clicked. Sending draw request to server.");
        gui.drawFaceDown();
    }

    /**
     * Handles the show face-up tiles button click event.
     * Requests the GUI to display the currently available face-up tiles.
     */
    @FXML
    private void handleShowFaceUp() {
        gui.showFaceUp();
    }

    /**
     * Handles the view other ships button click event.
     * Requests the GUI to display other players' ships for inspection.
     */
    @FXML
    private void viewOther() {
        gui.viewOthers();
    }

    /**
     * Handles the end building button click event.
     * Transitions the UI to show the end building scene where players can select
     * their starting position on the flight board.
     */
    @FXML
    private void handleEndButtonClick() {
        showEndBuildingScene();
    }

    /**
     * Handles the back button click event.
     * Returns the UI to the main build scene from other sub-scenes.
     */
    @FXML
    private void handleBackButtonClick() {
        showBuildScene();
    }

    /**
     * Handles the cheats button click event.
     * Displays the cheats popup scene for testing or development purposes.
     *
     * @param actionEvent the action event triggered by the button click
     */
    public void handleCheatsButtonClick(ActionEvent actionEvent) {
        showCheatsScene();
    }

    /**
     * Handles the ship 1 cheat button click.
     * Places a test tile at coordinates (103, 103) for development/testing purposes.
     *
     * @param actionEvent the action event triggered by the button click
     */
    public void onShip1Click(ActionEvent actionEvent) {
        gui.place(103, 103);
    }

    /**
     * Handles the ship 2 cheat button click.
     * Places a test tile at coordinates (104, 104) for development/testing purposes.
     *
     * @param actionEvent the action event triggered by the button click
     */
    public void onShip2Click(ActionEvent actionEvent) {
        gui.place(104, 104);
    }

    /**
     * Handles the ship 3 cheat button click.
     * Places a test tile at coordinates (105, 105) for development/testing purposes.
     *
     * @param actionEvent the action event triggered by the button click
     */
    public void onShip3Click(ActionEvent actionEvent) {
        gui.place(105, 105);
    }

    /**
     * Handles the fix button click event during ship repair phase.
     * Sends the list of selected tiles to break to the GUI controller and
     * clears all highlighting and selection state.
     */
    @FXML
    private void handleFixButtonClick() {
        gui.fixShip(tilesToBreak);
        for (ImageView cell : highlightedCells.values()) {
            cell.setStyle("");
        }
        tilesToBreak.clear();
        highlightedCells.clear();
    }

    /**
     * Shows the main build scene and hides other sub-scenes.
     * Sets visibility and management properties for the primary building interface.
     */
    public void showBuildScene() {
        endBuildingScene.setVisible(false);
        endBuildingScene.setManaged(false);

        cheatsPopup.setVisible(false);
        cheatsPopup.setManaged(false);

        buildScene.setVisible(true);
        buildScene.setManaged(true);
    }

    /**
     * Shows the end building scene and hides the main build scene.
     * Displays the interface for selecting starting positions on the flight board.
     */
    public void showEndBuildingScene() {
        buildScene.setVisible(false);
        buildScene.setManaged(false);

        endBuildingScene.setVisible(true);
        endBuildingScene.setManaged(true);
    }

    /**
     * Shows the cheats popup scene for development/testing purposes.
     * Displays additional controls for testing game functionality.
     */
    public void showCheatsScene() {
        buildScene.setManaged(false);

        cheatsPopup.setVisible(true);
        cheatsPopup.setManaged(true);
    }

    /**
     * Shows the fix button and hides all other action buttons.
     * Used during the ship repair phase to provide the appropriate UI controls.
     */
    public void showFixButton() {
        hideAllButtons();
        fixButton.setVisible(true);
        fixButton.setManaged(true);
    }

    /**
     * Hides all action buttons in the interface.
     * Sets visibility and management properties to false for all control buttons.
     */
    public void hideAllButtons() {
        timerButton.setVisible(false);
        timerButton.setManaged(false);

        endButton.setVisible(false);
        endButton.setManaged(false);

        showOtherShipsButton.setVisible(false);
        showOtherShipsButton.setManaged(false);

        fixButton.setVisible(false);
        fixButton.setManaged(false);

        cheatsButton.setVisible(false);
        cheatsButton.setManaged(false);
    }

    /**
     * Hides the specified button by setting its visibility and management to false.
     *
     * @param button the button to hide
     */
    public void hideButton(Button button) {
        button.setManaged(false);
        button.setVisible(false);
    }

    /**
     * Shows the specified button by setting its visibility and management to true.
     *
     * @param button the button to show
     */
    public void showButton(Button button) {
        button.setManaged(true);
        button.setVisible(true);
    }

    /**
     * Hides the specified pane by setting its visibility and management to false.
     *
     * @param pane the pane to hide
     */
    public void hidePane(Pane pane) {
        pane.setManaged(false);
        pane.setVisible(false);
    }

    /**
     * Shows the specified pane by setting its visibility and management to true.
     *
     * @param pane the pane to show
     */
    public void showPane(Pane pane) {
        pane.setManaged(true);
        pane.setVisible(true);
    }

    /**
     * Updates the controller for the crew loading phase.
     * Configures the interface for placing crew members on the ship by setting up
     * drag and drop functionality and hiding unnecessary UI elements.
     *
     * @param game the current game state containing player and ship information
     */
    @Override
    public void updateLoadCrewController(Game game) {
        System.out.println(">> LoadCrewController UPDATE CALLED - Level: " + game.getLevel());
        clearAllDragAndDropEvents();
        hideAllButtons();
        fixButton.setText("Load crew");
        fixButton.setOnAction(event -> {
            gui.loadCrew(crewAliens.get(CrewType.PINK_ALIEN), crewAliens.get(CrewType.BROWN_ALIEN));
        });
        title.setVisible(true);
        title.setManaged(true);
        title.setText("Load Crew!");
        title.alignmentProperty().set(Pos.TOP_RIGHT);
        showButton(fixButton);
        hidePane(pilesPane);
        hidePane(drawPane);
        pinkAlien.setVisible(crewAliens.get(CrewType.PINK_ALIEN) == null);
        pinkAlien.setManaged(crewAliens.get(CrewType.PINK_ALIEN) == null);
        brownAlien.setVisible(crewAliens.get(CrewType.BROWN_ALIEN) == null);
        brownAlien.setManaged(crewAliens.get(CrewType.BROWN_ALIEN) == null);
        updateShipForLoadCrew(game.getPlayer(gui.getClientNickname()));
    }

    /**
     * Updates the controller for the main building phase.
     * Refreshes all UI components based on the current game state, including
     * ship grid, held tile, face-up tiles, and level-specific elements.
     *
     * @param game the current game state
     */
    @Override
    public void updateBuildController(Game game) {
        Player currentPlayer = game.getPlayer(gui.getClientNickname());
        BuildState state = (BuildState) game.getGameState();
        Tile playerHeldTile = currentPlayer.getHeldTile();
        Ship playerShip = currentPlayer.getShip();
        int level = game.getLevel();

        updateShipGrid(currentPlayer);
        updateHeldTile(playerHeldTile);
        updateFaceUpTiles(game);
        updateFreePositions(game);

        if (level == 2) {
            updateBuffer(playerShip);
            updateTimer(game);
            updatePiles(currentPlayer);
        }
        if (state.getPlayerState().get(currentPlayer.getName()) == BuildPlayerState.READY) {
            hideAllButtons();
            if (level == 2) showButton(timerButton);
        }
    }

    /**
     * Updates the controller with the current game state and refreshes the scene accordingly.
     * This method logs the client's nickname for debugging purposes, retrieves the current game level,
     * composes the scene layout based on the level-specific requirements, and delegates state-specific
     * updates to the game state controller. The method ensures the UI is properly synchronized with
     * the current game state and level configuration.
     *
     * @param game the Game object containing the current state, level, and game state controller
     */
    @Override
    public void update(Game game) {
        System.out.println(gui.getClientNickname());
        int level = game.getLevel();
        composeSceneByLevel(level);
        game.getGameState().updateStateController(this, game);
    }

    /**
     * Configures the scene appearance and functionality based on the game level.
     * Loads appropriate background images, ship graphics, and flight board images,
     * and sets up level-specific UI elements and button behaviors.
     *
     * @param level the current game level (1 or 2)
     */
    private void composeSceneByLevel(int level) {
        String backgroundPath = "/images/background" + level + ".png";
        String shipPath = "/images/cardboard/ship" + level + ".jpg";
        String flightBoardPath = "/images/cardboard/flightboard" + level + ".png";
        try {
            Image backgroundImg = new Image(
                    Objects.requireNonNull(getClass().getResource(backgroundPath)).toExternalForm()
            );
            Image shipImg = new Image(
                    Objects.requireNonNull(getClass().getResource(shipPath)).toExternalForm()
            );
            Image boardImg = new Image(
                    Objects.requireNonNull(getClass().getResource(flightBoardPath)).toExternalForm()
            );
            BackgroundImage backgroundImage = new BackgroundImage(
                    backgroundImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            root.setBackground(new Background(backgroundImage));
            shipImage.setImage(shipImg);
            flightboardImg.setImage(boardImg);
        } catch (Exception e) {
            System.err.println("Immagine non trovata: " + shipPath);
            System.err.println("Immagine non trovata: " + flightBoardPath);
            System.err.println("Immagine non trovata: " + backgroundPath);
            e.printStackTrace();
        }

        if (level == 1) {
            hideButton(timerButton);
            pilesPane.setVisible(false);
            pilesPane.setManaged(false);
            endButton.setOnAction(event -> {
                gui.endBuilding(1);
            });
        }
        setupTrianglePositions(level);
    }

    /**
     * Applies or removes visual selection effects on the held tile.
     * When selected, adds a golden drop shadow effect and focuses the tile.
     * When deselected, removes effects and returns focus to the root.
     *
     * @param selected true to select the tile, false to deselect
     */
    private void selectHeldTile(boolean selected) {
        if (selected) {
            heldTile.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
            heldTile.requestFocus();
        } else {
            heldTile.setStyle("");
            root.requestFocus();
        }
    }

    /**
     * Updates the held tile display based on the current tile state.
     * Shows or hides the tile image, sets appropriate rotation, and configures
     * the draw button behavior based on whether a tile is currently held.
     *
     * @param tile the tile currently held by the player, or null if no tile is held
     */
    public void updateHeldTile(Tile tile) {
        try {
            if (tile != null) {
                drawButton.setText("Return");
                drawButton.setOnAction(event -> {
                    System.out.println("Returning held tile");
                    gui.returnTile();
                });
                String resourcePath = "/images/tiles/GT-new_tiles_16_for web" + tile.getId() + ".jpg";
                try {
                    Image img = new Image(
                            Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                    );
                    heldTile.setImage(img);
                    heldTile.setVisible(true);
                    heldTile.setRotate(tile.getRotation() * 90);
                } catch (Exception e) {
                    System.err.println("Failed to load image for held tile: " + resourcePath);
                    System.err.println("Error: " + e.getMessage());
                }
            } else {
                heldTile.setImage(null);
                heldTile.setVisible(false);
                heldTile.setStyle("");
                drawButton.setText("Draw");
                drawButton.setOnAction(event -> {
                    System.out.println("Drawing new tile");
                    gui.drawFaceDown();
                });
            }
        } catch (Exception e) {
            System.err.println("Error in updateHeldTile: " + e.getMessage());
        }
    }

    /**
     * Updates the ship grid display for the crew loading phase.
     * Configures drag and drop functionality for placing crew members on tiles
     * that support them, and displays existing crew placements.
     *
     * @param p the player whose ship is being updated
     */
    public void updateShipForLoadCrew(Player p) {
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();
        int level = p.getGame().getLevel();

        for (Node node : shipGrid.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            int tempcol = colIndex == null ? 0 : colIndex;
            int row = rowIndex == null ? 0 : rowIndex;


            if (level == 1 && (tempcol == 0 || tempcol == 6)) {
                continue;
            } else if (level == 1) {
                tempcol = tempcol - 1;
            }

            int col = tempcol;

            StackPane stack = (StackPane) node;

            ImageView cell = (ImageView) stack.getChildren().getFirst();
            Tile tile = shipMatrix[row][col];

            if (tile == null) {
                cell.setImage(null);
            } else {
                String resourcePath = "/images/tiles/GT-new_tiles_16_for web" + tile.getId() + ".jpg";
                try {
                    Image img = new Image(
                            Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                    );
                    cell.setImage(img);
                    cell.setRotate(tile.getRotation() * 90);
                    Coordinates c = new Coordinates(row, col);
                    if (crewAliens.containsValue(c)) {
                        updateHousingTile(c, stack);
                        cell.setOnDragEntered(null);
                        cell.setOnDragExited(null);
                        cell.setOnDragOver(null);
                        cell.setOnDragDropped(null);
                        continue;
                    }
                    if (tile.getSupportedCrewType() != null && (tile.getSupportedCrewType().contains(CrewType.PINK_ALIEN) || tile.getSupportedCrewType().contains(CrewType.BROWN_ALIEN))) {
                        cell.setOnDragEntered(event -> {
                            if (event.getGestureSource() != cell && event.getDragboard().hasImage()) {
                                String crewStr = event.getDragboard().getString();
                                if (crewStr != null && tile.getSupportedCrewType().contains(CrewType.valueOf(crewStr))) {
                                    cell.setStyle("-fx-effect: dropshadow(gaussian, limegreen, 10, 0.6, 0, 0);");
                                }
                            }
                        });

                        cell.setOnDragExited(event -> {
                            cell.setStyle("");
                        });
                        cell.setOnDragOver(event -> {
                            if (event.getGestureSource() != cell && event.getDragboard().hasImage()) {
                                String crewStr = event.getDragboard().getString();
                                if (crewStr != null && tile.getSupportedCrewType().contains(CrewType.valueOf(crewStr))) {
                                    event.acceptTransferModes(TransferMode.MOVE);
                                }
                            }
                            event.consume();
                        });

                        cell.setOnDragDropped(event -> {
                            Dragboard db = event.getDragboard();
                            boolean success = false;

                            if (db.hasString()) {
                                String alienType = db.getString();
                                CrewType type = CrewType.valueOf(alienType);
                                Coordinates coord = new Coordinates(row, col);

                                if (tile.getSupportedCrewType().contains(type)) {
                                    crewAliens.put(type, coord);
                                    updateHousingTile(coord, stack);
                                    success = true;

                                    if (crewAliens.get(CrewType.PINK_ALIEN) != null) {
                                        pinkAlien.setVisible(false);
                                        pinkAlien.setManaged(false);
                                    }
                                    if (crewAliens.get(CrewType.BROWN_ALIEN) != null) {
                                        brownAlien.setVisible(false);
                                        brownAlien.setManaged(false);
                                    }
                                }
                            }
                            event.setDropCompleted(success);
                            event.consume();
                        });
                    }
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + resourcePath);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Updates the ship grid display for the main building phase.
     * Shows current tiles, sets up drag and drop for tile placement,
     * and handles special states like ship fixing mode.
     *
     * @param p the player whose ship is being updated
     */
    public void updateShipGrid(Player p) {
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();
        BuildState state = (BuildState) p.getGame().getGameState();
        int level = p.getGame().getLevel();

        for (Node node : shipGrid.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            int tempcol = colIndex == null ? 0 : colIndex;
            int row = rowIndex == null ? 0 : rowIndex;


            if (level == 1 && (tempcol == 0 || tempcol == 6)) {
                continue;
            } else if (level == 1) {
                tempcol = tempcol - 1;
            }

            int col = tempcol;

            StackPane stack = (StackPane) node;

            ImageView cell = (ImageView) stack.getChildren().getFirst();
            Tile tile = shipMatrix[row][col];

            if (tile == null) {
                cell.setImage(null);
                if (ship.isPlacingLegal(p.getHeldTile(), row, col)) {
                    stack.setOnDragEntered(event -> {
                        if (event.getGestureSource() != cell && event.getDragboard().hasImage()) {
                            stack.setStyle("-fx-background-color: rgba(0,255,0,0.3);");
                        }
                    });

                    stack.setOnDragExited(event -> {
                        stack.setStyle("");
                    });
                    stack.setOnDragOver(event -> {
                        if (event.getGestureSource() != cell && event.getDragboard().hasImage()) {
                            event.acceptTransferModes(TransferMode.MOVE);
                        }
                        event.consume();
                    });

                    stack.setOnDragDropped(event -> {
                        Dragboard db = event.getDragboard();
                        boolean success = false;

                        if (db.hasImage()) {
                            gui.place(row, col);
                            success = true;
                        }

                        event.setDropCompleted(success);
                        event.consume();
                    });
                }
            } else {
                String resourcePath = "/images/tiles/GT-new_tiles_16_for web" + tile.getId() + ".jpg";
                try {
                    Image img = new Image(
                            Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                    );
                    cell.setImage(img);
                    cell.setRotate(tile.getRotation() * 90);
                    if (state.getPlayerState().get(p.getName()) == BuildPlayerState.FIXING) {
                        hidePane(drawPane);
                        hidePane(pilesPane);
                        title.setVisible(true);
                        title.setManaged(true);
                        title.setText("FIX YOUR SHIP!");
                        setFixEffects(cell, row, col);
                        showFixButton();
                    } else {
                        cell.setOnMouseClicked(null);
                    }
                    stack.setOnDragEntered(null);
                    stack.setOnDragExited(null);
                    stack.setOnDragOver(null);
                    stack.setOnDragDropped(null);
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + resourcePath);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sets up click effects for tiles during the ship fixing phase.
     * Allows players to select and deselect tiles for removal by clicking on them.
     * Selected tiles are highlighted with a golden drop shadow effect.
     *
     * @param cell the ImageView representing the tile
     * @param row  the row coordinate of the tile
     * @param col  the column coordinate of the tile
     */
    public void setFixEffects(ImageView cell, int row, int col) {
        Coordinates coordinates = new Coordinates(row, col);
        cell.setOnMouseClicked(event -> {
            if (!coordinates.isIn(tilesToBreak)) {
                cell.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
                tilesToBreak.add(coordinates);
                highlightedCells.put(coordinates, cell);
                System.out.println(tilesToBreak);
            } else {
                cell.setStyle("");
                tilesToBreak.remove(coordinates);
                highlightedCells.remove(coordinates);
                System.out.println(tilesToBreak);
            }
        });
    }

    /**
     * Updates the piles display based on current game state.
     * Shows either the contents of a selected pile or the backs of available piles,
     * and sets up appropriate click handlers for pile interaction.
     *
     * @param p the player whose pile state is being updated
     */
    public void updatePiles(Player p) {
        BuildState state = (BuildState) p.getGame().getGameState();
        int cardIdx = 0;
        if (state.getPlayerState().get(p.getName()) == BuildPlayerState.SHOWING_PILE) {
            int pileIdx = state.getIsLookingPile().get(p.getName());
            for (Node node : pilesGrid.getChildren()) {
                Integer colIndex = GridPane.getColumnIndex(node);
                ImageView cell = (ImageView) node;

                cardIdx = p.getGame().getPreFlightPiles().get(pileIdx - 1).get(colIndex);
                String resourcePath = "/images/cards/" + cardIdx + ".jpg";
                try {
                    Image img = new Image(
                            Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                    );
                    cell.setImage(img);
                    cell.setOnMouseClicked(event -> {
                        System.out.println("Return pile clicked: ");
                        gui.returnPile();
                    });
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + resourcePath);
                    e.printStackTrace();
                }
            }
        } else {
            for (Node node : pilesGrid.getChildren()) {
                Integer colIndex = GridPane.getColumnIndex(node);
                System.out.println(colIndex);
                ImageView cell = (ImageView) node;
                if (state.getIsLookingPile().containsValue(colIndex + 1)) {
                    cell.setImage(null);
                    cell.setOnMouseClicked(null);
                    cell.setOnMouseEntered(null);
                    cell.setOnMouseExited(null);
                } else {
                    String resourcePath = "/images/cards/back2.jpg";
                    try {
                        Image img = new Image(
                                Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                        );
                        cell.setImage(img);
                        cell.setOnMouseClicked(event -> {
                            System.out.println("Pick pile clicked: " + (colIndex + 1));
                            gui.pickPile(colIndex + 1);
                        });
                        cell.setOnMouseEntered(e -> cell.setStyle("-fx-effect: dropshadow(gaussian, gold, 8, 0.6, 0, 0);"));
                        cell.setOnMouseExited(e -> cell.setStyle(""));
                    } catch (Exception e) {
                        System.err.println("Immagine non trovata: " + resourcePath);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Updates the face-up tiles display.
     * Shows the currently available face-up tiles that players can choose from,
     * and sets up click handlers for tile selection with hover effects.
     *
     * @param g the current game state containing face-up tile information
     */
    public void updateFaceUpTiles(Game g) {
        List<Integer> faceUps = g.getFaceUpTiles();
        for (Node node : faceUpGrid.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            ImageView cell = (ImageView) node;
            if (colIndex < faceUps.size()) {
                String resourcePath = "/images/tiles/GT-new_tiles_16_for web" + faceUps.get(colIndex) + ".jpg";
                try {
                    Image img = new Image(
                            Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                    );
                    cell.setImage(img);
                    cell.setOnMouseClicked(event -> {
                        System.out.println("Clicked face-up card index: " + faceUps.get(colIndex));
                        gui.chooseFaceUp(colIndex);
                    });
                    cell.setOnMouseEntered(e -> cell.setStyle("-fx-effect: dropshadow(gaussian, gold, 8, 0.6, 0, 0);"));
                    cell.setOnMouseExited(e -> cell.setStyle(""));
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + resourcePath);
                    e.printStackTrace();
                }
            } else {
                cell.setImage(null);
                cell.setOnMouseClicked(null);
            }
        }
    }

    /**
     * Updates the timer button based on the current timer state.
     * Changes button text and functionality between "Timer" (to start) and "Stop" (to end)
     * based on remaining timer flips in the game.
     *
     * @param g the current game state containing timer information
     */
    public void updateTimer(Game g) {
        try {
            if (g.getBoard().getTimerFlipsRemaining() == 0) {
                timerButton.setText("Stop");
                timerButton.setOnAction(event -> {
                    System.out.println("Stopping build state");
                    gui.stopBuilding();
                });
            } else {
                timerButton.setText("Timer");
                timerButton.setOnAction(event -> {
                    System.out.println("Starting timer");
                    gui.startTimer();
                });
            }
        } catch (Exception e) {
            System.err.println("Error in updateHeldTile: " + e.getMessage());
        }
    }

    /**
     * Updates the buffer tiles display for level 2 gameplay.
     * Shows tiles in the buffer and sets up drag and drop functionality
     * for empty buffer slots and click handlers for filled slots.
     *
     * @param s the ship containing the buffer to update
     */
    public void updateBuffer(Ship s) {
        List<Tile> buffer = s.getTilesBuffer();
        for (Node node : bufferGrid.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            StackPane stack = (StackPane) node;
            ImageView cell = (ImageView) stack.getChildren().getFirst();
            if (colIndex >= buffer.size()) {
                cell.setImage(null);
                cell.setOnMouseClicked(null);
                cell.setOnMouseEntered(null);
                cell.setOnMouseExited(null);
                stack.setOnDragEntered(event -> {
                    if (event.getGestureSource() != cell && event.getDragboard().hasImage()) {
                        stack.setStyle("-fx-background-color: rgba(0,255,0,0.3);");
                    }
                });

                stack.setOnDragExited(event -> {
                    stack.setStyle("");
                });
                stack.setOnDragOver(event -> {
                    if (event.getGestureSource() != cell && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                    event.consume();
                });

                stack.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    boolean success = false;

                    if (db.hasImage()) {
                        gui.placeTileInBuffer();
                        success = true;
                    }

                    event.setDropCompleted(success);
                    event.consume();
                });
            } else {
                String resourcePath = "/images/tiles/GT-new_tiles_16_for web" + buffer.get(colIndex).getId() + ".jpg";
                try {
                    Image img = new Image(
                            Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                    );
                    cell.setImage(img);
                    cell.setOnMouseClicked(event -> {
                        System.out.println("Clicked buffer tile index: " + colIndex);
                        gui.chooseTileFromBuffer(colIndex);
                    });
                    cell.setOnMouseEntered(e -> cell.setStyle("-fx-effect: dropshadow(gaussian, gold, 8, 0.6, 0, 0);"));
                    cell.setOnMouseExited(e -> cell.setStyle(""));
                    stack.setOnDragEntered(null);
                    stack.setOnDragExited(null);
                    stack.setOnDragOver(null);
                    stack.setOnDragDropped(null);
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + resourcePath);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sets up the triangle position mappings and event handlers for the flight board.
     * Configures hover effects and click handlers for triangular position selectors
     * based on the current game level, allowing players to choose their starting position.
     *
     * @param level the current game level (1 or 2) determining which triangle set to use
     */
    private void setupTrianglePositions(int level) {
        trianglePositionMap.put(level == 2 ? pos1 : pos1_1, 1);
        trianglePositionMap.put(level == 2 ? pos2 : pos2_1, 2);
        trianglePositionMap.put(level == 2 ? pos3 : pos3_1, 3);
        trianglePositionMap.put(level == 2 ? pos4 : pos4_1, 4);

        for (Polygon triangle : trianglePositionMap.keySet()) {

            triangle.setOnMouseEntered(e -> {
                triangle.getStyleClass().add("polygon-hover");
            });

            triangle.setOnMouseExited(e -> {
                triangle.getStyleClass().remove("polygon-hover");
            });

            triangle.setOnMouseClicked(e -> {
                Integer pos = trianglePositionMap.get(triangle);
                if (pos != null) {
                    gui.endBuilding(pos);
                    disableTriangle(triangle);
                    showBuildScene();
                }
            });
        }
    }

    /**
     * Updates the visual state of triangular position selectors based on occupied positions.
     * Colors triangles according to the player occupying each position and disables
     * interaction for already taken positions.
     *
     * @param g the current game state containing flight board information
     */
    private void updateFreePositions(Game g) {
        Map<String, String> playerColorHex = Map.of(
                "YELLOW", "#FFFF00",
                "RED", "#FF0000",
                "BLUE", "#0000FF",
                "GREEN", "#00FF00"
        );

        for (Map.Entry<Polygon, Integer> entry : trianglePositionMap.entrySet()) {
            Polygon triangle = entry.getKey();
            FlightBoard board = g.getBoard();
            int pos = board.getStartingPosition(entry.getValue());

            if (g.getBoard().getCell(pos) != null) {
                String color = playerColorHex.get(g.getBoard().getCell(pos).getColor().toString());
                triangle.setStyle("-fx-fill: " + color + "; -fx-stroke: black; -fx-stroke-width: 1;");
                triangle.setDisable(true);
                triangle.setOnMouseEntered(null);
                triangle.setOnMouseExited(null);
                triangle.getStyleClass().clear();
            }
        }
    }

    /**
     * Disables a triangle position selector and removes all interaction handlers.
     * Used when a position has been selected or is no longer available.
     *
     * @param triangle the triangle polygon to disable
     */
    private void disableTriangle(Polygon triangle) {
        triangle.setDisable(true);
        triangle.setEffect(null);
        triangle.setOnMouseEntered(null);
        triangle.setOnMouseExited(null);
    }

    /**
     * Updates the visual representation of a tile that houses crew members.
     * Adds crew alien images to the specified stack pane based on the crew type
     * currently assigned to the given coordinates.
     *
     * @param c         the coordinates of the tile housing the crew
     * @param stackPane the UI container where the crew image should be displayed
     */
    public void updateHousingTile(Coordinates c, StackPane stackPane) {
        CrewType crewType = null;
        for (Map.Entry<CrewType, Coordinates> entry : crewAliens.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(c)) {
                crewType = entry.getKey();
            }
        }
        Node crewContainer;
        ImageView crewImage = new ImageView();
        crewImage.setFitWidth(25);
        crewImage.setFitHeight(25);
        crewImage.setPreserveRatio(true);
        String crewImagePath = switch (crewType) {
            case PINK_ALIEN -> "/images/pink_alien.png";
            case BROWN_ALIEN -> "/images/brown_alien.png";
            default -> null;
        };

        if (crewImagePath != null) {
            try {
                Image crewImg = new Image(Objects.requireNonNull(getClass().getResource(crewImagePath)).toExternalForm());
                crewImage.setImage(crewImg);
                crewImage.setOpacity(1.0);
                stackPane.getChildren().add(crewImage); // aggiunge l'immagine al centro
                StackPane.setAlignment(crewImage, Pos.CENTER);
            } catch (Exception e) {
                System.err.println("Immagine non trovata: " + crewImagePath);
                e.printStackTrace();
            }
        }
        crewContainer = crewImage;
        StackPane.setAlignment(crewContainer, Pos.CENTER);
    }

    /**
     * Updates the logs display with the provided log lines.
     * Concatenates all log entries and displays them in the logs text area
     * using Platform.runLater to ensure UI thread safety.
     *
     * @param logLines the list of log messages to display, or null if no logs available
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
     * Navigates to the build scene (no-op implementation).
     * This method is part of the ViewController interface but performs no action
     * in this controller as the build scene is already the primary scene.
     *
     * @param gui the GUI root reference (unused in this implementation)
     * @throws IOException if an I/O error occurs (not thrown in this implementation)
     */
    @Override
    public void goToBuildScene(GUIRoot gui) throws IOException {
        return;
    }
}
