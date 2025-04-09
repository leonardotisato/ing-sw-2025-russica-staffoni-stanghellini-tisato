package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.*;
import it.polimi.ingsw.cg04.model.tiles.HousingTile;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.TileLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// todo: test edge cases for place/break HousingTile and AlienSupportTile

class ShipTest {

    Shipyard shipyard = new Shipyard();

    Ship lev1Ship;
    Ship lev2Ship;

    private Tile shieldTile150;
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
    private Tile housingTile46;
    private Tile housingTile47;
    private Tile housingTile48;
    private Tile alienSupportTile141;
    private Tile alienSupportTile142;
    private Tile alienSupportTile143;
    private Tile housingTile33;
    private Tile structuralTile53;
    private Tile propulsorTile76;
    private Tile propulsorTile94;
    private Tile laserTile106;
    private Tile storageTile19;
    private Tile storageTile18;
    private Tile laserTile125;
    private Tile laserTile126;

    Map<Integer, Tile> tiles;


    @BeforeEach
    void setUp() {
        // new tests using the tileLoader
        ArrayList<Integer> faceDownTiles = new ArrayList<>();
        tiles = TileLoader.loadTilesFromJson("src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json", faceDownTiles);

        assertNotNull(tiles);
        assertFalse(tiles.isEmpty());

        lev1Ship = new Ship(1, PlayerColor.BLUE);
        lev2Ship = new Ship(2, PlayerColor.RED);

        laserTile126 = tiles.get(126);
        storageTile18 = tiles.get(18);
        structuralTile53 = tiles.get(53);
        propulsorTile76 = tiles.get(76);
        propulsorTile94 = tiles.get(94);
        laserTile106 = tiles.get(106);
        storageTile19 = tiles.get(19);
        laserTile125 = tiles.get(125);
        shieldTile150 = tiles.get(150);
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
        housingTile46 = tiles.get(46);
        housingTile47 = tiles.get(47);
        housingTile48 = tiles.get(48);
        alienSupportTile141 = tiles.get(141);
        alienSupportTile142 = tiles.get(142);
        alienSupportTile143 = tiles.get(143);
        housingTile33 = tiles.get(33); // central


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
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.getTile(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.getTile(1, -1));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.getTile(0, 5));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.getTile(5, 0));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.getTile(4, 2));

        assertThrows(IllegalArgumentException.class, () -> lev1Ship.getTile(-1, 1));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.getTile(1, -1));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.getTile(0, 7));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.getTile(5, 0));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.getTile(4, 2));

        // returns null if tile not present
        assertNull(lev1Ship.getTile(1, 1));

        // check central tile is correct
        assertInstanceOf(HousingTile.class, lev1Ship.getTile(2, 2));
        assertTrue(lev1Ship.getTile(2, 2).isCentralTile());
        assertEquals(33, lev1Ship.getTile(2, 2).getId());

        // check central tile is correct
        assertInstanceOf(HousingTile.class, lev2Ship.getTile(2, 3));
        assertTrue(lev2Ship.getTile(2, 3).isCentralTile());
        assertEquals(52, lev2Ship.getTile(2, 3).getId());

    }

    @Test
    void getNumBrokenTiles() {
        assertEquals(0, lev2Ship.getNumBrokenTiles());

        lev2Ship = shipyard.createShip3();

        assertEquals(0, lev2Ship.getNumBrokenTiles());

        lev2Ship.breakTile(1,3);
        assertEquals(1, lev2Ship.getNumBrokenTiles());
        lev2Ship.breakTile(3,1);
        assertEquals(2, lev2Ship.getNumBrokenTiles());
        lev2Ship.breakTile(1,2);
        assertEquals(3, lev2Ship.getNumBrokenTiles());
        assertEquals(3, lev2Ship.getNumBatteries());
        assertEquals(5, lev2Ship.getNumExposedConnectors());

    }

    // @Test
    void placeTile() {

        assertFalse(lev1Ship.placeTile(storageTile26, 2, 2), "Tile cannot be placed in the center where the central housing tile should be!");
        assertFalse(lev2Ship.placeTile(storageTile26, 2, 3), "Tile cannot be placed in the center where the central housing tile should be!");
        assertFalse(lev1Ship.placeTile(null, 3, 2), "Null tile cannot be placed!");
        assertFalse(lev1Ship.placeTile(storageTile26, 0, 0));
        assertFalse(lev1Ship.placeTile(storageTile26, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.placeTile(storageTile26, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.placeTile(storageTile26, 0, -1));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.placeTile(storageTile26, 1, 5));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.placeTile(storageTile26, 5, 1));

        // check init params are ok
        assertEquals(2, lev1Ship.getNumCrew(), "Initial crew should be 2, as central housing tile hosts 2 humans");
        assertEquals(0, lev1Ship.getNumBatteries());
        assertEquals(4, lev1Ship.getNumExposedConnectors(), "Initial exposed connectors should be 4, as central housing tile has 4 universal connectors");
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

        // check placement on top of existing is illegal
        assertFalse(lev1Ship.placeTile(laserTile121, 0, 2));

        // check resources are updated

        // battery -> check is in place + check added resources
        assertEquals(batteryTile5, lev1Ship.getTile(0, 2));
        assertEquals(batteryTile5.getMaxBatteryCapacity(), lev1Ship.getNumBatteries());

        // storage -> check is in place
        lev1Ship.placeTile(storageTile26, 1, 1);
        assertEquals(storageTile26, lev1Ship.getTile(1, 1));

        // shield -> check is in place + check added protected directions
        lev1Ship.placeTile(shieldTile151, 1, 2);
        assertEquals(shieldTile151, lev1Ship.getTile(1, 2));
        // list in ship and set in tile may cause problems
        assertTrue(lev1Ship.getProtectedDirections().containsAll(shieldTile151.getProtectedDirections()));

        // structural -> check is in place
        lev1Ship.placeTile(structuralTile58, 1, 3);
        assertEquals(structuralTile58, lev1Ship.getTile(1, 3));

        // propulsion -> check is in place + check increased baseFrePower if !double
        lev1Ship.placeTile(propulsorTile71, 2, 0);
        assertEquals(1, lev1Ship.getBasePropulsionPower()); //71 is single
        lev1Ship.placeTile(propulsorTile97, 2, 1);
        assertEquals(1, lev1Ship.getBasePropulsionPower()); // 97 is double

        // laser -> check is in place + check correct baseFirePower update
        boolean placement = lev1Ship.placeTile(laserTile123, 4, 4);
        assertTrue(placement);
        assertEquals(1, lev1Ship.getBaseFirePower());

        laserTile122.rotate90dx();
        lev1Ship.placeTile(laserTile122, 2, 3);
        assertEquals(1.5, lev1Ship.getBaseFirePower());

        laserTile121.rotate180();
        lev1Ship.placeTile(laserTile121, 2, 4);
        assertEquals(2, lev1Ship.getBaseFirePower());

        lev1Ship.placeTile(laserTile134, 3, 0);
        assertEquals(2, lev1Ship.getBaseFirePower());

        // todo: housing and alien support are tricky -> might have to extend tests
        // housing -> check is in place + check supportedCrew is updated + check adjacent alien supports params are updated
        // alien support -> check is in place + check adjacentHousingTiles is ok and adjacentHousingTile params are updated

        // place un housing e place un support vicino
        // check: aggiunto a lista di adj + aggiunto tipo di alieno supportato
        lev1Ship.placeTile(housingTile46, 3, 1);
        lev1Ship.placeTile(alienSupportTile141, 3, 2);
        assertTrue(alienSupportTile141.getAdjacentHousingTiles().contains(housingTile46));
        assertTrue(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor()));

        // place un support e place un housing vicino
        // check: aggiunto a lista di adj + aggiunto tipo di alieno supportato
        alienSupportTile142.rotate90dx();
        lev1Ship.placeTile(alienSupportTile142, 4, 0);
        lev1Ship.placeTile(housingTile47, 4, 1);
        assertTrue(alienSupportTile142.getAdjacentHousingTiles().contains(housingTile47));
        assertTrue(housingTile47.getSupportedCrewType().contains(alienSupportTile142.getSupportedAlienColor()));
        alienSupportTile143.rotate90sx();

        // empty all ship
        lev1Ship.breakAllTiles();

        // new ship formation
        lev1Ship.placeTile(housingTile33, 2, 2);
        lev1Ship.placeTile(structuralTile53, 4, 3);
        lev1Ship.placeTile(propulsorTile76, 3, 0);
        lev1Ship.placeTile(propulsorTile94, 3, 1);
        lev1Ship.placeTile(housingTile46, 3, 3);
        lev1Ship.placeTile(alienSupportTile141, 3, 4);
        lev1Ship.placeTile(laserTile106, 2, 0);
        lev1Ship.placeTile(storageTile19, 2, 1);
        lev1Ship.placeTile(laserTile125, 2, 3);
        lev1Ship.placeTile(storageTile18, 1, 2);

        assertEquals(2, lev1Ship.getBaseFirePower());
        assertEquals(1, lev1Ship.getBasePropulsionPower());
        assertEquals(2, lev1Ship.getNumCrewByType(CrewType.HUMAN));
        assertTrue(housingTile46.getSupportedCrewType().contains(CrewType.BROWN_ALIEN));
        assertTrue(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor()));
        assertTrue(housingTile46.getSupportedCrewType().contains(CrewType.HUMAN));
        assertEquals(2, housingTile46.getSupportedCrewType().size());
        assertTrue(alienSupportTile141.getAdjacentHousingTiles().contains(housingTile46));
        assertEquals(1, alienSupportTile141.getAdjacentHousingTiles().size());
        assertEquals(0, housingTile46.getNumCrew());

    }

    // @Test
    void breakTile() {

        assertThrows(IllegalArgumentException.class, () -> lev1Ship.breakTile(0, 0));
        assertThrows(IllegalArgumentException.class, () -> lev1Ship.breakTile(-1, 3));

        // BatteryTile
        lev1Ship.placeTile(batteryTile5, 0, 2);
        assertEquals(batteryTile5.getMaxBatteryCapacity(), lev1Ship.getNumBatteries());
        lev1Ship.breakTile(0, 2);
        assertEquals(0, lev1Ship.getNumBatteries());

        // StructuralTile
        lev1Ship.placeTile(structuralTile58, 0, 2);
        assertEquals(0, lev1Ship.getNumBatteries());
        lev1Ship.breakTile(0, 2);
        assertNull(lev1Ship.getTile(0, 2));
        // storage
        lev1Ship.placeTile(storageTile26, 0, 2);
        lev1Ship.addBox(BoxType.YELLOW, 0, 2);
        lev1Ship.addBox(BoxType.GREEN, 0, 2);
        assertEquals(1, lev1Ship.getBoxes(BoxType.YELLOW));// todo: non viene aggiunto param alla ship
        assertEquals(1, lev1Ship.getBoxes(BoxType.GREEN));
        assertThrows(RuntimeException.class, () -> lev1Ship.addBox(BoxType.RED, 0, 2)); // 26 is not special
        assertThrows(RuntimeException.class, () -> lev1Ship.addBox(BoxType.GREEN, 0, 2));   // is full
        lev1Ship.breakTile(0, 2);
        assertEquals(0, lev1Ship.getBoxes(BoxType.GREEN)); // resources are removed
        assertNull(lev1Ship.getTile(0, 2)); // tile is removed from matrix

        // shield

        // first let's try one shield
        lev1Ship.placeTile(shieldTile151, 0, 2); // 151 is UP RIGHT
        assertTrue(lev1Ship.getProtectedDirections().containsAll(shieldTile151.getProtectedDirections()));
        assertTrue(shieldTile151.getProtectedDirections().containsAll(lev1Ship.getProtectedDirections()));
        lev1Ship.breakTile(0, 2);
        assertTrue(lev1Ship.getProtectedDirections().isEmpty());
        lev1Ship.placeTile(shieldTile151, 0, 2); // 151 is UP RIGHT

        assertTrue(lev1Ship.getProtectedDirections().containsAll(shieldTile151.getProtectedDirections()));
        assertTrue(shieldTile151.getProtectedDirections().containsAll(lev1Ship.getProtectedDirections()));

        // now try 2 shield with common directions
        lev1Ship.placeTile(shieldTile151, 0, 2); // 151 is UP RIGHT
        shieldTile150.rotate90dx(); // now 150 has RIGHT DOWN (if rotate90dx works)
        lev1Ship.placeTile(shieldTile150, 1, 1);
        assertTrue(lev1Ship.getProtectedDirections().contains(Direction.UP));
        assertTrue(lev1Ship.getProtectedDirections().contains(Direction.RIGHT));
        assertTrue(lev1Ship.getProtectedDirections().contains(Direction.DOWN));
        assertFalse(lev1Ship.getProtectedDirections().contains(Direction.LEFT));

        lev1Ship.breakTile(0, 2); // should remain DOWN RIGHT
        assertTrue(lev1Ship.getProtectedDirections().contains(Direction.RIGHT));
        assertTrue(lev1Ship.getProtectedDirections().contains(Direction.DOWN));
        assertFalse(lev1Ship.getProtectedDirections().contains(Direction.UP));
        assertFalse(lev1Ship.getProtectedDirections().contains(Direction.LEFT));

        lev1Ship.breakTile(1, 1);
        assertFalse(lev1Ship.getProtectedDirections().contains(Direction.RIGHT));
        assertFalse(lev1Ship.getProtectedDirections().contains(Direction.DOWN));
        assertFalse(lev1Ship.getProtectedDirections().contains(Direction.UP));
        assertFalse(lev1Ship.getProtectedDirections().contains(Direction.LEFT));

        // propulsion
        assertEquals(0, lev1Ship.getBasePropulsionPower());
        lev1Ship.placeTile(propulsorTile97, 0, 2); // 97 is double
        assertEquals(0, lev1Ship.getBasePropulsionPower());
        lev1Ship.placeTile(propulsorTile71, 1, 1); // 71 is single
        assertEquals(1, lev1Ship.getBasePropulsionPower());
        lev1Ship.breakTile(0, 2);
        lev1Ship.breakTile(1, 1);
        assertEquals(0, lev1Ship.getBasePropulsionPower());

        // lasers
        assertEquals(0, lev1Ship.getBaseFirePower());
        lev1Ship.placeTile(laserTile134, 0, 2);     // double
        assertEquals(0, lev1Ship.getBaseFirePower());
        lev1Ship.placeTile(laserTile123, 1, 1);     // single, gun UP
        assertEquals(1, lev1Ship.getBaseFirePower());
        laserTile121.rotate90dx(); // gun not UP, should add only .5 baseFirePower
        lev1Ship.placeTile(laserTile121, 1, 2);
        assertEquals(1.5, lev1Ship.getBaseFirePower());

        lev1Ship.breakTile(1, 1);
        assertEquals(0.5, lev1Ship.getBaseFirePower());
        lev1Ship.breakTile(0, 2);
        assertEquals(0.5, lev1Ship.getBaseFirePower());
        lev1Ship.breakTile(1, 2);
        assertEquals(0, lev1Ship.getBaseFirePower());

        assertNull(lev1Ship.getTile(0, 2));
        assertNull(lev1Ship.getTile(1, 2));
        assertNull(lev1Ship.getTile(1, 1));

        // housingTile and AlienSupportTile
        // todo: tricky, might need more accurate testing
        // todo: cannot test this till we figure out how to add crew with the correct logic

        // test all the possible combinations
        // 1. add HousingTile (ht) add AlienSupport (as) -> rm ht rm as
        // 2. add as add ht -> rm as rm ht
        // 3. add hs add as -> rm as rm ht
        // 4. add as add ht -> rm ht rm as


        lev1Ship.placeTile(housingTile46, 2, 2);
        lev1Ship.placeTile(alienSupportTile141, 3, 2);
        assertFalse(alienSupportTile141.getAdjacentHousingTiles().contains(housingTile46)); // not valid connection
        assertFalse(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor())); // not valid connection
        lev1Ship.breakTile(2, 2);
        lev1Ship.breakTile(3, 2);


        // 1. add ht add as -> rm ht rm as
        // rotate and reposition to connect them
        alienSupportTile141.rotate90dx();
        lev1Ship.placeTile(housingTile46, 2, 2);
        assertEquals(2, lev1Ship.getNumCrewByType(CrewType.HUMAN)); // humans added automatically
        assertFalse(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor()));
        lev1Ship.placeTile(alienSupportTile141, 3, 2);
        assertTrue(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor()));
        assertEquals(0, lev1Ship.getNumCrewByType(CrewType.HUMAN));
        assertTrue(alienSupportTile141.getAdjacentHousingTiles().contains(housingTile46)); // valid connection
        assertTrue(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor())); // valid connection
        lev1Ship.breakTile(2, 2);
        assertFalse(alienSupportTile141.getAdjacentHousingTiles().contains(housingTile46));
        lev1Ship.breakTile(3, 2);


        // 2. add as add ht -> rm as rm ht
        lev1Ship.placeTile(alienSupportTile141, 3, 2);
        assertTrue(alienSupportTile141.getAdjacentHousingTiles().isEmpty());
        lev1Ship.placeTile(housingTile46, 2, 2);
        assertTrue(alienSupportTile141.getAdjacentHousingTiles().contains(housingTile46));
        assertTrue(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor()));
        lev1Ship.breakTile(3, 2);
        assertFalse(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor()));
        lev1Ship.breakTile(2, 2);


        // 3. add hs add as -> rm as rm ht
        lev1Ship.placeTile(housingTile46, 2, 2);
        assertEquals(1, housingTile46.getSupportedCrewType().size());
        assertTrue(housingTile46.getSupportedCrewType().contains(CrewType.HUMAN));
        lev1Ship.placeTile(alienSupportTile141, 3, 2);
        assertTrue(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor()));
        assertTrue(housingTile46.getSupportedCrewType().contains(CrewType.HUMAN));
        assertEquals(2, housingTile46.getSupportedCrewType().size());
        lev1Ship.breakTile(3, 2);
        assertEquals(0, alienSupportTile141.getAdjacentHousingTiles().size());
        assertEquals(1, housingTile46.getSupportedCrewType().size());
        assertTrue(housingTile46.getSupportedCrewType().contains(CrewType.HUMAN));
        lev1Ship.breakTile(2, 2);

        // 4. add as add ht -> rm ht rm as
        lev1Ship.placeTile(alienSupportTile141, 3, 2);
        assertTrue(alienSupportTile141.getAdjacentHousingTiles().isEmpty());
        lev1Ship.placeTile(housingTile46, 2, 2);
        assertTrue(alienSupportTile141.getAdjacentHousingTiles().contains(housingTile46));
        assertTrue(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor()));
        lev1Ship.breakTile(2, 2);
        assertTrue(alienSupportTile141.getAdjacentHousingTiles().isEmpty());
        lev1Ship.breakTile(3, 2);

        // more HousingTile and AlienSupportTile
        lev1Ship.placeTile(housingTile46, 2, 2);
        lev1Ship.placeTile(alienSupportTile141, 3, 2);
        lev1Ship.getTile(2, 2).addCrew(alienSupportTile141.getSupportedAlienColor());
        lev1Ship.addCrewByType(alienSupportTile141.getSupportedAlienColor());
        assertEquals(0, lev1Ship.getNumCrewByType(CrewType.HUMAN));
        assertEquals(1, lev1Ship.getNumCrewByType(alienSupportTile141.getSupportedAlienColor()));
        lev1Ship.breakTile(3, 2);
        assertEquals(0, lev1Ship.getNumCrewByType(CrewType.HUMAN));
        assertEquals(0, lev1Ship.getNumCrewByType(alienSupportTile141.getSupportedAlienColor()));
        lev1Ship.breakTile(2, 2);

        lev1Ship.breakAllTiles();
    }

    // @Test
    void breakTile2() {
        // new ship formation
        lev1Ship = shipyard.createShip2();
        housingTile46 = lev1Ship.getTile(3, 3);
        alienSupportTile141 = lev1Ship.getTile(3, 4);

        // add random resources to simulate batter
        lev1Ship.addBox(BoxType.BLUE, 1, 2);
        lev1Ship.addBox(BoxType.GREEN, 1, 2);
        assertEquals(1, lev1Ship.getBoxes(BoxType.BLUE));
        assertEquals(1, lev1Ship.getBoxes(BoxType.GREEN));
        assertThrows(RuntimeException.class, () -> storageTile18.addBox(BoxType.RED, 1));
        lev1Ship.addBox(BoxType.YELLOW, 2, 1);
        assertTrue(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor()));
        assertThrows(RuntimeException.class, () -> housingTile46.addCrew(CrewType.PINK_ALIEN));
        lev1Ship.addCrew(CrewType.BROWN_ALIEN, 3, 3);
        assertThrows(RuntimeException.class, () -> housingTile46.addCrew(CrewType.BROWN_ALIEN));
        assertThrows(RuntimeException.class, () -> housingTile46.addCrew(CrewType.PINK_ALIEN));
        assertEquals(0, lev1Ship.getNumCrewByType(CrewType.PINK_ALIEN));
        assertEquals(1, lev1Ship.getNumCrewByType(CrewType.BROWN_ALIEN));
        assertEquals(9, lev1Ship.getNumExposedConnectors());

        // remove every single one and check if something weird happens
        lev1Ship.breakTile(1, 2);
        assertEquals(0, lev1Ship.getBoxes(BoxType.BLUE));
        assertEquals(0, lev1Ship.getBoxes(BoxType.GREEN));
        assertEquals(8, lev1Ship.getNumExposedConnectors());

        lev1Ship.breakTile(2, 2);
        assertEquals(0, lev1Ship.getNumCrewByType(CrewType.HUMAN));
        assertEquals(8, lev1Ship.getNumExposedConnectors());

        lev1Ship.breakTile(3, 4);
        assertEquals(0, lev1Ship.getNumCrewByType(CrewType.BROWN_ALIEN));
        assertFalse(housingTile46.getSupportedCrewType().contains(alienSupportTile141.getSupportedAlienColor()));
        assertEquals(8, lev1Ship.getNumExposedConnectors());


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

        lev2Ship = shipyard.createShip3();

        assertEquals(6, lev2Ship.getNumBatteries());
        lev2Ship.breakTile(1, 2);
        assertEquals(3, lev2Ship.getNumBatteries());
    }

    @Test
    void getNumCrewByType() {
        assertEquals(2, lev1Ship.getNumCrewByType(CrewType.HUMAN));
        assertEquals(2, lev2Ship.getNumCrewByType(CrewType.HUMAN));
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
    void removeBox() {
        lev1Ship.placeTile(storageTile26, 3, 2);
        lev1Ship.addBox(BoxType.GREEN, 3, 2);
        lev1Ship.addBox(BoxType.GREEN, 3, 2);
        assertThrows(RuntimeException.class, () -> lev1Ship.addBox(BoxType.GREEN, 3, 2));
        assertEquals(2, lev1Ship.getBoxes(BoxType.GREEN));
        assertEquals(0, lev1Ship.getBoxes(BoxType.BLUE));
        assertEquals(0, lev1Ship.getBoxes(BoxType.RED));
        assertEquals(0, lev2Ship.getBoxes(BoxType.YELLOW));
        lev1Ship.removeBox(BoxType.GREEN, 3, 2);
        assertEquals(1, lev1Ship.getBoxes(BoxType.GREEN));
        lev1Ship.removeBox(BoxType.GREEN, 3, 2);
        assertEquals(0, lev1Ship.getBoxes(BoxType.GREEN));
        assertThrows(RuntimeException.class, () -> lev1Ship.removeBox(BoxType.GREEN, 3, 2));
        assertThrows(RuntimeException.class, () -> lev1Ship.removeBox(BoxType.RED, 3, 2));
        assertThrows(RuntimeException.class, () -> lev1Ship.addBox(BoxType.RED, 3, 2));
        assertThrows(RuntimeException.class, () -> lev1Ship.addBox(BoxType.YELLOW, 0, 0));
        assertThrows(RuntimeException.class, () -> lev1Ship.addBox(BoxType.YELLOW, -1, 0));
    }

    @Test
    void addBoxTest() {
    }

    @Test
    void setBoxesTest3() {
        lev2Ship = shipyard.createShip3();
        lev2Ship.addBox(BoxType.GREEN, 2, 1);
        lev2Ship.addBox(BoxType.RED, 2, 1);
        lev2Ship.addBox(BoxType.YELLOW, 3, 3);
        lev2Ship.addBox(BoxType.YELLOW, 3, 3);
        lev2Ship.addBox(BoxType.BLUE, 3, 3);

        // 1 red in (2, 1)
        // 1 green in (2, 1)
        // 2 yellow in (3, 3)
        // 1 blue in (3, 3)

        assertEquals(6, lev2Ship.getNumBatteries()); // 6 batteries

        lev2Ship.removeBestBoxes(2);
        assertEquals(0, lev2Ship.getTile(2, 1).getBoxes().get(BoxType.RED));
        assertEquals(0, lev2Ship.getBoxes(BoxType.RED));

        assertEquals(1, lev2Ship.getTile(3, 3).getBoxes().get(BoxType.YELLOW));
        assertEquals(1, lev2Ship.getBoxes(BoxType.YELLOW));

        assertEquals(1, lev2Ship.getBoxes(BoxType.BLUE));
        assertEquals(1, lev2Ship.getBoxes(BoxType.GREEN));

        // 1 green in (2, 1)
        // 1 yellow in (3, 3)
        // 1 blue in (3, 3)
        // 6 batteries

        lev2Ship.removeBestBoxes(2);
        assertEquals(0, lev2Ship.getTile(3, 3).getBoxes().get(BoxType.YELLOW));
        assertEquals(0, lev2Ship.getBoxes(BoxType.YELLOW));

        assertEquals(0, lev2Ship.getTile(2, 1).getBoxes().get(BoxType.GREEN));
        assertEquals(0, lev2Ship.getBoxes(BoxType.GREEN));

        assertEquals(0, lev2Ship.getBoxes(BoxType.RED));
        assertEquals(1, lev2Ship.getBoxes(BoxType.BLUE));

        // 1 blue in (3, 3)
        // 6 batteries

        lev2Ship.removeBestBoxes(4);
        assertEquals(0, lev2Ship.getTile(3, 3).getBoxes().get(BoxType.BLUE));
        assertEquals(0, lev2Ship.getBoxes(BoxType.BLUE));

        assertEquals(0, lev2Ship.getBoxes(BoxType.RED));
        assertEquals(0, lev2Ship.getBoxes(BoxType.YELLOW));
        assertEquals(0, lev2Ship.getBoxes(BoxType.GREEN));
        assertEquals(0, lev2Ship.getBoxes(BoxType.BLUE));

        assertEquals(3, lev2Ship.getNumBatteries());
    }

    @Test
    void removeBestBoxesTest() {

    }

    @Test
    void testRemoveBoxes() {
    }

    @Test
    void removeCrewByType() {
    }

    @Test
    void getNumExposedConnectors() {
        assertEquals(4, lev1Ship.getNumExposedConnectors());
    }

    @Test
    void getProtectedDirections() {
        assertTrue(lev1Ship.getProtectedDirections().isEmpty(), "Initially (without any shield) protected directions should be empty");
        lev1Ship.placeTile(batteryTile5, 1, 2);
        assertTrue(lev1Ship.getProtectedDirections().isEmpty(), "When adding non-shield tiles no changes should occur");

        // add shield check protectedDirections is updated
        lev1Ship.placeTile(shieldTile150, 2, 1);
        assertEquals(2, lev1Ship.getProtectedDirections().size());
        assertTrue(lev1Ship.getProtectedDirections().containsAll(List.of(Direction.UP, Direction.RIGHT)));

        // add other shieldTile, check protectedDirections is updated
        shieldTile151.rotate90dx();
        lev1Ship.placeTile(shieldTile151, 2, 3);
        assertEquals(4, lev1Ship.getProtectedDirections().size());
        assertTrue(lev1Ship.getProtectedDirections().containsAll(List.of(Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.DOWN)));

        // remove a shieldTile, make sure protectedDirections are updated
        lev1Ship.breakTile(2, 1);
        assertEquals(2, lev1Ship.getProtectedDirections().size());
        assertTrue(lev1Ship.getProtectedDirections().containsAll(List.of(Direction.DOWN, Direction.RIGHT)));
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
        assertEquals(4, lev1Ship.getNumExposedConnectors());
        lev1Ship.placeTile(alienSupportTile141, 3, 2); // placeTile has updateExposedConnectors in its implementation
        System.out.println(lev1Ship);
        assertEquals(5, lev1Ship.getNumExposedConnectors());
        lev1Ship.placeTile(alienSupportTile142, 3, 4);
        assertEquals(7, lev1Ship.getNumExposedConnectors());
        lev1Ship.breakTile(3, 4);   // breakTile has updateExposedConnectors in its implementation
        assertEquals(5, lev1Ship.getNumExposedConnectors());
        lev1Ship.placeTile(alienSupportTile142, 3, 1);
        assertEquals(6, lev1Ship.getNumExposedConnectors());
        lev1Ship.breakTile(3, 1);
        lev1Ship.breakTile(3, 2);


        lev1Ship.placeTile(alienSupportTile141, 2, 3);
        assertEquals(4, lev1Ship.getNumExposedConnectors());
        lev1Ship.placeTile(alienSupportTile142, 2, 4);
        assertEquals(4, lev1Ship.getNumExposedConnectors());
    }

    @Test
    void removeProtectedDirections() {
    }

    @Test
    void addProtectedDirections() {
    }

    @Test
    void isShipConnectedBFS() {

        assertTrue(lev1Ship.isShipConnectedBFS());
        lev1Ship.placeTile(alienSupportTile143, 3, 2);
        lev1Ship.placeTile(alienSupportTile141, 3, 3);
        assertTrue(lev1Ship.isShipConnectedBFS());
        lev1Ship.placeTile(alienSupportTile142, 0, 2);
        assertFalse(lev1Ship.isShipConnectedBFS());

        lev1Ship.breakTile(3, 3);
        lev1Ship.breakTile(3, 2);
        lev1Ship.breakTile(0, 2);
    }

    @Test
    void isShipLegal() {
        assertTrue(lev1Ship.isShipLegal());
        lev1Ship.placeTile(alienSupportTile143, 3, 2);
        assertTrue(lev1Ship.isShipLegal());
        lev1Ship.placeTile(housingTile46, 3, 1);
        assertFalse(lev1Ship.isShipLegal());
        lev1Ship.breakTile(3, 1);
        lev1Ship.placeTile(housingTile46, 3, 3);
        assertTrue(lev1Ship.isShipLegal());

        propulsorTile97.rotate90dx();
        lev1Ship.placeTile(propulsorTile97, 3, 1);
        assertFalse(lev1Ship.isShipLegal());
        lev1Ship.breakTile(3, 1);
        propulsorTile97.rotate90sx();
        lev1Ship.placeTile(propulsorTile97, 3, 2);
        assertTrue(lev1Ship.isShipLegal());

        lev1Ship.placeTile(laserTile121, 2, 1);
        assertTrue(lev1Ship.isShipLegal());
        lev1Ship.breakTile(2, 1);
        laserTile121.rotate90dx();
        laserTile121.rotate90dx();
        laserTile121.rotate90dx();
        lev1Ship.placeTile(laserTile121, 2, 1);
        assertFalse(lev1Ship.isShipLegal());

        lev1Ship.breakTile(2, 1);
        lev1Ship.placeTile(laserTile121, 1, 2);
        assertTrue(lev1Ship.isShipLegal());
    }

    //@Test
    void isShipLegal2() {

        lev1Ship = shipyard.createShip2();

        assertTrue(lev1Ship.isShipLegal());


        assertTrue(lev1Ship.getTile(3, 3).getSupportedCrewType().contains(lev1Ship.getTile(3, 4).getSupportedAlienColor()));
        // try placing
    }


    @Test
    void isShipLegal3() {

        lev2Ship = shipyard.createShip3();

        System.out.println(lev2Ship);
        assertTrue(lev2Ship.isShipConnectedBFS());
        assertTrue(lev2Ship.isShipLegal());
        assertEquals(4, lev2Ship.getNumExposedConnectors());
        assertEquals(6, lev2Ship.getNumBatteries());
        assertTrue(lev2Ship.getProtectedDirections().containsAll(Set.of(Direction.UP, Direction.RIGHT)));

        lev2Ship.breakTile(1,3);
        System.out.println(lev2Ship);
        assertFalse(lev2Ship.isShipLegal());
    }


    @Test
    void isShipLegal4() {

        lev2Ship = shipyard.createShip4();

        assertTrue(lev2Ship.isShipConnectedBFS());
        assertTrue(lev2Ship.isShipLegal());
        assertEquals(8, lev2Ship.getNumExposedConnectors());
        assertEquals(2, lev2Ship.getNumBatteries());
        assertTrue(lev2Ship.getProtectedDirections().containsAll(Set.of(Direction.UP, Direction.RIGHT)));
        assertEquals(1, lev2Ship.getBaseFirePower());
    }

    @Test
    void isShipLegal5() {

        lev2Ship = shipyard.createShip5();
        System.out.println(lev2Ship);
        assertTrue(lev2Ship.isShipConnectedBFS());
        assertTrue(lev2Ship.isShipLegal());
        assertEquals(5, lev2Ship.getNumExposedConnectors());
        assertEquals(7, lev2Ship.getNumBatteries());
        assertTrue(lev2Ship.getProtectedDirections().containsAll(Set.of(Direction.UP, Direction.RIGHT, Direction.LEFT)));
        assertEquals(1.5, lev2Ship.getBaseFirePower());
    }



    @Test
    void isShipLegal6(){
        lev2Ship = shipyard.createShip3();
        assertTrue(lev2Ship.isShipLegal());
        lev2Ship.breakTile(1, 3);
        assertFalse(lev2Ship.isShipLegal());
    }

    //@Test
    void checkMeteorTest2() {
        lev1Ship = shipyard.createShip2();
        assertTrue(lev1Ship.isShipLegal());

        // not a great ship...

        assertEquals(-1, lev1Ship.checkMeteor(Direction.UP, Attack.LIGHT, 0));
        assertEquals(2, lev1Ship.checkMeteor(Direction.UP, Attack.LIGHT, 1));
        assertEquals(2, lev1Ship.checkMeteor(Direction.UP, Attack.LIGHT, 2));
        assertEquals(-1, lev1Ship.checkMeteor(Direction.UP, Attack.LIGHT, 3));
        assertEquals(-1, lev1Ship.checkMeteor(Direction.UP, Attack.LIGHT, 4));
        assertEquals(-2, lev1Ship.checkMeteor(Direction.UP, Attack.HEAVY, 0));
        assertEquals(2, lev1Ship.checkMeteor(Direction.UP, Attack.HEAVY, 1));
        assertEquals(2, lev1Ship.checkMeteor(Direction.UP, Attack.HEAVY, 2));
        assertEquals(-2, lev1Ship.checkMeteor(Direction.UP, Attack.HEAVY, 3));
        assertEquals(2, lev1Ship.checkMeteor(Direction.UP, Attack.HEAVY, 4));

        assertEquals(-1, lev1Ship.checkMeteor(Direction.RIGHT, Attack.LIGHT, 0));
        assertEquals(2, lev1Ship.checkMeteor(Direction.RIGHT, Attack.LIGHT, 1));
        assertEquals(2, lev1Ship.checkMeteor(Direction.RIGHT, Attack.LIGHT, 2));
        assertEquals(2, lev1Ship.checkMeteor(Direction.RIGHT, Attack.LIGHT, 3));
        assertEquals(2, lev1Ship.checkMeteor(Direction.RIGHT, Attack.LIGHT, 4));
        assertEquals(-1, lev1Ship.checkMeteor(Direction.RIGHT, Attack.HEAVY, 0));
        assertEquals(2, lev1Ship.checkMeteor(Direction.RIGHT, Attack.HEAVY, 1));
        assertEquals(2, lev1Ship.checkMeteor(Direction.RIGHT, Attack.HEAVY, 2));
        assertEquals(2, lev1Ship.checkMeteor(Direction.RIGHT, Attack.HEAVY, 3));
        assertEquals(2, lev1Ship.checkMeteor(Direction.RIGHT, Attack.HEAVY, 4));

        assertEquals(-1, lev1Ship.checkMeteor(Direction.LEFT, Attack.LIGHT, 0));
        assertEquals(-1, lev1Ship.checkMeteor(Direction.LEFT, Attack.LIGHT, 1));
        assertEquals(-1, lev1Ship.checkMeteor(Direction.LEFT, Attack.LIGHT, 2));
        assertEquals(-1, lev1Ship.checkMeteor(Direction.LEFT, Attack.LIGHT, 3));
        assertEquals(2, lev1Ship.checkMeteor(Direction.LEFT, Attack.LIGHT, 4));
        assertEquals(-1, lev1Ship.checkMeteor(Direction.LEFT, Attack.HEAVY, 0));
        assertEquals(2, lev1Ship.checkMeteor(Direction.LEFT, Attack.HEAVY, 1));
        assertEquals(2, lev1Ship.checkMeteor(Direction.LEFT, Attack.HEAVY, 2));
        assertEquals(2, lev1Ship.checkMeteor(Direction.LEFT, Attack.HEAVY, 3));
        assertEquals(2, lev1Ship.checkMeteor(Direction.LEFT, Attack.HEAVY, 4));

        assertEquals(-1, lev1Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 0));
        assertEquals(-1, lev1Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 1));
        assertEquals(2, lev1Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 2));
        assertEquals(-1, lev1Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 3));
        assertEquals(-1, lev1Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 4));
        assertEquals(2, lev1Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 0));
        assertEquals(2, lev1Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 1));
        assertEquals(2, lev1Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 2));
        assertEquals(2, lev1Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 3));
        assertEquals(2, lev1Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 4));

    }

    @Test
    void checkMeteorTest3() {

        lev2Ship = shipyard.createShip3();

        assertEquals(-1, lev2Ship.checkMeteor(Direction.UP, Attack.LIGHT, 0));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.UP, Attack.HEAVY, 0));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.UP, Attack.LIGHT, 1));
        assertEquals(2, lev2Ship.checkMeteor(Direction.UP, Attack.HEAVY, 1));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.UP, Attack.LIGHT, 2));
        assertEquals(2, lev2Ship.checkMeteor(Direction.UP, Attack.HEAVY, 2));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.UP, Attack.LIGHT, 3));
        assertEquals(1, lev2Ship.checkMeteor(Direction.UP, Attack.HEAVY, 3));
        assertEquals(0, lev2Ship.checkMeteor(Direction.UP, Attack.LIGHT, 4));
        assertEquals(2, lev2Ship.checkMeteor(Direction.UP, Attack.HEAVY, 4));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.UP, Attack.LIGHT, 5));
        assertEquals(-2, lev2Ship.checkMeteor(Direction.UP, Attack.HEAVY, 5));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.UP, Attack.LIGHT, 6));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.UP, Attack.HEAVY, 6));

        assertEquals(-1, lev2Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 0));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 0));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 1));
        assertEquals(2, lev2Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 1));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 2));
        assertEquals(2, lev2Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 2));
        assertEquals(2, lev2Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 3));
        assertEquals(2, lev2Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 3));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 4));
        assertEquals(2, lev2Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 4));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 5));
        assertEquals(2, lev2Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 5));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.DOWN, Attack.LIGHT, 6));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.DOWN, Attack.HEAVY, 6));

        assertEquals(-1, lev2Ship.checkMeteor(Direction.LEFT, Attack.LIGHT, 0));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.LEFT, Attack.HEAVY, 0));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.LEFT, Attack.LIGHT, 1));
        assertEquals(2, lev2Ship.checkMeteor(Direction.LEFT, Attack.HEAVY, 1));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.LEFT, Attack.LIGHT, 2));
        assertEquals(2, lev2Ship.checkMeteor(Direction.LEFT, Attack.HEAVY, 2));
        assertEquals(2, lev2Ship.checkMeteor(Direction.LEFT, Attack.LIGHT, 3));
        assertEquals(2, lev2Ship.checkMeteor(Direction.LEFT, Attack.HEAVY, 3));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.LEFT, Attack.LIGHT, 4));
        assertEquals(2, lev2Ship.checkMeteor(Direction.LEFT, Attack.HEAVY, 4));

        assertEquals(-1, lev2Ship.checkMeteor(Direction.RIGHT, Attack.LIGHT, 0));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.RIGHT, Attack.HEAVY, 0));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.RIGHT, Attack.LIGHT, 1));
        assertEquals(2, lev2Ship.checkMeteor(Direction.RIGHT, Attack.HEAVY, 1));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.RIGHT, Attack.LIGHT, 2));
        assertEquals(2, lev2Ship.checkMeteor(Direction.RIGHT, Attack.HEAVY, 2));
        assertEquals(0, lev2Ship.checkMeteor(Direction.RIGHT, Attack.LIGHT, 3));
        assertEquals(2, lev2Ship.checkMeteor(Direction.RIGHT, Attack.HEAVY, 3));
        assertEquals(-1, lev2Ship.checkMeteor(Direction.RIGHT, Attack.LIGHT, 4));
        assertEquals(2, lev2Ship.checkMeteor(Direction.RIGHT, Attack.HEAVY, 4));
    }

    @Test
    void checkAttack2(){
        lev1Ship = shipyard.createShip2();

        assertEquals(2, lev1Ship.checkShot(Direction.UP, Attack.LIGHT, 0));
        assertEquals(2, lev1Ship.checkShot(Direction.UP, Attack.LIGHT, 1));
        assertEquals(2, lev1Ship.checkShot(Direction.UP, Attack.LIGHT, 2));
        assertEquals(2, lev1Ship.checkShot(Direction.UP, Attack.LIGHT, 3));
        assertEquals(2, lev1Ship.checkShot(Direction.UP, Attack.LIGHT, 4));
        assertEquals(2, lev1Ship.checkShot(Direction.UP, Attack.HEAVY, 0));
        assertEquals(2, lev1Ship.checkShot(Direction.UP, Attack.HEAVY, 1));
        assertEquals(2, lev1Ship.checkShot(Direction.UP, Attack.HEAVY, 2));
        assertEquals(2, lev1Ship.checkShot(Direction.UP, Attack.HEAVY, 3));
        assertEquals(2, lev1Ship.checkShot(Direction.UP, Attack.HEAVY, 4));

        assertEquals(-1, lev1Ship.checkShot(Direction.RIGHT, Attack.LIGHT, 0));
        assertEquals(2, lev1Ship.checkShot(Direction.RIGHT, Attack.LIGHT, 1));
        assertEquals(2, lev1Ship.checkShot(Direction.RIGHT, Attack.LIGHT, 2));
        assertEquals(2, lev1Ship.checkShot(Direction.RIGHT, Attack.LIGHT, 3));
        assertEquals(2, lev1Ship.checkShot(Direction.RIGHT, Attack.LIGHT, 4));
        assertEquals(-1, lev1Ship.checkShot(Direction.RIGHT, Attack.HEAVY, 0));
        assertEquals(2, lev1Ship.checkShot(Direction.RIGHT, Attack.HEAVY, 1));
        assertEquals(2, lev1Ship.checkShot(Direction.RIGHT, Attack.HEAVY, 2));
        assertEquals(2, lev1Ship.checkShot(Direction.RIGHT, Attack.HEAVY, 3));
        assertEquals(2, lev1Ship.checkShot(Direction.RIGHT, Attack.HEAVY, 4));

        assertEquals(-1, lev1Ship.checkShot(Direction.LEFT, Attack.LIGHT, 0));
        assertEquals(2, lev1Ship.checkShot(Direction.LEFT, Attack.LIGHT, 1));
        assertEquals(2, lev1Ship.checkShot(Direction.LEFT, Attack.LIGHT, 2));
        assertEquals(2, lev1Ship.checkShot(Direction.LEFT, Attack.LIGHT, 3));
        assertEquals(2, lev1Ship.checkShot(Direction.LEFT, Attack.LIGHT, 4));
        assertEquals(-1, lev1Ship.checkShot(Direction.LEFT, Attack.HEAVY, 0));
        assertEquals(2, lev1Ship.checkShot(Direction.LEFT, Attack.HEAVY, 1));
        assertEquals(2, lev1Ship.checkShot(Direction.LEFT, Attack.HEAVY, 2));
        assertEquals(2, lev1Ship.checkShot(Direction.LEFT, Attack.HEAVY, 3));
        assertEquals(2, lev1Ship.checkShot(Direction.LEFT, Attack.HEAVY, 4));

        assertEquals(2, lev1Ship.checkShot(Direction.DOWN, Attack.LIGHT, 0));
        assertEquals(2, lev1Ship.checkShot(Direction.DOWN, Attack.LIGHT, 1));
        assertEquals(2, lev1Ship.checkShot(Direction.DOWN, Attack.LIGHT, 2));
        assertEquals(2, lev1Ship.checkShot(Direction.DOWN, Attack.LIGHT, 3));
        assertEquals(2, lev1Ship.checkShot(Direction.DOWN, Attack.LIGHT, 4));
        assertEquals(2, lev1Ship.checkShot(Direction.DOWN, Attack.HEAVY, 0));
        assertEquals(2, lev1Ship.checkShot(Direction.DOWN, Attack.HEAVY, 1));
        assertEquals(2, lev1Ship.checkShot(Direction.DOWN, Attack.HEAVY, 2));
        assertEquals(2, lev1Ship.checkShot(Direction.DOWN, Attack.HEAVY, 3));
        assertEquals(2, lev1Ship.checkShot(Direction.DOWN, Attack.HEAVY, 4));
    }

    @Test
    void checkAttack3() {
        lev2Ship = shipyard.createShip3();

        assertEquals(-1, lev2Ship.checkShot(Direction.UP, Attack.LIGHT, 0));
        assertEquals(0, lev2Ship.checkShot(Direction.UP, Attack.LIGHT, 1));
        assertEquals(0, lev2Ship.checkShot(Direction.UP, Attack.LIGHT, 2));
        assertEquals(0, lev2Ship.checkShot(Direction.UP, Attack.LIGHT, 3));
        assertEquals(0, lev2Ship.checkShot(Direction.UP, Attack.LIGHT, 4));
        assertEquals(0, lev2Ship.checkShot(Direction.UP, Attack.LIGHT, 5));
        assertEquals(-1, lev2Ship.checkShot(Direction.UP, Attack.LIGHT, 6));
        assertEquals(-1, lev2Ship.checkShot(Direction.UP, Attack.HEAVY, 0));
        assertEquals(2, lev2Ship.checkShot(Direction.UP, Attack.HEAVY, 1));
        assertEquals(2, lev2Ship.checkShot(Direction.UP, Attack.HEAVY, 2));
        assertEquals(2, lev2Ship.checkShot(Direction.UP, Attack.HEAVY, 3));
        assertEquals(2, lev2Ship.checkShot(Direction.UP, Attack.HEAVY, 4));
        assertEquals(2, lev2Ship.checkShot(Direction.UP, Attack.HEAVY, 5));
        assertEquals(-1, lev2Ship.checkShot(Direction.UP, Attack.HEAVY, 6));

        assertEquals(-1, lev2Ship.checkShot(Direction.RIGHT, Attack.LIGHT, 0));
        assertEquals(0, lev2Ship.checkShot(Direction.RIGHT, Attack.LIGHT, 1));
        assertEquals(0, lev2Ship.checkShot(Direction.RIGHT, Attack.LIGHT, 2));
        assertEquals(0, lev2Ship.checkShot(Direction.RIGHT, Attack.LIGHT, 3));
        assertEquals(0, lev2Ship.checkShot(Direction.RIGHT, Attack.LIGHT, 4));
        assertEquals(-1, lev2Ship.checkShot(Direction.RIGHT, Attack.HEAVY, 0));
        assertEquals(2, lev2Ship.checkShot(Direction.RIGHT, Attack.HEAVY, 1));
        assertEquals(2, lev2Ship.checkShot(Direction.RIGHT, Attack.HEAVY, 2));
        assertEquals(2, lev2Ship.checkShot(Direction.RIGHT, Attack.HEAVY, 3));
        assertEquals(2, lev2Ship.checkShot(Direction.RIGHT, Attack.HEAVY, 4));

        assertEquals(-1, lev2Ship.checkShot(Direction.LEFT, Attack.LIGHT, 0));
        assertEquals(2, lev2Ship.checkShot(Direction.LEFT, Attack.LIGHT, 1));
        assertEquals(2, lev2Ship.checkShot(Direction.LEFT, Attack.LIGHT, 2));
        assertEquals(2, lev2Ship.checkShot(Direction.LEFT, Attack.LIGHT, 3));
        assertEquals(2, lev2Ship.checkShot(Direction.LEFT, Attack.LIGHT, 4));
        assertEquals(-1, lev2Ship.checkShot(Direction.LEFT, Attack.HEAVY, 0));
        assertEquals(2, lev2Ship.checkShot(Direction.LEFT, Attack.HEAVY, 1));
        assertEquals(2, lev2Ship.checkShot(Direction.LEFT, Attack.HEAVY, 2));
        assertEquals(2, lev2Ship.checkShot(Direction.LEFT, Attack.HEAVY, 3));
        assertEquals(2, lev2Ship.checkShot(Direction.LEFT, Attack.HEAVY, 4));

        assertEquals(-1, lev2Ship.checkShot(Direction.DOWN, Attack.LIGHT, 0));
        assertEquals(2, lev2Ship.checkShot(Direction.DOWN, Attack.LIGHT, 1));
        assertEquals(2, lev2Ship.checkShot(Direction.DOWN, Attack.LIGHT, 2));
        assertEquals(2, lev2Ship.checkShot(Direction.DOWN, Attack.LIGHT, 3));
        assertEquals(2, lev2Ship.checkShot(Direction.DOWN, Attack.LIGHT, 4));
        assertEquals(2, lev2Ship.checkShot(Direction.DOWN, Attack.LIGHT, 5));
        assertEquals(-1, lev2Ship.checkShot(Direction.DOWN, Attack.LIGHT, 6));
        assertEquals(-1, lev2Ship.checkShot(Direction.DOWN, Attack.HEAVY, 0));
        assertEquals(2, lev2Ship.checkShot(Direction.DOWN, Attack.HEAVY, 1));
        assertEquals(2, lev2Ship.checkShot(Direction.DOWN, Attack.HEAVY, 2));
        assertEquals(2, lev2Ship.checkShot(Direction.DOWN, Attack.HEAVY, 3));
        assertEquals(2, lev2Ship.checkShot(Direction.DOWN, Attack.HEAVY, 4));
        assertEquals(2, lev2Ship.checkShot(Direction.DOWN, Attack.HEAVY, 5));
        assertEquals(-1, lev2Ship.checkShot(Direction.DOWN, Attack.HEAVY, 6));
    }

    @Test
    void handleMeteor() {

    }

    //@Test
    void handleMeteor2() {
        lev1Ship.placeTile(housingTile33, 2, 2);
        lev1Ship.placeTile(structuralTile53, 4, 3);
        lev1Ship.placeTile(propulsorTile76, 3, 0);
        lev1Ship.placeTile(propulsorTile94, 3, 1);
        lev1Ship.placeTile(housingTile46, 3, 3);
        lev1Ship.placeTile(alienSupportTile141, 3, 4);
        lev1Ship.placeTile(laserTile106, 2, 0);
        lev1Ship.placeTile(storageTile19, 2, 1);
        lev1Ship.placeTile(laserTile125, 2, 3); // different!!!!
        lev1Ship.placeTile(storageTile18, 1, 2);
        assertTrue(lev1Ship.isShipLegal());

        lev1Ship.handleHit(Direction.UP, 2);
        lev1Ship.handleHit(Direction.DOWN, 2);
        lev1Ship.handleHit(Direction.UP, 2);
        lev1Ship.handleHit(Direction.RIGHT, 2);
        lev1Ship.handleHit(Direction.LEFT, 2);
        lev1Ship.handleHit(Direction.LEFT, 2);
        lev1Ship.handleHit(Direction.LEFT, 2);
        assertFalse(lev1Ship.isShipLegal());

        assertFalse(lev1Ship.handleHit(Direction.RIGHT, 1));
        assertFalse(lev1Ship.handleHit(Direction.UP, -1));
        assertFalse(lev1Ship.handleHit(Direction.UP, 5));

    }

    @Test
    void handleMeteor3() {
        lev2Ship = shipyard.createShip3();
        assertTrue(lev2Ship.isShipLegal());

        assertFalse(lev2Ship.handleHit(Direction.RIGHT, 6));
    }

    @Test
    void handleShot() {
    }

    @Test
    void testToString() {
        System.out.println(lev1Ship.toString());
        System.out.println(lev2Ship.toString());
    }

    @Test
    void getLevel() {
        assertEquals(1, lev1Ship.getLevel());
        assertEquals(2, lev2Ship.getLevel());
    }

    @Test
    void getColor() {
        assertEquals(PlayerColor.BLUE, lev1Ship.getColor());
        assertEquals(PlayerColor.RED, lev2Ship.getColor());
    }

    @Test
    void isSlotValid() {
        lev2Ship = shipyard.createShip3();
        assertFalse(lev2Ship.isSlotValid(4,3));
        assertFalse(lev2Ship.isSlotValid(0,5));
        assertFalse(lev2Ship.isSlotValid(0,3));
        assertFalse(lev2Ship.isSlotValid(1,6));

        assertTrue(lev2Ship.isSlotValid(1,5));
        assertTrue(lev2Ship.isSlotValid(4,6));
    }
}