package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.tiles.Tile;
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

public class FaceUpSceneController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private StackPane root;

    @FXML
    private ImageView background;

    private final List<ImageView> cellViews = new ArrayList<>();


    private GUIRoot gui;


    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.setImage(
                new Image(Objects.requireNonNull(getClass().getResource("/images/background.png")).toExternalForm())
        );
        background.setPreserveRatio(false);
        background.fitWidthProperty().bind(root.widthProperty());
        background.fitHeightProperty().bind(root.heightProperty());

        double baseWidth = 960.0;
        double baseHeight = 540.0;

        Scale scale = new Scale();
        gridPane.getTransforms().add(scale);

        root.widthProperty().addListener((obs, oldVal, newVal) -> updateScale(scale, root.getWidth(), root.getHeight(), baseWidth, baseHeight));
        root.heightProperty().addListener((obs, oldVal, newVal) -> updateScale(scale, root.getWidth(), root.getHeight(), baseWidth, baseHeight));

        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 10; col++) {
                final int r = row;
                final int c = col;

                StackPane stackPane = new StackPane();
                stackPane.setPadding(new Insets(5));
                stackPane.prefWidthProperty().bind(gridPane.widthProperty().divide(10));
                stackPane.prefHeightProperty().bind(stackPane.prefWidthProperty());

                ImageView imageView = new ImageView();
                cellViews.add(imageView);
                imageView.setPreserveRatio(false);
                imageView.fitWidthProperty().bind(stackPane.widthProperty());
                imageView.fitHeightProperty().bind(stackPane.heightProperty());

                imageView.setOnMouseClicked(event -> handleChoice(r, c));

                stackPane.getChildren().add(imageView);
                gridPane.add(stackPane, col, row);
            }
        }

        root.setStyle("-fx-background-color: transparent;");
        gridPane.setStyle("-fx-background-color: transparent;");

    }

    private void updateScale(Scale scale, double width, double height, double baseWidth, double baseHeight) {
        double scaleX = width / baseWidth;
        double scaleY = height / baseHeight;
        double scaleFactor = Math.min(scaleX, scaleY);

        scale.setX(scaleFactor);
        scale.setY(scaleFactor);

        double offsetX = (width - baseWidth * scaleFactor) / 2;
        double offsetY = (height - baseHeight * scaleFactor) / 2;
        gridPane.setTranslateX(offsetX);
        gridPane.setTranslateY(offsetY);
    }

    // todo: add button for closeFaceUpAction

    public void handleChoice(int row, int col) {
        int idx = row * 10 + col;

        gui.chooseFaceUp(idx);
    }

    public void update(Game game) {
        List<Integer> faceUpTiles = game.getFaceUpTiles();

        cellViews.forEach(iv -> iv.setImage(null));

        for (int i = 0; i < faceUpTiles.size() && i < cellViews.size(); i++) {

            int id = faceUpTiles.get(i) + 1;

            System.out.println("Loading faceup tiles.");
            String resourcePath = "/images/tiles/GT-new_tiles_16_for web" + id + ".jpg";

            Image img = new Image(
                    Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
            );
            cellViews.get(i).setImage(img);
        }
    }
}
