package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    private Tile shieldTile16;

    @BeforeEach
    void setUp() {
        Map<Direction, Connection> connections16 = new HashMap<>();
        connections16.put(Direction.UP, Connection.DOUBLE);
        connections16.put(Direction.RIGHT, Connection.SINGLE);
        connections16.put(Direction.DOWN, Connection.DOUBLE);
        connections16.put(Direction.LEFT, Connection.SINGLE);

        Set<Direction> protectedDirections16 = new HashSet<>();
        protectedDirections16.add(Direction.UP);
        protectedDirections16.add(Direction.RIGHT);

        shieldTile16 = new ShieldTile(connections16, protectedDirections16);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void broken() {
    }

    @Test
    void rotate90dx() {

        Set<Direction> newProtectedDirections16 = new HashSet<>();
        newProtectedDirections16.add(Direction.RIGHT);
        newProtectedDirections16.add(Direction.DOWN);

        shieldTile16.rotate90dx();

        assertEquals (Connection.SINGLE, shieldTile16.getConnection(Direction.UP));
        assertEquals(Connection.DOUBLE, shieldTile16.getConnection(Direction.RIGHT));
        assertEquals(Connection.SINGLE, shieldTile16.getConnection(Direction.DOWN));
        assertEquals(Connection.DOUBLE, shieldTile16.getConnection(Direction.LEFT));

        assertEquals(newProtectedDirections16, shieldTile16.getProtectedDirections());
    }

    @Test
    void rotate180() {
    }

    @Test
    void rotate90sx() {
    }

    @Test
    void getConnection() {
    }

    @Test
    void isValidConnection() {
    }

    @Test
    void getProtectedDirections() {
    }

    @Test
    void getMaxBatteryCapacity() {
    }

    @Test
    void getNumBatteries() {
    }

    @Test
    void removeBatteries() {
    }

    @Test
    void isSpecialStorageTile() {
    }

    @Test
    void getMaxBoxes() {
    }

    @Test
    void getBoxes() {
    }

    @Test
    void addBox() {
    }

    @Test
    void removeBox() {
    }

    @Test
    void isDoublePropulsor() {
    }

    @Test
    void isDoubleLaser() {
    }

    @Test
    void getShootingDirection() {
    }

    @Test
    void getSupportedAlienColor() {
    }

    @Test
    void isCentralTile() {
    }

    @Test
    void getSupportedAlienColors() {
    }

    @Test
    void getNumHumans() {
    }
}