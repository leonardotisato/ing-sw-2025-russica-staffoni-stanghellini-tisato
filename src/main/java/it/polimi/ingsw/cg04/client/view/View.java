package it.polimi.ingsw.cg04.client.view;

import it.polimi.ingsw.cg04.client.model.ClientModel;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;

import java.beans.PropertyChangeListener;
import java.io.IOException;

/**
 * Class that allows the visualization of the game state
 */
public abstract class View implements PropertyChangeListener {
    protected final ClientModel clientModel;
    protected final ServerHandler server;
    protected String nickname;
    protected boolean isViewingShips = false;

    protected View(ServerHandler server, ClientModel clientModel) {
        this.server = server;
        this.clientModel = clientModel;
        clientModel.setListener(this);
    }

    /**
     * Kills all the processes when the connection to the server is no longer available
     */
    public abstract void serverUnreachable();

    public void renderLobbyState(Game toDisplay) throws IOException {
        return;
    }
    public void renderBuildState(Game toDisplay) throws IOException {
        return;
    }

    public void renderEndGameState(Game toDisplay) throws IOException {
        return;
    }
}
