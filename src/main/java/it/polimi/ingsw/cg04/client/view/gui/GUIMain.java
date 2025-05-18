package it.polimi.ingsw.cg04.client.view.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class GUIMain extends Application {

    private Stage primaryStage;
    private static GUIMain instance;

    private static GUIRoot guiRoot;
    public Map<Scene, Object> sceneControllerMap = new HashMap<>();


    public GUIMain() {
        instance = this;
    }

    public static void launchApp(GUIRoot guiRootInstance) {
        guiRoot = guiRootInstance;
        Application.launch(GUIMain.class);

    }


    @Override
    public void start(Stage stage) throws Exception {
        try {
            this.primaryStage = new Stage();
            guiRoot.setGuiMain(this);
            guiRoot.goToFirstScene();
        } catch (Exception e) {
            e.printStackTrace(); // mostra l'errore vero in console
        }
    }

    public Object getControllerForScene(Scene scene) {
        return sceneControllerMap.get(scene);
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Stops the application
     */
    public void stop () {
        Platform.exit();
        System.exit(0);
    }

    /**
     * @return A reference to the primary stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

