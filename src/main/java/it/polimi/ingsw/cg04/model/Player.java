package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {

    private final String name;
    private final PlayerColor color;
    private Ship ship;
    private final Game game;
    private final FlightBoard flightBoard;

    private Integer currentCell = 0;
    private int loopsCompleted = 0;
    private double numCredits = 0;

    private Tile heldTile;

    private boolean retired = false;

    public Player(String name, PlayerColor color, Game game) {
        this.name = name;
        this.color = color;
        this.game = game;
        this.flightBoard = this.game.getBoard();
        this.ship = new Ship(game.getLevel(), this.color);
    }

    /**
     * To be only used for TESTING PURPOSES
     */
    public void setShip(Ship ship) {
        this.ship = ship;
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
     * Returns the Unicode escape sequence for the player's color.
     * Each color is represented by a specific Unicode value for console text formatting:
     * - BLUE corresponds to "\u001B[34m"
     * - GREEN corresponds to "\u001B[32m"
     * - RED corresponds to "\u001B[31m"
     * - Other colors (default case) correspond to "\u001B[33m".
     *
     * @return a string representing the Unicode escape sequence for the player's color.
     */
    public String getColorUnicode() {
        if (color == PlayerColor.BLUE) {
            return "\u001B[34m";
        }
        if (color == PlayerColor.GREEN) {
            return "\u001B[32m";
        }
        if (color == PlayerColor.RED) {
            return "\u001B[31m";
        }
        return "\u001B[33m";
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
     * Determines and returns the player's ranking based on their position relative
     * to other players in the game.
     *
     * @return the player's ranking as a 1-based integer value. If the player's color
     *         is not found in the list of sorted players, returns -1.
     */
    public Integer getRanking() {
        List<Player> sortedPlayers = this.game.getSortedPlayers();
        for (int i = 0; i < sortedPlayers.size(); i++) {
            if (sortedPlayers.get(i).getColor() == this.color) {
                return i + 1;
            }
        }
        return -1;
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
    public double getNumCredits() {
        return numCredits;
    }

    /**
     * Updates the player's credits by a specified delta value.
     *
     * @param delta the amount to modify the player's credits by (can be positive or negative).
     */
    public void updateCredits(double delta) {
        numCredits += delta;
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
     * @throws RuntimeException if the ship's tile buffer is full.
     */
    public void addTileInBuffer() {

        if (ship.getTilesBuffer().size() >= 2) {
            throw new RuntimeException("Buffer is full!");
        }

        ship.addTileInBuffer(heldTile);
        heldTile.setWasInBuffer();
        heldTile = null;
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
        boolean validPlacement = ship.placeTile(heldTile, x, y);

        if (validPlacement) {
            heldTile = null;
            return true;
        }

        System.out.println("Place tile failed");
        return false;
    }

    /**
     *
     * @return the {@code Game} object associated with the player.
     */
    public Game getGame() {
        return game;
    }

    /**
     *
     * @return {@code true} if the player is retired; {@code false} otherwise.
     */
    public boolean isRetired() {
        return retired;
    }

    /**
     * Updates the retired status of the player.
     *
     * @param retired a boolean flag indicating whether the player is retired.
     *                {@code true} if the player is retired; {@code false} otherwise.
     */
    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    /**
     *
     * @return {@code true} if the leader has lapped the player; {@code false} otherwise.
     */
    public boolean wasLapped() {
        int leaderLoops = game.getPlayer(0).getLoops();
        int leaderCell = game.getPlayer(0).getCurrentCell();

        // lapped condition
        return leaderLoops > getLoops() && leaderCell > getCurrentCell();
    }
}
