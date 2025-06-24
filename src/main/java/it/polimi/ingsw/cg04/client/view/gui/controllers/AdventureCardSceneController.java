package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.*;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.adventureCards.Pirates;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.*;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AdventureCardSceneController extends ViewController {

    @FXML
    private GridPane shipGrid, boxesGrid, planetsGrid;

    @FXML
    private StackPane root;

    @FXML
    private ImageView deck, currentCard;

    @FXML
    private Button quitButton, solveButton, choiceButton, diceButton, viewShipsButton;

    @FXML
    private Pane cardButtonsPane, flightButtonsPane;

    @FXML
    private TextArea logs, objectsInfo;

    @FXML
    private ImageView shipImage, flightboardImg;

    @FXML
    private TextField diceResult;

    @FXML
    private Polygon pos0_lev2, pos1_lev2, pos2_lev2, pos3_lev2, pos4_lev2, pos5_lev2,
            pos6_lev2, pos7_lev2, pos8_lev2, pos9_lev2, pos10_lev2,
            pos11_lev2, pos12_lev2, pos13_lev2, pos14_lev2, pos15_lev2,
            pos16_lev2, pos17_lev2, pos18_lev2, pos19_lev2, pos20_lev2,
            pos21_lev2, pos22_lev2, pos23_lev2;


    @FXML
    private Polygon pos0_lev1, pos1_lev1, pos2_lev1, pos3_lev1, pos4_lev1, pos5_lev1,
            pos6_lev1, pos7_lev1, pos8_lev1, pos9_lev1, pos10_lev1,
            pos11_lev1, pos12_lev1, pos13_lev1, pos14_lev1, pos15_lev1,
            pos16_lev1, pos17_lev1;

    private List<Coordinates> tilesToBreak = new ArrayList<>();
    private Map<Coordinates, Integer> selectedBatties = new HashMap<>();
    private List<Coordinates> selectedCannons = new ArrayList<>();
    private Map<Coordinates, Integer> selectedCrew = new HashMap<>();
    private List<Coordinates> selectedStorage = new ArrayList<>();
    private List<Map<BoxType, Integer>> boxesMaps = new ArrayList<>();

    private final Map<Integer, Polygon> level1Triangles = new HashMap<>();
    private final Map<Integer, Polygon> level2Triangles = new HashMap<>();


    private final double BASE_WIDTH = 960;
    private final double BASE_HEIGHT = 540;

    private final Map<Coordinates, Map<BoxType, Integer>> updatedStorageMap = new HashMap<>();
    private final Map<BoxType, Integer> remainingBoxes = new EnumMap<>(BoxType.class);
    private final Map<BoxType, Image> loadedBoxImages = new EnumMap<>(BoxType.class);
    private ImageView draggedImageView = null;
    private List<ImageView> storageImages = new ArrayList<>();
    private Integer chosenPlanetIdx;

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
        hidePlanetsButtons();
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
        level1Triangles.put(0, pos0_lev1);
        level1Triangles.put(1, pos1_lev1);
        level1Triangles.put(2, pos2_lev1);
        level1Triangles.put(3, pos3_lev1);
        level1Triangles.put(4, pos4_lev1);
        level1Triangles.put(5, pos5_lev1);
        level1Triangles.put(6, pos6_lev1);
        level1Triangles.put(7, pos7_lev1);
        level1Triangles.put(8, pos8_lev1);
        level1Triangles.put(9, pos9_lev1);
        level1Triangles.put(10, pos10_lev1);
        level1Triangles.put(11, pos11_lev1);
        level1Triangles.put(12, pos12_lev1);
        level1Triangles.put(13, pos13_lev1);
        level1Triangles.put(14, pos14_lev1);
        level1Triangles.put(15, pos15_lev1);
        level1Triangles.put(16, pos16_lev1);
        level1Triangles.put(17, pos17_lev1);
    }


    private void hidePane(Pane pane) {
        pane.setVisible(false);
        pane.setManaged(false);
        pane.setMouseTransparent(true);
    }

    private void showPane(Pane pane) {
        pane.setVisible(true);
        pane.setManaged(true);
        pane.setMouseTransparent(false);
    }

    public void update(Game game) {
        Player currentPlayer = game.getPlayer(gui.getClientNickname());
        Ship ship = currentPlayer.getShip();
        selectedStorage.clear();
        boxesMaps.clear();
        for (Coordinates c : ship.getTilesMap().get("StorageTile")) {
            selectedStorage.add(c);
            boxesMaps.add(ship.getTile(c.getX(), c.getY()).getBoxes());
        }
        composeSceneByLevel(game.getLevel());
        updateShip(currentPlayer);
        resetTileInteractions();
        makeBoxesContainersTransparent(game);
        updatePlayersInfo(game.getPlayers());
        updateFlightboardPositions(game);
        game.getGameState().updateStateController(this, game);
    }

    @Override
    public void updateFlightController(Game game) {
        hidePane(cardButtonsPane);
        hidePane(boxesGrid);
        hidePane(planetsGrid);
        showPane(flightButtonsPane);
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
        diceResult.setText("");
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
        AdventureCard card = game.getCurrentAdventureCard();
        Map<BoxType, Integer> rewards = new HashMap<>(card.getObtainedResources());
        setupBoxesGrid(game, rewards);
        enableAllStorageTileInteractions(game);
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
                c = new ArrayList<>(selectedStorage);
                storageMap = new ArrayList<>(boxesMaps);
            }
            gui.handleBoxes(c, storageMap);
            selectedStorage.clear();
            boxesMaps.clear();
        });

        choiceButton.setVisible(true);
        choiceButton.setManaged(true);
        choiceButton.setText("Pass turn");
        choiceButton.setOnAction(event -> {
            gui.handleBoxes(null, null);
            //updateStorageView(game);
        });
    }

    @Override
    public void updatePlanetsController(Game game) {
        showPane(cardButtonsPane);
        showPane(planetsGrid);
        showPlanetsButtons(game);
        diceButton.setVisible(false);
        diceButton.setManaged(false);
        solveButton.setVisible(false);
        solveButton.setManaged(false);
        quitButton.setVisible(false);
        quitButton.setManaged(false);
        deck.setOnMouseEntered(null);
        deck.setOnMouseExited(null);
        deck.setOnMouseClicked(null);
        deck.setStyle(null);

        loadCurrentCard(game);

        Player p = game.getPlayer(gui.getClientNickname());

        objectsInfo.setVisible(false);
        objectsInfo.setManaged(false);


        solveButton.setOnAction(event -> {
            List<Coordinates> c;
            List<Map<BoxType, Integer>> storageMap;
            if (selectedStorage.isEmpty()) {
                c = new ArrayList<>();
                storageMap = new ArrayList<>();
            } else {
                c = new ArrayList<>(selectedStorage);
                storageMap = new ArrayList<>(boxesMaps);
            }
            gui.landToPlanet(chosenPlanetIdx, c, storageMap);
            selectedStorage.clear();
            boxesMaps.clear();
        });

        choiceButton.setText("Pass turn");
        choiceButton.toFront();
        choiceButton.setOnAction(event -> {
            gui.landToPlanet(null, null, null);
            hidePlanetsButtons();
        });
    }

    @Override
    public void updateSlaversController(Game game) {

        Player p = game.getPlayer(gui.getClientNickname());
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();
        int level = p.getGame().getLevel();
        SlaversState state = (SlaversState) game.getGameState();

        showPane(cardButtonsPane);
        deck.setOnMouseEntered(null);
        deck.setOnMouseExited(null);
        deck.setOnMouseClicked(null);
        deck.setStyle(null);

        choiceButton.setVisible(false);
        choiceButton.setManaged(false);
        diceButton.setVisible(false);
        diceButton.setManaged(false);

        if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 3) { // DECIDE_REWARD = 3
            choiceButton.setVisible(true);
            choiceButton.setManaged(true);
            choiceButton.setText("Reject");
            choiceButton.setOnAction(event -> {
                gui.getRewards(false);
            });

            diceButton.setVisible(true);
            diceButton.setManaged(true);
            diceButton.setText("Accept");
            diceButton.setOnAction(event -> {
                gui.getRewards(true);
            });
        }

        quitButton.setVisible(true);
        quitButton.setManaged(true);
        quitButton.setText("Clear Selected");
        if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 1) { // ACTIVATE_CANNONS = 1
            quitButton.setOnAction(event -> {
                selectedBatties.clear();
                updateBatteriesView(game);
                objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
                selectedCannons.clear();
                updateCannonView(game);
                objectsInfo.appendText("\n\nSelected Cannons:\n\nNone selected.");
            });
        } else if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 2) { // REMOVE_CREW = 2
            quitButton.setOnAction(event -> {
                selectedCrew.clear();
                updateCrewView(game);
                updateCrewInfoText();
            });
        }

        loadCurrentCard(game);

        if (selectedBatties.isEmpty()) {
            objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
            objectsInfo.appendText("\n\nSelected Cannons:\n\nNone selected.");
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

            if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 1) {
                if (coords.isIn(ship.getTilesMap().get("BatteryTile"))
                        && (tile.getNumBatteries() - selectedBatties.getOrDefault(coords, 0)) > 0
                        && state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 1) {  // ACTIVATE_CANNONS = 1
                    enableBatteryTileInteraction((BatteryTile) tile, stack, row, col);
                }
                if (coords.isIn(ship.getTilesMap().get("LaserTile")) && tile.isDoubleLaser()
                        && state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 1) {
                    enableCannonTileInteraction((LaserTile) tile, stack, row, col);
                }

                solveButton.setOnAction(event -> {
                    List<Coordinates> batteriesCoords = new ArrayList<>();
                    List<Coordinates> cannonCoords = new ArrayList<>();

                    if (!selectedBatties.isEmpty()) {
                        for (Map.Entry<Coordinates, Integer> entry : selectedBatties.entrySet()) {
                            Coordinates coord = entry.getKey();
                            int count = entry.getValue();
                            for (int i = 0; i < count; i++) {
                                batteriesCoords.add(coord);
                            }
                        }
                        cannonCoords = selectedCannons;
                    }
                    List<Coordinates> batteriesCopy = new ArrayList<>(batteriesCoords);
                    List<Coordinates> cannonsCopy = new ArrayList<>(cannonCoords);
                    gui.compareFirePower(batteriesCopy, cannonsCopy);
                    selectedBatties.clear();
                    selectedCannons.clear();
                });
            } else if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 2) { // REMOVE_CREW = 2;
                if (coords.isIn(ship.getTilesMap().get("HousingTile")) && (tile.getNumCrew() - selectedCrew.getOrDefault(coords, 0)) > 0) {
                    resetTileInteractions();
                    updateCrewView(game);
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
        }
    }

    @Override
    public void updateSmugglersController(Game game) {
        Player p = game.getPlayer(gui.getClientNickname());
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();
        int level = p.getGame().getLevel();
        SmugglersState state = (SmugglersState) game.getGameState();
        AdventureCard card = game.getCurrentAdventureCard();


        showPane(cardButtonsPane);
        deck.setOnMouseEntered(null);
        deck.setOnMouseExited(null);
        deck.setOnMouseClicked(null);
        deck.setStyle(null);

        choiceButton.setVisible(false);
        choiceButton.setManaged(false);
        diceButton.setVisible(false);
        diceButton.setManaged(false);

        loadCurrentCard(game);

        if (state.getPlayed().get(game.getSortedPlayers().indexOf(p)) == 1) { // HANDLE_BOXES == 1
            setupBoxesGrid(game, card.getBoxes());
            enableAllStorageTileInteractions(game);
            choiceButton.setVisible(true);
            choiceButton.setManaged(true);
            choiceButton.setText("Reject");
            choiceButton.setOnAction(event -> {
                gui.handleBoxes(null, null);
            });
            solveButton.setOnAction(event -> {
                gui.handleBoxes(selectedStorage, boxesMaps);
            });
        }

        quitButton.setVisible(true);
        quitButton.setManaged(true);
        quitButton.setText("Clear Selected");
        if (state.getPlayed().get(game.getSortedPlayers().indexOf(p)) == 0) { // ACTIVATE_CANNONS = 0
            quitButton.setOnAction(event -> {
                selectedBatties.clear();
                updateBatteriesView(game);
                objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
                selectedCannons.clear();
                updateCannonView(game);
                objectsInfo.appendText("\n\nSelected Cannons:\n\nNone selected.");
            });

            solveButton.setOnAction(event -> {
                List<Coordinates> batteriesCoords = new ArrayList<>();
                List<Coordinates> cannonCoords = new ArrayList<>();

                if (!selectedBatties.isEmpty()) {
                    for (Map.Entry<Coordinates, Integer> entry : selectedBatties.entrySet()) {
                        Coordinates coord = entry.getKey();
                        int count = entry.getValue();
                        for (int i = 0; i < count; i++) {
                            batteriesCoords.add(coord);
                        }
                    }
                    cannonCoords = selectedCannons;
                }

                List<Coordinates> batteriesCopy = new ArrayList<>(batteriesCoords);
                List<Coordinates> cannonsCopy = new ArrayList<>(cannonCoords);

                gui.compareFirePower(batteriesCopy, cannonsCopy);
                selectedBatties.clear();
                selectedCannons.clear();
            });
        }

        if (selectedBatties.isEmpty()) {
            objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
            objectsInfo.appendText("\n\nSelected Cannons:\n\nNone selected.");
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

            if (state.getPlayed().get(game.getSortedPlayers().indexOf(p)) == 0) {
                if (coords.isIn(ship.getTilesMap().get("BatteryTile"))
                        && (tile.getNumBatteries() - selectedBatties.getOrDefault(coords, 0)) > 0) {  // ACTIVATE_CANNONS = 1
                    enableBatteryTileInteraction((BatteryTile) tile, stack, row, col);
                }
                if (coords.isIn(ship.getTilesMap().get("LaserTile")) && tile.isDoubleLaser()) {
                    enableCannonTileInteraction((LaserTile) tile, stack, row, col);
                }
            }
        }
    }

    @Override
    public void updateMeteorsRainController(Game game) {
        showPane(cardButtonsPane);
        deck.setOnMouseEntered(null);
        deck.setOnMouseExited(null);
        deck.setOnMouseClicked(null);
        deck.setStyle(null);
        diceButton.setVisible(true);
        diceButton.setManaged(true);
        diceButton.setOnAction(event -> {
            gui.rollDice();
        });

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
            MeteorsRainState state = (MeteorsRainState) game.getGameState();


            if (coords.isIn(ship.getTilesMap().get("BatteryTile"))
                    && (tile.getNumBatteries() - selectedBatties.getOrDefault(coords, 0)) > 0
                    && state.getPlayed().get(game.getSortedPlayers().indexOf(p)) == 1) {  // PROVIDE_BATTERY = 1
                System.out.println("Enabling batteries interaction.");
                enableBatteryTileInteraction((BatteryTile) tile, stack, row, col);
            }

            if (!ship.isShipLegal()) {
                setFixEffects(cell, row, col);
                hidePane(cardButtonsPane);
                quitButton.setText("Fix Ship");
                quitButton.setVisible(true);
                quitButton.setManaged(true);
                quitButton.setOnAction(event -> {
                    gui.fixShip(tilesToBreak);
                });
            }

            solveButton.setOnAction(event -> {
                if (selectedBatties.isEmpty()) {
                    gui.chooseBattery(-1, -1);
                } else {
                    Coordinates c = selectedBatties.entrySet().iterator().next().getKey();
                    gui.chooseBattery(c.getX(), c.getY());
                }
                selectedBatties.clear();
            });

            if(state.isRolled()) {
                diceResult.setText("Dice: " + state.getDiceResult());
            }

        }
        choiceButton.setText("Clear selected");
        choiceButton.setOnAction(event -> {
            selectedBatties.clear();
            updateBatteriesView(game);
            objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
        });
    }

    @Override
    public void updatePiratesController(Game game) {
        Player p = game.getPlayer(gui.getClientNickname());
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();
        int level = p.getGame().getLevel();
        PiratesState state = (PiratesState) game.getGameState();

        showPane(cardButtonsPane);
        deck.setOnMouseEntered(null);
        deck.setOnMouseExited(null);
        deck.setOnMouseClicked(null);
        deck.setStyle(null);

        choiceButton.setVisible(false);
        choiceButton.setManaged(false);
        diceButton.setVisible(false);
        diceButton.setManaged(false);

        // Decide reward
        if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 2) { // DECIDE_REWARD = 2
            choiceButton.setVisible(true);
            choiceButton.setManaged(true);
            choiceButton.setText("Reject");
            choiceButton.setOnAction(event -> {
                gui.getRewards(false);
            });

            diceButton.setVisible(true);
            diceButton.setManaged(true);
            diceButton.setText("Accept");
            diceButton.setOnAction(event -> {
                gui.getRewards(true);
            });
        }


        // Compare firepower
        quitButton.setVisible(true);
        quitButton.setManaged(true);
        quitButton.setText("Clear Selected");
        if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 1) { // ACTIVATE_CANNONS = 1
            quitButton.setOnAction(event -> {
                selectedBatties.clear();
                updateBatteriesView(game);
                objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
                selectedCannons.clear();
                updateCannonView(game);
                objectsInfo.appendText("\n\nSelected Cannons:\n\nNone selected.");
            });
        } else if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 2) { // REMOVE_CREW = 2
            quitButton.setOnAction(event -> {
                selectedCrew.clear();
                updateCrewView(game);
                updateCrewInfoText();
            });
        }

        loadCurrentCard(game);

        if (selectedBatties.isEmpty()) {
            objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
            objectsInfo.appendText("\n\nSelected Cannons:\n\nNone selected.");
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

            if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 1) { // ACTIVATE_CANNONS = 1
                if (coords.isIn(ship.getTilesMap().get("BatteryTile"))
                        && (tile.getNumBatteries() - selectedBatties.getOrDefault(coords, 0)) > 0) {
                    enableBatteryTileInteraction((BatteryTile) tile, stack, row, col);
                }
                if (coords.isIn(ship.getTilesMap().get("LaserTile")) && tile.isDoubleLaser()
                        && state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 1) {
                    enableCannonTileInteraction((LaserTile) tile, stack, row, col);
                }

                solveButton.setOnAction(event -> {
                    List<Coordinates> batteriesCoords = new ArrayList<>();
                    List<Coordinates> cannonCoords = new ArrayList<>();

                    if (!selectedBatties.isEmpty()) {
                        for (Map.Entry<Coordinates, Integer> entry : selectedBatties.entrySet()) {
                            Coordinates coord = entry.getKey();
                            int count = entry.getValue();
                            for (int i = 0; i < count; i++) {
                                batteriesCoords.add(coord);
                            }
                        }
                        cannonCoords = selectedCannons;
                    }
                    List<Coordinates> batteriesCopy = new ArrayList<>(batteriesCoords);
                    List<Coordinates> cannonsCopy = new ArrayList<>(cannonCoords);
                    gui.compareFirePower(batteriesCopy, cannonsCopy);
                    selectedBatties.clear();
                    selectedCannons.clear();
                });
            }
        }

        if (!state.isRolled() && state.isFirstWaitingForShot(p) && !state.getPlayerStates().contains(1)) {
            diceButton.setText("Roll Dice");
            diceButton.setVisible(true);
            diceButton.setManaged(true);
            diceButton.setOnAction(event -> {
                gui.rollDice();
            });

            if (selectedBatties.isEmpty()) {
                objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
            }
        }

        if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) >= 4) {
            if (state.isRolled()) {
                diceResult.setText("Dice: " + state.getDiceResult());
            }
        }


        if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 4) {
            if(state.isRolled()) {
                diceResult.setText("Dice: " + state.getDiceResult());
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

                if (coords.isIn(ship.getTilesMap().get("BatteryTile"))
                        && (tile.getNumBatteries() - selectedBatties.getOrDefault(coords, 0)) > 0) {
                    System.out.println("Enabling batteries interaction.");
                    enableBatteryTileInteraction((BatteryTile) tile, stack, row, col);
                }

                solveButton.setOnAction(event -> {
                    if (selectedBatties.isEmpty()) {
                        gui.chooseBattery(-1, -1);
                    } else {
                        Coordinates c = selectedBatties.entrySet().iterator().next().getKey();
                        gui.chooseBattery(c.getX(), c.getY());
                    }
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

        if (state.getPlayerStates().get(game.getSortedPlayers().indexOf(p)) == 5) {
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

                if (!ship.isShipLegal()) {
                    setFixEffects(cell, row, col);
                    hidePane(cardButtonsPane);
                    quitButton.setText("Fix Ship");
                    quitButton.setVisible(true);
                    quitButton.setManaged(true);
                    quitButton.setOnAction(event -> {
                        gui.fixShip(tilesToBreak);
                    });
                }
            }

            choiceButton.setText("Clear selected");
            choiceButton.setOnAction(event -> {
                tilesToBreak.clear();
                objectsInfo.setText("");
            });
        }
    }


    @Override
    public void updateWarZoneController(Game game) {
        showPane(cardButtonsPane);
        deck.setOnMouseEntered(null);
        deck.setOnMouseExited(null);
        deck.setOnMouseClicked(null);
        deck.setStyle(null);

        loadCurrentCard(game);

        WarZoneState state = (WarZoneState) game.getGameState();

        Player p = game.getPlayer(gui.getClientNickname());
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();
        int level = p.getGame().getLevel();

//        "CANNONS",
//                "PROPULSORS",
//                "CREW"

        switch (state.getCard().getPenaltyType().get(state.getPenaltyIdx())) {
            case "HANDLESHOTS":
                if (state.getWorstPlayerIdx() == game.getSortedPlayers().indexOf(p) && state.getWorstPlayerState() == 0) {
                    diceButton.setText("Roll Dice");
                    diceButton.setVisible(true);
                    diceButton.setManaged(true);
                    diceButton.setOnAction(event -> {
                        gui.rollDice();
                    });
                }
                else if (state.getWorstPlayerIdx() == game.getSortedPlayers().indexOf(p) && state.getWorstPlayerState() == 1) {
                    diceButton.setVisible(false);
                    diceButton.setManaged(false);
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

                        if (coords.isIn(ship.getTilesMap().get("BatteryTile"))
                                && (tile.getNumBatteries() - selectedBatties.getOrDefault(coords, 0)) > 0) {
                            System.out.println("Enabling batteries interaction.");
                            enableBatteryTileInteraction((BatteryTile) tile, stack, row, col);
                        }

                        solveButton.setOnAction(event -> {
                            if (selectedBatties.isEmpty()) {
                                gui.chooseBattery(-1, -1);
                            } else {
                                Coordinates c = selectedBatties.entrySet().iterator().next().getKey();
                                gui.chooseBattery(c.getX(), c.getY());
                            }
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
                else if (state.getWorstPlayerIdx() == 0 && state.getWorstPlayerState() == 2){
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

                        if (!ship.isShipLegal()) {
                            setFixEffects(cell, row, col);
                            hidePane(cardButtonsPane);
                            quitButton.setText("Fix Ship");
                            quitButton.setVisible(true);
                            quitButton.setManaged(true);
                            quitButton.setOnAction(event -> {
                                gui.fixShip(tilesToBreak);
                            });
                        }
                    }

                    choiceButton.setText("Clear selected");
                    choiceButton.setOnAction(event -> {
                        tilesToBreak.clear();
                        objectsInfo.setText("");
                    });
                }
                break;
            case "LOSECREW":
                if (state.isAllDone(state.getPlayed()) && state.getWorstPlayerIdx() == game.getSortedPlayers().indexOf(p)) {
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
                }
                break;
        }

        switch (state.getCard().getParameterCheck().get(state.getPenaltyIdx())) {
            case "CANNONS":
                if (state.getCurrPlayerIdx() == game.getSortedPlayers().indexOf(p)) {
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
                        } else if ((ship.getTilesMap().get("LaserTile").contains(coords) && tile.isDoubleLaser())) {
                            System.out.println("trovato un double cannon tile in coordinate " + coords);
                            enableCannonTileInteraction((LaserTile) tile, stack, row, col);
                        }

                        solveButton.setOnAction(event -> {
                            List<Coordinates> batteriesCoords = new ArrayList<>();
                            List<Coordinates> cannonCoords = new ArrayList<>();

                            if (!selectedBatties.isEmpty()) {
                                for (Map.Entry<Coordinates, Integer> entry : selectedBatties.entrySet()) {
                                    Coordinates coord = entry.getKey();
                                    int count = entry.getValue();
                                    for (int i = 0; i < count; i++) {
                                        batteriesCoords.add(coord);
                                    }
                                }
                                cannonCoords = selectedCannons;
                            }
                            List<Coordinates> batteriesCopy = new ArrayList<>(batteriesCoords);
                            List<Coordinates> cannonsCopy = new ArrayList<>(cannonCoords);
                            gui.compareFirePower(batteriesCopy, cannonsCopy);
                            selectedBatties.clear();
                            selectedCannons.clear();
                        });
                    }
                    quitButton.setText("Clear selected");
                    quitButton.setOnAction(event -> {
                        selectedBatties.clear();
                        updateBatteriesView(game);
                        objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected.");
                        selectedCannons.clear();
                        updateCannonView(game);
                        objectsInfo.appendText("\n\nSelected Cannons:\n\nNone selected.");
                    });
                }
                break;
            case "PROPULSORS":
                if (state.getCurrPlayerIdx() == game.getSortedPlayers().indexOf(p)) {
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
                break;
            case "CREW":
                solveButton.setOnAction(event -> {
                    gui.compareCrew();
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

    public void updateBatteriesView(Game game) {
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

    public void updateCrewView(Game game) {
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

    public void updateCannonView(Game game) {
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

            if (ship.getTilesMap().get("LaserTile").contains(coords) && tile.isDoubleLaser()) {
                System.out.println("Found D. Cannon at " + coords.getX() + " " + coords.getY());
                StackPane stack = (StackPane) n;
                enableCannonTileInteraction((LaserTile) tile, stack, row, col);
            }
        }
    }


    private void updateShip(Player p) {
        Ship ship = p.getShip();
        Tile[][] shipMatrix = ship.getTilesMatrix();

        int level = p.getGame().getLevel();
        storageImages.clear();

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
                stack.getChildren().remove(1, stack.getChildren().size());
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

        if (cellStack == null) return;

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

        if (currentCrew == 0) return;

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
            } else if (housingTile.getHostedCrewType() == CrewType.BROWN_ALIEN) {
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

        for (int i = currentBatteries; i < maxBatteries; i++) {
            ImageView batteryImage = new ImageView();
            batteryImage.setFitWidth(8);
            batteryImage.setFitHeight(15);
            batteryImage.setPreserveRatio(true);
            batteryImage.setImage(null);
            batteryBox.getChildren().add(batteryImage);
        }

        batteryBox.setRotate(batteryTile.getRotation() * 90);
        batteryBox.setMouseTransparent(true);
        cellStack.getChildren().add(batteryBox);
    }

    private void enableBatteryTileInteraction(BatteryTile tile, StackPane stack, int row, int col) {
        Coordinates coords = new Coordinates(row, col);
        int remaining = tile.getNumBatteries() - selectedBatties.getOrDefault(coords, 0);

        ImageView cell = (ImageView) stack.getChildren().getFirst();

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

        ImageView cell = (ImageView) stack.getChildren().getFirst();

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

    private void enableCannonTileInteraction(LaserTile tile, StackPane stack, int row, int col) {
        Coordinates coords = new Coordinates(row, col);

        ImageView cell = (ImageView) stack.getChildren().getFirst();

        cell.setOnMouseEntered(e -> {
            cell.setStyle("-fx-effect: dropshadow(gaussian, red, 10, 0.6, 0, 0);");
        });

        cell.setOnMouseExited(e -> cell.setStyle(""));

        cell.setOnMouseClicked(e -> {
            if (!coords.isIn(selectedCannons)) {
                cell.setStyle("-fx-effect: dropshadow(gaussian, red, 10, 0.6, 0, 0);");
                selectedCannons.add(coords);
            } else {
                cell.setStyle("");
                selectedCannons.remove(coords);
            }
            enableCannonTileInteraction(tile, stack, row, col);
            updateCannonsInfoText();
        });
    }

    private void updateBatteriesInfoText() {
        if (selectedBatties.isEmpty()) {
            objectsInfo.setText("🔋 Selected Batteries:\n\nNone selected\n\n.");
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

    private void updateCannonsInfoText() {
        if (selectedCannons.isEmpty()) {
            objectsInfo.appendText("Selected Cannons:\n\nNone selected.");
            return;
        }

        StringBuilder sb = new StringBuilder("Selected Cannons:\n\n");
        selectedCannons.forEach((coord) ->
                sb.append(String.format("• (%d, %d)\n", coord.getX(), coord.getY()))
        );

        objectsInfo.appendText(sb.toString());
    }


    private void updateStorageTile(StorageTile storageTile, StackPane cellStack, int row, int col) {
        if (cellStack.getChildren().size() > 1) {
            cellStack.getChildren().subList(1, cellStack.getChildren().size()).clear();
        }

        int maxStorage = storageTile.getMaxBoxes();
        Map<BoxType, Integer> boxes = boxesMaps.get(selectedStorage.indexOf(new Coordinates(row, col)));
        int usedStorage = 0;
        for (BoxType boxType : boxes.keySet()) {
            usedStorage += boxes.get(boxType);
        }

        Node storageContainer;

        List<String> colorList = new ArrayList<>();

        switch (maxStorage) {
            case 1:

                ImageView singleStorage = new ImageView();
                singleStorage.getProperties().put("coords", new Coordinates(row, col));
                singleStorage.setFitWidth(13);
                singleStorage.setFitHeight(13);
                singleStorage.setPreserveRatio(true);
                singleStorage.setPickOnBounds(true);
                System.out.println("adding storage image");
                storageImages.add(singleStorage);

                if (usedStorage == 1) {

                    String color = "";
                    for (BoxType boxType : boxes.keySet()) {
                        if (boxes.get(boxType) == 1) {
                            color = boxType.toString().toLowerCase();
                        }
                    }

                    String boxImage = "/images/objects/" + color + ".png";
                    try {
                        Image itemImg = new Image(Objects.requireNonNull(getClass().getResource(boxImage)).toExternalForm());
                        singleStorage.setImage(itemImg);
                        singleStorage.setOpacity(1.0);
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

                VBox doubleStorage = new VBox(3);
                doubleStorage.setAlignment(Pos.CENTER);

                for (BoxType boxType : boxes.keySet()) {
                    if (boxes.get(boxType) > 0) {
                        for (int i = 0; i < boxes.get(boxType); i++) {
                            colorList.add(boxType.toString().toLowerCase());
                        }
                    }
                }

                for (int i = 0; i < usedStorage; i++) {
                    ImageView storageImage = new ImageView();
                    storageImage.getProperties().put("coords", new Coordinates(row, col));
                    storageImage.setFitWidth(13);
                    storageImage.setFitHeight(13);
                    storageImage.setPreserveRatio(true);
                    storageImage.setPickOnBounds(true);
                    System.out.println("adding storage image");
                    storageImages.add(storageImage);


                    String boxImage = "/images/objects/" + colorList.get(i) + ".png";

                    try {
                        Image itemImg = new Image(Objects.requireNonNull(getClass().getResource(boxImage)).toExternalForm());
                        storageImage.setImage(itemImg);
                        storageImage.setOpacity(1.0);
                    } catch (Exception e) {
                        System.err.println("Immagine non trovata: " + boxImage);
                        e.printStackTrace();
                    }
                    doubleStorage.getChildren().add(storageImage);
                }

                for (int i = usedStorage; i < maxStorage; i++) {
                    ImageView storageImage = new ImageView();
                    storageImage.getProperties().put("coords", new Coordinates(row, col));
                    storageImage.setFitWidth(13);
                    storageImage.setFitHeight(13);
                    storageImage.setPickOnBounds(true);
                    System.out.println("adding storage image");
                    storageImages.add(storageImage);
                    storageImage.setPreserveRatio(true);
                    storageImage.setImage(null);
                    doubleStorage.getChildren().add(storageImage);
                }

                storageContainer = doubleStorage;

                colorList.clear();

                break;

            default:

                HBox triangleLayout = new HBox(3);
                triangleLayout.setAlignment(Pos.CENTER);

                VBox topRow = new VBox(4);
                topRow.setAlignment(Pos.CENTER);


                for (BoxType boxType : boxes.keySet()) {
                    if (boxes.get(boxType) > 0) {
                        for (int i = 0; i < boxes.get(boxType); i++) {
                            colorList.add(boxType.toString().toLowerCase());
                        }
                    }
                }

                for (int i = 0; i < 2; i++) {
                    ImageView storageImage = new ImageView();
                    storageImage.getProperties().put("coords", new Coordinates(row, col));
                    storageImage.setFitWidth(13);
                    storageImage.setFitHeight(13);
                    storageImage.setPreserveRatio(true);
                    storageImage.setPickOnBounds(true);
                    System.out.println("adding storage image");
                    storageImages.add(storageImage);

                    if (i < usedStorage) {
                        String boxImage = "/images/objects/" + colorList.get(i) + ".png";
                        try {
                            Image itemImg = new Image(Objects.requireNonNull(getClass().getResource(boxImage)).toExternalForm());
                            storageImage.setImage(itemImg);
                            storageImage.setOpacity(1.0);
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
                bottomStorage.getProperties().put("coords", new Coordinates(row, col));
                bottomStorage.setFitWidth(13);
                bottomStorage.setFitHeight(13);
                bottomStorage.setPreserveRatio(true);
                bottomStorage.setPickOnBounds(true);
                System.out.println("adding storage image");
                storageImages.add(bottomStorage);

                if (usedStorage > 2) {
                    String boxImage = "/images/objects/" + colorList.get(2) + ".png";
                    try {
                        Image itemImg = new Image(Objects.requireNonNull(getClass().getResource(boxImage)).toExternalForm());
                        bottomStorage.setImage(itemImg);
                        bottomStorage.setOpacity(1.0);
                    } catch (Exception e) {
                        System.err.println("Immagine non trovata: " + boxImage);
                        e.printStackTrace();
                    }
                } else {
                    bottomStorage.setImage(null);
                }
                triangleLayout.setPickOnBounds(true);
                triangleLayout.setMinSize(20, 30);
                triangleLayout.getChildren().addAll(topRow, bottomStorage);
                storageContainer = triangleLayout;

                colorList.clear();

                break;
        }

        storageContainer.setRotate(storageTile.getRotation() * 90 + 180);

        cellStack.getChildren().add(storageContainer);
    }

    private void setupBoxesGrid(Game game, Map<BoxType, Integer> rewards) {
        AdventureCard card = game.getCurrentAdventureCard();
        remainingBoxes.clear();
        boxesGrid.setVisible(true);
        boxesGrid.setManaged(true);

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
                            draggedImageView = imageView;
                            Dragboard db = draggedImageView.startDragAndDrop(TransferMode.MOVE);
                            Image original = draggedImageView.getImage();
                            ImageView scaledView = new ImageView(original);
                            scaledView.setFitWidth(15);
                            scaledView.setFitHeight(15);
                            scaledView.setPreserveRatio(true);

                            SnapshotParameters params = new SnapshotParameters();
                            params.setFill(javafx.scene.paint.Color.TRANSPARENT);
                            Image scaledDragImage = scaledView.snapshot(params, null);

                            db.setDragView(scaledDragImage, scaledDragImage.getWidth() / 2, scaledDragImage.getHeight() / 2);


                            ClipboardContent content = new ClipboardContent();
                            String payload = "boxType:" + type.name() + ":source:grid";
                            content.putString(payload);
                            db.setContent(content);
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
                if (selectedCrew.getOrDefault(coordinates, 0) == 0) {
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
            if (!coordinates.isIn(tilesToBreak)) {
                cell.setStyle("-fx-effect: dropshadow(gaussian, lawngreen, 10, 0.6, 0, 0);");
                tilesToBreak.add(coordinates);
            } else {
                cell.setStyle("");
                tilesToBreak.remove(coordinates);
            }
        });
    }

    public void enableStorageTileInteraction(ImageView targetImage, int row, int col, Game game) {
        Coordinates targetCoords = new Coordinates(row, col);
        Ship ship = game.getPlayer(gui.getClientNickname()).getShip();
        targetImage.setMouseTransparent(false);
        Tile tile = ship.getTile(row, col);
        if (tile.getMaxBoxes() > 1) targetImage.getParent().setMouseTransparent(false);
        targetImage.toFront();
        System.out.println("enabling storage tile interaction " + targetCoords);

        targetImage.setOnDragDetected(e -> {
            draggedImageView = targetImage;

            BoxType box = getBoxTypeFromImage(targetImage.getImage());
            if (box == null) return;

            Dragboard db = targetImage.startDragAndDrop(TransferMode.MOVE);
            Image original = draggedImageView.getImage();
            ImageView scaledView = new ImageView(original);
            scaledView.setFitWidth(15);
            scaledView.setFitHeight(15);
            scaledView.setPreserveRatio(true);

            SnapshotParameters params = new SnapshotParameters();
            params.setFill(javafx.scene.paint.Color.TRANSPARENT);
            Image scaledDragImage = scaledView.snapshot(params, null);

            db.setDragView(scaledDragImage, scaledDragImage.getWidth() / 2, scaledDragImage.getHeight() / 2);
            String payload = "boxType:" + box.name() +
                    ":source:tile" +
                    ":originRow:" + row +
                    ":originCol:" + col;

            ClipboardContent content = new ClipboardContent();
            content.putString(payload);
            db.setContent(content);
            e.consume();
        });

        targetImage.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        targetImage.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (!db.hasString()) return;

            try {
                String[] parts = db.getString().split(":");
                BoxType draggedBox = BoxType.valueOf(parts[1]);
                if (draggedBox == BoxType.RED && !ship.getTile(targetCoords.getX(), targetCoords.getY()).isSpecialStorageTile())
                    return;
                boolean fromTile = "tile".equals(parts[3]);
                boolean fromGrid = "grid".equals(parts[3]);

                Coordinates originCoords = null;
                if (fromTile) {
                    int originRow = Integer.parseInt(parts[5]);
                    int originCol = Integer.parseInt(parts[7]);
                    originCoords = new Coordinates(originRow, originCol);
                }

                int targetIndex = selectedStorage.indexOf(targetCoords);
                if (targetIndex == -1) {
                    selectedStorage.add(targetCoords);
                    boxesMaps.add(new EnumMap<>(BoxType.class));
                    targetIndex = boxesMaps.size() - 1;
                }
                Map<BoxType, Integer> targetMap = boxesMaps.get(targetIndex);

                BoxType currentBox = getBoxTypeFromImage(targetImage.getImage());
                if (currentBox != null) {
                    targetMap.put(currentBox, targetMap.getOrDefault(currentBox, 1) - 1);

                    if (fromGrid) {
                        remainingBoxes.put(currentBox, remainingBoxes.getOrDefault(currentBox, 0) + 1);
                    } else if (fromTile && originCoords != null) {
                        int originIndex = selectedStorage.indexOf(originCoords);
                        if (originIndex == -1) {
                            selectedStorage.add(originCoords);
                            boxesMaps.add(new EnumMap<>(BoxType.class));
                            originIndex = boxesMaps.size() - 1;
                        }
                        Map<BoxType, Integer> originMap = boxesMaps.get(originIndex);
                        originMap.put(currentBox, originMap.getOrDefault(currentBox, 0) + 1);
                    }
                }

                if (fromTile && originCoords != null) {
                    int originIndex = selectedStorage.indexOf(originCoords);
                    if (originIndex != -1) {
                        Map<BoxType, Integer> originMap = boxesMaps.get(originIndex);
                        originMap.put(draggedBox, originMap.getOrDefault(draggedBox, 0) - 1);
                    }
                } else if (fromGrid) {
                    remainingBoxes.put(draggedBox, remainingBoxes.getOrDefault(draggedBox, 0) - 1);
                }

                targetMap.put(draggedBox, targetMap.getOrDefault(draggedBox, 0) + 1);

                updateBoxesGrid();
                updateShip(game.getPlayer(gui.getClientNickname()));
                enableAllStorageTileInteractions(game);
                updateBoxesGrid();
                event.setDropCompleted(true);
                System.out.println(selectedStorage);
                System.out.println(boxesMaps);

            } catch (Exception ex) {
                ex.printStackTrace();
                event.setDropCompleted(false);
            }

            event.consume();
        });
    }

    private void enableAllStorageTileInteractions(Game game) {
        Player p = game.getPlayer(gui.getClientNickname());
        Ship ship = p.getShip();
        System.out.println("size:  " + storageImages.size());
        for (ImageView storageImage : storageImages) {

            Coordinates coords = (Coordinates) storageImage.getProperties().get("coords");
            System.out.println("found storage: " + coords);
            enableStorageTileInteraction(storageImage, coords.getX(), coords.getY(), game);
        }
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
            if (!coordinates.isIn(tilesToBreak)) {
                cell.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.6, 0, 0);");
                tilesToBreak.add(coordinates);
            } else {
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
        Map<Integer, Polygon> currentMap = new HashMap<>();
        currentMap = game.getLevel() == 1 ? level1Triangles : level2Triangles;
        for (Polygon triangle : currentMap.values()) {
            triangle.setStyle("-fx-fill: transparent; -fx-stroke: transparent;");
        }

        for (Player player : game.getPlayers()) {
            int position = player.getPosition();
            PlayerColor color = player.getColor();
            if (position < 0) {
                position = currentMap.size() + position;
            }
            Polygon triangle = currentMap.get(position);
            System.out.println("Position: " + position + " Color: " + color + " Triangle: ");
            if (triangle != null && color != null) {
                triangle.setStyle("-fx-fill: " + playerColorHex.get(color.toString()) + "; -fx-stroke: black; -fx-stroke-width: 1;");
            }
        }
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

    private void loadCurrentCard(Game game) {
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

    private BoxType getBoxTypeFromImage(Image image) {
        if (image == null || image.getUrl() == null) return null;

        for (BoxType type : BoxType.values()) {
            if (image.getUrl().contains(type.name().toLowerCase())) {
                return type;
            }
        }
        return null;
    }

    public void showPlanetsButtons(Game game) {
        AdventureCard card = game.getCurrentAdventureCard();
        PlanetsState state = (PlanetsState) game.getGameState();
        for (Node node : planetsGrid.getChildren()) {
            Button button = (Button) node;
            if (GridPane.getColumnIndex(button) < card.getPlanetReward().size() && !state.getChosenPlanets().containsValue(GridPane.getColumnIndex(button))) {
                button.setVisible(true);
                button.setManaged(true);
                button.setOnAction(event -> {
                    chosenPlanetIdx = GridPane.getColumnIndex(button);
                    setupBoxesGrid(game, card.getPlanetReward().get(chosenPlanetIdx));
                    enableAllStorageTileInteractions(game);
                    solveButton.setVisible(true);
                    solveButton.setManaged(true);
                });
            } else {
                button.setVisible(false);
                button.setManaged(false);
            }
        }
    }

    public void hidePlanetsButtons() {
        for (Node node : planetsGrid.getChildren()) {
            Button button = (Button) node;
            if (GridPane.getColumnIndex(button) < 4) {
                button.setVisible(false);
                button.setManaged(false);
            }
        }
    }

    public void makeBoxesContainersTransparent(Game game) {
        Ship ship = game.getPlayer(gui.getClientNickname()).getShip();
        for (ImageView storage : storageImages) {
            Coordinates c = (Coordinates) storage.getProperties().get("coords");
            Tile tile = ship.getTile(c.getX(), c.getY());
            storage.setMouseTransparent(true);
            if (tile.getMaxBoxes() > 1) storage.getParent().setMouseTransparent(true);
        }
    }


}
