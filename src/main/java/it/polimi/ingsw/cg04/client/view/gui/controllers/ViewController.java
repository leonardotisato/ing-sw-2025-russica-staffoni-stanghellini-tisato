package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.util.List;

public abstract class ViewController implements Initializable {
    private GUIRoot gui;

    public void showLogs(List<String> logLines) {
        return;
    }

    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    public void goToLobbyScene(GUIRoot gui) throws IOException {
        gui.goToLobbyScene();
    }

    public void goToPrelobbyScene(GUIRoot gui) throws IOException {
        gui.goToPrelobbyScene();
    }

    public void goToBuildScene(GUIRoot gui) throws IOException {
        gui.goToBuildScene();
    }

    public void goToFaceUpScene(GUIRoot gui) throws IOException {
        gui.goToFaceUpScene();
    }

    public void goToEndScene(GUIRoot gui) throws IOException {
        gui.goToEndScene();
    }

    public void goToViewOthersScene(GUIRoot gui) throws IOException {
        gui.goToViewOthersScene();
    }

    public void goToAdventureCardScene(GUIRoot gui) throws IOException {
        gui.goToAdventureCardScene();
    }

    public void goToErrorScene(GUIRoot gui) throws  IOException {
        gui.goToErrorScene();
    }

    public void update(Game g) {
        return;
    }

    public void updateLoadCrewController(Game game){ return;}
    public void updateBuildController(Game game){ return;}
    public void updateFlightController(Game game){ return;}
    public void updateOpenSpaceController(Game game){ return;}
    public void updateAbandonedShipController(Game game){ return;}
    public void updateAbandonedStationController(Game game){ return;}

    public void refreshJoinableGames(List<Game.GameInfo> games) {
        return;
    }
}
