package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.CompareFirePowerAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.Shipyard;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SmugglersStateTest {

    private GamesController controller;
    private Game game;
    private Player p1, p2, p3;
    private final Shipyard shipyard = new Shipyard();

    @BeforeEach
    void setUp() {

        game = new Game(2, "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json", "src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json");

        p1 = game.addPlayer("Alice", PlayerColor.RED);
        p2 = game.addPlayer("Bob", PlayerColor.BLUE);
        p3 = game.addPlayer("Charlie", PlayerColor.GREEN);

        p1.setShip(shipyard.createShip3());
        p2.setShip(shipyard.createShip4());
        p3.setShip(shipyard.createShip5());

        p1.getShip().addBox(BoxType.RED, 2, 1);
        p1.getShip().addBox(BoxType.YELLOW, 2, 1);

        p2.getShip().addBox(BoxType.YELLOW, 3, 6);

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
    void SmugglersPattern(){
        game.setCurrentAdventureCard(game.getCardById(2));
        game.setGameState(game.getCurrentAdventureCard().createState(game));

        List<Coordinates> batteries = new ArrayList<>();
        List<Coordinates> doubleCannons = new ArrayList<>();
        List<Coordinates> housingUnits = new ArrayList<>();
        List<Integer> numCrewPerUnit = new ArrayList<>();

        PlayerAction action1 = new CompareFirePowerAction(p1.getName(), null, null);
        controller.onActionReceived(action1);

        assertEquals(0, p1.getShip().getBoxes().values().stream().mapToInt(i -> i).sum());
        assertEquals(3, p1.getShip().getNumCrew());


        PlayerAction action2 = new CompareFirePowerAction(p2.getName(), null, null);

        assertEquals(0, p1.getShip().getBoxes().values().stream().mapToInt(i -> i).sum());
        assertEquals(3, p1.getShip().getNumCrew());

        batteries.add(new Coordinates(4, 1));
        doubleCannons.add(new Coordinates(3, 6));
        PlayerAction action3 = new CompareFirePowerAction(p3.getName(), batteries, doubleCannons);

        controller.onActionReceived(action3);
        assertInstanceOf(FlightState.class, game.getGameState());
        assertNull(game.getCurrentAdventureCard());
    }
}
