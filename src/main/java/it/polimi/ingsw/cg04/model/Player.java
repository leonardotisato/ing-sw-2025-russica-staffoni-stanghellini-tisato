package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.enumerations.PlayerState;
import it.polimi.ingsw.cg04.model.tiles.HousingTile;
import it.polimi.ingsw.cg04.model.tiles.StorageTile;
import it.polimi.ingsw.cg04.model.tiles.Tile;

public class Player {

    private final String name;
    private final PlayerColor color;
    private PlayerState state;
    private Ship ship;
    private final Game game;
    private final FlightBoard flightBoard;

    private int position = 0;
    private int numCredits = 0;

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

    public void setState(PlayerState state) {
        this.state = state;
    }

    public Ship getShip() {
        return ship;
    }


    public int getPosition() {
        return position;
    }

    // update position by positive or negative delta
    // todo: this method does not consider the fact that if other player occupies a cell in the path that cell doesnt count
    public void move(int delta) {
        position += delta;
    }

    public int getLoops() {
        return position / flightBoard.getPathSize();
    }

    public int getCurrentCell() {
        return position % flightBoard.getPathSize();
    }

    public int getNumCredits() {
        return numCredits;
    }

    public void updateCredits(int delta) {
        numCredits += delta;
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

    public boolean bookTile() {
        // todo
        assert (ship.getTilesBuffer().size() <= 2);
        if (ship.getTilesBuffer().size() >= 2) {
            return false;
        }

        ship.addTileInBuffer(heldTile);
        heldTile = null;
        return true;
    }

    public boolean placeTile(int x, int y) {
        // todo
        boolean validPlacement = ship.placeTile(heldTile, x, y);

        if (validPlacement) {
            heldTile = null;
            return true;
        }

        return false;
    }


    public void chooseBookedTile(int idx) {
    }

    public void showFaceUpTile() {
    }

    public void showPile(int idx) {
    }

    public void returnPile() {
    }

    // load resources both in tile and ship maps
    public void loadResource(int x, int y, BoxType box) {
        if(!(ship.getTile(x, y) instanceof StorageTile)) {
            throw new RuntimeException("Illegal Operation! Not a StorageTile!");
        }
        ship.getTile(x, y).addBox(box);
    }

    // todo: rivedere logica
    public void removeResource(int x, int y, BoxType box) {
        if(!(ship.getTile(x, y) instanceof StorageTile)) {
            throw new RuntimeException("Illegal Operation! Not a StorageTile!");
        }
        ship.getTile(x, y).removeBox(box);
    }

    // todo: rivedere logica
    public void removeCrew(int x, int y) {
        if(!(ship.getTile(x, y) instanceof HousingTile)) {
            throw new RuntimeException("Illegal Operation! Not a HousingTile!");
        }
        ship.getTile(x, y).removeCrewMember();
    }

    public void useBattery(int x, int y) {

    }
}
