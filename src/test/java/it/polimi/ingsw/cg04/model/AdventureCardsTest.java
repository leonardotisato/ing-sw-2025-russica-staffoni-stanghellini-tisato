package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.adventureCards.AbandonedShip;
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

    private Game game;
    private Player p;
//    Tile centerTile33;
//    Tile housingTile35;
//    Tile propulsorTile71;
//    Tile batteryTile16;
//    Tile storageTile68;
//    Tile laserTile101;

    @BeforeEach
    void setUp() {
        game = new Game(1, "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json", "src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json");
        game.addPlayer("Filippo", PlayerColor.BLUE);
        p = game.getPlayer("Filippo");
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
        boxes.put(BoxType.YELLOW, 0);
        boxes.put(BoxType.GREEN, 0);
        p.getShip().placeTile(centerTile33, 2, 2);
        p.getShip().placeTile(housingTile35, 3, 2);
        p.getShip().placeTile(propulsorTile71, 3, 1);
        p.getShip().placeTile(batteryTile16, 2, 1);
        p.getShip().placeTile(storageTile68, 2, 3);
        p.getShip().placeTile(laserTile119, 1, 1);
        p.getShip().placeTile(laserTile131, 1, 2);
        p.getShip().placeTile(laserTile101, 1, 3);
//        p.getShip().addCrew(CrewType.HUMAN, 2, 2);
//        p.getShip().addCrew(CrewType.HUMAN, 3, 2);
        p.getShip().setBoxes(boxes, 2, 3);
        p.setCurrentCell(4);
    }
    
    @Test
    void testAbandonedShip() {
        List<Integer> numCrewMembersLost = new ArrayList<>();
        numCrewMembersLost.add(2);
        numCrewMembersLost.add(2);
        List<List<Integer>> coordinates = new ArrayList<>();
        coordinates.add(List.of(2, 2));
        coordinates.add(List.of(3, 2));
        game.setCurrentAdventureCard(game.getCardById(37));
        AbandonedShip card = (AbandonedShip) game.getCurrentAdventureCard();
        card.solveEffect(p, numCrewMembersLost, coordinates);
        assertEquals(6, p.getNumCredits());
        assertEquals(0, p.getShip().getNumCrew());
        assertEquals(0, p.getShip().getTile(2,2).getNumCrew());
        assertEquals(0, p.getShip().getTile(3,2).getNumCrew());
        assertEquals(3, p.getCurrentCell());
        assertThrows(RuntimeException.class, () -> card.solveEffect(p, List.of(1), List.of(List.of(2,1))));
    }
}
