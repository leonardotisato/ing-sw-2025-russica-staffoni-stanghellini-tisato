package it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SetNicknameActionTest {

    private final GamesController controller = new GamesController();

    SetNicknameActionTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        try {
            controller.onInitActionReceived(new SetNicknameAction("Alice"));
        } catch (InvalidActionException ignored) {}
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSetNicknameAction() {
        // conflicting nickname
        assertThrowsOnAction(new SetNicknameAction("Alice"));

        // valid nickname
        assertDoesNotThrowsOnAction(new SetNicknameAction("Bob"));
    }

    private void assertThrowsOnAction(InitAction action) {
        assertThrows(InvalidActionException.class, () -> action.checkAction(controller));
    }

    private void assertDoesNotThrowsOnAction(InitAction action) {
        assertDoesNotThrow(() -> action.checkAction(controller));
    }
}