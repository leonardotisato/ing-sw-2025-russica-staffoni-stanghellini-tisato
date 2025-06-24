package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.controller.PlayerActions.LoadCrewAction;
import it.polimi.ingsw.cg04.controller.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.utils.Shipyard;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LoadCrewStateTest {

    private GamesController controller;
    private Game game;
    private Player p1, p2, p3;
    private final Shipyard shipyard = new Shipyard();

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

        assertEquals(p1, game.getBoard().getCell(5));
        assertEquals(p2, game.getBoard().getCell(3));
        assertEquals(p3, game.getBoard().getCell(2));

        controller = new GamesController();
        controller.addGame(game);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void testLoadCrewState() {
        game.setGameState(new LoadCrewState(game));

        PlayerAction aliceAction;
        PlayerAction bobAction;
        PlayerAction charlieAction;

        Coordinates pinkAlienCoords;
        Coordinates brownAlienCoords;

        // bob try to load before alice
        bobAction = new LoadCrewAction("Bob", null, null);
        try {
            controller.onActionReceived(bobAction);
        } catch (InvalidActionException | InvalidStateException ignored) {
        }


        // alice loads her crew
        aliceAction = new LoadCrewAction("Alice", null, null);
        try {
            controller.onActionReceived(aliceAction);
        } catch (InvalidActionException | InvalidStateException ignored) {
        }


        // make sure her ship has the right number of crew per type
        assertEquals(4, p1.getShip().getNumCrewByType(CrewType.HUMAN));
        assertEquals(0, p1.getShip().getNumCrewByType(CrewType.PINK_ALIEN));
        assertEquals(0, p1.getShip().getNumCrewByType(CrewType.BROWN_ALIEN));

        // bob loads his crew
        brownAlienCoords = new Coordinates(2, 2);
        bobAction = new LoadCrewAction("Bob", null, brownAlienCoords);
        try {
            controller.onActionReceived(bobAction);
        } catch (InvalidActionException | InvalidStateException ignored) {
        }

        assertEquals(2, p2.getShip().getNumCrewByType(CrewType.HUMAN));
        assertEquals(1, p2.getShip().getNumCrewByType(CrewType.BROWN_ALIEN));

        // charlie loads his crew
        pinkAlienCoords = new Coordinates(3, 4);
        brownAlienCoords = new Coordinates(1, 2);
        charlieAction = new LoadCrewAction("Charlie", pinkAlienCoords, brownAlienCoords);
        try {
            controller.onActionReceived(charlieAction);
        } catch (InvalidActionException | InvalidStateException ignored) {
        }

        assertEquals(8, p3.getShip().getNumCrewByType(CrewType.HUMAN));
        assertEquals(1, p3.getShip().getNumCrewByType(CrewType.BROWN_ALIEN));
        assertEquals(1, p3.getShip().getNumCrewByType(CrewType.PINK_ALIEN));

        // assert state of the game has changed
        assertInstanceOf(FlightState.class, game.getGameState());

    }

    @Test
    void renderLoadCrewStateTest() throws InterruptedException, InvalidStateException {
        game.setGameState(new LoadCrewState(game));
        LoadCrewState state = (LoadCrewState) game.getGameState();
        System.out.println(state.render("Alice"));
    }

    @Test
    void IncorrectStateTest() {
        game.setGameState(new LoadCrewState(game));
        LoadCrewState state = (LoadCrewState) game.getGameState();
        assertThrows(InvalidStateException.class, () -> {
            state.placeTile(null, 0, 0);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.setShip(null, 0, 0);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.rotateTile(null, null);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.placeInBuffer(null);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.chooseTile(null, 0);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.chooseTileFromBuffer(null, 0);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.showFaceUp(null);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.drawFaceDown(null);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.returnTile(null);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.closeFaceUpTiles(null);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.pickPile(null, 1);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.endBuilding(null, 1);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.startTimer(null);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.stopBuilding(null);
        });
        assertThrows(InvalidStateException.class, () -> {
            state.spreadEpidemic(null);
        });


    }
}