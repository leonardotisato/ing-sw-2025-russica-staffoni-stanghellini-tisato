package it.polimi.ingsw.cg04.view;

import it.polimi.ingsw.cg04.network.Client.ServerHandler;

/**
 * Class that allows the visualization of the game state
 */
public abstract class View {
    // protected final ClientModel clientModel;
    protected final ServerHandler server;

    protected View(ServerHandler server) {
        this.server = server;
    }

    /**
     * Kills all the processes when the connection to the server is no longer available
     */
    public abstract void serverUnreachable();
}
