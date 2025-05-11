package it.polimi.ingsw.cg04.client.view;

import it.polimi.ingsw.cg04.client.ClientModel;
import it.polimi.ingsw.cg04.network.Client.ServerHandler;

/**
 * Class that allows the visualization of the game state
 */
public abstract class View implements Observer{
    protected final ClientModel clientModel;
    protected final ServerHandler server;

    protected View(ServerHandler server, ClientModel clientModel) {
        this.server = server;
        this.clientModel = clientModel;
    }

    /**
     * Kills all the processes when the connection to the server is no longer available
     */
    public abstract void serverUnreachable();
}
