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
    private final Ship ship;
    private final Game game;
    private final FlightBoard flightBoard;

    private Integer currentCell = 0;
    private int loopsCompleted = 0;
    private int numCredits = 0;

    private Tile heldTile;

    public Player(String name, PlayerColor color, Game game) {
        this.name = name;
        this.color = color;
        this.state = PlayerState.LOBBY;
        this.game = game;
        this.flightBoard = this.game.getBoard();
        this.ship = new Ship(game.getLevel(), this.color);
    }

    /**
     * @return the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the color of the player.
     */
    public PlayerColor getColor() {
        return color;
    }

    /**
     * @return the {@code PlayerState} of the player.
     */
    public PlayerState getState() {
        return state;
    }

    /**
     * Sets the current state of the player.
     *
     * @param state the new {@code PlayerState} to set for the player.
     */
    public void setState(PlayerState state) {
        this.state = state;
    }

    /**
     * @return the {@code Ship} object associated with the player.
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * Calculates and returns the player's position on the flight board.
     * The position is calculated as the sum of the current cell and the number of loops completed timed by the path length
     *
     * @return the player's current position as an integer.
     */
    public int getPosition() {
        return currentCell + (flightBoard.getPathSize() * loopsCompleted);
    }

    /**
     * Moves the player by a specified delta.
     * The delta value can be positive or negative, and it updates the player's current cell on the flight board.
     *
     * @param delta the change in position (positive for forward movement, negative for backward).
     */
    public void move(int delta) {
        currentCell = flightBoard.move(this, delta);
    }

    /**
     * @return the number of loops completed.
     */
    public int getLoops() {
        return loopsCompleted;
    }

    /**
     * Increments the number of loops completed by the player by one.
     */
    public void addLoop() {
        loopsCompleted++;
    }

    /**
     * Decrements the number of loops completed by the player by one.
     */
    public void removeLoop() {
        loopsCompleted--;
    }

    /**
     * @return the index of the current cell the player occupies.
     */
    public int getCurrentCell() {
        return currentCell;
    }


    /**
     * Sets the player's current cell on the flight board.
     *
     * @param currentCell the new current cell index for the player.
     */
    public void setCurrentCell(int currentCell) {
        this.currentCell = currentCell;
    }

    /**
     * @return the number of credits currently possessed by the player
     */
    public int getNumCredits() {
        return numCredits;
    }

    /**
     * Updates the player's credits by a specified delta value.
     * If the resulting credit count is less than zero, the credits will be set to zero.
     *
     * @param delta the amount to modify the player's credits by (can be positive or negative).
     */
    public void updateCredits(int delta) {
        if (numCredits + delta < 0) {
            numCredits = 0;
        } else {
            numCredits += delta;
        }
    }

    /**
     * @return the {@code Tile} currently held by the player.
     */
    public Tile getHeldTile() {
        return heldTile;
    }

    /**
     * Sets the tile that the player is currently holding.
     *
     * @param t the new {@code Tile} to be held by the player.
     */
    public void setHeldTile(Tile t) {
        heldTile = t;
    }

    /**
     * Returns the tile currently held by the player.
     * <p>
     * If the player is not holding any tile, this method throws an exception.
     * The held tile is removed after this method is called, and the method returns its ID.
     *
     * @return the ID of the tile currently held by the player.
     * @throws RuntimeException if no tile is currently held.
     */
    public Integer returnTile() {

        if (heldTile == null) {
            throw new RuntimeException("No tile are currently held");
        }

        Integer tileId = heldTile.getId();
        heldTile = null;
        return tileId;
    }

    /**
     * Books the tile currently held by the player, adding it to the ship's buffer.
     * <p>
     * If the ship's tile buffer is full (contains 2 tiles), an exception is thrown.
     * The held tile is added to the buffer and removed from the player's possession.
     *
     * @return {@code true} if the tile is successfully booked; otherwise, an exception is thrown.
     * @throws RuntimeException if the ship's tile buffer is full.
     */
    public boolean bookTile() {

        if (ship.getTilesBuffer().size() >= 2) {
            throw new RuntimeException("Buffer is full!");
        }

        ship.addTileInBuffer(heldTile);
        heldTile = null;
        return true;
    }


    /**
     * Attempts to place the tile currently held by the player at the specified coordinates (x, y).
     * <p>
     * The method delegates the placement to the ship's placeTile method. If the placement is valid, the tile
     * is removed from the player's possession and the method returns {@code true}.
     * If the placement is invalid, the tile remains held by the player, and {@code false} is returned.
     *
     * @param x the x-coordinate where the tile should be placed.
     * @param y the y-coordinate where the tile should be placed.
     * @return {@code true} if the tile is successfully placed; {@code false} otherwise.
     */
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

    /**
     * Chooses a tile from the ship's buffer to be held by the player.
     * <p>
     * The method takes the tile at the specified index from the ship's tile buffer and assigns it to the player.
     *
     * @param idx the index of the tile to choose from the buffer.
     */
    public void chooseBookedTile(int idx) {
        heldTile = ship.takeFromTileBuffer(idx);
    }

    // todo: secondo me non vanno nel model

    public void showFaceUpTiles() {
    }

    public void showPile(int idx) {
    }

    public void returnPile() {
    }

    public void loadResource(int x, int y, BoxType box) {
        if (!(ship.getTile(x, y) instanceof StorageTile)) {
            throw new RuntimeException("Illegal Operation! Not a StorageTile!");
        }
        ship.addBox(box, x, y);
    }

    public void removeResource(int x, int y, BoxType box) {
        if (!(ship.getTile(x, y) instanceof StorageTile)) {
            throw new RuntimeException("Illegal Operation! Not a StorageTile!");
        }
        ship.removeBox(box, x, y);
    }

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
