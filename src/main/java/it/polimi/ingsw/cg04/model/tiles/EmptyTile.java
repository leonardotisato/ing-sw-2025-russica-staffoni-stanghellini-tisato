package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;

import java.util.HashMap;

public class EmptyTile extends Tile {

    /**
     * Constructs an EmptyTile object with specified row and column coordinates.
     * Used to render ship's tile matrix
     *
     * @param row the row coordinate of the tile
     * @param column the column coordinate of the tile
     */
    public EmptyTile(int row, int column) {
        super();
        this.shortName = row + " " + column;
        this.connections = new HashMap<>();
        for (Direction d : Direction.values()) {
            connections.put(d, Connection.EMPTY);
        }
    }


    /**
     * Constructs an EmptyTile object to serve as bufferTile during ship construction render
     *
     * @param index the integer index used to create a unique identifier for the buffer's short name
     */
    public EmptyTile(int index) {
        super();
        this.shortName = "Buff: " + index;
        this.connections = new HashMap<>();
        for (Direction d : Direction.values()) {
            connections.put(d, Connection.EMPTY);
        }
    }
}
