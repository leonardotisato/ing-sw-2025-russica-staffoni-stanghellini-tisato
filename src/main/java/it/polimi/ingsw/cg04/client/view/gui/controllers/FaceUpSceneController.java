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

    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

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

        // System.out.println("Try loading faceup tiles.");
        // test();
    }

//    private void test() {
//        System.out.println("Loading faceup tiles.");
//        for (int i = 0; i < 130; i++) {
//            String resourcePath = "/images/tiles/1.jpg";
//
//            Image img = new Image(
//                    Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
//            );
//            cellViews.get(i).setImage(img);
//        }
//    }

    @FXML
    public void close() {
        System.out.println("Faceup scene closed.");
        gui.closeFaceUp();
    }

    @FXML
    public void handleChoice(int row, int col) {
        int idx = row * 16 + col;
        if(idx < size) {
            System.out.println("Faceup tile selected at row: " + row + ", col: " + col + ", index: " + idx);
            gui.chooseFaceUp(idx);
        }
    }
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

    @Override
    public void goToFaceUpScene(GUIRoot gui) {
        return;
    }

}