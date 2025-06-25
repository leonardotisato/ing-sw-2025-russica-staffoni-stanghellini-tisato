package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.BuildActions.DrawFaceDownAction;
import it.polimi.ingsw.cg04.model.PlayerActions.BuildActions.EndBuildingAction;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Shipyard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Lev1BuildStateTest {

    private GamesController controller;
    private final Shipyard ship = new Shipyard();

    @BeforeEach
    void setUp() throws IOException {

        Game game = new Game(1, 4, 0, "Alice", PlayerColor.RED);
        Player p1 = game.getPlayer("Alice");
        Player p2 = game.addPlayer("Bob", PlayerColor.BLUE);
        Player p3 = game.addPlayer("Charlie", PlayerColor.GREEN);

        controller = new GamesController();
        controller.addGame(game);

        game.setGameState(new BuildState(game));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void endBuildingTest() {
        try {
            controller.onActionReceived(new EndBuildingAction("Alice", 1));
        } catch (InvalidActionException | InvalidStateException ignored) {
            System.out.println("unable to end building");
        }
    }

}
