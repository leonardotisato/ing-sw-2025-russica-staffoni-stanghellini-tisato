package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.controller.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.PiratesState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.controller.PlayerActions.AdventureCardActions.ChooseBatteryAction;
import it.polimi.ingsw.cg04.controller.PlayerActions.AdventureCardActions.CompareFirePowerAction;
import it.polimi.ingsw.cg04.controller.PlayerActions.AdventureCardActions.GetRewardsAction;
import it.polimi.ingsw.cg04.controller.PlayerActions.AdventureCardActions.RollDiceAction;
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

class PiratesStateTest {

    private GamesController controller;
    private Game game;
    private Player p1, p2, p3;
    private final Shipyard shipyard = new Shipyard();

    @BeforeEach
    void setUp() throws IOException {
        game = new Game(2);

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
    void testPiratesState() {

        game.setCurrentAdventureCard(game.getCardById(3));
        game.setGameState(game.getCurrentAdventureCard().createState(game));

        // "firePower": 3 !!! specific cast is done so force firepower can be called
        PiratesState piratesState = (PiratesState) game.getGameState();
        piratesState.FORCE_OPPONENT_FIREPOWER(3);

        AdventureCardState currAdventureCardState = (AdventureCardState) game.getGameState();

        // new slavers adventure begins!
        PlayerAction aliceAction;
        PlayerAction bobAction;
        PlayerAction charlieAction;

        List<Coordinates> batteries = new ArrayList<>();
        List<Coordinates> doubleCannons = new ArrayList<>();

        // bob tries to act before his turn begin
        bobAction = new CompareFirePowerAction("Bob", null, null);
        assertInvalidState(bobAction, p2);

        // alice activate her double cannon and achieves tie
        batteries.add(new Coordinates(1, 2));
        doubleCannons.add(new Coordinates(1, 3));

        aliceAction = new CompareFirePowerAction("Alice", batteries, doubleCannons);
        try {
            controller.onActionReceived(aliceAction);
        } catch (InvalidActionException | InvalidStateException ignored) {
        }

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
        } catch (InvalidActionException | InvalidStateException ignored) {
        }

        batteries.clear();
        doubleCannons.clear();

        // charlie defeats the opponent
        batteries.add(new Coordinates(4, 1));
        doubleCannons.add(new Coordinates(3, 6));

        charlieAction = new CompareFirePowerAction("Charlie", batteries, doubleCannons);
        try {
            controller.onActionReceived(charlieAction);
        } catch (InvalidActionException | InvalidStateException ignored) {
        }

        batteries.clear();
        doubleCannons.clear();

        // charlie accepts the reward too soon
        charlieAction = new GetRewardsAction("Charlie", true);
        try {
            controller.onActionReceived(charlieAction);
        } catch (InvalidActionException | InvalidStateException ignored) {
        }


        bobAction = new RollDiceAction("Bob");
        try {
            controller.onActionReceived(bobAction);
        } catch (InvalidActionException | InvalidStateException ignored) {
        }


        batteries.add(new Coordinates(2, 5));
        bobAction = new ChooseBatteryAction("Bob", 2, 5);
        try {
            controller.onActionReceived(bobAction);
        } catch (InvalidActionException | InvalidStateException ignored) {
        }

        batteries.clear();

        // bob accepts his fate and rolls again
        bobAction = new RollDiceAction("Bob");
        try {
            controller.onActionReceived(bobAction);
        } catch (InvalidActionException | InvalidStateException ignored) {
        }


        // bob accepts his fate and rolls again (last meteor)
        bobAction = new RollDiceAction("Bob");
        try {
            controller.onActionReceived(bobAction);
        } catch (InvalidActionException | InvalidStateException ignored) {
        }


    }

    private void assertInvalidAction(PlayerAction action, Player p) {
        assertThrows(InvalidActionException.class, () -> action.checkAction(p));
    }

    private void assertInvalidState(PlayerAction action, Player p) {
        assertThrows(InvalidStateException.class, () -> action.execute(p));
    }

    @Test
    void gettersTest() {
        game.setCurrentAdventureCard(game.getCardById(3));
        PiratesState state = new PiratesState(game);
        state.FORCE_OPPONENT_FIREPOWER(3);
        assertNotNull(state);
        assertNotNull(state.getCurrMeteorIdx());
        assertNotNull(state.getDiceResult());
        assertFalse(state.isRolled());
        assertNotNull(state.getPlayerStates());
        state.getAllPlanetsChosen();
        state.setAllPlanetsChosen(true);
        state.setPlayerToBeMovedIdx(1);
        state.getCurrPlayers();
        state.getChosenPlanets();
        state.checkAction(p1);
        state.setContext(game);
        state.setCard(game.getCardById(3));
        state.setCurrPlayerIdx(1);
        state.setPlayed(null);
        state.setSortedPlayers(null);
        state.getContext();
        state.getCard();
        state.getCurrPlayerIdx();
        state.getPlayed();
        state.getSortedPlayers();
        state.triggerNextState();
        state.disconnect(p1);

    }
}