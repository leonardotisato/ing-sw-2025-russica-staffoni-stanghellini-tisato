package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.BuildState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.scene.control.Button;
import javafx.scene.Node;



import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

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
    private AnchorPane background;

    @FXML private Group scalableGroup;

    @FXML
    private GridPane bufferGrid;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private TextArea logs;


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
    private void handleTimer() {
        gui.startTimer();
    }

    @FXML
    private void viewOther() {
        gui.viewOthers2();
    }

    @Override
    public void update(Game game) {
        System.out.println(gui.getClientNickname());
        Player currentPlayer = game.getPlayer(gui.getClientNickname());
        if (currentPlayer == null) {
            System.out.println("currentPlayer is null! nickname: " + gui.getClientNickname());
        }
        System.out.println(gui.getClientNickname());
        Tile playerHeldTile = currentPlayer.getHeldTile();
        Ship playerShip = currentPlayer.getShip();
        updateHeldTile(playerHeldTile);
        updateShipGrid(playerShip);
        updatePiles(currentPlayer);
        updateFaceUpTiles(currentPlayer.getGame());
        updateBuffer(playerShip);
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
        }catch (Exception e) {
            System.err.println("Error in updateHeldTile: " + e.getMessage());
        }
    }

    public void updateShipGrid(Ship ship) {
        Tile[][] shipMatrix = ship.getTilesMatrix();

        for (Node node : shipGrid.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            int col = colIndex == null ? 0 : colIndex;
            int row = rowIndex == null ? 0 : rowIndex;


            ImageView cell = (ImageView) node;
            Tile tile = shipMatrix[row][col];

            if (tile == null) {
                cell.setImage(null);
                cell.setOnDragEntered(event -> {
                    if (event.getGestureSource() != cell && event.getDragboard().hasImage()) {
                        cell.setStyle("-fx-effect: dropshadow(gaussian, limegreen, 8, 0.6, 0, 0);");
                    }
                });

                cell.setOnDragExited(event -> {
                    cell.setStyle("");
                });
                cell.setOnDragOver(event -> {
                    if (event.getGestureSource() != cell && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                    event.consume();
                });

                cell.setOnDragDropped(event -> {
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

            else {
                String resourcePath = "/images/tiles/GT-new_tiles_16_for web" + tile.getId() + ".jpg";
                try {
                    Image img = new Image(
                            Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                    );
                    cell.setImage(img);
                    cell.setRotate(tile.getRotation() * 90);
                    cell.setOnDragEntered(null);
                    cell.setOnDragExited(null);
                    cell.setOnDragOver(null);
                    cell.setOnDragDropped(null);
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + resourcePath);
                    e.printStackTrace();
                }
            }
        }
    }

    public void updatePiles(Player p) {
        BuildState state = (BuildState) p.getGame().getGameState();
        int cardIdx = 0;
        if (state.getPlayerState().get(p.getName()) == BuildPlayerState.SHOWING_PILE){
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
        }
        else{
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



    public void updateFaceUpTiles(Game g){
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
            }
            else {
                cell.setImage(null);
                cell.setOnMouseClicked(null);
            }
        }
    }

    public void updateBuffer(Ship s){
        List<Tile> buffer = s.getTilesBuffer();
        for (Node node : bufferGrid.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            ImageView cell = (ImageView) node;
            if(colIndex >= buffer.size()) {
                cell.setImage(null);
                cell.setOnMouseClicked(null);
                cell.setOnMouseEntered(null);
                cell.setOnMouseExited(null);
                cell.setOnDragEntered(event -> {
                    if (event.getGestureSource() != cell && event.getDragboard().hasImage()) {
                        cell.setStyle("-fx-effect: dropshadow(gaussian, limegreen, 8, 0.6, 0, 0);");
                    }
                });

                cell.setOnDragExited(event -> {
                    cell.setStyle("");
                });
                cell.setOnDragOver(event -> {
                    if (event.getGestureSource() != cell && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                    event.consume();
                });

                cell.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    boolean success = false;

                    if (db.hasImage()) {
                        gui.placeTileInBuffer();
                        success = true;
                    }

                    event.setDropCompleted(success);
                    event.consume();
                });
            }
            else{
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
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + resourcePath);
                    e.printStackTrace();
                }
            }
        }
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
