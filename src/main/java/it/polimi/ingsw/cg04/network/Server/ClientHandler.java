package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.PlayerActions.Action;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

import java.util.Collections;

public abstract class ClientHandler implements VirtualClient {

    String nickname;
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
            if (nickname != null) {
                controller.disconnect(nickname);
                System.out.println("Disconnecting " + nickname);
            }
        } catch (Exception e) {
            addLogs(Collections.singletonList(e.getMessage()));
        }

        server.unsubscribe(this);
    }

    public void handleAction(Action action) {
        try {
            System.out.println("Received action: " + action.getClass().getSimpleName() + " from " + action.getPlayerNickname());
            action.dispatchTo(controller);
        } catch (InvalidActionException e) {
            addLogs(Collections.singletonList(e.getReason()));
        } catch (InvalidStateException e) {
            addLogs(Collections.singletonList(e.getReason()));
        }
    }

    public boolean handleSubscription(Action action) {
        if (nickname != null) {
            addLogs(Collections.singletonList("You are already connected as " + nickname));
            return false;
        }
        try {
            System.out.println("Received subscription request as " + action.getPlayerNickname() + " from a new client");
            action.dispatchTo(controller);
            nickname = action.getPlayerNickname();
            server.subscribe(this);
            return true;
        } catch (InvalidActionException e) {
            addLogs(Collections.singletonList(e.getReason()));
            return false;
        } catch (InvalidStateException e) {
            addLogs(Collections.singletonList(e.getReason()));
            return false;
        }
    }
}
