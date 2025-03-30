package it.polimi.ingsw.cg04.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class FlightBoard {
    protected Player[] path;
    protected int pathSize;
    protected Map<Integer, Integer> startingPosition;
    protected Bank bank;
    protected Map<Integer, Integer> endGameCredits;
    protected int mostBeautifulShipCredits;
    // for testing purposes
    protected final Random rand = new Random();

    public FlightBoard() {
    }

    /**
     * Returns the total number of cells in the flight path.
     *
     * @return the size of the path.
     */
    public int getPathSize() {
        return pathSize;
    }

    /**
     * Returns the array representing the flight path.
     * Each index in the array corresponds to a cell in the path and may contain a Player occupying that position.
     *
     * @return an array of Players representing the path.
     */
    public Player[] getPath() {
        return path;
    }

    /**
     * Retrieves the player currently occupying a specific cell in the path.
     *
     * @param idx the index of the cell in the path.
     * @return the Player at the specified index, or {@code null} if the cell is unoccupied.
     * @throws ArrayIndexOutOfBoundsException if the index is out of range.
     */
    public Player getCell(int idx) {
        if (idx < 0 || idx >= path.length) {
            throw new ArrayIndexOutOfBoundsException("Index " + idx + " is out of bounds for path of size " + path.length);
        }
        return path[idx];
    }

    /**
     * Retrieves the starting position of a player given the idx representing order of ship completion
     * <p>
     * E.g. first player to finish building will start from getStartingPosition(1)
     *
     * @param idx the index in the starting position list.
     * @return the starting position of the player.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public int getStartingPosition(int idx) {
        if (idx < 0 || idx > 4) {
            throw new IndexOutOfBoundsException("Index " + idx + " is out of bounds for starting positions list of size " + startingPosition.size());
        }
        return startingPosition.get(idx);
    }


    /**
     * Places a player at a specific cell in the flight path.
     * <p>
     * The cell index is wrapped around using modulo arithmetic to ensure it remains within the valid range.
     * The player's current cell is updated accordingly.
     *
     * @param cell   the target cell index.
     * @param player the player to place in the cell.
     */
    public void occupyCell(int cell, Player player) {
        int mapIndex = cell % pathSize;
        path[mapIndex] = player;
        player.setCurrentCell(mapIndex);
    }

    /**
     * Frees a specific cell in the flight path, making it unoccupied.
     * <p>
     * The cell index is wrapped around using modulo arithmetic to ensure it remains within the valid range.
     *
     * @param cell the index of the cell to free.
     */
    public void freeCell(int cell) {
        int mapIndex = cell % pathSize;
        path[mapIndex] = null;
    }

    /**
     * Moves the player along the path by a specified number of steps, wrapping around the path when necessary.
     * If the player completes a full loop of the path, the loop counter is updated accordingly.
     *
     * @param player The player object to be moved.
     * @param delta  The number of steps the player should move. A positive value moves the player forward,
     *               while a negative value moves the player backward. The path wraps around when the player
     *               moves past the end or beginning of the path.
     * @return The new cell index that the player occupies after the move.
     * @throws RuntimeException If the player's current cell is invalid or null in the path.
     */
    public int move(Player player, int delta) {
        int oldPlayerCell = player.getCurrentCell();
        int newCell = oldPlayerCell;
        int stepsTaken = 0;


        if (Arrays.stream(path).toList().contains(player)) {
            if (Arrays.stream(path).toList().indexOf(player) != oldPlayerCell) {
                throw new RuntimeException("Player vs Board cell mismatch");
            }
        }

        if (delta > 0) {
            while (stepsTaken < delta) {
                newCell = (newCell + 1) % pathSize;
                if (path[newCell] == null) {
                    stepsTaken++;
                }
            }

            if (newCell <= oldPlayerCell) {
                player.addLoop();
            }

        } else if (delta < 0) {
            while (stepsTaken > delta) {
                newCell = (newCell - 1 + pathSize) % pathSize;
                if (path[newCell] == null) {
                    stepsTaken--;
                }
            }

            if (newCell >= oldPlayerCell) {
                player.removeLoop();
            }
        }

        freeCell(oldPlayerCell);
        occupyCell(newCell, player);

        return newCell;
    }

    /**
     * @return The number of credits associated with the most beautiful ship.
     */
    public int getMostBeautifulShipCredits() {
        return mostBeautifulShipCredits;
    }

    // timer methods for lev2 board

    /**
     * Starts the timer with a fixed duration of 5 seconds.
     */
    public void startTimer() {
    }

    /**
     * Checks whether the timer has expired.
     *
     * @return {@code true} if the current system time is greater than or equal to the timer end time, {@code false} otherwise.
     */
    public boolean isTimerExpired() {
        return false;
    }

    /**
     * Attempts to flip the timer if it has expired and the maximum number of flips has not been exceeded.
     *
     * @return {@code true} if the timer was successfully flipped, {@code false} otherwise.
     */
    public boolean flipTimer() {
        return false;
    }

    /**
     * Gets the number of times the timer has been flipped.
     *
     * @return the number of timer flips used.
     */
    public int getTimerFlipsUsed() {
        return 0;
    }

    /**
     * Gets the remaining number of flips allowed.
     *
     * @return the number of remaining timer flips.
     */
    public int getTimerFlipsRemaining() {
        return 0;
    }

    /**
     * Gets the remaining time before the timer expires.
     *
     * @return the remaining time in milliseconds, or 0 if the timer has expired.
     */
    public long getRemainingTime() {
        return 0;
    }

    public List<Integer> createAdventureCardsDeck(Game game) { return null; }

    @Override
    public String toString() {
        return "FlightBoard{" +
                "pathSize=" + pathSize +
                ", path=" + Arrays.stream(path)
                .map(cell -> cell == null ? "null" : cell.getName())
                .collect(Collectors.joining(", ", "[", "]")) +
                '}';
    }
}
