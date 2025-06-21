package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
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
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;

import java.net.URL;
import java.util.*;

public class AdventureCardSceneController extends ViewController {

    @FXML
    private GridPane shipGrid, boxesGrid;

    @FXML
    private Button fixButton;

    @FXML
    private StackPane root;

    @FXML
    private ImageView deck, currentCard;

    @FXML
    private Button quitButton, solveButton, choiceButton, diceButton;

    @FXML
    private Pane cardButtonsPane;

    @FXML
    private TextArea logs, objectsInfo;

    @FXML
    private ImageView shipImage, flightboardImg;

    @FXML private Polygon pos0_lev2, pos1_lev2, pos2_lev2, pos3_lev2, pos4_lev2, pos5_lev2,
            pos6_lev2, pos7_lev2, pos8_lev2, pos9_lev2, pos10_lev2,
            pos11_lev2, pos12_lev2, pos13_lev2, pos14_lev2, pos15_lev2,
            pos16_lev2, pos17_lev2, pos18_lev2, pos19_lev2, pos20_lev2,
            pos21_lev2, pos22_lev2, pos23_lev2;

    private List<Coordinates> tilesToBreak = new ArrayList<>();
    private Map<Coordinates, Integer> selectedBatties = new HashMap<>();
    private List<Coordinates> selectedCannons = new ArrayList<>();
    private Map<Coordinates, Integer> selectedCrew = new HashMap<>();
    private List<Coordinates> selectedStorage = new ArrayList<>();
    private List<Map<BoxType, Integer>> boxesMap = new ArrayList<>();

    private final Map<Integer, Polygon> level2Triangles = new HashMap<>();


    private final double BASE_WIDTH = 960;
    private final double BASE_HEIGHT = 540;

    private final Map<BoxType, ImageView> boxImages = new EnumMap<>(BoxType.class);
    private final Map<BoxType, Integer> remainingBoxes = new EnumMap<>(BoxType.class);
    private final Map<BoxType, Image> loadedBoxImages = new EnumMap<>(BoxType.class);

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
        resetTileInteractions();
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
            quitButton.setText("Retire");
            quitButton.setOnAction(event -> {
                gui.retire();
            });
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

        loadCurrentCard(game);

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

        }
        choiceButton.setText("Clear selected");
        choiceButton.setOnAction(event -> {
            selectedBatties.clear();
            updateBatteriesView(game);
            objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
        });
    }

    @Override
    public void updateAbandonedShipController(Game game) {
        showPane(cardButtonsPane);
        diceButton.setVisible(false);
        diceButton.setManaged(false);
        deck.setOnMouseEntered(null);
        deck.setOnMouseExited(null);
        deck.setOnMouseClicked(null);
        deck.setStyle(null);

        loadCurrentCard(game);

        Player p = game.getPlayer(gui.getClientNickname());
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();
        int level = p.getGame().getLevel();

        if (selectedCrew.isEmpty()) {
            objectsInfo.setText("Selected Crew:\n\nNone selected.");
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
            Coordinates coords = new Coordinates(row, col);
            Tile tile = shipMatrix[row][col];

            if (coords.isIn(ship.getTilesMap().get("HousingTile")) && (tile.getNumCrew() - selectedCrew.getOrDefault(coords, 0)) > 0) {
                updateHousingTile((HousingTile) tile, stack, row, col);
                enableHousingTileInteraction((HousingTile) tile, stack, row, col);
            }

            solveButton.setOnAction(event -> {
                List<Coordinates> c;
                List<Integer> removedCrew;
                if (selectedCrew.isEmpty()) {
                    c = new ArrayList<>();
                    removedCrew = new ArrayList<>();
                } else {
                    c = new ArrayList<>(selectedCrew.keySet());
                    removedCrew = new ArrayList<>(selectedCrew.values());
                }
                gui.removeCrew(c, removedCrew);
                if (!selectedCrew.isEmpty()) selectedCrew.clear();
            });
        }

        choiceButton.setText("Clear selected");
        choiceButton.setOnAction(event -> {
            selectedCrew.clear();
            updateCrewView(game);
            updateCrewInfoText();
        });

        quitButton.setText("Pass Turn");
        quitButton.setOnAction(event -> {
            gui.removeCrew(null, null);
            selectedCrew.clear();
        });
    }

    @Override
    public void updateStardustController(Game game) {
        showPane(cardButtonsPane);
        diceButton.setVisible(false);
        diceButton.setManaged(false);
        deck.setOnMouseEntered(null);
        deck.setOnMouseExited(null);
        deck.setOnMouseClicked(null);
        choiceButton.setVisible(false);
        choiceButton.setManaged(false);
        quitButton.setVisible(false);
        quitButton.setManaged(false);
        deck.setStyle(null);

        loadCurrentCard(game);

        solveButton.setOnAction(event -> {
            gui.starDust();
        });

        if (selectedBatties.isEmpty()) {
            objectsInfo.setText("");
        }
    }

    @Override
    public void updateEpidemicController(Game game) {
        showPane(cardButtonsPane);
        diceButton.setVisible(false);
        diceButton.setManaged(false);
        deck.setOnMouseEntered(null);
        deck.setOnMouseExited(null);
        deck.setOnMouseClicked(null);
        choiceButton.setVisible(false);
        choiceButton.setManaged(false);
        quitButton.setVisible(false);
        quitButton.setManaged(false);
        deck.setStyle(null);

        loadCurrentCard(game);

        solveButton.setOnAction(event -> {
            gui.epidemic();
        });

        if (selectedBatties.isEmpty()) {
            objectsInfo.setText("");
        }
    }

    @Override
    public void updateAbandonedStationController(Game game) {
        setupBoxesGrid(game);
        showPane(cardButtonsPane);
        diceButton.setVisible(false);
        diceButton.setManaged(false);
        deck.setOnMouseEntered(null);
        deck.setOnMouseExited(null);
        deck.setOnMouseClicked(null);
        deck.setStyle(null);

        loadCurrentCard(game);

        Player p = game.getPlayer(gui.getClientNickname());
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();
        int level = p.getGame().getLevel();

        objectsInfo.setVisible(false);
        objectsInfo.setManaged(false);



        solveButton.setOnAction(event -> {
            List<Coordinates> c;
            List<Map<BoxType, Integer>> storageMap;
            if (selectedStorage.isEmpty()) {
                c = new ArrayList<>();
                storageMap = new ArrayList<>();
            } else {
                c = new ArrayList<>(selectedBatties.keySet());
                storageMap = new ArrayList<>(boxesMap);
            }
            gui.handleBoxes(c, storageMap);
            selectedStorage.clear();
            boxesMap.clear();
            });

        choiceButton.setText("Reset boxes");
        choiceButton.setOnAction(event -> {
            selectedStorage.clear();
            boxesMap.clear();
            //updateStorageView(game);
        });
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

    public void updateCrewView(Game game){
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

            if (ship.getTilesMap().get("HousingTile").contains(coords)) {
                StackPane stack = (StackPane) n;
                updateHousingTile((HousingTile) tile, stack, row, col);
                enableHousingTileInteraction((HousingTile) tile, stack, row, col);
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
        Coordinates coordinates = new Coordinates(row, col);
        int currentCrew = housingTile.getNumCrew() - selectedCrew.getOrDefault(coordinates, 0);

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
                } catch (Exception e) {
                    System.err.println("Immagine non trovata: " + crewImagePath);
                    e.printStackTrace();
                }

                crewBox.getChildren().add(crewImage);
            }

            crewContainer = crewBox;
        }
        crewContainer.setMouseTransparent(true);
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

            updateBatteriesInfoText();
        });
    }

    private void enableHousingTileInteraction(HousingTile tile, StackPane stack, int row, int col) {
        Coordinates coords = new Coordinates(row, col);
        int remaining = tile.getNumCrew() - selectedCrew.getOrDefault(coords, 0);

        ImageView cell = (ImageView)stack.getChildren().getFirst();

        if (remaining <= 0) {
            cell.setOnMouseClicked(null);
            cell.setOnMouseEntered(null);
            cell.setOnMouseExited(null);
            return;
        }

        cell.setOnMouseEntered(e -> {
            cell.setStyle("-fx-effect: dropshadow(gaussian, red, 10, 0.6, 0, 0);");
        });

        cell.setOnMouseExited(e -> cell.setStyle(""));

        cell.setOnMouseClicked(e -> {
            selectedCrew.put(coords, selectedCrew.getOrDefault(coords, 0) + 1);
            updateHousingTile(tile, stack, row, col);
            enableHousingTileInteraction(tile, stack, row, col);
            updateCrewInfoText();
        });
    }

    private void updateBatteriesInfoText() {
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

    private void updateCrewInfoText() {
        if (selectedCrew.isEmpty()) {
            objectsInfo.setText("Selected Crew:\n\nNone selected.");
            return;
        }

        StringBuilder sb = new StringBuilder("Selected crew:\n\n");
        selectedCrew.forEach((coord, count) ->
                sb.append(String.format("• (%d, %d): %d\n", coord.getX(), coord.getY(), count))
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
                        enableStorageTileInteraction(singleStorage, row, col);
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
                        enableStorageTileInteraction(storageImage, row, col);
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
                            enableStorageTileInteraction(storageImage, row, col);
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
                        enableStorageTileInteraction(bottomStorage, row, col);
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

    private void setupBoxesGrid(Game game) {
        AdventureCard card = game.getCurrentAdventureCard();
        Map<BoxType, Integer> rewards = card.getObtainedResources();
        remainingBoxes.clear();

        for (Node node : boxesGrid.getChildren()) {
            ImageView imageView = (ImageView) node;
            int col = GridPane.getColumnIndex(imageView);

            BoxType type = switch (col) {
                case 0 -> BoxType.RED;
                case 1 -> BoxType.YELLOW;
                case 2 -> BoxType.GREEN;
                case 3 -> BoxType.BLUE;
                default -> null;
            };

            if (type != null && rewards.containsKey(type) && rewards.get(type) > 0) {
                String path = "/images/objects/" + type.name().toLowerCase() + ".png";
                try {
                    Image img = new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
                    imageView.setImage(img);
                    imageView.setVisible(true);
                    imageView.setOpacity(1.0);

                    loadedBoxImages.put(type, img);
                    remainingBoxes.put(type, rewards.get(type));

                    imageView.setOnDragDetected(e -> {
                        if (remainingBoxes.getOrDefault(type, 0) > 0) {
                            Dragboard db = imageView.startDragAndDrop(TransferMode.MOVE);
                            db.setDragView(imageView.getImage());
                            db.setContent(Map.of(DataFormat.PLAIN_TEXT, type.name()));
                            e.consume();
                        }
                    });

                    } catch (Exception e) {
                        System.err.println("Box image not found: " + path);
                        e.printStackTrace();
                    }
                } else {
                    imageView.setImage(null);
                    imageView.setVisible(false);
                }
        }
    }


    // todo: fix the onXClicked() methods

    public void onCrewClicked(ImageView crewImage, int row, int col) {
        Coordinates coordinates = new Coordinates(row, col);
        String selectedEffect = "-fx-effect: dropshadow(gaussian, red, 10, 0.6, 0, 0);";
        crewImage.setOnMouseEntered(e -> {
            if (!selectedEffect.equals(crewImage.getStyle()))
                crewImage.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
        });

        crewImage.setOnMouseExited(e -> {
            if (!selectedEffect.equals(crewImage.getStyle()))
                crewImage.setStyle("");
        });

        crewImage.setOnMouseClicked(e -> {
            if (selectedEffect.equals(crewImage.getStyle())) {
                crewImage.setStyle("");
                selectedCrew.put(coordinates, selectedCrew.getOrDefault(coordinates, 0) - 1);
                if(selectedCrew.getOrDefault(coordinates, 0) == 0) {
                    selectedCrew.remove(coordinates);
                }
            } else {
                crewImage.setStyle(selectedEffect);
                selectedCrew.put(coordinates, selectedCrew.getOrDefault(coordinates, 0) + 1);
            }
            updateCrewInfoText();
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

    public void enableStorageTileInteraction(ImageView targetImage, int row, int col) {
        Coordinates coordinates = new Coordinates(row, col);

        targetImage.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        targetImage.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                try {
                    BoxType newBox = BoxType.valueOf(db.getString());

                    String currentUrl = targetImage.getImage() != null ? targetImage.getImage().getUrl() : null;
                    BoxType oldBox = null;

                    if (currentUrl != null) {
                        for (BoxType bt : BoxType.values()) {
                            if (currentUrl.contains(bt.toString().toLowerCase())) {
                                oldBox = bt;
                                break;
                            }
                        }
                    }

                    String newPath = "/images/objects/" + newBox.toString().toLowerCase() + ".png";
                    Image img = new Image(Objects.requireNonNull(getClass().getResource(newPath)).toExternalForm());
                    targetImage.setImage(img);

                    int index = selectedStorage.indexOf(coordinates);
                    if (index == -1) {
                        selectedStorage.add(coordinates);
                        boxesMap.add(new EnumMap<>(BoxType.class));
                        index = boxesMap.size() - 1;
                    }

                    Map<BoxType, Integer> content = boxesMap.get(index);

                    if (oldBox != null) {
                        content.put(oldBox, content.getOrDefault(oldBox, 1) - 1);
                        if (content.get(oldBox) <= 0) {
                            content.remove(oldBox);
                        }
                        remainingBoxes.put(oldBox, remainingBoxes.getOrDefault(oldBox, 0) + 1);
                    }

                    content.put(newBox, content.getOrDefault(newBox, 0) + 1);
                    remainingBoxes.put(newBox, remainingBoxes.getOrDefault(newBox, 0) - 1);

                    updateBoxesGrid();

                    event.setDropCompleted(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    event.setDropCompleted(false);
                }
            }
            event.consume();
        });
    }

    private void updateBoxesGrid() {
        for (Node node : boxesGrid.getChildren()) {
            ImageView imageView = (ImageView) node;
            int col = GridPane.getColumnIndex(imageView) != null ? GridPane.getColumnIndex(imageView) : 0;
            BoxType type = switch (col) {
                case 0 -> BoxType.RED;
                case 1 -> BoxType.YELLOW;
                case 2 -> BoxType.GREEN;
                case 3 -> BoxType.BLUE;
                default -> null;
            };

            if (type != null && remainingBoxes.getOrDefault(type, 0) > 0) {
                imageView.setImage(loadedBoxImages.get(type));
                imageView.setVisible(true);
            } else {
                imageView.setImage(null);
                imageView.setVisible(false);
            }
        }
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
            if(position < 0) {
                position = level2Triangles.size() + position;
            }
            Polygon triangle = level2Triangles.get(position);
            System.out.println("Position: " + position + " Color: " + color + " Triangle: ");
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

    private void loadCurrentCard(Game game){
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
    }

    private void resetTileInteractions() {
        for (Node node : shipGrid.getChildren()) {
            StackPane stack = (StackPane) node;
            ImageView cell = (ImageView) stack.getChildren().getFirst();
            cell.setOnMouseEntered(null);
            cell.setOnMouseExited(null);
            cell.setOnMouseClicked(null);
            cell.setStyle("");

            if (stack.getChildren().size() > 1) {
                for (int i = 1; i < stack.getChildren().size(); i++) {
                    Node child = stack.getChildren().get(i);
                    child.setStyle("");
                    child.setOnMouseClicked(null);
                    child.setOnMouseEntered(null);
                    child.setOnMouseExited(null);
                }
            }
        }
    }


}
