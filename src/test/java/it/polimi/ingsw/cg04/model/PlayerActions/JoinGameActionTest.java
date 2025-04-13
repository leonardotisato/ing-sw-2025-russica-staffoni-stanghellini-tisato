package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JoinGameActionTest {

    private final GamesController controller = new GamesController();

    @BeforeEach
    void setUp() {
        InitAction createAction;
        createAction = new CreateGameAction(2, 2, "Alice", PlayerColor.BLUE);
        controller.onActionReceived(createAction);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCheckJoinAction() {
        InitAction joinAction;

        // valid action
        joinAction = new JoinGameAction(0, "Carl", PlayerColor.RED);
        assertTrue(joinAction.checkAction(controller));

        // valid action
        joinAction = new JoinGameAction(0, "Bob", PlayerColor.YELLOW);
        assertTrue(joinAction.checkAction(controller));

        // nick collision
        joinAction = new JoinGameAction(0, "Alice", PlayerColor.RED);
        assertFalse(joinAction.checkAction(controller));

        // color collision
        joinAction = new JoinGameAction(0, "Carl", PlayerColor.BLUE);
        assertFalse(joinAction.checkAction(controller));

        // game not found
        joinAction = new JoinGameAction(1, "Carl", PlayerColor.YELLOW);
        assertFalse(joinAction.checkAction(controller));

        // game not found
        joinAction = new JoinGameAction(-1, "Alice", PlayerColor.RED);
        assertFalse(joinAction.checkAction(controller));
    }
}