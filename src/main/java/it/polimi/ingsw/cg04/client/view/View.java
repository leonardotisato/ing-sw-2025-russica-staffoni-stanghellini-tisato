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

    abstract public void updateGame(Game toPrint, String nickname);

    abstract public void updateLogs(List<String> logs);

    abstract public void updateJoinableGames(List<Game.GameInfo> joinableGames);

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

    abstract public void renderLobbyState(Game toDisplay) throws IOException;

    abstract public void renderBuildState(Game toDisplay) throws IOException;

    abstract public void renderEndGameState(Game toDisplay) throws IOException;

    public abstract void renderLoadCrewState(Game toDisplay) throws IOException;

    public abstract void renderFlightState(Game toDisplay) throws IOException;

    public abstract void renderAbandonedShipState(Game toDisplay) throws IOException;

    public abstract void renderAbandonedStationState(Game toDisplay) throws IOException;

    public abstract void renderEpidemicState(Game toDisplay) throws IOException;

    public abstract void renderMeteorsRainState(Game toDisplay) throws IOException;

    public abstract void renderOpenSpaceState(Game toDisplay) throws IOException;

    public abstract void renderPiratesState(Game toDisplay) throws IOException;

    public abstract void renderPlanetsState(Game toDisplay) throws IOException;

    public abstract void renderSlaversState(Game toDisplay) throws IOException;

    public abstract void renderSmugglersState(Game toDisplay) throws IOException;

    public abstract void renderStardustState(Game toDisplay) throws IOException;

    public abstract void renderWarZoneState(Game toDisplay) throws IOException;

}
