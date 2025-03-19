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
    private Tile laserTile123;

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
        laserTile134 = tiles.get(134);
        laserTile123 = tiles.get(123);
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

        // check resources are updated
        assertEquals(2, lev1Ship.getNumBatteries());

        // check tile attributes are updated

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