package it.polimi.ingsw.cg04.controller;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.InitAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamesController {

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

    public List<Game> getGames() {
        return games;
    }

    public void onActionReceived(PlayerAction action) {

        Game g = nickToGame.get(action.getPlayerNickname());

        // handle unknown players
        if (g == null) {
            if (((InitAction) action).checkAction(this)) {
                ((InitAction) action).execute(this);
                return;
            } else {
                // this makes controller hard fail! ideally if check fails user should be warned
                throw new RuntimeException("Wrong parameters!");
            }
        }

        // handle known players
        Player p = g.getPlayer(action.getPlayerNickname());
        if (action.checkAction(p)) {
            action.execute(p);
        } else {
            // this makes controller hard fail! ideally if check fails user should be warned
            throw new RuntimeException("Wrong parameters!");
        }
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

//    public void beginGame(Game game) {
//        game.beginGame();
//    }
//
//    public void endGame(Game game) {
//        game.endGame();
//    }
//
//
//    public void createGame(int level) {
//    }
//
//    public void reconnect(Game game, Player player) {
//    }
//
//    public void disconnect(Game game, Player player) {
//    }
//
//    public void addPlayer(Game game, String playerName, Player player, PlayerColor color) {
//        game.addPlayer(playerName, color);
//    }

}
