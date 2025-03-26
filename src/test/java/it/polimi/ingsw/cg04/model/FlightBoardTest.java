package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightBoardTest {

    FlightBoard fb1;
    Player p1, p2, p3, p4;
    Game game;

    @BeforeEach
    void setUp() {
        game = new Game(1, "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json", "src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json");

        fb1 = new FlightBoardLev1();

        p1 = new Player("Alice", PlayerColor.RED, game);
        p2 = new Player("Bob", PlayerColor.BLUE, game);
        p3 = new Player("Charlie", PlayerColor.GREEN, game);
        p4 = new Player("David", PlayerColor.GREEN, game);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getPathSize() {
        assertEquals(18, fb1.getPathSize());
    }

    @Test
    void occupyCell() {

    }

    @Test
    void freeCell() {
    }

    @Test
    void move() {
        // occupy/free cells and check that players are correctly placed
        int startPositionOfFirst = fb1.getStartingPosition(1);
        int startPositionOfSecond = fb1.getStartingPosition(2);
        int startPositionOfThird = fb1.getStartingPosition(3);
        int startPositionOfFourth = fb1.getStartingPosition(4);

        fb1.occupyCell(startPositionOfFirst, p1);
        fb1.occupyCell(startPositionOfSecond, p2);
        fb1.occupyCell(startPositionOfThird, p3);
        fb1.occupyCell(startPositionOfFourth, p4);

        // move p2 one step forward
        fb1.move(p2, 1);
        assertEquals(p2, fb1.getCell(3));
        assertNull(fb1.getCell(2));

        // move p2 one step forward, skipping p1
        fb1.move(p2, 1);
        assertEquals(p2, fb1.getCell(5));
        assertNull(fb1.getCell(3));
        assertNull(fb1.getCell(2));

        // move p3 one step backwards, skipping p4
        fb1.move(p3, -1);
        System.out.println(fb1.pathSize-1);
        assertEquals(p3, fb1.getCell(fb1.pathSize-1));
        assertNull(fb1.getCell(1));

        fb1.move(p4, -1);
        System.out.println(fb1.pathSize-2);
        assertEquals(p4, fb1.getCell(fb1.pathSize-2));
        assertNull(fb1.getCell(0));

    }

    @Test
    void giveMostBeautifulShipCredits() {
    }

    @Test
    void getPath() {

        Player[] path = fb1.getPath();
        assertNotNull(path);
        for (Player p : path) {
            assertNull(p);
        }

        // occupy/free cells and check that players are correctly placed
        int startPositionOfFirst = fb1.getStartingPosition(1);
        int startPositionOfSecond = fb1.getStartingPosition(2);
        int startPositionOfThird = fb1.getStartingPosition(3);
        int startPositionOfFourth = fb1.getStartingPosition(4);

        fb1.occupyCell(startPositionOfFirst, p1);
        fb1.occupyCell(startPositionOfSecond, p2);
        fb1.occupyCell(startPositionOfThird, p3);
        fb1.occupyCell(startPositionOfFourth, p4);

        path = fb1.getPath();

        assertNotNull(path);
        assertEquals(p1, fb1.getCell(startPositionOfFirst));
        assertEquals(p2, fb1.getCell(startPositionOfSecond));
        assertEquals(p3, fb1.getCell(startPositionOfThird));
        assertEquals(p4, fb1.getCell(startPositionOfFourth));

    }
}