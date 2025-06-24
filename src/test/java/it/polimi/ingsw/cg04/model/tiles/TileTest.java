package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.utils.TileLoader;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    private Tile shieldTile151;
    private Tile batteryTile5;
    private Tile storageTile26;
    private Tile laserTile134;
    private Tile laserTile123;
    private Tile propulsorTile71;
    private Tile strucutralTile56;
    private Tile batteryTile13;

    @BeforeEach
    void setUp() {

        // new tests using the tileLoader
        ArrayList<Integer> faceDownTiles = new ArrayList<>();
        Map<Integer, Tile> tiles = TileLoader.loadTilesFromJson("jsons/TilesFile.json", faceDownTiles);

        assertNotNull(tiles);
        assertFalse(tiles.isEmpty());

        shieldTile151 = tiles.get(151);
        batteryTile5 = tiles.get(5);
        storageTile26 = tiles.get(26);
        laserTile134 = tiles.get(134);
        laserTile123 = tiles.get(123);
        propulsorTile71 = tiles.get(71);
        strucutralTile56 = tiles.get(56);
        batteryTile13 = tiles.get(13);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void broken() {
    }

    @Test
    void rotate() {

        // check tile151
        Set<Direction> newProtectedDirections151 = new HashSet<>();
        newProtectedDirections151.add(Direction.RIGHT);
        newProtectedDirections151.add(Direction.DOWN);

        shieldTile151.rotate90dx();

        assertEquals(1, shieldTile151.getRotation());

        assertEquals(Connection.SINGLE, shieldTile151.getConnection(Direction.UP));
        assertEquals(Connection.DOUBLE, shieldTile151.getConnection(Direction.RIGHT));
        assertEquals(Connection.SINGLE, shieldTile151.getConnection(Direction.DOWN));
        assertEquals(Connection.DOUBLE, shieldTile151.getConnection(Direction.LEFT));

        assertEquals(newProtectedDirections151, shieldTile151.getProtectedDirections());

        shieldTile151.rotate90dx();
        assertEquals(2, shieldTile151.getRotation());

        shieldTile151.rotate90dx();
        assertEquals(3, shieldTile151.getRotation());

        shieldTile151.rotate90dx();
        assertEquals(0, shieldTile151.getRotation());

        shieldTile151.rotate90dx();
        assertEquals(1, shieldTile151.getRotation());

        shieldTile151.rotate90sx();
        assertEquals(0, shieldTile151.getRotation());

        shieldTile151.rotate180();
        assertEquals(2, shieldTile151.getRotation());

        shieldTile151.rotate90sx();
        assertEquals(1, shieldTile151.getRotation());

        shieldTile151.rotate180();
        assertEquals(3, shieldTile151.getRotation());

        // check tile5
        batteryTile5.rotate90dx();
        assertEquals(Connection.SINGLE, batteryTile5.getConnection(Direction.UP));
        assertEquals(Connection.UNIVERSAL, batteryTile5.getConnection(Direction.RIGHT));
        assertEquals(Connection.EMPTY, batteryTile5.getConnection(Direction.DOWN));
        assertEquals(Connection.EMPTY, batteryTile5.getConnection(Direction.LEFT));
    }

    @Test
    void getConnection() {
        assertEquals(Connection.DOUBLE, shieldTile151.getConnection(Direction.UP));
        assertEquals(Connection.SINGLE, shieldTile151.getConnection(Direction.RIGHT));
        assertEquals(Connection.DOUBLE, shieldTile151.getConnection(Direction.DOWN));
        assertEquals(Connection.SINGLE, shieldTile151.getConnection(Direction.LEFT));
    }

    @Test
    void isValidConnection() {
        // shieldTile151 vs shieldTile151
        assertTrue(shieldTile151.isValidConnection(Direction.RIGHT, shieldTile151));
        assertTrue(shieldTile151.isValidConnection(Direction.DOWN, shieldTile151));
        assertTrue(shieldTile151.isValidConnection(Direction.UP, shieldTile151));
        assertTrue(shieldTile151.isValidConnection(Direction.LEFT, shieldTile151));

        // shieldTile151 vs batteryTile5
        assertFalse(batteryTile5.isValidConnection(Direction.DOWN, shieldTile151));
        assertFalse(shieldTile151.isValidConnection(Direction.UP, batteryTile5));
        assertFalse(shieldTile151.isValidConnection(Direction.LEFT, batteryTile5));
        assertTrue(shieldTile151.isValidConnection(Direction.RIGHT, batteryTile5));
        assertTrue(batteryTile5.isValidConnection(Direction.LEFT, shieldTile151));

        storageTile26.rotate90dx();
        assertTrue(batteryTile5.isValidConnection(Direction.RIGHT, storageTile26));

        // check connection vs laser gun
        assertFalse(laserTile123.isValidConnection(Direction.UP, storageTile26));
        assertFalse(laserTile134.isValidConnection(Direction.UP, storageTile26));
        assertFalse(storageTile26.isValidConnection(Direction.DOWN, laserTile123));
        assertFalse(laserTile134.isValidConnection(Direction.UP, laserTile123));

        // check connection propulsor on top of other tile
        assertFalse(propulsorTile71.isValidConnection(Direction.DOWN, storageTile26));
        assertFalse(storageTile26.isValidConnection(Direction.UP, propulsorTile71));


    }

    @Test
    void getProtectedDirections() {
        assertNull(batteryTile5.getProtectedDirections());
        assertNotNull(shieldTile151.getProtectedDirections());
    }

    @Test
    void getMaxBatteryCapacity() {
        assertNull(shieldTile151.getMaxBatteryCapacity());
    }

    @Test
    void getNumBatteries() {
        assertNull(shieldTile151.getNumBatteries());
        assertEquals(2, batteryTile5.getNumBatteries());
    }

    @Test
    void removeBatteries() {
        batteryTile5.removeBatteries(1);
        assertEquals(1, batteryTile5.getNumBatteries());
        batteryTile5.removeBatteries(1);
        assertEquals(0, batteryTile5.getNumBatteries());
        assertThrows(IllegalArgumentException.class, () -> batteryTile5.removeBatteries(-1));
        assertThrows(RuntimeException.class, () -> batteryTile5.removeBatteries(2));
        assertEquals(0, batteryTile5.getNumBatteries());

        // try to removeBattery on non batteryTiles
        assertThrows(RuntimeException.class, () -> shieldTile151.removeBatteries(3));
    }

    @Test
    void isSpecialStorageTile() {
        assertNull(shieldTile151.isSpecialStorageTile());
        assertNull(batteryTile5.isSpecialStorageTile());
        assertFalse(storageTile26.isSpecialStorageTile());
    }

    @Test
    void getMaxBoxes() {
        assertNull(shieldTile151.getMaxBoxes());
        assertNull(batteryTile5.getMaxBoxes());
        assertEquals(2, storageTile26.getMaxBoxes());
    }

    @Test
    void getBoxes() {
        assertNull(shieldTile151.getBoxes());
        assertNull(batteryTile5.getBoxes());

        assertNotNull(storageTile26.getBoxes());
    }

    @Test
    void addBox() {
        // try to add special box into normal storage
        assertThrows(RuntimeException.class, () -> storageTile26.addBox(BoxType.RED, 1));

        storageTile26.addBox(BoxType.BLUE, 1);
        assertEquals(4, storageTile26.getBoxes().size());
        assertEquals(1, storageTile26.getBoxes().get(BoxType.BLUE));

        storageTile26.addBox(BoxType.GREEN, 1);
        assertEquals(4, storageTile26.getBoxes().size());
        assertEquals(1, storageTile26.getBoxes().get(BoxType.GREEN));

        // check if maxCapacity exceeded works
        assertThrows(RuntimeException.class, () -> storageTile26.addBox(BoxType.YELLOW, 1));
    }

    @Test
    void removeBox() {
        assertThrows(RuntimeException.class, () -> storageTile26.removeBox(BoxType.BLUE, 1));
        assertThrows(RuntimeException.class, () -> storageTile26.removeBox(BoxType.GREEN, 1));
        assertThrows(RuntimeException.class, () -> storageTile26.removeBox(BoxType.RED, 1));
        assertThrows(RuntimeException.class, () -> storageTile26.removeBox(BoxType.YELLOW, 1));

        storageTile26.addBox(BoxType.GREEN, 1);
        assertThrows(RuntimeException.class, () -> storageTile26.removeBox(BoxType.BLUE, 1));
        assertEquals(1, storageTile26.getBoxes().get(BoxType.GREEN));
        storageTile26.removeBox(BoxType.GREEN, 1);
        assertEquals(4, storageTile26.getBoxes().size());
        assertEquals(0, storageTile26.getBoxes().get(BoxType.BLUE));
        assertEquals(0, storageTile26.getBoxes().get(BoxType.GREEN));

        storageTile26.addBox(BoxType.BLUE, 1);
        storageTile26.addBox(BoxType.YELLOW, 1);
        assertThrows(RuntimeException.class, () -> storageTile26.removeBox(BoxType.GREEN, 1));
        assertEquals(1, storageTile26.getBoxes().get(BoxType.BLUE));
        storageTile26.removeBox(BoxType.YELLOW, 1);
        assertEquals(1, storageTile26.getBoxes().get(BoxType.BLUE));
        assertEquals(0, storageTile26.getBoxes().get(BoxType.GREEN));
        assertEquals(0, storageTile26.getBoxes().get(BoxType.YELLOW));
    }

    @Test
    void isDoublePropulsor() {
        assertNull(shieldTile151.isDoublePropulsor());
    }

    @Test
    void isDoubleLaser() {
        assertNull(shieldTile151.isDoubleLaser());
        assertTrue(laserTile134.isDoubleLaser());
        assertFalse(laserTile123.isDoubleLaser());
    }

    @Test
    void getShootingDirection() {
        assertNull(shieldTile151.getShootingDirection());
        assertNull(batteryTile5.getShootingDirection());
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

    @Test
    void testToString() {

        // not real tests. just making sure it prints all and correct info.
        System.out.println(shieldTile151.toString());
        System.out.println(batteryTile5.toString());
        System.out.println(storageTile26.toString());
        System.out.println(laserTile134.toString());
        System.out.println(laserTile123.toString());
        System.out.println(storageTile26.toString());
    }

    @Test
    void getId() {
        assertEquals(151, shieldTile151.getId());
        assertEquals(5, batteryTile5.getId());
        assertEquals(26, storageTile26.getId());
        assertEquals(123, laserTile123.getId());
    }

    @Test
    void drawTest() {
        System.out.println(strucutralTile56.draw());
        System.out.println(batteryTile5.draw());
        System.out.println(storageTile26.draw());
        storageTile26.rotate90dx();
        storageTile26.addBox(BoxType.BLUE, 1);
        storageTile26.addBox(BoxType.GREEN, 1);
        System.out.println(storageTile26.draw());
        System.out.println(laserTile134.draw());
        System.out.println(propulsorTile71.draw());
        propulsorTile71.rotate90dx();
        System.out.println(propulsorTile71.draw());
        System.out.println(shieldTile151.draw());
        System.out.println(batteryTile13.draw());
    }

    @Test
    void testTile() {
        storageTile26.removeCrewMember();
        storageTile26.getHostedCrewType();
        storageTile26.addCrew(CrewType.PINK_ALIEN);
        storageTile26.removeSupportedCrewType(CrewType.PINK_ALIEN);
        storageTile26.addSupportedCrewType(CrewType.PINK_ALIEN);
        storageTile26.updateSupportedCrewTypes();
        storageTile26.removeAdjacentHousingTile(strucutralTile56);
        storageTile26.addAdjacentHousingTile(strucutralTile56);
        assertThrows(RuntimeException.class, () -> strucutralTile56.addBox(BoxType.BLUE, 1));
        assertThrows(RuntimeException.class, () -> strucutralTile56.removeBox(BoxType.YELLOW, 1));

    }
}