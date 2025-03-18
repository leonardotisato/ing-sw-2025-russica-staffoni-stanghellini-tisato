package it.polimi.ingsw.cg04.model.tiles;



import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.utils.TileLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class TilesLoaderTest {
    @Test
    void AlienSupportTileFromJson() {
        // Carica il JSON di test
        ArrayList<Integer> faceDownTiles = new ArrayList<>();
        Map<Integer, Tile> tiles = TileLoader.loadTilesFromJson("src/test/java/it/polimi/ingsw/cg04/model/tiles/TilesFile.json", faceDownTiles);

        assertNotNull(tiles);
        assertFalse(tiles.isEmpty());

        Tile tile = tiles.get(1);
        AlienSupportTile alienSupportTile = (AlienSupportTile) tile;
        assertNotNull(tile);
        assertInstanceOf(AlienSupportTile.class, tile);
        assertEquals(CrewType.PINK_ALIEN, alienSupportTile.getSupportedAlienColor());
    }
}
