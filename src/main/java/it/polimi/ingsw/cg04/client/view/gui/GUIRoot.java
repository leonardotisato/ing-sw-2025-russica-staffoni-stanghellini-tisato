package it.polimi.ingsw.cg04.client.view.gui;

import it.polimi.ingsw.cg04.client.model.ClientModel;
import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.controllers.*;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.BuildState;
import it.polimi.ingsw.cg04.model.GameStates.EndGameState;
import it.polimi.ingsw.cg04.model.GameStates.LoadCrewState;
import it.polimi.ingsw.cg04.model.GameStates.LobbyState;
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

    private String clientNickname;


    LoginController loginController;
    PrelobbySceneController prelobbySceneController;
    LobbySceneController lobbySceneController;

    FaceUpSceneController faceUpSceneController;


    EndSceneController endSceneController;

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

    }

    /**
     * GuiMain setter method
     *
     * @param guiMain the object to set
     */
    public void setGuiMain(GUIMain guiMain) {
        this.guiMain = guiMain;
    }


    public void updateGame(Game toDisplay, String nickname) {

        try {
            if (toDisplay != null) {
                if (toDisplay.getGameState() instanceof LobbyState) {
                    if (lobbySceneController != null) {
                        lobbySceneController.refreshLobby(toDisplay);
                    }
                } else if (toDisplay.getGameState() instanceof BuildState) {
                    if (((BuildState) toDisplay.getGameState()).getPlayerState().get(nickname) == BuildPlayerState.SHOWING_FACE_UP) {
                        if (faceUpSceneController == null) {
                            goToFaceUpScene();
                            Platform.runLater(() -> {
                                faceUpSceneController.update(toDisplay);
                            });
                        } else {
                            Platform.runLater(() -> {
                                faceUpSceneController.update(toDisplay);
                            });
                        }
                    }

                    // todo: aggiungi check per capire se mostrare others scene, build scene

                } else if (toDisplay.getGameState() instanceof LoadCrewState) {

                } else if (toDisplay.getGameState() instanceof AdventureCardState) {
                    // todo: aggiunge check per capire se mostrare others scene o adCard scene

                } else if (toDisplay.getGameState() instanceof EndGameState) {
                    if (endSceneController == null) {
                        goToEndScene();
                        Platform.runLater(() -> {
                            endSceneController.update(toDisplay);
                        });
                    } else {
                        Platform.runLater(() -> {
                            endSceneController.update(toDisplay);
                        });
                    }
                }
            }
        } catch (NullPointerException ignored) {
            System.out.println("Game not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateLogs(List<String> newValue) {

    }

    // todo implement: it should refresh the games in preLobby scene
    private void updateJoinableGames(List<Game.GameInfo> newValue) {
        // Il prelobby è aperto solo se il controller è già stato creato
        if (prelobbySceneController != null) {
            prelobbySceneController.refreshJoinableGames(newValue);
        }
    }

    private void updateCurrentGame(List<Game.GameInfo> newValue) {

    }

    public Scene getScene() {
        return guiMain.getPrimaryStage().getScene();
    }

    private void changeScene(Scene scene) {
        Platform.runLater(() -> {
            Stage stage = guiMain.getPrimaryStage();
            stage.setTitle("Galaxy Trucker");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        });
    }

    /**
     * Initialize the first Scene where the player chooses the nickname and the number of players.
     */
    public void goToFirstScene() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/loginscene.fxml"));
        Parent root = loader.load();

        loginController = loader.getController();
        loginController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/loginscene.css")).toExternalForm());

        Stage stage = guiMain.getPrimaryStage();
        stage.setWidth(960);
        stage.setHeight(540);
        stage.setResizable(true);

        scene.setUserData(loginController);
        guiMain.sceneControllerMap.put(scene, loginController);
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

        prelobbySceneController = loader.getController();
        prelobbySceneController.setGUI(this);

        Scene scene = new Scene(root);

        Stage stage = guiMain.getPrimaryStage();
        stage.setWidth(960);
        stage.setHeight(540);
        stage.setResizable(true);

        scene.setUserData(prelobbySceneController);
        guiMain.sceneControllerMap.put(scene, prelobbySceneController);
        changeScene(scene);
    }

    /**
     * Initialize the Lobby Scene.
     *
     * @throws IOException
     */
    public void gotoLobbyScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/LobbyScene.fxml"));
        Parent root = loader.load();

        lobbySceneController = loader.getController();
        lobbySceneController.setGUI(this);

        Scene scene = new Scene(root);

        Stage stage = guiMain.getPrimaryStage();
        stage.setWidth(960);
        stage.setHeight(540);
        stage.setResizable(true);

        scene.setUserData(lobbySceneController);
        guiMain.sceneControllerMap.put(scene, lobbySceneController);
        changeScene(scene);
    }


    public void goToFaceUpScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/FaceUpScene.fxml"));
        Parent root = loader.load();

        faceUpSceneController = loader.getController();
        faceUpSceneController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/FaceUpScene.css")).toExternalForm());

        Stage stage = guiMain.getPrimaryStage();
        stage.setResizable(true);

        scene.setUserData(faceUpSceneController);
        guiMain.sceneControllerMap.put(scene, faceUpSceneController);

        changeScene(scene);
    }


    public void goToEndScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/it/polimi/ingsw/cg04/EndScene.fxml"));
        Parent root = loader.load();

        endSceneController = loader.getController();
        endSceneController.setGUI(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/cg04/EndScene.css")).toExternalForm());

        Stage stage = guiMain.getPrimaryStage();
        stage.setResizable(true);

        scene.setUserData(endSceneController);
        guiMain.sceneControllerMap.put(scene, endSceneController);

        changeScene(scene);
    }

    // Actions
    public void setNickname(String nickname) {
        this.clientNickname = nickname;
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
                // System.out.println("Logs updated");
                updateLogs((List<String>) evt.getNewValue());
            }
            case "JOINABLE_GAMES" -> {
                updateJoinableGames((List<Game.GameInfo>) evt.getNewValue());
            }
        }
    }
}
