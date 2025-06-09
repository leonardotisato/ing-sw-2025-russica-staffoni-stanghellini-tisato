package it.polimi.ingsw.cg04.network.Client;

import it.polimi.ingsw.cg04.client.model.ClientModel;
import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.client.view.tui.TUI;
import it.polimi.ingsw.cg04.model.Game;

import java.util.List;

public abstract class ServerHandler implements VirtualServer {
    protected String nickname;
    private final ClientModel clientModel;
    private final View view;

    protected final String serverIP;
    protected final String serverPort;

    protected ServerHandler(String serverIP, String serverPort, String viewType) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        clientModel = new ClientModel();

        if(viewType.equals("GUI")) {
            view = new GUIRoot(clientModel, this);
        } else {
            view = new TUI(this, clientModel);
        }
    }

    /**
     * Sets the nickname of the player associated with the server handler.
     *
     * @param nickname the nickname to be associated with the server handler
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @param logs a list of log messages to be added to the client's log history
     */
    public void addLogs(List<String> logs) {
        clientModel.addLog(logs);
    }

    /**
     * Sets the game instance in the client model associated with the given player nickname.
     *
     * @param game the game instance to be set in the client model
     */
    public void setGame(Game game) {
        clientModel.setGame(game, nickname);
    }

    /**
     * Notifies the associated view that the connection to the server is no longer available.
     */
    protected void notifyViewServerUnreachable(){
        view.serverUnreachable();
    }

    /**
     * Sends a list of joinable games to the client model for display.
     *
     * @param infos a list of game information objects representing joinable games
     */
    public void sendJoinableGames(List<Game.GameInfo> infos){clientModel.displayJoinableGames(infos);}
}
