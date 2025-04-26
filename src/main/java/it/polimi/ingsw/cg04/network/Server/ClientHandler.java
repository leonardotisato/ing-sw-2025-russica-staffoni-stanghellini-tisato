package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.PlayerActions.Action;

import java.rmi.RemoteException;

public abstract class ClientHandler {

    final String nickname;
    final GamesController controller;
    final Server server;

    public ClientHandler(GamesController controller, Server server) {
        this.nickname = null;
        this.controller = controller;
        this.server = server;
    }

    public String getNickName() {
        return nickname;
    }

    public GamesController getController() {
        return controller;
    }

    public Server getServer() {
        return server;
    }

    protected void disconnect() {
        try {
            if(nickname != null)
                //controller.disconnect(nickname);
                System.out.println("Disconnecting " + nickname);
        } catch (Exception e) {
            //addLog(e.getMessage());
        }

        //server.unsubscribe(this);
    }

    public void handleAction(Action action) throws RemoteException {
        action.dispatchTo(controller);
    }


}
