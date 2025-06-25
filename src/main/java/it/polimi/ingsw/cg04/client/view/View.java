package it.polimi.ingsw.cg04.client.view;

import it.polimi.ingsw.cg04.client.model.ClientModel;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

/**
 * Class that allows the visualization of the game state
 */
public abstract class View implements PropertyChangeListener {
    protected final ClientModel clientModel;
    protected final ServerHandler server;
    protected String nickname;
    protected boolean isViewingShips = false;
    protected int columnOffset = 0;

    protected View(ServerHandler server, ClientModel clientModel) {
        this.server = server;
        this.clientModel = clientModel;
        clientModel.setListener(this);
    }

    /**
     * Kills all the processes when the connection to the server is no longer available
     */
    public abstract void serverUnreachable();

    /**
     * Updates the current game state and associated player's nickname.
     *
     * @param toPrint   the current state of the game to be displayed or processed
     * @param nickname  the nickname of the player whose state is associated with the game
     */
    abstract public void updateGame(Game toPrint, String nickname);

    /**
     * Updates the list of logs displayed or processed by the system.
     *
     * @param logs a list of strings, where each string represents an individual log entry
     */
    abstract public void updateLogs(List<String> logs);

    /**
     * Updates the list of games that are currently joinable in the system.
     *
     * @param joinableGames a list of {@link Game.GameInfo} objects, where
     *                      each object represents the information of a game
     *                      that can be joined.
     */
    abstract public void updateJoinableGames(List<Game.GameInfo> joinableGames);

    /**
     * Handles property change events triggered by changes in the client model.
     * The behavior of this method is determined by the property name specified in the event.
     * Depending on the property name, it updates the game state, log entries, or the list
     * of joinable games.
     *
     * @param evt the event object that describes the change to a property,
     *            including the property name, old value, and new value
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "GAME_UPDATE" -> {
                // System.out.println("Game updated");
                columnOffset = ((Game) evt.getNewValue()).getLevel() == 2 ? 4 : 5;
                updateGame((Game) evt.getNewValue(), (String) evt.getOldValue());
            }
            case "LOGS_UPDATE" -> {
                // System.out.println("Logs updated");
                updateLogs((List<String>) evt.getNewValue());
            }
            case "JOINABLE_GAMES" -> updateJoinableGames((List<Game.GameInfo>) evt.getNewValue());
        }
    }

    /**
     * Renders the current state of the game when the player is in the lobby.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state to be displayed in the lobby
     * @throws IOException if an I/O error occurs during the rendering process
     */
    abstract public void renderLobbyState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is in the build phase.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state to be displayed during the build phase
     * @throws IOException if an I/O error occurs during the rendering process
     */
    abstract public void renderBuildState(Game toDisplay) throws IOException;

    /**
     * Renders the game state at the end of the game session.
     *
     * @param toDisplay the instance of the {@link Game} representing the final state of the game
     *                  to be displayed or processed
     * @throws IOException if an I/O error occurs during the rendering process
     */
    abstract public void renderEndGameState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is in the load crew phase.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state to be displayed during the load crew phase
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderLoadCrewState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is in the flight phase.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state to be displayed during the flight phase
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderFlightState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is in abandoned ship adv.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state to be displayed in the abandoned ship phase
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderAbandonedShipState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is within an abandoned station scenario.
     *
     * @param toDisplay the instance of the {@link Game} representing the abandoned station state to be displayed
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderAbandonedStationState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game during the epidemic event phase.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state
     *                  to be displayed or processed during the epidemic event phase
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderEpidemicState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is in the meteors rain phase.
     *
     * @param toDisplay the instance of the {@link Game} representing the current game state
     *                  to be displayed during the meteors rain phase
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderMeteorsRainState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is in the open space phase.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state
     *                  to be displayed during the open space phase
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderOpenSpaceState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is in the pirates phase.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state to be displayed during the pirates phase
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderPiratesState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is in planets adventure.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state to be displayed
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderPlanetsState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is in slavers adventure
     *
     * @param toDisplay the instance of the {@link Game} representing the game state to be displayed during the slaver interaction phase
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderSlaversState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player encounters smugglers.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state to be displayed during the smugglers encounter
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderSmugglersState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is in the stardust phase.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state to be displayed during the stardust phase
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderStardustState(Game toDisplay) throws IOException;

    /**
     * Renders the current state of the game when the player is in the war zone phase.
     *
     * @param toDisplay the instance of the {@link Game} representing the game state
     *                  to be displayed during the war zone phase
     * @throws IOException if an I/O error occurs during the rendering process
     */
    public abstract void renderWarZoneState(Game toDisplay) throws IOException;

}
