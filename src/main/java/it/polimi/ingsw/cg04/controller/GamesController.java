package it.polimi.ingsw.cg04.controller;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
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

    public List<Game> getGames() { return games; }

    public void onActionReceived(PlayerAction action) {
        Game g = nickToGame.get(action.getPlayerNickname());
        Player p = g.getPlayer(action.getPlayerNickname());
        if (action.checkAction(p)){
            action.execute(p);
        }
        else {
            // this makes controller hard fail! ideally if check fails user should be warned
            throw new RuntimeException("Wrong parameters!");
        }
    }

    public void addGame(Game game) {
        games.add(game);
        for (Player p : game.getPlayers()) {
            nickToGame.put(p.getName(), game);
        }
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
