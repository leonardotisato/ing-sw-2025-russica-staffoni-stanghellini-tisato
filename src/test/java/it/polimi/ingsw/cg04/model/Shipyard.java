package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.TileLoader;

import java.util.ArrayList;
import java.util.Map;

public class Shipyard {

    private Map<Integer, Tile> tiles;

    /**
     *
     * @return a level 1 ship
     */
    public Ship createShip2() {
        ArrayList<Integer> faceDownTiles = new ArrayList<>();
        tiles = TileLoader.loadTilesFromJson("src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json", faceDownTiles);

        Ship lev1Ship = new Ship(1, PlayerColor.BLUE);

        lev1Ship.placeTile(tiles.get(33), 2, 2);
        lev1Ship.placeTile(tiles.get(53), 4, 3);
        lev1Ship.placeTile(tiles.get(76), 3, 0);
        lev1Ship.placeTile(tiles.get(94), 3, 1);
        lev1Ship.placeTile(tiles.get(46), 3, 3);
        lev1Ship.placeTile(tiles.get(141), 3, 4);
        lev1Ship.placeTile(tiles.get(106), 2, 0);
        lev1Ship.placeTile(tiles.get(19), 2, 1);
        lev1Ship.placeTile(tiles.get(125), 2, 3);
        lev1Ship.placeTile(tiles.get(18), 1, 2);

        return lev1Ship;
    }

    /**
     *
     * @return a level 2 ship
     */
    public Ship createShip3() {
        ArrayList<Integer> faceDownTiles = new ArrayList<>();
        tiles = TileLoader.loadTilesFromJson("src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json", faceDownTiles);

        Ship ship3 = new Ship(2, PlayerColor.BLUE);

        tiles.get(14).rotate90dx();
        ship3.placeTile(tiles.get(14), 1,2);

        ship3.placeTile(tiles.get(136), 1,3);

        tiles.get(68).rotate90dx();
        tiles.get(68).rotate90dx();
        ship3.placeTile(tiles.get(68), 2, 1);
        
        tiles.get(149);
        ship3.placeTile(tiles.get(149), 2, 2);

        ship3.placeTile(tiles.get(34), 2, 3);

        ship3.placeTile(tiles.get(37), 2, 4);

        ship3.placeTile(tiles.get(118), 2, 5);

        ship3.placeTile(tiles.get(99), 3, 1);

        tiles.get(15).rotate90dx();
        tiles.get(15).rotate90dx();
        tiles.get(15).rotate90dx();
        ship3.placeTile(tiles.get(15), 3, 2);

        ship3.placeTile(tiles.get(30), 3, 3);

        tiles.get(57).rotate90dx();
        ship3.placeTile(tiles.get(57), 3, 4);
        
        tiles.get(74);
        ship3.placeTile(tiles.get(74), 4, 4);

        return ship3;
    }
}
