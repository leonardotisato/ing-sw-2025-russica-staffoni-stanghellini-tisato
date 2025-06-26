package it.polimi.ingsw.cg04.client.view.gui.controllers;

import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.model.Game;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.util.List;

public abstract class ViewController implements Initializable {
    private GUIRoot gui;

    /**
     * Displays log messages in the controller's UI. Default implementation does nothing.
     *
     * @param logLines list of log message strings to display
     */
    public void showLogs(List<String> logLines) {
        return;
    }

    /**
     * Sets the GUI root reference for this controller.
     *
     * @param gui the GUIRoot instance managing the GUI application
     */
    public void setGUI(GUIRoot gui) {
        this.gui = gui;
    }

    /**
     * Navigates to the lobby scene.
     *
     * @param gui the GUIRoot instance for scene management
     * @throws IOException if an I/O error occurs during scene transition
     */
    public void goToLobbyScene(GUIRoot gui) throws IOException {
        gui.goToLobbyScene();
    }

    /**
     * Navigates to the pre-lobby scene.
     *
     * @param gui the GUIRoot instance for scene management
     * @throws IOException if an I/O error occurs during scene transition
     */
    public void goToPrelobbyScene(GUIRoot gui) throws IOException {
        gui.goToPrelobbyScene();
    }

    /**
     * Navigates to the build scene.
     *
     * @param gui the GUIRoot instance for scene management
     * @throws IOException if an I/O error occurs during scene transition
     */
    public void goToBuildScene(GUIRoot gui) throws IOException {
        gui.goToBuildScene();
    }

    /**
     * Navigates to the face-up tiles scene.
     *
     * @param gui the GUIRoot instance for scene management
     * @throws IOException if an I/O error occurs during scene transition
     */
    public void goToFaceUpScene(GUIRoot gui) throws IOException {
        gui.goToFaceUpScene();
    }

    /**
     * Navigates to the end game scene.
     *
     * @param gui the GUIRoot instance for scene management
     * @throws IOException if an I/O error occurs during scene transition
     */
    public void goToEndScene(GUIRoot gui) throws IOException {
        gui.goToEndScene();
    }

    /**
     * Navigates to the view others scene.
     *
     * @param gui the GUIRoot instance for scene management
     * @throws IOException if an I/O error occurs during scene transition
     */
    public void goToViewOthersScene(GUIRoot gui) throws IOException {
        gui.goToViewOthersScene();
    }

    /**
     * Navigates to the adventure card scene.
     *
     * @param gui the GUIRoot instance for scene management
     * @throws IOException if an I/O error occurs during scene transition
     */
    public void goToAdventureCardScene(GUIRoot gui) throws IOException {
        gui.goToAdventureCardScene();
    }

    /**
     * Navigates to the error scene.
     *
     * @param gui the GUIRoot instance for scene management
     * @throws IOException if an I/O error occurs during scene transition
     */
    public void goToErrorScene(GUIRoot gui) throws  IOException {
        gui.goToErrorScene();
    }

    /**
     * Updates the controller with the current game state
     *
     * @param g the Game object containing the current state
     */
    public void update(Game g) {
        return;
    }

    /**
     * Updates the load crew controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateLoadCrewController(Game game){ return;}

    /**
     * Updates the build controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateBuildController(Game game){ return;}

    /**
     * Updates the flight controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateFlightController(Game game){ return;}

    /**
     * Updates the open space controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateOpenSpaceController(Game game){ return;}

    /**
     * Updates the abandoned ship controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateAbandonedShipController(Game game){ return;}

    /**
     * Updates the stardust controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateStardustController(Game game) { return;}

    /**
     * Updates the epidemic controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateEpidemicController(Game game) {return; }

    /**
     * Updates the abandoned station controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateAbandonedStationController(Game game){ return;}

    /**
     * Updates the meteors rain controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateMeteorsRainController(Game game){ return; }

    /**
     * Updates the pirates controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updatePiratesController(Game game){ return; }

    /**
     * Updates the planets controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updatePlanetsController(Game game){ return; }

    /**
     * Updates the slavers controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateSlaversController(Game game){ return; }

    /**
     * Updates the smugglers controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateSmugglersController(Game game){ return; }

    /**
     * Updates the war zone controller with game state.
     *
     * @param game the Game object containing current state
     */
    public void updateWarZoneController(Game game){ return;}

    /**
     * Refreshes the list of joinable games. Default implementation does nothing.
     *
     * @param games list of GameInfo objects representing available games
     */
    public void refreshJoinableGames(List<Game.GameInfo> games) {
        return;
    }
}
