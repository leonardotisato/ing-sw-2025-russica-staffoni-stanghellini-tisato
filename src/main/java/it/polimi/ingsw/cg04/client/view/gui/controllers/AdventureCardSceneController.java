package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.BuildState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.tiles.BatteryTile;
import it.polimi.ingsw.cg04.model.tiles.HousingTile;
import it.polimi.ingsw.cg04.model.tiles.StorageTile;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class AdventureCardSceneController extends ViewController {

    @FXML
    private GridPane shipGrid;

    @FXML
    private Button fixButton;

    private List<Coordinates> tilesToBreak = new ArrayList<>();
    private List<Coordinates> selectedBatties = new ArrayList<>();
    private List<Coordinates> selectedCannons = new ArrayList<>();
    private List<Coordinates> selectedCrew = new ArrayList<>();
    private List<Coordinates> selectedStorage = new ArrayList<>();
    private List<Map<BoxType, Integer>> boxesMap = new ArrayList<>();

    private GUIRoot gui;

    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void update(Game game) {
        Player currentPlayer = game.getPlayer(gui.getClientNickname());
        // updateCard(game.getAdventureCard());
        updateShip(currentPlayer);
    }

    private void updateShip(Player p) {
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();
        BuildState state = (BuildState) p.getGame().getGameState();

        int level = p.getGame().getLevel();

        for (Node node : shipGrid.getChildren()) {
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

            StackPane stack = (StackPane) node;

            ImageView cell = (ImageView) stack.getChildren().getFirst();
            Tile tile = shipMatrix[row][col];

            if (tile == null){
                cell.setImage(null);
            } else {
                String resourcePath = "/images/tiles/GT-new_tiles_16_for web" + tile.getId() + ".jpg";
                try {
                    Image img = new Image(
                            Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
                    );
                    cell.setImage(img);
                    cell.setRotate(tile.getRotation() * 90);
                    if(state.getPlayerState().get(p.getName()) == BuildPlayerState.FIXING){
                        setFixEffects(cell, row, col);
                        showFixButton();
                    }
                    else{
                        cell.setOnMouseClicked(null);
                    }
                    stack.setOnDragEntered(null);
                    stack.setOnDragExited(null);
                    stack.setOnDragOver(null);
                    stack.setOnDragDropped(null);

                    String type = tile.getName();
                    if (type.equals("Housing") || type.equals("Battery") || type.equals("S. Storage") || type.equals("N. Storage")) {
                        updateTile(stack, tile, row, col);
                    }

                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + resourcePath);
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateTile(StackPane cellStack, Tile tile, int row, int col) {
        String type = tile.getName();

        if(cellStack == null) return;

        switch (type) {
            case "Housing":
                updateHousingTile((HousingTile) tile, cellStack, row, col);
                break;

            case "Battery":
                updateBatteryTile((BatteryTile) tile, cellStack, row, col);
                break;

            case "S. Storage", "N. Storage":
                updateStorageTile((StorageTile) tile, cellStack, row, col);
                break;
        }
    }

    private void updateHousingTile(HousingTile housingTile, StackPane cellStack, int row, int col) {

        if (cellStack.getChildren().size() > 1) {
            cellStack.getChildren().subList(1, cellStack.getChildren().size()).clear();
        }

        int maxCrew = 2;
        int currentCrew = housingTile.getNumCrew();

        if(currentCrew == 0) return;

        Node crewContainer;

        if (currentCrew == 1) {
            ImageView crewImage = new ImageView();
            crewImage.setFitWidth(25);
            crewImage.setFitHeight(25);
            crewImage.setPreserveRatio(true);

            if (housingTile.getHostedCrewType() == CrewType.HUMAN) {
                String crewImagePath = "/images/objects/human.png";
                try {
                    Image crewImg = new Image(Objects.requireNonNull(getClass().getResource(crewImagePath)).toExternalForm());
                    crewImage.setImage(crewImg);
                    crewImage.setOpacity(1.0);
                    onCrewClicked(crewImage, row, col);
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + crewImagePath);
                    e.printStackTrace();
                }
            } else if (housingTile.getHostedCrewType() == CrewType.PINK_ALIEN) {
                String crewImagePath = "/images/objects/pink.png";
                try {
                    Image crewImg = new Image(Objects.requireNonNull(getClass().getResource(crewImagePath)).toExternalForm());
                    crewImage.setImage(crewImg);
                    crewImage.setOpacity(1.0);
                    onCrewClicked(crewImage, row, col);
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + crewImagePath);
                    e.printStackTrace();
                }
            } else if (housingTile.getHostedCrewType() == CrewType.BROWN_ALIEN){
                String crewImagePath = "/images/objects/brown.png";
                try {
                    Image crewImg = new Image(Objects.requireNonNull(getClass().getResource(crewImagePath)).toExternalForm());
                    crewImage.setImage(crewImg);
                    crewImage.setOpacity(1.0);
                    onCrewClicked(crewImage, row, col);
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + crewImagePath);
                    e.printStackTrace();
                }
            }

            crewContainer = crewImage;
            StackPane.setAlignment(crewContainer, Pos.CENTER);

        } else {
            HBox crewBox = new HBox(3);
            crewBox.setAlignment(Pos.CENTER);

            for (int i = 0; i < maxCrew; i++) {
                ImageView crewImage = new ImageView();
                crewImage.setFitWidth(18);
                crewImage.setFitHeight(20);
                crewImage.setPreserveRatio(true);

                String crewImagePath = "/images/objects/human.png";
                try {
                    Image crewImg = new Image(Objects.requireNonNull(getClass().getResource(crewImagePath)).toExternalForm());
                    crewImage.setImage(crewImg);
                    crewImage.setOpacity(1.0);
                    onCrewClicked(crewImage, row, col);
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + crewImagePath);
                    e.printStackTrace();
                }

                crewBox.getChildren().add(crewImage);
            }

            crewContainer = crewBox;
        }

        cellStack.getChildren().add(crewContainer);
    }

    private void updateBatteryTile(BatteryTile batteryTile, StackPane cellStack, int row, int col) {

        if (cellStack.getChildren().size() > 1) {
            cellStack.getChildren().subList(1, cellStack.getChildren().size()).clear();
        }

        int maxBatteries = batteryTile.getMaxBatteryCapacity();
        int currentBatteries = batteryTile.getNumBatteries();

        VBox batteryBox = new VBox(1);
        batteryBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < currentBatteries; i++) {
            ImageView batteryImage = new ImageView();
            batteryImage.setFitWidth(8);
            batteryImage.setFitHeight(15);
            batteryImage.setPreserveRatio(true);

            String batteryImagePath = "/images/objects/battery.png";
            try {
                Image batteryImg = new Image(Objects.requireNonNull(getClass().getResource(batteryImagePath)).toExternalForm());
                batteryImage.setImage(batteryImg);
                onBatteryClicked(batteryImage, row, col);

            } catch (Exception e) {
                System.err.println("Immagine non trovata: " + batteryImagePath);
                e.printStackTrace();
            }

            batteryBox.getChildren().add(batteryImage);
        }

        batteryBox.setRotate(batteryTile.getRotation() * 90);

        cellStack.getChildren().add(batteryBox);
    }

    private void updateStorageTile(StorageTile storageTile, StackPane cellStack, int row, int col) {

        if (cellStack.getChildren().size() > 1) {
            cellStack.getChildren().subList(1, cellStack.getChildren().size()).clear();
        }

        int maxStorage = storageTile.getMaxBoxes();
        Map<BoxType, Integer> boxes = storageTile.getBoxes();
        int usedStorage = 0;
        for(BoxType boxType : boxes.keySet()){
            usedStorage += boxes.get(boxType);
        }

        Node storageContainer;

        List<String> colorList = new ArrayList<>();

        switch (maxStorage) {
            case 1:

                ImageView singleStorage = new ImageView();
                singleStorage.setFitWidth(12);
                singleStorage.setFitHeight(12);
                singleStorage.setPreserveRatio(true);

                if (usedStorage == 1) {

                    String color = "";
                    for (BoxType boxType : boxes.keySet()) {
                        if(boxes.get(boxType) == 1) {
                            color = boxType.toString().toLowerCase();
                        }
                    }

                    String boxImage = "/images/objects/" + color + ".png";
                    try {
                        Image itemImg = new Image(Objects.requireNonNull(getClass().getResource(boxImage)).toExternalForm());
                        singleStorage.setImage(itemImg);
                        singleStorage.setOpacity(1.0);
                        onStorageClicked(singleStorage, row, col);
                    } catch (Exception e) {
                        System.err.println("Immagine non trovata: " + boxImage);
                        e.printStackTrace();
                    }
                } else {
                    singleStorage.setImage(null);
                }

                storageContainer = singleStorage;
                StackPane.setAlignment(storageContainer, Pos.CENTER);
                break;

            case 2:

                VBox doubleStorage = new VBox(2);
                doubleStorage.setAlignment(Pos.CENTER);

                for (BoxType boxType : boxes.keySet()) {
                    if(boxes.get(boxType) > 0) {
                        for (int i = 0; i < boxes.get(boxType); i++) {
                            colorList.add(boxType.toString().toLowerCase());
                        }
                    }
                }

                for (int i = 0; i < usedStorage; i++) {
                    ImageView storageImage = new ImageView();
                    storageImage.setFitWidth(12);
                    storageImage.setFitHeight(12);
                    storageImage.setPreserveRatio(true);


                    String boxImage = "/images/objects/" + colorList.get(i) + ".png";

                    try {
                        Image itemImg = new Image(Objects.requireNonNull(getClass().getResource(boxImage)).toExternalForm());
                        storageImage.setImage(itemImg);
                        storageImage.setOpacity(1.0);
                        onStorageClicked(storageImage, row, col);
                    } catch (Exception e) {
                        System.err.println("Immagine non trovata: " + boxImage);
                        e.printStackTrace();
                    }

                    doubleStorage.getChildren().add(storageImage);
                }

                storageContainer = doubleStorage;

                colorList.clear();

                break;

            default:

                VBox triangleLayout = new VBox(1);
                triangleLayout.setAlignment(Pos.CENTER);

                HBox topRow = new HBox(2);
                topRow.setAlignment(Pos.CENTER);

                for (BoxType boxType : boxes.keySet()) {
                    if(boxes.get(boxType) > 0) {
                        for (int i = 0; i < boxes.get(boxType); i++) {
                            colorList.add(boxType.toString().toLowerCase());
                        }
                    }
                }

                for (int i = 0; i < 2; i++) {
                    ImageView storageImage = new ImageView();
                    storageImage.setFitWidth(12);
                    storageImage.setFitHeight(12);
                    storageImage.setPreserveRatio(true);

                    if (i < usedStorage) {
                        String boxImage = "/images/objects/" + colorList.get(i) + ".png";
                        try {
                            Image itemImg = new Image(Objects.requireNonNull(getClass().getResource(boxImage)).toExternalForm());
                            storageImage.setImage(itemImg);
                            storageImage.setOpacity(1.0);
                            onStorageClicked(storageImage, row, col);
                        } catch (Exception e) {
                            System.err.println("Immagine non trovata: " + boxImage);
                            e.printStackTrace();
                        }
                    } else {
                        storageImage.setImage(null);
                    }

                    topRow.getChildren().add(storageImage);
                }

                ImageView bottomStorage = new ImageView();
                bottomStorage.setFitWidth(12);
                bottomStorage.setFitHeight(12);
                bottomStorage.setPreserveRatio(true);

                if (usedStorage > 2) {
                    String boxImage = "/images/objects/" + colorList.get(2) + ".png";
                    try {
                        Image itemImg = new Image(Objects.requireNonNull(getClass().getResource(boxImage)).toExternalForm());
                        bottomStorage.setImage(itemImg);
                        bottomStorage.setOpacity(1.0);
                        onStorageClicked(bottomStorage, row, col);
                    } catch (Exception e) {
                        System.err.println("Immagine non trovata: " + boxImage);
                        e.printStackTrace();
                    }
                } else {
                    bottomStorage.setImage(null);
                }

                triangleLayout.getChildren().addAll(topRow, bottomStorage);
                storageContainer = triangleLayout;

                colorList.clear();

                break;
        }

        storageContainer.setRotate(storageTile.getRotation() * 90);

        cellStack.getChildren().add(storageContainer);
    }

    // todo: fix the onXClicked() methods

    public void onCrewClicked(ImageView cell, int row, int col) {
        Coordinates coordinates = new Coordinates(row, col);
        cell.setOnMouseClicked(event -> {
            if(!coordinates.isIn(tilesToBreak)) {
                cell.setStyle("-fx-effect: dropshadow(gaussian, red, 10, 0.6, 0, 0);");
                tilesToBreak.add(coordinates);
            }
            else{
                cell.setStyle("");
                tilesToBreak.remove(coordinates);
            }
        });
    }

    public void onBatteryClicked(ImageView cell, int row, int col) {
        Coordinates coordinates = new Coordinates(row, col);
        cell.setOnMouseClicked(event -> {
            if(!coordinates.isIn(tilesToBreak)) {
                cell.setStyle("-fx-effect: dropshadow(gaussian, lawngreen, 10, 0.6, 0, 0);");
                tilesToBreak.add(coordinates);
            }
            else{
                cell.setStyle("");
                tilesToBreak.remove(coordinates);
            }
        });
    }

    public void onStorageClicked(ImageView cell, int row, int col) {
        Coordinates coordinates = new Coordinates(row, col);
        cell.setOnMouseClicked(event -> {
            if(!coordinates.isIn(tilesToBreak)) {
                cell.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
                tilesToBreak.add(coordinates);
            }
            else{
                cell.setStyle("");
                tilesToBreak.remove(coordinates);
            }
        });
    }


    public void setFixEffects(ImageView cell, int row, int col) {
        Coordinates coordinates = new Coordinates(row, col);
        cell.setOnMouseClicked(event -> {
            if(!coordinates.isIn(tilesToBreak)) {
                cell.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
                tilesToBreak.add(coordinates);
            }
            else{
                cell.setStyle("");
                tilesToBreak.remove(coordinates);
            }
        });
    }

    public void hideAllButtons(){
        // todo: implement
    }

    @FXML
    private void handleFixButtonClick() {
        gui.fixShip(tilesToBreak);
        tilesToBreak.clear();
    }

    public void showFixButton(){
        hideAllButtons();
        fixButton.setVisible(true);
        fixButton.setManaged(true);
    }
}
