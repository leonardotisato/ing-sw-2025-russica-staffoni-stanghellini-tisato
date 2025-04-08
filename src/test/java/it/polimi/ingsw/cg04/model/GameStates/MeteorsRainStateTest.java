package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.ChooseBatteryAction;
import it.polimi.ingsw.cg04.model.PlayerActions.FixShipAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.PlayerActions.RollDiceAction;
import it.polimi.ingsw.cg04.model.Shipyard;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeteorsRainStateTest {

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

    @AfterEach
    void tearDown() {
    }

    @Test
    void testMeteorsRainState() {

        // controller forces state of the game
        game.setCurrentAdventureCard(game.getCardById(9));
        game.setGameState(game.getCurrentAdventureCard().createState(game));

        AdventureCardState currAdventureCardState = (AdventureCardState) game.getGameState();

        // dice roll action arrives from client
        PlayerAction aliceRolls1 = new RollDiceAction("Alice");

        // controller handles the action
        controller.onActionReceived(aliceRolls1);

        // assumes random seed in flight-board=42
        System.out.println(p1.getShip().isShipLegal());
        System.out.println(p2.getShip().isShipLegal());
        System.out.println(p3.getShip().isShipLegal());

        // now players need to send action on what to do next

        // bob (p2) needs to remove several tiles from his ship
        List<Coordinates> removeTiles1 = new ArrayList<>();
        removeTiles1.add(new Coordinates(2, 1));
        removeTiles1.add(new Coordinates(2, 2));
        removeTiles1.add(new Coordinates(3, 2));

        PlayerAction bobNastyFix = new FixShipAction("Bob", removeTiles1);

        // simulate controller behaviour
        controller.onActionReceived(bobNastyFix);
        System.out.println(currAdventureCardState.getPlayed());

        // alice decides to use a battery save her tile
        // first she inputs a tile where there are no batteries
        PlayerAction aliceWrongCoords = new ChooseBatteryAction("Alice", 1, 3);

        // simulate controller behaviour
        assertThrows(RuntimeException.class, () -> controller.onActionReceived(aliceWrongCoords));
        System.out.println(currAdventureCardState.getPlayed());

        // first she inputs a tile where there are no batteries
        PlayerAction aliceCorrectedCoords = new ChooseBatteryAction("Alice", 1, 2);

        // simulate controller behaviour
        controller.onActionReceived(aliceCorrectedCoords);
        System.out.println(currAdventureCardState.getPlayed());

        // charlie decides to lose the tile
        PlayerAction charlieYoloAction = new ChooseBatteryAction("Charlie", -1, -1);

        // simulate controller behaviour
        controller.onActionReceived(charlieYoloAction);
        System.out.println(currAdventureCardState.getPlayed());

        // charlie tries to use battery
        PlayerAction charlieTryUseBattery = new ChooseBatteryAction("Charlie", -3, 5);

        // simulate controller behaviour
        assertThrows(RuntimeException.class, () -> controller.onActionReceived(charlieTryUseBattery));
        System.out.println(currAdventureCardState.getPlayed());

        // charlie tries to fix a ship removing wierd index
        List<Coordinates> removeTiles2 = new ArrayList<>();
        removeTiles2.add(new Coordinates(-4, 5));
        removeTiles2.add(new Coordinates(16, 4));
        PlayerAction charlieWierdFix = new FixShipAction("Charlie", removeTiles2);

        // simulate controller behaviour
        assertThrows(RuntimeException.class, () -> controller.onActionReceived(charlieWierdFix));
        System.out.println(currAdventureCardState.getPlayed());

        //charlie fixes his ship
        List<Coordinates> removeTiles3 = new ArrayList<>();
        removeTiles3.add(new Coordinates(1, 2));
        PlayerAction charlieFix = new FixShipAction("Charlie", removeTiles3);

        // simulate controller behaviour
        controller.onActionReceived(charlieFix);
        System.out.println(currAdventureCardState.getPlayed());


        // NEW ATTACK SEQUENCE...

        // dice roll action arrives from client
        aliceRolls1 = new RollDiceAction("Alice");

        // controller handles the action
        controller.onActionReceived(aliceRolls1);
    }
}