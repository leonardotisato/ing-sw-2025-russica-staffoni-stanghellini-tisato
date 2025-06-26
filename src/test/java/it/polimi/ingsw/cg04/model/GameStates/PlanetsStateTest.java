package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.PlanetsState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.utils.Shipyard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class PlanetsStateTest {
    private GamesController controller;
    private Game game;
    private Player p1, p2, p3;
    private final Shipyard ship = new Shipyard();


    @BeforeEach
    void setUp() throws IOException {

        game = new Game(2, 4, 0, "Alice", PlayerColor.RED);
        p1 = game.getPlayer("Alice");
        p2 = game.addPlayer("Bob", PlayerColor.BLUE);
        p3 = game.addPlayer("Charlie", PlayerColor.GREEN);

        controller = new GamesController();
        controller.addGame(game);

        game.setGameState(new PlanetsState(game));
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void gettersetterTest() {
        PlanetsState state = new PlanetsState(game);
        state.getChosenPlanets();
        state.getAllPlanetsChosen();
        state.getCurrPlayers();
        state.setAllPlanetsChosen(true);
    }
}
