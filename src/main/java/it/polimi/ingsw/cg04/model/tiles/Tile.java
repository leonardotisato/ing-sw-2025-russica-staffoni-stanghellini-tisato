package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.AlienColor;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;


import java.util.Map;
import java.util.Set;


public abstract class Tile {
    private Map<Direction, Connection> connections;

    // todo
    // per poter sfruttare polimorfismo definisco attributi di tutte le sottoclassi di Tile

    // housingTile

    // alienSupportTile

    // batteryTile

    // propulsorTile

    // laserTile

    // storageTile

    //shieldTile

    // clockwise rotation of the Tile
    public void rotate90dx() {
        Connection temp = connections.get(Direction.UP);

        connections.put(Direction.UP, connections.get(Direction.LEFT));
        connections.put(Direction.LEFT, connections.get(Direction.DOWN));
        connections.put(Direction.DOWN, connections.get(Direction.RIGHT));
        connections.put(Direction.RIGHT, temp);
    }

    public void rotate180() {
        rotate90dx();
        rotate90dx();
    }

    // counterclockwise rotation of the Tile
    public void rotate90sx() {
        rotate90dx();
        rotate90dx();
        rotate90dx();
    }

    // todo
}
