package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.SlaversState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions.CompareFirePowerAction;
import it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions.GetRewardsAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions.RemoveCrewAction;
import it.polimi.ingsw.cg04.model.Shipyard;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlaversStateTest {

    private GamesController controller;
    private Game game;
    private Player p1, p2, p3;
    private final Shipyard shipyard = new Shipyard();

    @BeforeEach
    void setUp() {

        game = new Game(2, "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json", "src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json");

        // set seed for controlled testing environment
        game.getBoard().setRandSeed(42);

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
    void testSlaversState() {
        // controller forces state of the game
        game.setCurrentAdventureCard(game.getCardById(1));
        game.setGameState(game.getCurrentAdventureCard().createState(game));

        // "firePower": 3 !!!
        SlaversState slaversState = (SlaversState) game.getGameState();
        slaversState.FORCE_OPPONENT_FIREPOWER(3);

        AdventureCardState currAdventureCardState = (AdventureCardState) game.getGameState();

        // new slavers adventure begins!
        PlayerAction aliceAction;
        PlayerAction bobAction;
        PlayerAction charlieAction;

        List<Coordinates> batteries = new ArrayList<>();
        List<Coordinates> doubleCannons = new ArrayList<>();
        List<Coordinates> housingUnits = new ArrayList<>();
        List<Integer> numCrewPerUnit = new ArrayList<>();

        // bob tries to act before his turn begin
        bobAction = new CompareFirePowerAction("Bob", null, null);
        assertInvalidState(bobAction, p2);

        // alice activate her double cannon and achieves tie
        batteries.add(new Coordinates(1, 2));
        doubleCannons.add(new Coordinates(1, 3));

        aliceAction = new CompareFirePowerAction("Alice", batteries, doubleCannons);
        try {
            controller.onActionReceived(aliceAction);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        batteries.clear();
        doubleCannons.clear();

        // now bob needs to play he will send a bunch of wrong messages because his cat is jumping on the keyboard

        // out of grid
        batteries.add(new Coordinates(-1, 2));
        doubleCannons.add(new Coordinates(88, 23));
        bobAction = new CompareFirePowerAction("Bob", batteries, doubleCannons);

        assertInvalidAction(bobAction, p2);
        batteries.clear();
        doubleCannons.clear();

        // in grid but invalid
        batteries.add(new Coordinates(1, 2));
        doubleCannons.add(new Coordinates(1, 3));

        bobAction = new CompareFirePowerAction("Bob", batteries, doubleCannons);

        assertInvalidAction(bobAction, p2);
        batteries.clear();
        doubleCannons.clear();

        // battery and cannon but cannon is not double
        batteries.add(new Coordinates(2, 5));
        doubleCannons.add(new Coordinates(2, 6));

        bobAction = new CompareFirePowerAction("Bob", batteries, doubleCannons);

        assertInvalidAction(bobAction, p2);
        batteries.clear();
        doubleCannons.clear();

        // now bob realizes he is screwed, he WILL lose and there is nothing he can do
        bobAction = new CompareFirePowerAction("Bob", null, null);
        try {
            controller.onActionReceived(bobAction);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        batteries.clear();
        doubleCannons.clear();

        // now bob is in removeCrew state and charlie needs to activate his cannons

        // charlie defeats the opponent
        batteries.add(new Coordinates(4, 1));
        doubleCannons.add(new Coordinates(3, 6));

        charlieAction = new CompareFirePowerAction("Charlie", batteries, doubleCannons);
        try {
            controller.onActionReceived(charlieAction);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        batteries.clear();
        doubleCannons.clear();

        // charlie accepts the reward too soon
        charlieAction = new GetRewardsAction("Charlie", true);
        try {
            controller.onActionReceived(charlieAction);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        // bob removes the crew, but too few are sent
        housingUnits.add(new Coordinates(2, 2));
        numCrewPerUnit.add(2);

        bobAction = new RemoveCrewAction("Bob", housingUnits, numCrewPerUnit);
        try {
            controller.onActionReceived(bobAction);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        housingUnits.clear();
        numCrewPerUnit.clear();

        // bob removes the crew
        housingUnits.add(new Coordinates(2, 2));
        housingUnits.add(new Coordinates(2, 3));
        numCrewPerUnit.add(2);
        numCrewPerUnit.add(1);

        bobAction = new RemoveCrewAction("Bob", housingUnits, numCrewPerUnit);
        try {
            controller.onActionReceived(bobAction);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        housingUnits.clear();
        numCrewPerUnit.clear();

        System.out.println(game.getBoard());
        try {
            controller.onActionReceived(charlieAction);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertEquals(5, p3.getNumCredits());
        System.out.println(game.getBoard());
    }


    private void assertInvalidAction(PlayerAction action, Player p) {
        assertThrows(InvalidActionException.class, () -> action.checkAction(p));
    }

    private void assertInvalidState(PlayerAction action, Player p) {
        assertThrows(InvalidStateException.class, () -> action.execute(p));
    }
}