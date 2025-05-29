package it.polimi.ingsw.cg04.network.Client;

import it.polimi.ingsw.cg04.client.model.ClientModel;
import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.GUIRoot;
import it.polimi.ingsw.cg04.client.view.tui.TUI;
import it.polimi.ingsw.cg04.model.Game;

import java.rmi.RemoteException;
import java.util.List;

public abstract class ServerHandler implements VirtualServer {
    protected String nickname;
    private final ClientModel clientModel;
    private View view;

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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void addLogs(List<String> logs) {
        clientModel.addLog(logs);
    }

    public void setGame(Game game) {
        clientModel.setGame(game, nickname);
    }

    protected void notifyViewServerUnreachable(){
        view.serverUnreachable();
    }

    public void sendJoinableGames(List<Game.GameInfo> infos){clientModel.displayJoinableGames(infos);}
}
