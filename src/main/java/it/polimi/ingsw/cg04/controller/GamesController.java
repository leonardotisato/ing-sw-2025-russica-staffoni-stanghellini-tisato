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

    // todo: delete me?
//    public void updateCredits(Game game, Player player, int credits) {
//        game.updateCredits(player, credits);
//    }
//
//    public void placeTile(Game game, Player player, int x, int y) {
//        game.placeTile(player, x, y);
//    }
//
//    public void bookTile(Game game, Player player) {
//        game.bookTile(player);
//    }
//
//    public void pickFaceDownTile(Game game, Player player) {
//        game.pickFaceDownTile(player);
//    }
//
//    public void returnTile(Game game, Player player) {
//        game.returnTile(player);
//    }
//
//    public void chooseFaceUpTile(Game game, Player player, int listIdx) {
//        game.chooseFaceUpTile(player, listIdx);
//    }
//
//    public void chooseBookedTile(Game game, Player player, int idx) {
//        game.chooseBookedTile(player, idx);
//    }
//
//    public void showFaceUpTiles(Game game, Player player) {
//        game.showFaceUpTiles(player);
//    }
//
//    public void showPile(Game game, Player player, int idx) {
//        game.showPile(player, idx);
//    }
//
//    public void returnPile(Game game, Player player) {
//        game.returnPile(player);
//    }
//
//    public void checkShips(Game game) {
//        game.checkShips();
//    }
//
//    public void loadResource(Game game, Player player, int x, int y, BoxType box) {
//        game.loadResource(player, x, y, box);
//    }
//
//    public void removeResource(Game game, Player player, int x, int y, BoxType box) {
//        game.removeResource(player, x, y, box);
//    }
//
//    public void removeCrew(Game game, Player player, int x, int y) {
//        game.removeCrew(player, x, y);
//    }
//
//    public void useBattery(Game game, Player player, int x, int y) {
//        game.useBattery(player, x, y);
//    }
//
//    public void movePlayer(Game game, Player player, int steps) {
//        game.movePlayer(player, steps);
//    }
//
//    public void setPlayerState(Game game, Player player, ExPlayerState state) {
//        game.setPlayerState(player, state);
//    }
//
//    public void setGameState(Game game, GameState state) {
//        game.setGameState(state);
//    }
//@Deprecated
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
