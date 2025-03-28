package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.Game;
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
        Game game = new Game(1, "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json", "src/main/java/it/polimi/ingsw/cg04/resources/game.getTilesDeckMap()File.json");
        game.addPlayer("Filippo", PlayerColor.BLUE);
        Player p = game.getPlayer("Filippo");
        Tile centerTile33 = game.getTilesDeckMap().get(33);
        Tile housingTile35 = game.getTilesDeckMap().get(35);
        housingTile35.rotate90dx();
        Tile propulsorTile71 = game.getTilesDeckMap().get(71);
        Tile batteryTile16 = game.getTilesDeckMap().get(16);
        Tile storageTile68 = game.getTilesDeckMap().get(68);
        Tile laserTile101 = game.getTilesDeckMap().get(101);
        laserTile101.rotate90dx();
        Tile laserTile119 = game.getTilesDeckMap().get(119);
        laserTile119.rotate90sx();
        Tile laserTile131 = game.getTilesDeckMap().get(131);
        Map<BoxType, Integer> boxes = new HashMap<>();
        boxes.put(BoxType.BLUE, 1);
        boxes.put(BoxType.RED, 1);
        p.getShip().placeTile(centerTile33, 2, 2);
        p.getShip().placeTile(housingTile35, 3, 2);
        p.getShip().placeTile(propulsorTile71, 3, 1);
        p.getShip().placeTile(batteryTile16, 2, 1);
        p.getShip().placeTile(storageTile68, 2, 3);
        p.getShip().placeTile(laserTile119, 1, 1);
        p.getShip().placeTile(laserTile131, 1, 2);
        p.getShip().placeTile(laserTile101, 1, 3);
        p.getShip().addCrew(CrewType.HUMAN, 2, 2);
        p.getShip().addCrew(CrewType.HUMAN, 3, 2);
        p.getShip().setBoxes(boxes, 2, 3);
    }
    
    @Test
    void testAbandonedShip() {
        
    }
}
