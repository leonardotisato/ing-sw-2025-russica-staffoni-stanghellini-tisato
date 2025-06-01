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

    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.widthProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        root.heightProperty().addListener((obs, oldVal, newVal) -> scaleUI());

        shipGridList = List.of(shipGrid1, shipGrid2, shipGrid3, shipGrid4);

        nicknames = List.of(nick1, nick2, nick3, nick4);

        backgrounds = List.of(back1, back2, back3, back4);
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

    public void updateShipGrid(Ship ship, int idx, int level) {
        Tile[][] shipMatrix = ship.getTilesMatrix();

        for (Node node : shipGridList.get(idx).getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            int tempcol = colIndex == null ? 0 : colIndex;
            int temprow = rowIndex == null ? 0 : rowIndex;

            tempcol = level == 1 ? tempcol + 1 : tempcol;

            if(tempcol == ship.getShipWidth()) {
                break;
            }

            int col = tempcol;
            int row = temprow;

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

    @FXML
    public void back() {
        gui.home();
    }

    @Override
    public void goToViewOthersScene(GUIRoot gui) throws IOException {
        return;
    }

}
