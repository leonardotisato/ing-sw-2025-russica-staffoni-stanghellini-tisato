package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
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

    private Integer currentCell = 0;
    private int loopsCompleted = 0;
    private int numCredits = 0;

    private Tile heldTile;

    public Player(String name, PlayerColor color, Game game) {
        this.name = name;
        this.color = color;
        this.state = PlayerState.IN_LOBBY;
        this.game = game;
        this.flightBoard = this.game.getBoard();
        this.ship = new Ship(game.getLevel(), this.color);
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
        return currentCell + (flightBoard.getPathSize() * loopsCompleted);
    }

    // update position by positive or negative delta
    public void move(int delta) {
        currentCell = flightBoard.move(this, delta);
    }

    public int getLoops() {
        return loopsCompleted;
    }

    public void addLoop() {
        loopsCompleted++;
    }

    public void removeLoop() {
        loopsCompleted--;
    }

    public int getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(int currentCell) {
        this.currentCell = currentCell;
    }

    public int getNumCredits() {
        return numCredits;
    }

    public void updateCredits(int delta) {
        if (numCredits + delta < 0) {
            numCredits = 0;
        } else {
            numCredits += delta;
        }
    }

    public Tile getHeldTile() {
        return heldTile;
    }

    public void setHeldTile(Tile t) {
        heldTile = t;
    }

    public void chooseFaceUpTile(int index) {
        // todo

        // List<Tile> faceUpTiles = flightBoard.getFaceUpTiles();

        // todo: ask user which he would like to pick
        int selection = -1;

        // heldTile = faceUpTiles.remove(selection);

    }

    public Integer returnTile() {

        if (heldTile == null) {
            throw new RuntimeException("No tile are currently held");
        }

        Integer tileId = heldTile.getId();
        heldTile = null;
        return tileId;
    }

    public boolean bookTile() {

        if (ship.getTilesBuffer().size() >= 2) {
            throw new RuntimeException("Buffer is full!");
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

        System.out.println("Place tile failed");
        return false;
    }


    public void chooseBookedTile(int idx) {
        heldTile = ship.takeFromTileBuffer(idx);
    }

    // todo: secondo me non va nel model
    public void showFaceUpTiles() {
    }

    // todo: secondo me non va nel model
    public void showPile(int idx) {
    }

    // todo: secondo me non va nel model
    public void returnPile() {
    }

    // todo: secondo me non va nel model ?
    public void loadResource(int x, int y, BoxType box) {
        if (!(ship.getTile(x, y) instanceof StorageTile)) {
            throw new RuntimeException("Illegal Operation! Not a StorageTile!");
        }
        ship.addBox(box, x, y);
    }

    // todo: secondo me non va nel model ?
    public void removeResource(int x, int y, BoxType box) {
        if (!(ship.getTile(x, y) instanceof StorageTile)) {
            throw new RuntimeException("Illegal Operation! Not a StorageTile!");
        }
        ship.removeBox(box, x, y);
    }

    // todo: secondo me non va nel model ?
    public void removeCrew(int x, int y) {
        if (!(ship.getTile(x, y) instanceof HousingTile)) {
            throw new RuntimeException("Illegal Operation! Not a HousingTile!");
        }

        // removes crew member from ship and tile
        ship.removeCrewByType(ship.getTile(x, y).removeCrewMember());
    }

    public void addCrewByType(CrewType crewType, int x, int y) {
        if (!(ship.getTile(x, y) instanceof HousingTile)) {
            throw new RuntimeException("Illegal Operation! Not a HousingTile!");
        }

        // removes crew member from ship and tile
        ship.getTile(x, y).addCrew(crewType);
        ship.addCrewByType(crewType);
    }

    public void useBattery(int x, int y) {

    }
}
