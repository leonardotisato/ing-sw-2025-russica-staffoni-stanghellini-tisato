package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions.ChoosePropulsorAction;
import it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions.StardustAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.Shipyard;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceStateTest {

    private GamesController controller;
    private Game game;
    private Player p1, p2, p3;
    private final Shipyard shipyard = new Shipyard();

    @BeforeEach
    void setUp() throws IOException {
        game = new Game(2, "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json", "src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json");

        p1 = game.addPlayer("Alice", PlayerColor.RED);
        p2 = game.addPlayer("Bob", PlayerColor.BLUE);
        p3 = game.addPlayer("Charlie", PlayerColor.GREEN);

        p1.setShip(shipyard.createShip3());
        p2.setShip(shipyard.createShip4());
        p3.setShip(shipyard.createShip5());

        assertTrue(p1.getShip().isShipLegal());
        assertTrue(p2.getShip().isShipLegal());
        assertTrue(p3.getShip().isShipLegal());

        // p1.move(7);
        p1.move(11);
        p2.move(6);
        p3.move(5);

        game.getBoard().setRandSeed(0);

        // assertEquals(p1, game.getBoard().getCell(7));
        assertEquals(p1, game.getBoard().getCell(11));
        assertEquals(p2, game.getBoard().getCell(6));
        assertEquals(p3, game.getBoard().getCell(5));

        assertEquals(4, p1.getShip().getNumCrew());
        assertEquals(4, p2.getShip().getNumCrew());
        assertEquals(12, p3.getShip().getNumCrew());

        controller = new GamesController();
        controller.addGame(game);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void openSpaceTest() {
        game.setCurrentAdventureCard(game.getCardById(5));
        game.setGameState(game.getCurrentAdventureCard().createState(game));

        AdventureCardState currAdventureCardState = (AdventureCardState) game.getGameState();

        PlayerAction alice = new ChoosePropulsorAction("Alice", new ArrayList<>(), new ArrayList<>());
        try {
            controller.onActionReceived(alice);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        PlayerAction bob = new ChoosePropulsorAction("Bob", new ArrayList<>(), new ArrayList<>());
        try {
            controller.onActionReceived(bob);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        PlayerAction charlie = new ChoosePropulsorAction("Charlie", new ArrayList<>(), new ArrayList<>());
        try {
            controller.onActionReceived(charlie);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertEquals(2, game.getPlayers().size());
    }
}