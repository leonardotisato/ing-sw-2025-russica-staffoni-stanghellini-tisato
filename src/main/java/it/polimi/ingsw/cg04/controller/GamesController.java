package it.polimi.ingsw.cg04.controller;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.EndGameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.InitAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.network.Server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamesController {

    private Server server;

    private final List<Game> games;
    private final Map<Game, List<Player>> connectedPlayers;
    private final Map<Game, List<Player>> disconnectedPlayers;
    private final Map<String, Game> nickToGame;

    public GamesController() {
        games = new ArrayList<>();
        connectedPlayers = new HashMap<>();
        disconnectedPlayers = new HashMap<>();
        nickToGame = new HashMap<>();
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public List<Game> getGames() {
        return games;
    }

    public Game getGame(int id) {
        return games.stream().filter(g -> g.getId() == id).findFirst().orElse(null);
    }

    public Game getGameFromNickname(String nickname) {
        return nickToGame.get(nickname);
    }

    public void onActionReceived(PlayerAction action) throws InvalidActionException, InvalidStateException {

        try {
            Game g = nickToGame.get(action.getPlayerNickname());
            Player p = g.getPlayer(action.getPlayerNickname());
            action.checkAction(p);
            action.execute(p);


            List<String> recipients = connectedPlayers.get(g).stream().map(Player::getName).toList();

            // send logs collected while executing the action
            List<String> collectedLogs = action.getLogs();
            server.broadcastLogs(recipients, collectedLogs);

            // send game snapshot to players
            server.broadcastGameUpdate(recipients, g.deepCopy());

        } catch (NullPointerException e) {
            System.out.println("Player not in a game");
        }
    }

    public void onInitActionReceived(InitAction action) throws InvalidActionException {
            action.checkAction(this);
            action.execute(this);
    }

    public void addGame(Game game) {
        games.add(game);
        connectedPlayers.put(game, new ArrayList<>());
        for (Player p : game.getPlayers()) {
            nickToGame.put(p.getName(), game);
            connectedPlayers.get(game).add(p);
        }
    }

    public List<Integer> getJoinableGames() {
        List<Integer> joinableGames = new ArrayList<>();

        for (Game g : games) {
            if (!g.hasStarted() && connectedPlayers.get(g).size() < g.getMaxPlayers()) {
                joinableGames.add(g.getId());
            }
        }

        return joinableGames;
    }


    public boolean isNickNameTaken(String nickname) {
        return nickToGame.containsKey(nickname);
    }

    public void addConnectedPlayer(Game game, Player player) {
        connectedPlayers.get(game).add(player);
    }

    public void addNicktoGame(String nickname, Game game) {
        nickToGame.put(nickname, game);
    }

    public void disconnect(String nickname) {
        Game g = nickToGame.get(nickname);
        Player p = g.getPlayer(nickname);
        if(!disconnectedPlayers.containsKey(g)) disconnectedPlayers.put(g, new ArrayList<>());
        disconnectedPlayers.get(g).add(p);
        connectedPlayers.get(g).remove(p);
        g.disconnect(p);
    }

}
