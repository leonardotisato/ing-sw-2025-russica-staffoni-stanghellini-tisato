package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;

import java.util.HashMap;

public class EmptyTile extends Tile {
    public EmptyTile(int row, int column) {
        super();
        this.shortName = row + " " + column;
        this.connections = new HashMap<>();
        for (Direction d : Direction.values()) {
            connections.put(d, Connection.EMPTY);
        }
    }
}
