package it.polimi.ingsw.cg04.client.view.gui;

import it.polimi.ingsw.cg04.client.model.ClientModel;
import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.controllers.*;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.BuildState;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GUIRoot extends View {

    GUIMain guiMain;

    private final Thread guiThread;

    ViewController currController;

    /**
     * Constructor
     *
     * @param clientModel the client model
     * @param server      the server
     */
    public GUIRoot(ClientModel clientModel, ServerHandler server) {
        super(server, clientModel);
        guiThread = new Thread(() -> GUIMain.launchApp(this));
        guiThread.start();
    }

    @Override
    public void serverUnreachable() {
        try {
            currController.goToErrorScene(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(10),
                event -> guiMain.stop()
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * GuiMain setter method
     *
     * @param guiMain the object to set
     */
    public void setGuiMain(GUIMain guiMain) {
        this.guiMain = guiMain;
    }

    public String getClientNickname() {
        return nickname;
    }


    @Override
    public void renderLobbyState(Game toDisplay) throws IOException {
        currController.goToLobbyScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderBuildState(Game toDisplay) throws IOException {
        BuildPlayerState playerState = ((BuildState) toDisplay.getGameState()).getPlayerState().get(nickname);
        System.out.println("Rendering build state: " + playerState);

        if (playerState == BuildPlayerState.SHOWING_FACE_UP) {
            currController.goToFaceUpScene(this);
            Platform.runLater(() -> currController.update(toDisplay));
        } else {
            System.out.println("now in default");
            System.out.println(this.getClientNickname());
            currController.goToBuildScene(this);
            Platform.runLater(() -> currController.update(toDisplay));
        }
    }

    @Override
    public void renderLoadCrewState(Game toDisplay) throws IOException {
        currController.goToBuildScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderFlightState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderAbandonedShipState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderAbandonedStationState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderEpidemicState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderMeteorsRainState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderOpenSpaceState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    public void renderPiratesState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderPlanetsState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderSlaversState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderSmugglersState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderStardustState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderWarZoneState(Game toDisplay) throws IOException {
        currController.goToAdventureCardScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void renderEndGameState(Game toDisplay) throws IOException {
        currController.goToEndScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }

    @Override
    public void updateGame(Game toDisplay, String nickname) {
        try {
            if (toDisplay != null) {
                if (isViewingShips) {

                    currController.goToViewOthersScene(this);
                    Platform.runLater(() -> currController.update(toDisplay));
                } else {
                    toDisplay.getGameState().updateView(this, toDisplay);
                }
            }
        } catch (NullPointerException | IOException e) {
            System.out.println("Game not found");
            e.printStackTrace();
        }
    }

    @Override
    public void updateLogs(List<String> logs) {
        Scene currentScene = this.getScene();

        if (currentScene != null && guiMain != null) {
            currController.showLogs(logs);
        }
    }

    @Override
    public void updateJoinableGames(List<Game.GameInfo> newValue) {
        try {
            currController.goToPrelobbyScene(this);
        } catch (IOException e) {
            System.out.println("Error while updating joinable games");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Platform.runLater(() -> currController.refreshJoinableGames(newValue));
    }

    /**
     * Returns the current JavaFX Scene from the primary stage.
     *
     * @return the active {@link Scene}
     */
    public Scene getScene() {
        return guiMain.getPrimaryStage().getScene();
    }

    /**
     * Changes the current scene on the primary stage.
     *
     * @param scene  the new {@link Scene} to display
     */
    private void changeScene(Scene scene) {

        Platform.runLater(() -> {
            Stage stage = guiMain.getPrimaryStage();
            boolean wasFullScreen = stage.isFullScreen();
            stage.setTitle("Galaxy Trucker");
            stage.setScene(scene);
            stage.setResizable(true);
            // stage.setFullScreen(false);
            stage.show();
            Platform.runLater(() -> {
                if (!stage.isFullScreen()) {
                    stage.setFullScreenExitHint("");
                    stage.setFullScreen(true);
                }
            });
        });
    }

    /**
     * Prepares the given scene with a specified size and associates it with the current controller,
     * then displays it on the primary stage.
     *
     * @param scene   the {@link Scene} to prepare and show
     * @param width   the width to set if not in full-screen mode
     * @param height  the height to set if not in full-screen mode
     */
    private void prepareAndShowScene(Scene scene, int width, int height) {
        Stage stage = guiMain.getPrimaryStage();
        boolean wasFullScreen = stage.isFullScreen();
        if (!wasFullScreen) {
            stage.setWidth(width);
            stage.setHeight(height);
        }
        stage.setResizable(true);

        scene.setUserData(currController);
        guiMain.sceneControllerMap.put(scene, currController);
        changeScene(scene);
    }

    /**
     * Initialize the first Scene where the player chooses the nickname and the number of players.
     */
    public void goToFirstScene() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/LoginScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/LoginScene.css")).toExternalForm());

        prepareAndShowScene(scene, 960, 540);
    }

    /**
     * Initialize the second Scene where the player chooses the game mode and the number of players.
     *
     * @throws IOException in cases of fxml file issues...
     */
    public void goToPrelobbyScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/PrelobbyScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/PrelobbyScene.css")).toExternalForm());

        prepareAndShowScene(scene, 960, 540);
    }

    /**
     * Initialize the Lobby Scene.
     *
     * @throws IOException in cases of fxml file issues...
     */
    public void goToLobbyScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/LobbyScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/LobbyScene.css")).toExternalForm());

        prepareAndShowScene(scene, 960, 540);
    }

    /**
     * Loads and displays the FaceUp scene.
     *
     * @throws IOException if the FXML or CSS resource cannot be loaded
     */
    public void goToFaceUpScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/FaceUpScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/FaceUpScene.css")).toExternalForm());

        prepareAndShowScene(scene, 960, 540);
    }

    /**
     * Loads and displays the Build scene with its controller.
     *
     * @throws IOException if the FXML or CSS resource cannot be loaded
     */
    public void goToBuildScene() throws IOException {
        System.out.println("going to build scene");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/BuildScene.fxml"));
        Parent root = loader.load();

        currController = (BuildSceneController) loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/BuildScene.css")).toExternalForm());

        prepareAndShowScene(scene, 600, 400);
    }

    /**
     * Loads and displays the View Others scene.
     *
     * @throws IOException if the FXML or CSS resource cannot be loaded
     */
    public void goToViewOthersScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/ViewOthersScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/ViewOthersScene.css")).toExternalForm());

        prepareAndShowScene(scene, 600, 400);
    }

    /**
     * Loads and displays the Adventure Card scene.
     *
     * @throws IOException if the FXML or CSS resource cannot be loaded
     */
    public void goToAdventureCardScene() throws IOException {
        System.out.println("going to adventure card scene");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/AdventureCardScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/AdventureCardScene.css")).toExternalForm());

        prepareAndShowScene(scene, 960, 540);
    }

    /**
     * Loads and displays the End scene at the end of the game.
     *
     * @throws IOException if the FXML or CSS resource cannot be loaded
     */
    public void goToEndScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/EndScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/EndScene.css")).toExternalForm());

        Stage stage = guiMain.getPrimaryStage();
        stage.setResizable(true);

        scene.setUserData(currController);
        guiMain.sceneControllerMap.put(scene, currController);

        changeScene(scene);
    }

    /**
     * Loads and displays the Error scene in case of a connection failure.
     *
     * @throws IOException if the FXML or CSS resource cannot be loaded
     */
    public void goToErrorScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/ErrorScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/ErrorScene.css")).toExternalForm());

        Stage stage = guiMain.getPrimaryStage();
        stage.setResizable(true);

        scene.setUserData(currController);
        guiMain.sceneControllerMap.put(scene, currController);

        changeScene(scene);
    }

    // Actions
    /**
     * Sets the player's nickname and informs the server.
     *
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
        server.setNickname(nickname);
    }

    /**
     * Sends a request to the server to create a new game.
     *
     * @param color   the player's chosen color
     * @param level   the game difficulty level
     * @param players the maximum number of players
     */
    public void createGame(String color, int level, int players) {
        server.createGame(level, players, PlayerColor.valueOf(color));
    }

    /**
     * Sends a request to join an existing game.
     *
     * @param gameId the ID of the game to join
     * @param color  the player's chosen color
     */
    public void joinGame(int gameId, String color) {
        server.joinGame(gameId, PlayerColor.valueOf(color));
    }

    /**
     * Chooses a face-up tile by index.
     *
     * @param idx the index of the tile
     */
    public void chooseFaceUp(int idx) {
        server.chooseTile(idx);
    }

    /**
     * Closes the face-up tile selection.
     */
    public void closeFaceUp() {
        server.closeFaceUpTiles();
    }

    /**
     * Draws a tile from the face-down stack.
     */
    public void drawFaceDown() {
        server.drawFaceDown();
    }

    /**
     * Places the selected tile on the ship grid.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void place(int x, int y) {
        server.place(x, y);
    }

    /**
     * Returns the currently held tile.
     */
    public void returnTile() {
        server.returnTile();
    }

    /**
     * Picks a pile by index.
     *
     * @param pileIndex the index of the pile
     */
    public void pickPile(int pileIndex) {
        server.pickPile(pileIndex);
    }

    /**
     * Starts the timer.
     */
    public void startTimer() {
        server.startTimer();
    }

    /**
     * Stops the building phase.
     */
    public void stopBuilding() {
        server.stopBuilding();
    }

    /**
     * Shows the face-up tiles to the player.
     */
    public void showFaceUp() {
        server.showFaceUp();
    }

    /**
     * Returns a tile pile to the common area.
     */
    public void returnPile() {
        server.returnPile();
    }

    /**
     * Rotates the selected tile in a given direction.
     *
     * @param direction the rotation direction
     */
    public void rotateTile(String direction) {
        server.rotate(direction);
    }

    /**
     * Chooses a tile from the buffer by index.
     *
     * @param idx the index of the buffer tile
     */
    public void chooseTileFromBuffer(int idx) {
        server.chooseTileFromBuffer(idx);
    }

    /**
     * Places the current tile into the buffer.
     */
    public void placeTileInBuffer() {
        server.placeInBuffer();
    }

    /**
     * Ends building and sets the starting position.
     *
     * @param pos the position to set
     */
    public void endBuilding(int pos) {
        server.endBuilding(pos);
    }

    /**
     * Loads aliens to the ship.
     *
     * @param pink  the pink alien coordinates
     * @param brown the brown alien coordinates
     */
    public void loadCrew(Coordinates pink, Coordinates brown) {
        server.loadCrew(pink, brown);
    }

    /**
     * Requests the next adventure card from the server.
     */
    public void getNextAdventureCard(){
        server.getNextAdventureCard();
    }

    /**
     * Chooses double propulsors to activate.
     *
     * @param coords         coordinates of selected battery tiles
     * @param usedBatteries  number of batteries to use for each indicated tile
     */
    public void choosePropulsor(List<Coordinates> coords, List<Integer> usedBatteries){
        server.choosePropulsor(coords, usedBatteries);
    }

    /**
     * Removes crew from selected coordinates.
     *
     * @param coords       coordinates where crew are removed
     * @param removedCrew  list of crew types to remove
     */
    public void removeCrew(List<Coordinates> coords, List<Integer> removedCrew){
        server.removeCrew(coords, removedCrew);
    }

    /**
     * Handles box interactions using specified coordinates and values.
     *
     * @param coords     coordinates of storage tiles
     * @param boxesMap   list of box type and value mappings for each indicated storage tile
     */
    public void handleBoxes(List<Coordinates> coords, List<Map<BoxType, Integer>> boxesMap){
        server.handleBoxes(coords, boxesMap);
    }

    /**
     * Lands the ship on a planet.
     *
     * @param planetIdx   the index of the selected planet
     * @param coordinates     coordinates of storage tiles
     * @param boxes   list of box type and value mappings for each indicated storage tile
     */
    public void landToPlanet(Integer planetIdx, List<Coordinates> coordinates, List<Map<BoxType, Integer>> boxes){
        server.landToPlanet(planetIdx, coordinates, boxes);
    }

    /**
     * Triggers the stardust event.
     */
    public void starDust() {
        server.starDust();
    }

    /**
     * Spreads an epidemic event.
     */
    public void epidemic() {
        server.spreadEpidemic();
    }

    /**
     * Rolls the dices.
     */
    public void rollDice() {
        server.rollDice();
    }

    /**
     * Chooses a battery to activate at specific coordinates.
     *
     * @param x x-coordinate of the battery
     * @param y y-coordinate of the battery
     */
    public void chooseBattery(int x, int y) {
        server.chooseBattery(x, y);
    }

    /**
     * Claims the rewards based on the provided input.
     *
     * @param b true if rewards have been accepted, false otherwise
     */
    public void getRewards(Boolean b) {
        server.getRewards(b);
    }

    /**
     * Compares firepower using selected batteries and cannons.
     *
     * @param batteries list of battery coordinates
     * @param cannons   list of double cannon coordinates
     */
    public void compareFirePower(List<Coordinates> batteries, List<Coordinates> cannons) {
        server.compareFirePower(batteries, cannons);
    }

    /**
     * Compares crew count with other players.
     * Can be sent only by the game leader.
     */
    public void compareCrew(){
        server.compareCrew();
    }

    /**
     * Retires the player from the game.
     */
    public void retire(){
        server.retire();
    }

    /**
     * Fixes the ship removing the tiles at the specified coordinates.
     *
     * @param coords coordinates of damaged components to remove
     */
    public void fixShip(List<Coordinates> coords) {
        server.fixShip(coords);
    }

    /**
     * Views other players' ships.
     */
    public void viewOthers() {
        isViewingShips = true;
        updateGame(clientModel.getGame(), nickname);
    }

    /**
     * Returns to the player's own ship view.
     */
    public void home() {
        isViewingShips = false;
        updateGame(clientModel.getGame(), nickname);
    }

}