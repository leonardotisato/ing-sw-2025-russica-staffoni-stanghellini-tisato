package it.polimi.ingsw.cg04.client.view.gui;

import it.polimi.ingsw.cg04.client.model.ClientModel;
import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.controllers.*;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.BuildState;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GUIRoot extends View {

    GUIMain guiMain;

    private Thread guiThread;

    ViewController currController;

    private int columnOffset = 0;

    private boolean isLoggedOut;

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
        isLoggedOut = false;

    }

    @Override
    public void serverUnreachable() {
//        try {
//            goToErrorScene();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        guiMain.stop();
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
        switch (playerState) {
            case SHOWING_FACE_UP:
                currController.goToFaceUpScene(this);
                Platform.runLater(() -> currController.update(toDisplay));
                break;
            default:
                System.out.println("now in default");
                System.out.println(this.getClientNickname());
                currController.goToBuildScene(this);
                Platform.runLater(() -> currController.update(toDisplay));
                break;
        }
    }

    @Override
    public void renderEndGameState(Game toDisplay) throws IOException {
        currController.goToEndScene(this);
        Platform.runLater(() -> currController.update(toDisplay));
    }


    public void updateGame(Game toDisplay, String nickname) {
        try {
            if (toDisplay != null) {
                if(isViewingShips) {

                    currController.goToViewOthersScene(this);
                    Platform.runLater(() -> currController.update(toDisplay));
                } else {
                    toDisplay.getGameState().updateView(this, toDisplay);
                }
            }
        } catch (NullPointerException | IOException ignored) {
            System.out.println("Game not found");
            ignored.printStackTrace();
        }
    }

    public void updateLogs(List<String> logs) {
        Scene currentScene = this.getScene();

        if (currentScene != null && guiMain != null) {
            System.out.println("if");
            ViewController controller = (ViewController)guiMain.getControllerForScene(currentScene);
            controller.showLogs(logs);
        }
    }

    // todo implement: it should refresh the games in preLobby scene
    private void updateJoinableGames(List<Game.GameInfo> newValue) throws IOException {
            currController.goToPrelobbyScene(this);
            Platform.runLater(() -> {
                currController.refreshJoinableGames(newValue);
            });
        }

    private void updateCurrentGame(List<Game.GameInfo> newValue) {

    }

    public Scene getScene() {
        return guiMain.getPrimaryStage().getScene();
    }

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
                    stage.setFullScreen(true);
                }
            });
        });
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

        Stage stage = guiMain.getPrimaryStage();
        stage.setWidth(960);
        stage.setHeight(540);
        stage.setResizable(true);

        scene.setUserData(currController);
        guiMain.sceneControllerMap.put(scene, currController);
        changeScene(scene);
    }

    /**
     * Initialize the second Scene where the player chooses the game mode and the number of players.
     *
     * @throws IOException
     */
    public void goToPrelobbyScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/PrelobbyScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/PrelobbyScene.css")).toExternalForm());


        Stage stage = guiMain.getPrimaryStage();
        boolean wasFullScreen = stage.isFullScreen();
        if (!wasFullScreen) {
            stage.setWidth(960);
            stage.setHeight(540);
        }
        stage.setResizable(true);

        scene.setUserData(currController);
        guiMain.sceneControllerMap.put(scene, currController);
        changeScene(scene);
    }

    /**
     * Initialize the Lobby Scene.
     *
     * @throws IOException
     */
    public void goToLobbyScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/LobbyScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/LobbyScene.css")).toExternalForm());

        Stage stage = guiMain.getPrimaryStage();
        boolean wasFullScreen = stage.isFullScreen();
        if (!wasFullScreen) {
            stage.setWidth(960);
            stage.setHeight(540);
        }
        stage.setResizable(true);

        scene.setUserData(currController);
        guiMain.sceneControllerMap.put(scene, currController);
        changeScene(scene);
    }


    public void goToFaceUpScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/FaceUpScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/FaceUpScene.css")).toExternalForm());

        Stage stage = guiMain.getPrimaryStage();
        boolean wasFullScreen = stage.isFullScreen();
        if (!wasFullScreen) {
            stage.setWidth(960);
            stage.setHeight(540);
        }
        stage.setResizable(true);

        scene.setUserData(currController);
        guiMain.sceneControllerMap.put(scene, currController);

        changeScene(scene);
    }

    public void goToBuildScene() throws IOException {
        System.out.println("going to build scene");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/BuildScene.fxml"));
        Parent root = loader.load();

        currController = (BuildSceneController) loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/BuildScene.css")).toExternalForm());
        Stage stage = guiMain.getPrimaryStage();
        boolean wasFullScreen = stage.isFullScreen();
        if (!wasFullScreen) {
            stage.setWidth(600);
            stage.setHeight(400);
        }
        stage.setResizable(true);

        scene.setUserData(currController);
        guiMain.sceneControllerMap.put(scene, currController);

        changeScene(scene);
    }

    public void goToViewOthersScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/ViewOthersScene.fxml"));
        Parent root = loader.load();

        currController = loader.getController();
        currController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/ViewOthersScene.css")).toExternalForm());
        Stage stage = guiMain.getPrimaryStage();
        boolean wasFullScreen = stage.isFullScreen();
        if (!wasFullScreen) {
            stage.setWidth(600);
            stage.setHeight(400);
        }
        stage.setResizable(true);

        scene.setUserData(currController);
        guiMain.sceneControllerMap.put(scene, currController);

        changeScene(scene);
    }

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

    // Actions
    public void setNickname(String nickname) {
        this.nickname = nickname;
        server.setNickname(nickname);
    }

    public void createGame(String color, int level, int players) {
        server.createGame(level, players, PlayerColor.valueOf(color));
    }

    public void joinGame(int gameId, String color) {
        server.joinGame(gameId, PlayerColor.valueOf(color));
    }

    public void chooseFaceUp(int idx) {
        server.chooseTile(idx);
    }

    public void closeFaceUp() {
        server.closeFaceUpTiles();
    }

    public void drawFaceDown() {
        server.drawFaceDown();
    }

    public void place(int x, int y) {
        server.place(x, y);
    }

    public void returnTile() {
        server.returnTile();
    }

    public void pickPile(int pileIndex) {
        server.pickPile(pileIndex);
    }

    public void startTimer() {
        server.startTimer();
    }

    public void showFaceUp() {
        server.showFaceUp();
    }

    public void returnPile() {
        server.returnPile();
    }

    public void rotateTile(String direction) {
        server.rotate(direction);
    }

    public void chooseTileFromBuffer(int idx) {
        server.chooseTileFromBuffer(idx);
    }

    public void placeTileInBuffer() {
        server.placeInBuffer();
    }

    public void viewOthers2() {
        isViewingShips = true;
        updateGame(clientModel.getGame(), nickname);
    }

    public void home() {
        isViewingShips = false;
        updateGame(clientModel.getGame(), nickname);
    }

    // todo: add case to handle setNick
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "GAME_UPDATE" -> {
                // System.out.println("Game updated");
                updateGame((Game) evt.getNewValue(), (String) evt.getOldValue());
                columnOffset = ((Game) evt.getNewValue()).getLevel() == 2 ? 4 : 5;
            }
            case "LOGS_UPDATE" -> {
                System.out.println("Logs updated");
                updateLogs((List<String>) evt.getNewValue());
            }
            case "JOINABLE_GAMES" -> {
                try {
                    updateJoinableGames((List<Game.GameInfo>) evt.getNewValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}