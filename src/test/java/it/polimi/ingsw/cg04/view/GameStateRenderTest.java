package it.polimi.ingsw.cg04.view;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.utils.Shipyard;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStateRenderTest {

    private GamesController controller;
    private Game game;
    private Player p1, p2, p3;
    private final Shipyard shipyard = new Shipyard();

    @BeforeEach
    void setUp() throws IOException {
        game = new Game(2, 4, 0, "Alice", PlayerColor.RED);

        // set seed for controlled testing environment
        game.getBoard().setRandSeed(42);

        p1 = game.getPlayer("Alice");
        p2 = game.addPlayer("Bob", PlayerColor.BLUE);
        p3 = game.addPlayer("Charlie", PlayerColor.GREEN);

        p1.setShip(shipyard.createShip3());
        p2.setShip(shipyard.createShip4());
        p3.setShip(shipyard.createShip3());

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

    @Test
    void testMeteorsRainRender(){
        game.setCurrentAdventureCard(game.getCardById(9));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        System.out.println(game.render("Alice"));
    }

    @Test
    void testPlanetsRender(){
        game.setCurrentAdventureCard(game.getCardById(13));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        System.out.println(game.render("Alice"));
    }

    @Test
    void testSmugglersRender(){
        game.setCurrentAdventureCard(game.getCardById(2));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        System.out.println(game.render("Alice"));
    }

    @Test
    void testSlaversRender(){
        game.setCurrentAdventureCard(game.getCardById(1));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        System.out.println(game.render("Alice"));
    }

    @Test
    void testPiratesRender(){
        game.setCurrentAdventureCard(game.getCardById(3));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        System.out.println(game.render("Alice"));
    }

    @Test
    void testWarzoneRender(){
        game.setCurrentAdventureCard(game.getCardById(16));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        System.out.println(game.render("Alice"));
    }
}
