package it.polimi.ingsw.cg04.controller;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.CreateGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.InitAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.JoinGameAction;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GamesControllerTest {

    GamesController controller = new GamesController();

    @BeforeEach
    void setUp() {

        // add a game
        InitAction createAction;
        createAction = new CreateGameAction(2, 2, "Charlie", PlayerColor.BLUE);
        controller.onActionReceived(createAction);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCreateGame() {
        CreateGameAction createAction = new CreateGameAction(2,4, "Alice", PlayerColor.BLUE);
        controller.onActionReceived(createAction);

        // check controller has the game and game was initialized correctly
        assertEquals(2, controller.getGames().size());
        Game g = controller.getGames().get(1);
        assertNotNull(g);
        assertEquals("Alice", g.getPlayers().getFirst().getName());
        assertEquals(PlayerColor.BLUE, g.getPlayers().getFirst().getColor());
        assertEquals(1, g.getPlayers().size());
        assertEquals(2, g.getLevel());
        assertEquals(4, g.getMaxPlayers());

        // check that same nickname cannot be taken
        assertThrowsOnAction(createAction);

        // todo: if another Charlie tries to createGame, controller handles him as the other charlie...
    }

    @Test
    void testJoinGame() {

        // test that players can be correctly added
        InitAction joinAction;

        joinAction = new JoinGameAction(0, "Bob", PlayerColor.RED);
        controller.onActionReceived(joinAction);

        assertEquals(1, controller.getGames().size());

        Game g = controller.getGames().getFirst();
        assertNotNull(g);
        assertEquals(2, g.getPlayers().size());
        assertEquals(2, g.getLevel());


        assertEquals("Charlie", g.getPlayers().getFirst().getName());
        assertEquals(PlayerColor.BLUE, g.getPlayers().getFirst().getColor());

        assertEquals("Bob", g.getPlayers().get(1).getName());
        assertEquals(PlayerColor.RED, g.getPlayers().get(1).getColor());

        // check that no other player can join since maxPlayer = 2
        joinAction = new JoinGameAction(0, "David", PlayerColor.YELLOW);
        assertThrowsOnAction(joinAction);

    }

    @Test
    void testGetJoinableGames() {
        assertEquals(1, controller.getJoinableGames().size());
    }

    private void assertThrowsOnAction(InitAction action) {
        assertThrows(InvalidActionException.class, () -> action.checkAction(controller));
    }

    private void assertDoesNotThrowsOnAction(InitAction action) {
        assertDoesNotThrow(() -> action.checkAction(controller));
}
}