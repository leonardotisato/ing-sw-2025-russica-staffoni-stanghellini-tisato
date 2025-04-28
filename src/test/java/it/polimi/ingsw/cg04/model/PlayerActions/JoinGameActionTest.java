package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.CreateGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.InitAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.JoinGameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.SetNicknameAction;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
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

        try {
            controller.onInitActionReceived(createAction);
        } catch (InvalidActionException ignored) {
            System.out.println("Game not created");
        }

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCheckJoinAction() {
        InitAction joinAction;

        // valid action
        joinAction = new JoinGameAction(0, "Carl", PlayerColor.RED);
        assertDoesNotThrowsOnAction(joinAction);

        // valid action
        joinAction = new JoinGameAction(0, "Bob", PlayerColor.YELLOW);
        assertDoesNotThrowsOnAction(joinAction);

        // color collision
        joinAction = new JoinGameAction(0, "Carl", PlayerColor.BLUE);
        assertThrowsOnAction(joinAction);

        // game not found
        joinAction = new JoinGameAction(1, "Carl", PlayerColor.YELLOW);
        assertThrowsOnAction(joinAction);

        // game not found
        joinAction = new JoinGameAction(-1, "Alice", PlayerColor.RED);
        assertThrowsOnAction(joinAction);
    }

    private void assertThrowsOnAction(InitAction action) {
        assertThrows(InvalidActionException.class, () -> action.checkAction(controller));
    }

    private void assertDoesNotThrowsOnAction(InitAction action) {
        assertDoesNotThrow(() -> action.checkAction(controller));
    }
}