package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.controller.PlayerActions.AdventureCardActions.*;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.controller.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.utils.Shipyard;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WarZoneStateTest {

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

        // p1.move(7);
        p1.move(11);
        p2.move(6);
        p3.move(5);

        game.getBoard().setRandSeed(0);

        // assertEquals(p1, game.getBoard().getCell(7));
        assertEquals(p1, game.getBoard().getCell(11));
        assertEquals(p2, game.getBoard().getCell(6));
        assertEquals(p3, game.getBoard().getCell(5));

        assertEquals(4, p1.getShip().getNumCrew());
        assertEquals(4, p2.getShip().getNumCrew());
        assertEquals(12, p3.getShip().getNumCrew());

        controller = new GamesController();
        controller.addGame(game);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testWarZoneState() {
        game.setCurrentAdventureCard(game.getCardById(16));
        game.setGameState(game.getCurrentAdventureCard().createState(game));

        AdventureCardState currAdventureCardState = (AdventureCardState) game.getGameState();

        // First penalty test

        PlayerAction leaderCompareCrew = new CompareCrewAction("Alice");
        try {
            controller.onActionReceived(leaderCompareCrew);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

//        PlayerAction notLeaderCompareCrew = new CompareCrewAction("Bob");
//        controller.onActionReceived(notLeaderCompareCrew);

        // Second penalty test

        List<Coordinates> p1Coords = new ArrayList<>();
        List<Integer> p1Num = new ArrayList<>();
        p1Coords.add(new Coordinates(1, 2));
        p1Num.add(1);
        PlayerAction usePropAction1 = new ChoosePropulsorAction("Alice", p1Coords, p1Num);
        try {
            controller.onActionReceived(usePropAction1);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        List<Coordinates> p2Coords = new ArrayList<>();
        List<Integer> p2Num = new ArrayList<>();
        PlayerAction usePropAction2 = new ChoosePropulsorAction("Bob", p2Coords, p2Num);
        try {
            controller.onActionReceived(usePropAction2);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        List<Coordinates> p3Coords = new ArrayList<>();
        List<Integer> p3Num = new ArrayList<>();
        PlayerAction usePropAction3 = new ChoosePropulsorAction("Charlie", p2Coords, p2Num);
        try {
            controller.onActionReceived(usePropAction3);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        List<Coordinates> worstPCoords = new ArrayList<>();
        List<Integer> crewNum = new ArrayList<>();
        worstPCoords.add(new Coordinates(2, 2));
        crewNum.add(1);
        worstPCoords.add(new Coordinates(2, 3));
        crewNum.add(1);
        PlayerAction removeCrew = new RemoveCrewAction("Bob", worstPCoords, crewNum);
        try {
            controller.onActionReceived(removeCrew);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        // Third penalty test

        List<Coordinates> p1Batteries = new ArrayList<>();
        List<Coordinates> p1Cannons = new ArrayList<>();
        PlayerAction useCannonAction1 = new CompareFirePowerAction("Alice", p1Batteries, p1Cannons);
        try {
            controller.onActionReceived(useCannonAction1);
        } catch (InvalidActionException | InvalidStateException ignored) {        }



        List<Coordinates> p2Batteries = new ArrayList<>();
        List<Coordinates> p2Cannons = new ArrayList<>();
        PlayerAction useCannonAction2 = new CompareFirePowerAction("Bob", p2Batteries, p2Cannons);
        try {
            controller.onActionReceived(useCannonAction2);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        List<Coordinates> p3Batteries = new ArrayList<>();
        List<Coordinates> p3Cannons = new ArrayList<>();
        PlayerAction useCannonAction3 = new CompareFirePowerAction("Charlie", p3Batteries, p3Cannons);
        try {
            controller.onActionReceived(useCannonAction3);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        PlayerAction aliceRolls1 = new RollDiceAction("Alice");
        try {
            controller.onActionReceived(aliceRolls1);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        PlayerAction aliceUse1 = new ChooseBatteryAction("Alice", -1, -1);
        // PlayerAction aliceUse1 = new ChooseBatteryAction("Alice", -1, -1);
        try {
            controller.onActionReceived(aliceUse1);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


//        List<Coordinates> tileCoords = new ArrayList<>();
//        tileCoords.add(new Coordinates(3, 1));
//        PlayerAction aliceRemove1 = new FixShipAction("Alice", tileCoords);
//        controller.onActionReceived(aliceRemove1);

        PlayerAction aliceRolls2 = new RollDiceAction("Alice");
        try {
            controller.onActionReceived(aliceRolls2);
        } catch (InvalidActionException | InvalidStateException ignored) {        }



        
    }
}