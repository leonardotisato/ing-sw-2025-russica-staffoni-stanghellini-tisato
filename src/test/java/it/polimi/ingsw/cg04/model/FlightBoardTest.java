package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FlightBoardTest {

    FlightBoard fb1, fb2;
    Player p1, p2, p3, p4;
    Game game;

    int startPositionOfFirst, startPositionOfSecond, startPositionOfThird, startPositionOfFourth;

    @BeforeEach
    void setUp() {
        game = new Game(1);

        fb1 = new FlightBoardLev1(game);

        p1 = new Player("Alice", PlayerColor.RED, game);
        p2 = new Player("Bob", PlayerColor.BLUE, game);
        p3 = new Player("Charlie", PlayerColor.GREEN, game);
        p4 = new Player("David", PlayerColor.YELLOW, game);

        // occupy/free cells and check that players are correctly placed
        startPositionOfFirst = fb1.getStartingPosition(1);
        startPositionOfSecond = fb1.getStartingPosition(2);
        startPositionOfThird = fb1.getStartingPosition(3);
        startPositionOfFourth = fb1.getStartingPosition(4);

        fb1.occupyCell(startPositionOfFirst, p1);
        fb1.occupyCell(startPositionOfSecond, p2);
        fb1.occupyCell(startPositionOfThird, p3);
        fb1.occupyCell(startPositionOfFourth, p4);

        fb2 = new FlightBoardLev2(game);
        fb2.startTimer();

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
        System.out.println(fb1.pathSize - 1);
        assertEquals(p3, fb1.getCell(fb1.pathSize - 1));
        assertNull(fb1.getCell(1));
        assertEquals(-1, p3.getLoops());

        fb1.move(p4, -1);
        System.out.println(fb1.pathSize - 2);
        assertEquals(p4, fb1.getCell(fb1.pathSize - 2));
        assertNull(fb1.getCell(0));
        assertEquals(-1, p4.getLoops());

        System.out.println(fb1.toString());

    }

    @Test
    void getMostBeautifulShipCredits() {
    }

    @Test
    void getPath() {

        Player[] path = fb1.getPath();

        assertNotNull(path);
        assertEquals(p1, fb1.getCell(startPositionOfFirst));
        assertEquals(p2, fb1.getCell(startPositionOfSecond));
        assertEquals(p3, fb1.getCell(startPositionOfThird));
        assertEquals(p4, fb1.getCell(startPositionOfFourth));

    }

    @Test
    void testToString() {
        System.out.println(fb1.toString());
    }

    @Test
    void startTimer() {
        assertFalse(fb2.isTimerExpired());
    }

    @Test
    void drawTestLev1() {
        fb1.move(p1, 6);
        fb1.move(p2, 6);
        //fb1.move(p3, 6);
        //fb1.move(p4, 6);
        System.out.println(fb1.draw());
    }

    @Test
    void drawTestLev2(){
        game = new Game(2);

        fb2 = new FlightBoardLev2(game);

        p1 = new Player("Alice", PlayerColor.RED, game);
        p2 = new Player("Bob", PlayerColor.BLUE, game);
        p3 = new Player("Charlie", PlayerColor.GREEN, game);
        p4 = new Player("David", PlayerColor.YELLOW, game);

        // occupy/free cells and check that players are correctly placed
        startPositionOfFirst = fb2.getStartingPosition(1);
        startPositionOfSecond = fb2.getStartingPosition(2);
        startPositionOfThird = fb2.getStartingPosition(3);
        startPositionOfFourth = fb2.getStartingPosition(4);

        fb2.occupyCell(startPositionOfFirst, p1);
        fb2.occupyCell(startPositionOfSecond, p2);
        fb2.occupyCell(startPositionOfThird, p3);
        fb2.occupyCell(startPositionOfFourth, p4);
        System.out.println(fb2.draw());

        fb2.move(p1, 6);
        fb2.move(p4, -3);
        fb2.move(p3, -3);
        fb2.move(p2, -4);


        System.out.println(fb2.draw());


    }

//    @Test
//    void isTimerExpired() throws InterruptedException {
//        Thread.sleep(5100);
//        assertTrue(fb2.isTimerExpired());
//    }
}