package it.polimi.ingsw.cg04.model.utils;

import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Shipyard implements Serializable {

    ArrayList<Integer> faceDownTiles = new ArrayList<>();
    Map<Integer, Tile> tiles = TileLoader.loadTilesFromJson("jsons/TilesFile.json", faceDownTiles);

    /**
     * @return a level 1 ship
     */
    public Ship createShip2() {

        Map<Integer, Tile> tiles = TileLoader.loadTilesFromJson("jsons/TilesFile.json", faceDownTiles);

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
        /*
        lev1Ship.placeTile(tiles.get(103), 0, 2);
        lev1Ship.placeTile(tiles.get(105), 1, 1);
        tiles.get(154).rotate90sx();
        lev1Ship.placeTile(tiles.get(154), 1, 2);
        lev1Ship.placeTile(tiles.get(35), 1, 3);
        tiles.get(14).rotate90sx();
        lev1Ship.placeTile(tiles.get(14), 2, 0);
        tiles.get(26).rotate90dx();
        lev1Ship.placeTile(tiles.get(26), 2, 1);
        lev1Ship.placeTile(tiles.get(33), 2, 2);
        tiles.get(27).rotate180();
        lev1Ship.placeTile(tiles.get(6), 2, 3);

        lev1Ship.placeTile(tiles.get(10), 2, 4);
        lev1Ship.placeTile(tiles.get(33), 0, 0);
        lev1Ship.placeTile(tiles.get(33), 0, 0);
        lev1Ship.placeTile(tiles.get(33), 0, 0);
        lev1Ship.placeTile(tiles.get(33), 0, 0);
        lev1Ship.placeTile(tiles.get(33), 0, 0);
        lev1Ship.placeTile(tiles.get(33), 0, 0);
        lev1Ship.placeTile(tiles.get(33), 0, 0);
        lev1Ship.placeTile(tiles.get(33), 0, 0);
        lev1Ship.placeTile(tiles.get(33), 0, 0);

         */

        return lev1Ship;
    }

    /**
     * @return a level 2 ship
     */
    public Ship createShip3() {

        Map<Integer, Tile> tiles = TileLoader.loadTilesFromJson("jsons/TilesFile.json", faceDownTiles);

        Ship ship3 = new Ship(2, PlayerColor.BLUE);

        tiles.get(14).rotate90dx();
        ship3.placeTile(tiles.get(14), 1, 2);

        ship3.placeTile(tiles.get(136), 1, 3);

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

    public Ship createShip4() {

        Map<Integer, Tile> tiles = TileLoader.loadTilesFromJson("jsons/TilesFile.json", faceDownTiles);

        Ship ship4 = new Ship(2, PlayerColor.RED);

        ship4.placeTile(tiles.get(149), 1, 4);
        ship4.placeTile(tiles.get(141), 2, 1);
        ship4.placeTile(tiles.get(41), 2, 2);
        ship4.placeTile(tiles.get(52), 2, 3);
        ship4.placeTile(tiles.get(59), 2, 4);
        ship4.placeTile(tiles.get(10), 2, 5);
        ship4.placeTile(tiles.get(114), 2, 6);
        ship4.placeTile(tiles.get(98), 3, 2);
        //ship4.placeTile(tiles.get(75), 3, 3); // !!!!!!
        ship4.placeTile(tiles.get(20), 3, 6);

        return ship4;
    }

    public Ship createShip5() {

        Map<Integer, Tile> tiles = TileLoader.loadTilesFromJson("jsons/TilesFile.json", faceDownTiles);

        Ship lev2Ship = new Ship(2, PlayerColor.YELLOW);

        Tile alienSupportTile139 = tiles.get(139);
        alienSupportTile139.rotate90dx();
        alienSupportTile139.rotate90dx();
        alienSupportTile139.rotate90dx();
        lev2Ship.placeTile(alienSupportTile139, 0, 2);


        Tile storageTile70 = tiles.get(70);
        storageTile70.rotate90dx();
        storageTile70.rotate90dx();
        storageTile70.rotate90dx();
        lev2Ship.placeTile(storageTile70, 0, 4);


        Tile structuralTile59 = tiles.get(59);
        structuralTile59.rotate90dx();
        lev2Ship.placeTile(structuralTile59, 1, 1);


        Tile housingTile37 = tiles.get(37);
        lev2Ship.placeTile(housingTile37, 1, 2);


        Tile shieldTile151 = tiles.get(151);
        shieldTile151.rotate90dx();
        shieldTile151.rotate90dx();
        shieldTile151.rotate90dx();
        lev2Ship.placeTile(shieldTile151, 1, 3);


        Tile housingTile49 = tiles.get(49);
        housingTile49.rotate90dx();
        lev2Ship.placeTile(housingTile49, 1, 4);


        Tile propulsorTile89 = tiles.get(89);
        lev2Ship.placeTile(propulsorTile89, 1, 5);


        Tile laserTile114 = tiles.get(114);
        laserTile114.rotate90dx();
        laserTile114.rotate90dx();
        laserTile114.rotate90dx();
        lev2Ship.placeTile(laserTile114, 2, 0);


        Tile batteryTile6 = tiles.get(6);
        lev2Ship.placeTile(batteryTile6, 2, 1);


        Tile storageTile19 = tiles.get(19);
        lev2Ship.placeTile(storageTile19, 2, 2);


        Tile housingTile61 = tiles.get(61);
        lev2Ship.placeTile(housingTile61, 2, 3);


        Tile alienSupportTile146 = tiles.get(146);
        lev2Ship.placeTile(alienSupportTile146, 2, 4);


        Tile housingTile40 = tiles.get(40);
        housingTile40.rotate90dx();
        lev2Ship.placeTile(housingTile40, 3, 0);


        Tile housingTile44 = tiles.get(44);
        lev2Ship.placeTile(housingTile44, 3, 1);


        Tile propulsorTile95 = tiles.get(95);
        lev2Ship.placeTile(propulsorTile95, 3, 2);


        Tile batteryTile11 = tiles.get(11);
        lev2Ship.placeTile(batteryTile11, 3, 3);


        Tile housingTile50 = tiles.get(50);
        housingTile50.rotate90dx();
        housingTile50.rotate90dx();
        lev2Ship.placeTile(housingTile50, 3, 4);


        Tile laserTile120 = tiles.get(120);
        lev2Ship.placeTile(laserTile120, 3, 5);


        Tile laserTile126 = tiles.get(126);
        lev2Ship.placeTile(laserTile126, 3, 6);


        Tile storageTile26 = tiles.get(26);
        storageTile26.rotate90dx();
        lev2Ship.placeTile(storageTile26, 4, 0);


        Tile batteryTile17 = tiles.get(17);
        lev2Ship.placeTile(batteryTile17, 4, 1);


        Tile propulsorTile78 = tiles.get(78);
        lev2Ship.placeTile(propulsorTile78, 4, 4);


        Tile storageTile64 = tiles.get(64);
        lev2Ship.placeTile(storageTile64, 4, 5);


        Tile shieldTile150 = tiles.get(150);
        lev2Ship.placeTile(shieldTile150, 4, 6);


        return lev2Ship;
    }
}
