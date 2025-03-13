package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.enumerations.PlayerState;
import it.polimi.ingsw.cg04.model.ships.Ship;
import it.polimi.ingsw.cg04.model.tiles.Tile;

import java.util.List;

public class Player {

    private final String name;
    private final PlayerColor color;
    private PlayerState state;
    private int loops = 0;
    private int position = 0;
    private int numCredits = 0;
    private Ship ship;
    private final Game game;
    private FlightBoard flightBoard;
    private Tile heldTile;

    public Player(String name, PlayerColor color, Game game) {
        this.name = name;
        this.color = color;
        this.state = PlayerState.IN_LOBBY;
        this.game = game;
        this.flightBoard = this.game.getBoard();
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }

    public PlayerState getState() {
        return state;
    }

    public int getLoops() {
        return loops;
    }

    public int getPosition() {
        return position;
    }

    public int getNumCredits() {
        return numCredits;
    }

    public Ship getShip() {
        return ship;
    }

    public void addCredits(int credits) {
        numCredits += credits;
    }

    public void removeCredits(int credits) {
        numCredits -= credits;
    }

    public void addLoop() {
        loops++;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void chooseFaceUpTile(int index) {
        // todo

        // List<Tile> faceUpTiles = flightBoard.getFaceUpTiles();

        // todo: ask user which he would like to pick
        int selection = -1;

        // heldTile = faceUpTiles.remove(selection);

    }

    public Tile pickFaceDownTile() {
        // todo
        return null;
    }

    public void returnTile() {
        // todo
    }

    public boolean bufferTile() {
        // todo
        assert (ship.getTilesBuffer().size() <= 2);
        if (ship.getTilesBuffer().size() >= 2) {
            return false;
        }

        ship.addTileInBuffer(heldTile);
        heldTile = null;
        return true;
    }

    public boolean placeTile(Tile tile, int x, int y) {
        // todo
        boolean validPlacement = ship.placeTile(heldTile, x, y);

        if (validPlacement) {
            heldTile = null;
            return true;
        }

        return false;
    }

    public void move(int steps) {

    }


}
