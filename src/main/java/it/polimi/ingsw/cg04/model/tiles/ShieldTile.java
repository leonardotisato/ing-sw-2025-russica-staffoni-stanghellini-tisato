package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.ships.Ship;

import java.util.*;

public class ShieldTile extends Tile {

    private Set<Direction> protectedDirections;

    public ShieldTile(Map<Direction, Connection> connectionMap, Set<Direction> protectedDirs) {
        super(connectionMap);
        this.protectedDirections = protectedDirs;
    }

    @Override
    public Set<Direction> getProtectedDirections() {
        return protectedDirections;
    }

    @Override
    public void rotate90dx() {

        // rotate connections
        super.rotate90dx();

        // handle protected directions
        Map<Direction, Direction> rotationMap = Map.of(
                Direction.UP, Direction.RIGHT,
                Direction.RIGHT, Direction.DOWN,
                Direction.DOWN, Direction.LEFT,
                Direction.LEFT, Direction.UP
        );

        Set<Direction> newProtectedDirections = new HashSet<Direction>();
        for (Direction dir : protectedDirections) {
            newProtectedDirections.add(rotationMap.get(dir));
        }
        protectedDirections = newProtectedDirections;
    }

    @Override
    public void rotate180() {
        this.rotate90dx();
        this.rotate90dx();
    }

    @Override
    public void rotate90sx() {
        this.rotate90dx();
        this.rotate90dx();
        this.rotate90dx();
    }

    @Override
    public void broken(Ship ship) {
        ship.removeProtectedDirections(getProtectedDirections());
    }

}
