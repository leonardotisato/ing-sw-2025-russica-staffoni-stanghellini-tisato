package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.BuildState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.BatteryTile;
import it.polimi.ingsw.cg04.model.tiles.HousingTile;
import it.polimi.ingsw.cg04.model.tiles.StorageTile;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;

import java.net.URL;
import java.util.*;

public class AdventureCardSceneController extends ViewController {

    @FXML
    private GridPane shipGrid;

    @FXML
    private Button fixButton;

    @FXML
    private StackPane root;

    @FXML
    ImageView deck, currentCard;

    @FXML
    Button quitButton, solveButton, choiceButton, diceButton;

    @FXML
    Pane cardButtonsPane;

    @FXML
    TextArea logs, objectsInfo;

    @FXML
    ImageView shipImage, flightboardImg;

    @FXML private Polygon pos0_lev2, pos1_lev2, pos2_lev2, pos3_lev2, pos4_lev2, pos5_lev2,
            pos6_lev2, pos7_lev2, pos8_lev2, pos9_lev2, pos10_lev2,
            pos11_lev2, pos12_lev2, pos13_lev2, pos14_lev2, pos15_lev2,
            pos16_lev2, pos17_lev2, pos18_lev2, pos19_lev2, pos20_lev2,
            pos21_lev2, pos22_lev2, pos23_lev2;

    private List<Coordinates> tilesToBreak = new ArrayList<>();
    private Map<Coordinates, Integer> selectedBatties = new HashMap<>();
    private List<Coordinates> selectedCannons = new ArrayList<>();
    private List<Coordinates> selectedCrew = new ArrayList<>();
    private List<Coordinates> selectedStorage = new ArrayList<>();
    private List<Map<BoxType, Integer>> boxesMap = new ArrayList<>();

    private final Map<Integer, Polygon> level2Triangles = new HashMap<>();


    private final double BASE_WIDTH = 960;
    private final double BASE_HEIGHT = 540;

    private GUIRoot gui;

    @FXML
    private Group scalableGroup;
    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.widthProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        root.heightProperty().addListener((obs, oldVal, newVal) -> scaleUI());
        quitButton.setOnAction(event -> {
            gui.retire();
        });
        setupFlightboardTriangles();


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

    private void setupFlightboardTriangles() {
        level2Triangles.put(0, pos0_lev2);
        level2Triangles.put(1, pos1_lev2);
        level2Triangles.put(2, pos2_lev2);
        level2Triangles.put(3, pos3_lev2);
        level2Triangles.put(4, pos4_lev2);
        level2Triangles.put(5, pos5_lev2);
        level2Triangles.put(6, pos6_lev2);
        level2Triangles.put(7, pos7_lev2);
        level2Triangles.put(8, pos8_lev2);
        level2Triangles.put(9, pos9_lev2);
        level2Triangles.put(10, pos10_lev2);
        level2Triangles.put(11, pos11_lev2);
        level2Triangles.put(12, pos12_lev2);
        level2Triangles.put(13, pos13_lev2);
        level2Triangles.put(14, pos14_lev2);
        level2Triangles.put(15, pos15_lev2);
        level2Triangles.put(16, pos16_lev2);
        level2Triangles.put(17, pos17_lev2);
        level2Triangles.put(18, pos18_lev2);
        level2Triangles.put(19, pos19_lev2);
        level2Triangles.put(20, pos20_lev2);
        level2Triangles.put(21, pos21_lev2);
        level2Triangles.put(22, pos22_lev2);
        level2Triangles.put(23, pos23_lev2);
    }


    private void hidePane(Pane pane){
        pane.setVisible(false);
        pane.setManaged(false);
    }
    private void showPane(Pane pane){
        pane.setVisible(true);
        pane.setManaged(true);
    }

    public void update(Game game) {
        Player currentPlayer = game.getPlayer(gui.getClientNickname());
        game.getGameState().updateStateController(this, game);
        composeSceneByLevel(game.getLevel());
        updateShip(currentPlayer);
        updatePlayersInfo(game.getPlayers());
        updateFlightboardPositions(game);
    }

    @Override
    public void updateFlightController(Game game) {
        hidePane(cardButtonsPane);
        String resourcePath = "/images/cards/back" + game.getLevel() + ".jpg";
        try {
            Image img = new Image(
                    Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
            );
            deck.setImage(img);
            deck.setOnMouseEntered(e -> deck.setStyle("-fx-effect: dropshadow(gaussian, gold, 8, 0.6, 0, 0);"));
            deck.setOnMouseExited(e -> deck.setStyle(""));
            deck.setOnMouseClicked(event -> {
                System.out.println("Get Next Adventure Card clicked: ");
                gui.getNextAdventureCard();
            });
            currentCard.setImage(null);
        } catch (Exception e) {
            System.err.println("Image not found: " + resourcePath);
            e.printStackTrace();
        }
    }

    @Override
    public void updateOpenSpaceController(Game game) {
        showPane(cardButtonsPane);
        diceButton.setVisible(false);
        diceButton.setManaged(false);
        deck.setOnMouseEntered(null);
        deck.setOnMouseExited(null);
        deck.setOnMouseClicked(null);
        deck.setStyle(null);

        AdventureCard currCard = game.getCurrentAdventureCard();
        String resourcePath = "/images/cards/" + getKeyByValue(game.getAdventureCardsMap(), currCard) + ".jpg";
        try {
            Image img = new Image(
                    Objects.requireNonNull(getClass().getResource(resourcePath)).toExternalForm()
            );
            currentCard.setImage(img);
        } catch (Exception e) {
            System.err.println("Image not found: " + resourcePath);
            e.printStackTrace();
        }

        Player p = game.getPlayer(gui.getClientNickname());
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();
        int level = p.getGame().getLevel();

        if (selectedBatties.isEmpty()) {
            objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
        }

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
            Coordinates coords = new Coordinates(row, col);
            Tile tile = shipMatrix[row][col];

            if (coords.isIn(ship.getTilesMap().get("BatteryTile")) && (tile.getNumBatteries() - selectedBatties.getOrDefault(coords, 0)) > 0) {
                System.out.println("trovata una battery tile in coordinate " + coords);
                enableBatteryTileInteraction((BatteryTile) tile, stack, row, col);
            }

            solveButton.setOnAction(event -> {
                List<Coordinates> c;
                List<Integer> usedBatteries;
                if (selectedBatties.isEmpty()) {
                    c = new ArrayList<>();
                    usedBatteries = new ArrayList<>();
                } else {
                    c = new ArrayList<>(selectedBatties.keySet());
                    usedBatteries = new ArrayList<>(selectedBatties.values());
                }
                gui.choosePropulsor(c, usedBatteries);
                selectedBatties.clear();
            });

            choiceButton.setText("Clear selected");
            choiceButton.setOnAction(event -> {
                selectedBatties.clear();
                updateBatteriesView(game);
                objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
            });
        }
    }

    private <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void updateBatteriesView(Game game){
        Player p = game.getPlayer(gui.getClientNickname());
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();

        for (Node n : shipGrid.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(n);
            Integer rowIndex = GridPane.getRowIndex(n);

            int tempcol = colIndex == null ? 0 : colIndex;
            int row = rowIndex == null ? 0 : rowIndex;
            int level = game.getLevel();
            if (level == 1 && (tempcol == 0 || tempcol == 6)) {
                continue;
            } else if (level == 1) {
                tempcol = tempcol - 1;
            }

            int col = tempcol;
            Coordinates coords = new Coordinates(row, col);
            Tile tile = shipMatrix[row][col];

            if (ship.getTilesMap().get("BatteryTile").contains(coords)) {
                StackPane stack = (StackPane) n;
                updateBatteryTile((BatteryTile) tile, stack, row, col);
                enableBatteryTileInteraction((BatteryTile) tile, stack, row, col);
            }
        }
    }

    private void updateShip(Player p) {
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();

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
                String crewImagePath = "/images/human.png";
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
                String crewImagePath = "/images/pink_alien.png";
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
                String crewImagePath = "/images/brown_alien.png";
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

                String crewImagePath = "/images/human.png";
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
        Coordinates coordinates = new Coordinates(row, col);
        int currentBatteries = batteryTile.getNumBatteries() - selectedBatties.getOrDefault(coordinates, 0);

        HBox batteryBox = new HBox(1);
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
        batteryBox.setMouseTransparent(true);
        cellStack.getChildren().add(batteryBox);
    }

    private void enableBatteryTileInteraction(BatteryTile tile, StackPane stack, int row, int col) {
        Coordinates coords = new Coordinates(row, col);
        int remaining = tile.getNumBatteries() - selectedBatties.getOrDefault(coords, 0);

        ImageView cell = (ImageView)stack.getChildren().getFirst();

        if (remaining <= 0) {
            cell.setOnMouseClicked(null);
            cell.setOnMouseEntered(null);
            cell.setOnMouseExited(null);
            return;
        }

        cell.setOnMouseEntered(e -> {
            cell.setStyle("-fx-effect: dropshadow(gaussian, lawngreen, 10, 0.6, 0, 0);");
        });

        cell.setOnMouseExited(e -> cell.setStyle(""));

        cell.setOnMouseClicked(e -> {
            selectedBatties.put(coords, selectedBatties.getOrDefault(coords, 0) + 1);

            updateBatteryTile(tile, stack, row, col);
            enableBatteryTileInteraction(tile, stack, row, col);

            updateObjectsInfoText();
        });
    }

    private void updateObjectsInfoText() {
        if (selectedBatties.isEmpty()) {
            objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
            return;
        }

        StringBuilder sb = new StringBuilder("🔋 Selected Batteries:\n\n");
        selectedBatties.forEach((coord, count) ->
                sb.append(String.format("• (%d, %d): %s\n", coord.getX(), coord.getY(), "🔋".repeat(count)))
        );

        objectsInfo.setText(sb.toString());
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

    public void updateFlightboardPositions(Game game) {
        Map<String, String> playerColorHex = Map.of(
                "YELLOW", "#FFFF00",
                "RED", "#FF0000",
                "BLUE", "#0000FF",
                "GREEN", "#00FF00"
        );
        for (Polygon triangle : level2Triangles.values()) {
            triangle.setStyle("-fx-fill: transparent; -fx-stroke: transparent;");
        }

        for (Player player : game.getPlayers()) {
            int position = player.getPosition();
            PlayerColor color = player.getColor();
            Polygon triangle = level2Triangles.get(position);
            if (triangle != null && color != null) {
                triangle.setStyle("-fx-fill: " + playerColorHex.get(color.toString()) + "; -fx-stroke: black; -fx-stroke-width: 1;");
            }
        }
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

    @Override
    public void goToAdventureCardScene(GUIRoot gui) {
        return;
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

    @FXML
    private TextArea playersInfo;

    public void updatePlayersInfo(List<Player> players) {
        players.sort(Comparator.comparingInt(Player::getRanking));

        StringBuilder sb = new StringBuilder();
        for (Player player : players) {
            String nickname = player.getName();
            int ranking = player.getRanking();
            Double credits = player.getNumCredits();
            String colorHex = player.getColor().toString(); // esempio: "#ff00ff"

            sb.append(String.format("Rank %d: %s  [Credits: %.2f]  Color: %s%n", ranking, nickname, credits, colorHex));
        }

        playersInfo.setText(sb.toString());
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
    }

}
