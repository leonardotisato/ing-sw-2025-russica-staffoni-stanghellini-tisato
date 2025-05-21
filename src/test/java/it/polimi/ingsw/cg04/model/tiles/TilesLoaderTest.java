package it.polimi.ingsw.cg04.model.tiles;



import it.polimi.ingsw.cg04.model.utils.TileLoader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class TilesLoaderTest {
    @Test
    void LoadAllFromJson() {
        // Carica il JSON di test
        ArrayList<Integer> faceDownTiles = new ArrayList<>();
        Map<Integer, Tile> tiles = TileLoader.loadTilesFromJson("jsons/MiniTilesFile.json", faceDownTiles);

        assertNotNull(tiles);
        assertNotNull(faceDownTiles);
        assertFalse(tiles.isEmpty());
        assertFalse(faceDownTiles.isEmpty());

        assertInstanceOf(AlienSupportTile.class, tiles.get(1));
        assertInstanceOf(BatteryTile.class, tiles.get(2));
        assertInstanceOf(HousingTile.class, tiles.get(3));
        assertInstanceOf(LaserTile.class, tiles.get(4));
        assertInstanceOf(PropulsorTile.class, tiles.get(5));
        assertInstanceOf(ShieldTile.class, tiles.get(6));
        assertInstanceOf(StorageTile.class, tiles.get(7));

    }
}
