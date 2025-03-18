package it.polimi.ingsw.cg04.model.tiles;

import it.polimi.ingsw.cg04.model.enumerations.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AlienSupportTileTest {

    private Tile housingTile50;
    private Tile alienSupportTile137;

    @BeforeEach
    void setUp() {

        // housingTile50
        Map<Direction, Connection> connection50 = new HashMap<>();
        connection50.put(Direction.UP, Connection.DOUBLE);
        connection50.put(Direction.DOWN, Connection.DOUBLE);
        connection50.put(Direction.LEFT, Connection.UNIVERSAL);
        connection50.put(Direction.RIGHT, Connection.EMPTY);

        housingTile50 = new HousingTile(connection50, false, PlayerColor.BLUE);

        // alienSupportTile137
        Map<Direction, Connection> connection137 = new HashMap<>();
        connection137.put(Direction.UP, Connection.SINGLE);
        connection137.put(Direction.DOWN, Connection.EMPTY);
        connection137.put(Direction.LEFT, Connection.SINGLE);
        connection137.put(Direction.RIGHT, Connection.SINGLE);

        CrewType supportedAlienColor = CrewType.BROWN_ALIEN;

        alienSupportTile137 = new AlienSupportTile(connection137, supportedAlienColor);
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void broken() {
        assertInstanceOf(AlienSupportTile.class, alienSupportTile137);
        assertTrue(alienSupportTile137.getAdjacentHousingTiles().isEmpty());
        alienSupportTile137.addAdjacentHousingTile(housingTile50);
        assertTrue(alienSupportTile137.getAdjacentHousingTiles().contains(housingTile50));
    }
}