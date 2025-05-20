package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Shipyard;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LoadCrewActionTest {

    private Game game;
    private Player p1, p2, p3;
    private final Shipyard shipyard = new Shipyard();
    private GamesController controller;

    @BeforeEach
    void setUp() throws IOException {
        game = new Game(2);

        p1 = game.addPlayer("Alice", PlayerColor.RED);
        p2 = game.addPlayer("Bob", PlayerColor.BLUE);
        p3 = game.addPlayer("Charlie", PlayerColor.GREEN);

        p1.setShip(shipyard.createShip3());
        p2.setShip(shipyard.createShip4());
        p3.setShip(shipyard.createShip5());

        assertTrue(p1.getShip().isShipLegal());
        assertTrue(p2.getShip().isShipLegal());
        assertTrue(p3.getShip().isShipLegal());

        p1.move(5);
        p2.move(3);
        p3.move(2);

        controller = new GamesController();
        controller.addGame(game);

        assertEquals(p1, game.getBoard().getCell(5));
        assertEquals(p2, game.getBoard().getCell(3));
        assertEquals(p3, game.getBoard().getCell(2));

    }

    @Test
    void checkAction() {

        Coordinates pinkAlienCoords;
        Coordinates brownAlienCoords;
        PlayerAction loadCrewAction;

        // invalid coords
        pinkAlienCoords = new Coordinates(0, 0);
        brownAlienCoords = new Coordinates(0,1);
        loadCrewAction = new LoadCrewAction("Alice", pinkAlienCoords, brownAlienCoords);
        assertThrowsOnAction(loadCrewAction, p1);

        // in grid but empty
        pinkAlienCoords = new Coordinates(0, 2);
        brownAlienCoords = new Coordinates(1,1);
        loadCrewAction = new LoadCrewAction("Alice", pinkAlienCoords, brownAlienCoords);
        assertThrowsOnAction(loadCrewAction, p1);

        // out of grid
        pinkAlienCoords = new Coordinates(-12, 2);
        brownAlienCoords = new Coordinates(10,1);
        loadCrewAction = new LoadCrewAction("Alice", pinkAlienCoords, brownAlienCoords);
        assertThrowsOnAction(loadCrewAction, p1);

        // in ship but other tiles
        pinkAlienCoords = new Coordinates(2, 1);
        brownAlienCoords = new Coordinates(2,2);
        loadCrewAction = new LoadCrewAction("Alice", pinkAlienCoords, brownAlienCoords);
        assertThrowsOnAction(loadCrewAction, p1);

        // one null other tile
        pinkAlienCoords = new Coordinates(0, 4);
        brownAlienCoords = new Coordinates(1,6);
        loadCrewAction = new LoadCrewAction("Alice", pinkAlienCoords, brownAlienCoords);
        assertThrowsOnAction(loadCrewAction, p1);

        // two same housing tiles
        pinkAlienCoords = new Coordinates(1, 5);
        brownAlienCoords = new Coordinates(1,5);
        loadCrewAction = new LoadCrewAction("Alice", pinkAlienCoords, brownAlienCoords);
        assertThrowsOnAction(loadCrewAction, p1);


        // now test on ship 5 (p3) since it can host both aliens

        // two housing where alien cannot live
        pinkAlienCoords = new Coordinates(3, 0);
        brownAlienCoords = new Coordinates(3,1);
        loadCrewAction = new LoadCrewAction("Charlie", pinkAlienCoords, brownAlienCoords);
        assertThrowsOnAction(loadCrewAction, p3);

        // brown correct, pink wrong
        pinkAlienCoords = new Coordinates(3, 0);
        brownAlienCoords = new Coordinates(1,2);
        loadCrewAction = new LoadCrewAction("Charlie", pinkAlienCoords, brownAlienCoords);
        assertThrowsOnAction(loadCrewAction, p3);

        // brown correct, pink wrong but a support is neighbouring!
        pinkAlienCoords = new Coordinates(1, 4);
        brownAlienCoords = new Coordinates(1,2);
        loadCrewAction = new LoadCrewAction("Charlie", pinkAlienCoords, brownAlienCoords);
        assertThrowsOnAction(loadCrewAction, p3);

        // brown correct, pink wrong because alien is place in central
        pinkAlienCoords = new Coordinates(2, 3);
        brownAlienCoords = new Coordinates(1,2);
        loadCrewAction = new LoadCrewAction("Charlie", pinkAlienCoords, brownAlienCoords);
        assertThrowsOnAction(loadCrewAction, p3); // todo for some reason pink passes the test...

        // both correct
        pinkAlienCoords = new Coordinates(3, 4);
        brownAlienCoords = new Coordinates(1,2);
        loadCrewAction = new LoadCrewAction("Charlie", pinkAlienCoords, brownAlienCoords);
        assertDoesNotThrowsOnAction(loadCrewAction, p3);

        // brown null other wrong
        pinkAlienCoords = new Coordinates(1, 4);
        brownAlienCoords = null;
        loadCrewAction = new LoadCrewAction("Charlie", pinkAlienCoords, brownAlienCoords);
        assertThrowsOnAction(loadCrewAction, p3);

        // pink null brown correct
        pinkAlienCoords = null;
        brownAlienCoords = new Coordinates(1,2);
        loadCrewAction = new LoadCrewAction("Charlie", pinkAlienCoords, brownAlienCoords);
        assertDoesNotThrowsOnAction(loadCrewAction, p3);

        // both null
        pinkAlienCoords = null;
        brownAlienCoords = null;
        loadCrewAction = new LoadCrewAction("Charlie", pinkAlienCoords, brownAlienCoords);
        assertDoesNotThrowsOnAction(loadCrewAction, p3);


    }

    private void assertThrowsOnAction(PlayerAction action, Player p) {
        assertThrows(InvalidActionException.class, () -> action.checkAction(p));
    }

    private void assertDoesNotThrowsOnAction(PlayerAction action, Player p) {
        assertDoesNotThrow(() -> action.checkAction(p));
    }

}