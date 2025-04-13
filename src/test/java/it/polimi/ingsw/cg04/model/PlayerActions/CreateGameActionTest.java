package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameActionTest {

    private final GamesController controller = new GamesController();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCreateGameAction() {
        InitAction createAction;

        // valid action
        createAction = new CreateGameAction(2, 2, "Alice", PlayerColor.BLUE);
        assertTrue(createAction.checkAction(controller));

        // valid action
        createAction = new CreateGameAction(1, 3, "Bob", PlayerColor.RED);
        assertTrue(createAction.checkAction(controller));

        // valid action
        createAction = new CreateGameAction(2, 4, "Alice", PlayerColor.YELLOW);
        assertTrue(createAction.checkAction(controller));

        // valid action
        createAction = new CreateGameAction(1, 2, "=^.^=", PlayerColor.BLUE);
        assertTrue(createAction.checkAction(controller));

        // wrong level
        createAction = new CreateGameAction(3, 2, "Alice", PlayerColor.BLUE);
        assertFalse(createAction.checkAction(controller));

        // wrong level
        createAction = new CreateGameAction(0, 2, "Alice", PlayerColor.BLUE);
        assertFalse(createAction.checkAction(controller));

        // wrong num players
        createAction = new CreateGameAction(2, 1, "Alice", PlayerColor.BLUE);
        assertFalse(createAction.checkAction(controller));

        // wrong num players
        createAction = new CreateGameAction(2, 0, "Alice", PlayerColor.BLUE);
        assertFalse(createAction.checkAction(controller));

        // wrong num players
        createAction = new CreateGameAction(2, 16, "Alice", PlayerColor.BLUE);
        assertFalse(createAction.checkAction(controller));

        // wrong num players
        createAction = new CreateGameAction(2, 5, "Alice", PlayerColor.BLUE);
        assertFalse(createAction.checkAction(controller));
    }
}