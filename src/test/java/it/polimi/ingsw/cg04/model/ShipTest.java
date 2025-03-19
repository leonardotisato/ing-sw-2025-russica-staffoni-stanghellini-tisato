package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.TileLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    Ship lev1Ship;
    Ship lev2Ship;

    private Tile shieldTile151;
    private Tile batteryTile5;
    private Tile storageTile26;
    private Tile laserTile134;
    private Tile laserTile121;
    private Tile laserTile123;
    private Tile laserTile122;
    private Tile structuralTile58;
    private Tile propulsorTile71;
    private Tile propulsorTile97;

    @BeforeEach
    void setUp() {
        // new tests using the tileLoader
        ArrayList<Integer> faceDownTiles = new ArrayList<>();
        Map<Integer, Tile> tiles = TileLoader.loadTilesFromJson("src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json", faceDownTiles);

        assertNotNull(tiles);
        assertFalse(tiles.isEmpty());

        lev1Ship = new Ship(1, PlayerColor.BLUE);
        lev2Ship = new Ship(2, PlayerColor.RED);

        shieldTile151 = tiles.get(151);
        batteryTile5 = tiles.get(5);
        storageTile26 = tiles.get(26);
        laserTile134 = tiles.get(134); // double
        laserTile121 = tiles.get(121); // single
        laserTile122 = tiles.get(122); // single
        laserTile123 = tiles.get(123); // single
        structuralTile58 = tiles.get(58);
        propulsorTile71 = tiles.get(71); // single
        propulsorTile97 = tiles.get(97); // double
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getTilesMatrix() {
        assertNotNull(lev1Ship.getTilesMatrix());
        assertNotNull(lev2Ship.getTilesMatrix());
    }

    @Test
    void getValidSlots() {
        assertNotNull(lev1Ship.getValidSlots());
        assertNotNull(lev2Ship.getValidSlots());
    }

    @Test
    void getTile() {

    }

    @Test
    void getNumBrokenTiles() {
        assertEquals(0, lev1Ship.getNumBrokenTiles());
        assertEquals(0, lev2Ship.getNumBrokenTiles());
    }

    @Test
    void placeTile() {

        // check init params are ok
        assertEquals(0, lev1Ship.getNumCrew());
        assertEquals(0, lev1Ship.getNumBatteries());
        assertEquals(0, lev1Ship.getNumExposedConnectors());
        assertEquals(0, lev2Ship.getBaseFirePower());
        assertEquals(0, lev1Ship.getBasePropulsionPower());
        assertTrue(lev1Ship.getProtectedDirections().isEmpty());
        assertTrue(lev1Ship.getTilesBuffer().isEmpty());

        // check placement is ok
        lev1Ship.placeTile(storageTile26, 0, 0);
        lev1Ship.placeTile(batteryTile5, 0, 2);
        assertEquals(lev1Ship.getTile(0, 2), batteryTile5);
        assertThrowsExactly(IllegalArgumentException.class, () -> lev1Ship.getTile(0, 0));
        assertFalse(lev1Ship.placeTile(storageTile26, 0, 2));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> lev1Ship.placeTile(laserTile134, -1, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> lev1Ship.placeTile(laserTile134, 0, -1));


        // check resources are updated

        // battery -> check is in place + check added resources
        assertEquals(batteryTile5, lev1Ship.getTile(0, 2));
        assertEquals(batteryTile5.getMaxBatteryCapacity(), lev1Ship.getNumBatteries());

        // storage -> check is in place
        lev1Ship.placeTile(storageTile26, 1, 1);
        assertEquals(storageTile26, lev1Ship.getTile(1,1));

        // shield -> check is in place + check added protected directions
        lev1Ship.placeTile(shieldTile151, 1, 2);
        assertEquals(shieldTile151, lev1Ship.getTile(1,2));
        // list in ship and set in tile may cause problems
        assertTrue(lev1Ship.getProtectedDirections().containsAll(shieldTile151.getProtectedDirections()));

        // structural -> check is in place
        lev1Ship.placeTile(structuralTile58, 1, 3);
        assertEquals(structuralTile58, lev1Ship.getTile(1,3));

        // propulsion -> check is in place + check increased baseFrePower if !double
        lev1Ship.placeTile(propulsorTile71, 2, 0);
        assertEquals(1, lev1Ship.getBasePropulsionPower()); //71 is single
        lev1Ship.placeTile(propulsorTile97, 2, 1);
        assertEquals(1, lev1Ship.getBasePropulsionPower()); // 97 is double

        // laser piu piu! -> check is in place + check correct baseFirePower update
        lev1Ship.placeTile(laserTile123, 2, 2);
        assertEquals(1, lev1Ship.getBaseFirePower());

        laserTile122.rotate90dx();
        lev1Ship.placeTile(laserTile122, 2, 3);
        assertEquals(1.5, lev1Ship.getBaseFirePower());

        laserTile121.rotate180();
        lev1Ship.placeTile(laserTile121, 2, 4);
        assertEquals(2, lev1Ship.getBaseFirePower());

        lev1Ship.placeTile(laserTile134, 3, 0);
        assertEquals(2, lev1Ship.getBaseFirePower());



    }

    @Test
    void breakTile() {
    }

    @Test
    void getTilesBuffer() {
        assertNotNull(lev1Ship.getTilesBuffer());
        assertNotNull(lev2Ship.getTilesBuffer());
    }

    @Test
    void addTileInBuffer() {
    }

    @Test
    void getNumBatteries() {
        assertEquals(0, lev1Ship.getNumBatteries());
        assertEquals(0, lev2Ship.getNumBatteries());
    }

    @Test
    void getNumCrewByType() {
        assertEquals(0, lev1Ship.getNumCrewByType(CrewType.HUMAN));
        assertEquals(0, lev2Ship.getNumCrewByType(CrewType.HUMAN));
        assertEquals(0, lev1Ship.getNumCrewByType(CrewType.PINK_ALIEN));
        assertEquals(0, lev2Ship.getNumCrewByType(CrewType.PINK_ALIEN));
        assertEquals(0, lev1Ship.getNumCrewByType(CrewType.BROWN_ALIEN));
        assertEquals(0, lev2Ship.getNumCrewByType(CrewType.BROWN_ALIEN));
    }

    @Test
    void getNumCrew() {
    }

    @Test
    void getNumAliens() {
    }

    @Test
    void getBoxes() {
    }

    @Test
    void testGetBoxes() {
    }

    @Test
    void removeBatteries() {
    }

    @Test
    void removeBoxes() {
    }

    @Test
    void testRemoveBoxes() {
    }

    @Test
    void removeCrewByType() {
    }

    @Test
    void getNumExposedConnectors() {
    }

    @Test
    void getProtectedDirections() {
    }

    @Test
    void getBaseFirePower() {
    }

    @Test
    void getBasePropulsionPower() {
    }

    @Test
    void calcTotalPropulsionPower() {
    }

    @Test
    void calcTotalFirePower() {
    }

    @Test
    void updateBaseFirePower() {
    }

    @Test
    void updateBasePropulsionPower() {
    }

    @Test
    void updateExposedConnectors() {
    }

    @Test
    void removeProtectedDirections() {
    }

    @Test
    void isShipLegal() {
    }

    @Test
    void handleMeteor() {
    }

    @Test
    void handleShot() {
    }
}