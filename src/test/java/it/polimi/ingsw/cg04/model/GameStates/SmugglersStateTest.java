package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions.CompareFirePowerAction;
import it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions.HandleBoxesAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.utils.Shipyard;
import it.polimi.ingsw.cg04.model.adventureCards.Smugglers;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SmugglersStateTest {

    private GamesController controller;
    private Game game;
    private Player p1, p2, p3;
    private final Shipyard shipyard = new Shipyard();

    @BeforeEach
    void setUp() throws IOException {

        game = new Game(2);

        // set seed for controlled testing environment
        game.getBoard().setRandSeed(42);

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
        Smugglers card = (Smugglers) game.getCurrentAdventureCard();
        card.setFirePower(3);

        List<Coordinates> batteries = new ArrayList<>();
        List<Coordinates> doubleCannons = new ArrayList<>();
        List<Coordinates> housingUnits = new ArrayList<>();
        List<Integer> numCrewPerUnit = new ArrayList<>();

        //not enough firepower, p1 decides not to use batteries
        PlayerAction action1 = new CompareFirePowerAction(p1.getName(), null, null);
        try {
            controller.onActionReceived(action1);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        //smugglers steal all the boxes
        assertEquals(0, p1.getShip().getBoxes().values().stream().mapToInt(i -> i).sum());
        assertEquals(6, p1.getShip().getNumBatteries());

        //also the second player can't defend himself
        PlayerAction action2 = new CompareFirePowerAction(p2.getName(), null, null);
        try {
            controller.onActionReceived(action2);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        //he loses a box and a battery
        assertEquals(0, p2.getShip().getBoxes().values().stream().mapToInt(i -> i).sum());
        assertEquals(1, p2.getShip().getNumBatteries());


        //the third player decides to defend himself, he activates a double cannon
        batteries.add(new Coordinates(4, 1));
        doubleCannons.add(new Coordinates(3, 6));
        PlayerAction action3 = new CompareFirePowerAction(p3.getName(), batteries, doubleCannons);
        try {
            controller.onActionReceived(action3);
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        //now the third player should send the new boxes map
        List<Coordinates> storageTilesCoordinates3 = p3.getShip().getTilesMap().get("StorageTile");
        List<Map<BoxType, Integer>> boxes3 = new ArrayList<>();
        boxes3.add(new HashMap<>(Map.of(BoxType.RED, 0, BoxType.GREEN, 1, BoxType.YELLOW, 1, BoxType.BLUE, 0)));
        while (boxes3.size() < storageTilesCoordinates3.size() - 1) {
            boxes3.add(new HashMap<>(Map.of(BoxType.RED, 0, BoxType.GREEN, 0, BoxType.YELLOW, 0, BoxType.BLUE, 0)));
        }
        // wrong number of boxes after reward
        boxes3.add(new HashMap<>(Map.of(BoxType.RED, 0, BoxType.GREEN, 0, BoxType.YELLOW, 0, BoxType.BLUE, 2)));
        PlayerAction action4 = new HandleBoxesAction(p3.getName(), storageTilesCoordinates3, boxes3);
        assertThrows(InvalidStateException.class, () -> action4.execute(p3));


        //now p3 sends the right list of box maps
        boxes3.removeLast();
        boxes3.add(new HashMap<>(Map.of(BoxType.RED, 0, BoxType.GREEN, 0, BoxType.YELLOW, 0, BoxType.BLUE, 1)));
        PlayerAction action5 = new HandleBoxesAction(p3.getName(), storageTilesCoordinates3, boxes3);
        try {
            controller.onActionReceived(action5);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertInstanceOf(FlightState.class, game.getGameState());
        assertNull(game.getCurrentAdventureCard());
    }
}
