package it.polimi.ingsw.cg04.controller;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.controller.PlayerActions.LobbyActions.InitAction;
import it.polimi.ingsw.cg04.controller.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.network.Server.Server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.*;

public class GamesController implements PropertyChangeListener {

    private final Server server;

    private final List<Game> games;
    private final Map<Game, List<Player>> connectedPlayers;
    private final Map<Game, List<Player>> disconnectedPlayers;
    private final Map<String, Game> nickToGame;

    public GamesController() throws IOException {
        games = new ArrayList<>();
        connectedPlayers = new HashMap<>();
        disconnectedPlayers = new HashMap<>();
        nickToGame = new HashMap<>();
        server = new Server(this);
        server.start();
    }


    /**
     *
     * @return a list of Game objects representing all the games managed by the controller
     */
    public List<Game> getGames() {
        return games;
    }

    /**
     *
     * @param id the unique identifier of the game to be retrieved
     * @return the Game object matching the specified ID, or null if no matching game is found
     */
    public Game getGame(int id) {
        return games.stream().filter(g -> g.getId() == id).findFirst().orElse(null);
    }

    /**
     *
     * @param nickname the nickname of the player whose associated game is to be retrieved
     * @return the Game object associated with the given nickname, or null if no game is found
     */
    public Game getGameFromNickname(String nickname) {
        return nickToGame.get(nickname);
    }

    /**
     * Handles the reception and processing of a player action within the associated game context.
     * It verifies the legitimacy of the action, executes it if valid, updates the game state accordingly,
     * broadcasts the updated game snapshot and operation logs to all connected players.
     *
     * @param action the player action to be processed.
     * @throws InvalidActionException if the action is deemed invalid for the player
     *                                (e.g., the player is retired and cannot perform actions).
     * @throws InvalidStateException if the action is inconsistent with the current state
     *                               of the game or player.
     */
    public void onActionReceived(PlayerAction action) throws InvalidActionException, InvalidStateException {

        try {
            System.out.println("Action received");
            Game g = nickToGame.get(action.getPlayerNickname());
            Player p = g.getPlayer(action.getPlayerNickname());
            g.getGameState().initLogs();

            if (!p.isRetired()) {
                action.checkAction(p);
                action.execute(p);
                System.out.println("Action executed");
                g = nickToGame.get(action.getPlayerNickname());

            } else {
                System.out.println("Retired exception thrown.");
                throw new InvalidActionException("You are retired. You can't play anymore, wait for the game to finish.");
            }


            List<String> recipients = connectedPlayers.get(g).stream().map(Player::getName).toList();

            // send game snapshot to players
            server.broadcastGameUpdate(recipients, g.deepCopy());

            // send logs collected while executing the action
            List<String> collectedLogs = action.getLogs();
            server.broadcastLogs(recipients, collectedLogs);
            g.getGameState().initLogs();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the processing of an initial action received within the game system.
     * It validates and executes the provided InitAction, updates game states and connected players,
     * and broadcasts relevant updates to recipient players, including game snapshots, logs,
     * and joinable game information.
     *
     * @param action the InitAction to be processed.
     * @throws InvalidActionException if the action is deemed invalid by the validation process.
     */
    public void onInitActionReceived(InitAction action) throws InvalidActionException {
        System.out.println("Init action received");
        action.checkAction(this);
        action.execute(this);
        System.out.println("Init action executed");

        String playerNickname = action.getPlayerNickname();
        Game g = nickToGame.get(playerNickname);
        List<String> recipients;

        if (g != null) {
            List<Player> players = connectedPlayers.get(g);
            recipients = players.stream().map(Player::getName).toList();

            // send game snapshot to players
            server.broadcastGameUpdate(recipients, g.deepCopy());
            // send logs collected while executing the action
            List<String> collectedLogs = action.getLogs();

            if (collectedLogs != null) {
                server.broadcastLogs(recipients, collectedLogs);
            }

            List<Game.GameInfo> gameInfos = getJoinableGames();
            recipients = getRecipientsForJoinableGames();
            server.broadcastJoinableGames(recipients, gameInfos);
        }
    }

    /**
     * Adds a game to the games collection managed by the controller.
     * Updates the tracking structures for the game and its associated players.
     *
     * @param game the Game object to be added. This object contains the players and other game-related information.
     */
    public void addGame(Game game) {
        games.add(game);
        connectedPlayers.put(game, new ArrayList<>());
        for (Player p : game.getPlayers()) {
            nickToGame.put(p.getName(), game);
            connectedPlayers.get(game).add(p);
        }
    }

    /**
     *
     * @return a list of GameInfo objects representing the joinable games.
     */
    public List<Game.GameInfo> getJoinableGames() {
        List<Game.GameInfo> gameInfos = new ArrayList<>();
        for (Game game : games) {
            if (!game.hasStarted()) gameInfos.add(game.getGameInfo());
        }
        return gameInfos;
    }

    /**
     *
     * @return a list of nicknames of players who are currently not associated with any game
     *         and can receive joinable game notifications.
     */
    public List<String> getRecipientsForJoinableGames() {
        List<String> recipients = new ArrayList<>();
        for (String nickname : nickToGame.keySet()) {
            if (nickToGame.get(nickname) == null) recipients.add(nickname);
        }
        return recipients;
    }

    /**
     * Checks whether a given nickname is already associated with a game in the system.
     *
     * @param nickname the nickname to be checked for existence in the system
     * @return true if the given nickname is already taken, false otherwise
     */
    public boolean isNickNameTaken(String nickname) {
        return nickToGame.containsKey(nickname);
    }

    /**
     * Adds a player to the set of connected players for the specified game.
     *
     * @param game   the game to which the player is being connected
     * @param player the player to be added to the game's list of connected players
     */
    public void addConnectedPlayer(Game game, Player player) {
        connectedPlayers.get(game).add(player);
    }

    /**
     * Associates a player's nickname with a specific game in the system.
     *
     * @param nickname the nickname of the player to be linked to the specified game
     * @param game     the game to which the nickname is being associated
     */
    public void addNicktoGame(String nickname, Game game) {
        nickToGame.put(nickname, game);
    }

    /**
     * Disconnects a player identified by their nickname from their associated game.
     * The player is removed from the list of connected players for the game
     * and added to the list of disconnected players. Notifies the game about the player's disconnection.
     *
     * @param nickname the nickname of the player to be disconnected
     */
    public void disconnect(String nickname) {
        Game g = nickToGame.get(nickname);
        if (g != null) {
            Player p = g.getPlayer(nickname);
            if (!disconnectedPlayers.containsKey(g)) disconnectedPlayers.put(g, new ArrayList<>());
            disconnectedPlayers.get(g).add(p);
            connectedPlayers.get(g).remove(p);
            g.disconnect(p);
        }
    }

    /**
     * Handles property change events.
     *
     * @param evt the property change event containing the property name, source,
     *            and new value related to the change. The "source" is expected to
     *            be the game ID (Integer), and the "new value" is expected to
     *            represent the timer number that expired.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // handle timer update
        switch (evt.getPropertyName()) {
            case "timerExpired" -> {
                Integer gameId = (Integer) evt.getSource();
                Game g = games.stream().filter(game -> game.getId() == gameId).findFirst().orElse(null);
                if (g == null) throw new RuntimeException("Game not found");

                List<Player> players = connectedPlayers.get(g);
                List<String> recipients = players.stream().map(Player::getName).toList();

                // send game snapshot and log to players
                server.broadcastGameUpdate(recipients, g.deepCopy());
                server.broadcastLogs(recipients, Collections.singletonList("A timer has expired! That was timer number " + evt.getNewValue() + "!"));

            }
        }
    }
}
