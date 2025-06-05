package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.FlightBoard;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.BuildState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
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
    private Pane pilesPane;

    private final Map<Polygon, Integer> trianglePositionMap = new HashMap<>();

    private List<Coordinates> tilesToBreak = new ArrayList<>();

    private GUIRoot gui;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.widthProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        root.heightProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        initDragAndDrop();
        logs.setEditable(false);
        drawButton.setOnAction(event -> handleDraw());
        heldTile.setVisible(false);
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

    }

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

    @FXML
    private void handleDraw() {
        System.out.println("Draw button clicked. Sending draw request to server.");
        gui.drawFaceDown();
    }

    @FXML
    private void handleShowFaceUp() {
        gui.showFaceUp();
    }

    @FXML
    private void viewOther() {
        gui.viewOthers2();
    }

    @FXML
    private void handleEndButtonClick() {
        showEndBuildingScene();
    }

    @FXML
    private void handleBackButtonClick() {
        showBuildScene();
    }

    public void handleCheatsButtonClick(ActionEvent actionEvent) {
        showCheatsScene();
    }

    public void onShip1Click(ActionEvent actionEvent) {
        gui.place(103, 103);
    }

    public void onShip2Click(ActionEvent actionEvent) {
        gui.place(104, 104);
    }

    public void onShip3Click(ActionEvent actionEvent) {
        gui.place(105, 105);
    }

    @FXML
    private void handleFixButtonClick() {
        gui.fixShip(tilesToBreak);
        tilesToBreak.clear();
    }

    public void showBuildScene() {
        endBuildingScene.setVisible(false);
        endBuildingScene.setManaged(false);

        cheatsPopup.setVisible(false);
        cheatsPopup.setManaged(false);

        buildScene.setVisible(true);
        buildScene.setManaged(true);
    }

    public void showEndBuildingScene() {
        buildScene.setVisible(false);
        buildScene.setManaged(false);

        endBuildingScene.setVisible(true);
        endBuildingScene.setManaged(true);
    }

    public void showCheatsScene() {
        buildScene.setManaged(false);

        cheatsPopup.setVisible(true);
        cheatsPopup.setManaged(true);
    }

    public void showFixButton() {
        hideAllButtons();
        fixButton.setVisible(true);
        fixButton.setManaged(true);
    }

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

    public void hideButton(Button button) {
        button.setManaged(false);
        button.setVisible(false);
    }

    public void showButton(Button button) {
        button.setManaged(true);
        button.setVisible(true);
    }

    @Override
    public void update(Game game) {
        System.out.println(gui.getClientNickname());
        Player currentPlayer = game.getPlayer(gui.getClientNickname());
        BuildState state = (BuildState) game.getGameState();
        Tile playerHeldTile = currentPlayer.getHeldTile();
        Ship playerShip = currentPlayer.getShip();
        int level = game.getLevel();

        composeSceneByLevel(level);
        updateHeldTile(playerHeldTile);
        updateShipGrid(currentPlayer);
        updateFaceUpTiles(currentPlayer.getGame());
        updateFreePositions(currentPlayer.getGame());

        if (level == 2) {
            updateBuffer(playerShip);
            updateTimer(currentPlayer.getGame());
            updatePiles(currentPlayer);
        }
        if (state.getPlayerState().get(currentPlayer.getName()) == BuildPlayerState.READY) {
            hideAllButtons();
            if (level == 2) showButton(timerButton);
        }
    }

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

    private void selectHeldTile(boolean selected) {
        if (selected) {
            heldTile.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
            heldTile.requestFocus();
        } else {
            heldTile.setStyle("");
            root.requestFocus();
        }
    }

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

    public void setFixEffects(ImageView cell, int row, int col) {
        Coordinates coordinates = new Coordinates(row, col);
        cell.setOnMouseClicked(event -> {
            if (!coordinates.isIn(tilesToBreak)) {
                cell.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
                tilesToBreak.add(coordinates);
            } else {
                cell.setStyle("");
                tilesToBreak.remove(coordinates);
            }
        });
    }

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

    private void disableTriangle(Polygon triangle) {
        triangle.setDisable(true);
        triangle.setEffect(null);
        triangle.setOnMouseEntered(null);
        triangle.setOnMouseExited(null);
    }

    @Override
    public void showLogs(List<String> logLines) {
        if (logLines == null || logs == null) return;

        StringBuilder sb = new StringBuilder();
        for (String line : logLines) {
            sb.append(line).append("\n");
        }

        Platform.runLater(() -> logs.setText(sb.toString()));
    }

    @Override
    public void goToBuildScene(GUIRoot gui) throws IOException {
        return;
    }
}
