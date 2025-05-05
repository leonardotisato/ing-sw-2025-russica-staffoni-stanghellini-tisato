package it.polimi.ingsw.cg04.model.tiles;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.Ship;

import java.util.*;

public class ShieldTile extends Tile {

    @Expose
    private Set<Direction> protectedDirections;

    public ShieldTile() {
        super();
        this.shortName = "Shield";
        this.tileColor = "\u001B[34m";
    }

    @Override
    public Set<Direction> getProtectedDirections() {
        return protectedDirections;
    }

    public void setProtectedDirections(Set<Direction> protectedDirections) {
        this.protectedDirections = protectedDirections;
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

        Set<Direction> newProtectedDirections = new HashSet<>();
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

    /**
     * removes this tile {@code protectedDirections} from ship attribute {@code protectedDirections}
     *
     * @param ship
     * @param x
     * @param y
     */
    @Override
    public void broken(Ship ship, int x, int y) {
        ship.removeProtectedDirections(getProtectedDirections());
    }

    /**
     * adds this tile {@code protectedDirections} to ship attribute {@code protectedDirections}
     *
     * @param ship
     * @param x
     * @param y
     */
    @Override
    public void place(Ship ship, int x, int y) {
        ship.addProtectedDirections(getProtectedDirections());
    }
}
