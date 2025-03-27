package it.polimi.ingsw.cg04.controller;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.GameState;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.enumerations.PlayerState;

import java.util.List;
import java.util.Map;

public class GamesHandler {

    private List<Game> games;
    private Map<Game, List<Player>> connectedPlayers;
    private Map<Game, List<Player>> disconnectedPlayers;

    public GamesHandler() {}

    public List<Game> getGames() { return games; }

    public void updateCredits(Game game, Player player, int credits) {
        game.updateCredits(player, credits);
    }

    public void placeTile(Game game, Player player, int x, int y) {
        game.placeTile(player, x, y);
    }

    public void bookTile(Game game, Player player) {
        game.bookTile(player);
    }

    public void pickFaceDownTile(Game game, Player player) {
        game.pickFaceDownTile(player);
    }

    public void returnTile(Game game, Player player) {
        game.returnTile(player);
    }

    public void chooseFaceUpTile(Game game, Player player, int listIdx) {
        game.chooseFaceUpTile(player, listIdx);
    }

    public void chooseBookedTile(Game game, Player player, int idx) {
        game.chooseBookedTile(player, idx);
    }

    public void showFaceUpTiles(Game game, Player player) {
        game.showFaceUpTiles(player);
    }

    public void showPile(Game game, Player player, int idx) {
        game.showPile(player, idx);
    }

    public void returnPile(Game game, Player player) {
        game.returnPile(player);
    }

    public void checkShips(Game game) {
        game.checkShips();
    }

    public void loadResource(Game game, Player player, int x, int y, BoxType box) {
        game.loadResource(player, x, y, box);
    }

    public void removeResource(Game game, Player player, int x, int y, BoxType box) {
        game.removeResource(player, x, y, box);
    }

    public void removeCrew(Game game, Player player, int x, int y) {
        game.removeCrew(player, x, y);
    }

    public void useBattery(Game game, Player player, int x, int y) {
        game.useBattery(player, x, y);
    }

    public void movePlayer(Game game, Player player, int steps) {
        game.movePlayer(player, steps);
    }

    public void setPlayerState(Game game, Player player, PlayerState state) {
        game.setPlayerState(player, state);
    }

    public void setGameState(Game game, GameState state) {
        game.setGameState(state);
    }

    public void beginGame(Game game) {
        game.beginGame();
    }

    public void endGame(Game game) {
        game.endGame();
    }


    public void createGame(int level) {
        // todo
    }

    public void reconnect(Game game, Player player) {
        // todo
    }

    public void disconnect(Game game, Player player) {
        // todo
    }

    public void addPlayer(Game game, String playerName, Player player, PlayerColor color) {
        game.addPlayer(playerName, color);
    }

}
