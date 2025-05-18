package it.polimi.ingsw.cg04.client.view.gui;

import it.polimi.ingsw.cg04.client.model.ClientModel;
import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.controllers.LoginController;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.Objects;

public class GUIRoot extends View {

    GUIMain guiMain;

    private Thread guiThread;

    private String clientNickname;

    LoginController loginController;

    private boolean isLoggedOut;

    /**
     * Constructor
     * @param clientModel the client model
     * @param server the server
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
     * @param guiMain the object to set
     */
    public void setGuiMain(GUIMain guiMain) {
        this.guiMain = guiMain;
    }

    public void updateGame() {

    }

    public void updateLogs() {

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
     * Initialize the first Scene where the player choose the nickname and the number of players.
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

    public void setNickname(String nickname) {
        this.clientNickname = nickname;
        server.setNickname(nickname);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
