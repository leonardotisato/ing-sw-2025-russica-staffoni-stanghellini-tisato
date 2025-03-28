package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.CardLoader;
import it.polimi.ingsw.cg04.model.utils.TileLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AdventureCardsTest {

    @BeforeEach
    void setUp() {
        List<Integer> cards1 = new ArrayList<>();
        List<Integer> cards2 = new ArrayList<>();
        Map<Integer, AdventureCard> cards = CardLoader.loadCardsFromJson("src/test/java/it/polimi/ingsw/cg04/model/resources/AdventureCardsFile.json", cards1, cards2);
        ArrayList<Integer> faceDownTiles = new ArrayList<>();
        Map<Integer, Tile> tiles = TileLoader.loadTilesFromJson("src/test/java/it/polimi/ingsw/cg04/model/tiles/TilesFile.json", faceDownTiles);
        Ship ship = new Ship(1, PlayerColor.BLUE);
        Tile centerTile33 = tiles.get(33);
        Tile housingTile35 = tiles.get(35);
        housingTile35.rotate90dx();
        Tile propulsorTile71 = tiles.get(71);
        Tile batteryTile16 = tiles.get(16);
        Tile storageTile68 = tiles.get(68);
        Tile laserTile101 = tiles.get(101);
        laserTile101.rotate90dx();
        Tile laserTile119 = tiles.get(119);
        laserTile119.rotate90sx();
        Tile laserTile131 = tiles.get(131);
        Map<BoxType, Integer> boxes = new HashMap<>();
        boxes.put(BoxType.BLUE, 1);
        boxes.put(BoxType.RED, 1);
        ship.placeTile(centerTile33, 2, 2);
        ship.placeTile(housingTile35, 3, 2);
        ship.placeTile(propulsorTile71, 3, 1);
        ship.placeTile(batteryTile16, 2, 1);
        ship.placeTile(storageTile68, 2, 3);
        ship.placeTile(laserTile119, 1, 1);
        ship.placeTile(laserTile131, 1, 2);
        ship.placeTile(laserTile101, 1, 3);
        ship.addCrewByType(CrewType.HUMAN, 2, 2);
        ship.addCrewByType(CrewType.HUMAN, 3, 2);
        ship.setBoxes(boxes, 2, 3);



    }
}
