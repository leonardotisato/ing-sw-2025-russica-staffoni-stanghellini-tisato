package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.controller.PlayerActions.LobbyActions.CreateGameAction;
import it.polimi.ingsw.cg04.controller.PlayerActions.LobbyActions.InitAction;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameActionTest {

    private final GamesController controller = new GamesController();

    CreateGameActionTest() throws IOException {
    }

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
        assertDoesNotThrowsOnAction(createAction);

        // valid action
        createAction = new CreateGameAction(1, 3, "Bob", PlayerColor.RED);
        assertDoesNotThrowsOnAction(createAction);

        // valid action
        createAction = new CreateGameAction(2, 4, "Alice", PlayerColor.YELLOW);
        assertDoesNotThrowsOnAction(createAction);

        // valid action
        createAction = new CreateGameAction(1, 2, "=^.^=", PlayerColor.BLUE);
        assertDoesNotThrowsOnAction(createAction);

        // wrong level
        createAction = new CreateGameAction(3, 2, "Alice", PlayerColor.BLUE);
        assertThrowsOnAction(createAction);

        // wrong level
        createAction = new CreateGameAction(0, 2, "Alice", PlayerColor.BLUE);
        assertThrowsOnAction(createAction);

        // wrong num players
        createAction = new CreateGameAction(2, 1, "Alice", PlayerColor.BLUE);
        assertThrowsOnAction(createAction);

        // wrong num players
        createAction = new CreateGameAction(2, 0, "Alice", PlayerColor.BLUE);
        assertThrowsOnAction(createAction);

        // wrong num players
        createAction = new CreateGameAction(2, 16, "Alice", PlayerColor.BLUE);
        assertThrowsOnAction(createAction);

        // wrong num players
        createAction = new CreateGameAction(2, 5, "Alice", PlayerColor.BLUE);
        assertThrowsOnAction(createAction);
    }

    private void assertThrowsOnAction(InitAction action) {
        assertThrows(InvalidActionException.class, () -> action.checkAction(controller));
    }

    private void assertDoesNotThrowsOnAction(InitAction action) {
        assertDoesNotThrow(() -> action.checkAction(controller));
    }
}