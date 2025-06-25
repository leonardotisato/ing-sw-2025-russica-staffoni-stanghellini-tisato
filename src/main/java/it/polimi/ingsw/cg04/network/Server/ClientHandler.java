package it.polimi.ingsw.cg04.network.Server;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.controller.PlayerActions.Action;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

import java.util.Collections;
import java.util.List;

public abstract class ClientHandler implements VirtualClient {

    String nickname;
    final GamesController controller;
    final Server server;

    public ClientHandler(GamesController controller, Server server) {
        this.nickname = null;
        this.controller = controller;
        this.server = server;
    }

    /**
     * @return the nickname of the client or null if not set.
     */
    public String getNickName() {
        return nickname;
    }

    /**
     * @return the GamesController instance associated with this client handler.
     */
    public GamesController getController() {
        return controller;
    }

    /**
     * @return the Server instance associated with the client handler
     */
    public Server getServer() {
        return server;
    }

    /**
     * Disconnects the current client from the server and its associated game.
     */
    protected void disconnect() {
        try {
            if (nickname != null) {
                controller.disconnect(nickname);
                System.out.println("Disconnecting " + nickname);
            }
        } catch (Exception e) {
            System.out.println("Error disconnecting " + nickname);
            addLogs(Collections.singletonList(e.getMessage()));
        }

        server.unsubscribe(this);
    }

    /**
     * Handles an incoming {@code Action} instance by processing it through the associated game controller.
     * Logs any errors and updates the game state accordingly if exceptions occur during processing.
     *
     * @param action the action to be processed.
     */
    public void handleAction(Action action) {
        try {
            System.out.println("Received action: " + action.getClass().getSimpleName() + " from " + action.getPlayerNickname());
            action.dispatchTo(controller);

        } catch (InvalidActionException e) {

            Game g = controller.getGameFromNickname(action.getPlayerNickname());
            setGame(g.deepCopy());
            addLogs(Collections.singletonList(e.getReason()));

        } catch (InvalidStateException e) {

            Game g = controller.getGameFromNickname(action.getPlayerNickname());
            setGame(g.deepCopy());
            addLogs(Collections.singletonList(e.getReason()));
        }
    }

    /**
     * Handles the subscription process for a client.
     *
     * If the client is already associated with a nickname, the operation is logged and fails.
     * Otherwise, the client's subscription request is processed, and if valid, the client is subscribed to the server.
     *
     * @param action the {@code Action} instance containing the subscription request and
     *               the player's nickname.
     *
     * @return {@code true} if the subscription is successfully completed;
     *         {@code false} if the subscription fails due to invalid actions,
     *         invalid state, or the client already being subscribed.
     */
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

    /**
     * Provides a list of games that are currently available for the client to join.
     * This method retrieves the list of joinable games by querying the associated
     * game controller.
     *
     * @return a list of {@code Game.GameInfo} objects representing the joinable games.
     */
    public List<Game.GameInfo> provideJoinableGames() {
        return controller.getJoinableGames();
    }
}
