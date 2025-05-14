package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.BuildActions.*;
import it.polimi.ingsw.cg04.model.PlayerActions.FixShipAction;
import it.polimi.ingsw.cg04.model.Shipyard;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import it.polimi.ingsw.cg04.model.utils.TuiDrawer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildStateTest {

    private GamesController controller;
    private Game game;
    private Player p1, p2, p3;
    private final Shipyard ship = new Shipyard();

    @BeforeEach
    void setUp() throws IOException {
        game = new Game(2, "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json", "src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json");

        p1 = game.addPlayer("Alice", PlayerColor.RED);
        p2 = game.addPlayer("Bob", PlayerColor.BLUE);
        p3 = game.addPlayer("Charlie", PlayerColor.GREEN);

        controller = new GamesController();
        controller.addGame(game);

        game.setGameState(new BuildState(game));
    }


    // in the first part we test the correctness of the BuildState methods and the relative PlayerActions checks
    // in the second part we test the correctness of players-state, and it's relative checks


    @Test
    void placeTileTest(){
        try {
            controller.onActionReceived(new DrawFaceDownAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        int id = game.getPlayer("Alice").getHeldTile().getId();

        try {
            controller.onActionReceived(new ReturnTileAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        try {
            controller.onActionReceived(new ChooseTileAction("Alice", 0));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertEquals(game.getTileById(id), p1.getHeldTile());

        try {
            controller.onActionReceived(new PlaceAction("Alice", 2,4));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertNull(p1.getHeldTile());
    }

    @Test
    void placeInBufferTest(){
        try {
            controller.onActionReceived(new DrawFaceDownAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        int idx = game.getPlayer("Alice").getHeldTile().getId();

        assertEquals(game.getTileById(idx), p1.getHeldTile());

        try {
            controller.onActionReceived(new PlaceInBufferAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertTrue(p1.getShip().getTilesBuffer().contains(game.getTileById(idx)));
        assertEquals(1, p1.getShip().getTilesBuffer().size());

        try {
            controller.onActionReceived(new DrawFaceDownAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        idx = game.getPlayer("Alice").getHeldTile().getId();

        assertEquals(game.getTileById(idx), p1.getHeldTile());

        try {
            controller.onActionReceived(new PlaceInBufferAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertTrue(p1.getShip().getTilesBuffer().contains(game.getTileById(idx)));
        assertEquals(2, p1.getShip().getTilesBuffer().size());

        try {
            controller.onActionReceived(new DrawFaceDownAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        idx = game.getPlayer("Alice").getHeldTile().getId();

        assertEquals(game.getTileById(idx), p1.getHeldTile());
        assertThrows(InvalidActionException.class, () -> (new PlaceInBufferAction("Alice")).checkAction(p1));
        assertFalse(p1.getShip().getTilesBuffer().contains(game.getTileById(idx)));
        assertEquals(2, p1.getShip().getTilesBuffer().size());
        assertEquals(game.getTileById(idx), p1.getHeldTile());
    }

    @Test
    void chooseTileTest(){
        try {
            controller.onActionReceived(new DrawFaceDownAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        try {
            controller.onActionReceived(new ReturnTileAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        try {
            controller.onActionReceived(new DrawFaceDownAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        try {
            controller.onActionReceived(new ReturnTileAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        try {
            controller.onActionReceived(new ChooseTileAction("Alice", 0));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertThrows(InvalidActionException.class, () -> (new ChooseTileAction("Alice", 0)).checkAction(p1));

        try {
            controller.onActionReceived(new PlaceAction("Alice", 3, 2));
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        try {
            controller.onActionReceived(new ChooseTileAction("Alice", 0));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

    }

    @Test
    void showFaceUpTest(){
        try {
            controller.onActionReceived(new ShowFaceUpAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        try {
            controller.onActionReceived(new CloseFaceUpTilesAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        try {
            controller.onActionReceived(new DrawFaceDownAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertThrows(InvalidActionException.class,() -> (new ShowFaceUpAction("Alice")).checkAction(p1));

        try {
            controller.onActionReceived(new CloseFaceUpTilesAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        try {
            controller.onActionReceived(new ShowFaceUpAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

    }

    @Test
    void closeFaceUpTest(){

    }

    @Test
    void drawFaceDownTest(){
        assertNull(p1.getHeldTile());
        try {
            controller.onActionReceived(new DrawFaceDownAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertNotNull(p1.getHeldTile());
        assertThrows(InvalidActionException.class, () -> (new DrawFaceDownAction("Alice")).checkAction(p1));
    }

    @Test
    void returnTileTest(){

    }

    @Test
    void pickPileTest(){
        assertThrows(InvalidStateException.class, () -> new ReturnPileAction("Alice").execute(p1));
        assertThrows(InvalidStateException.class, () -> new ReturnPileAction("Alice").execute(p1));
        assertThrows(InvalidStateException.class, () -> new ReturnPileAction("Alice").execute(p1));
        try {
            controller.onActionReceived(new PickPileAction("Alice", 2));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertThrows(InvalidStateException.class, () -> new PickPileAction("Alice", 1).execute(p1));
    }

    @Test
    void returnPileTest(){
        assertThrows(InvalidStateException.class, () -> new ReturnPileAction("Alice").execute(p1));
        try {
            controller.onActionReceived(new PickPileAction("Alice", 1));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertThrows(InvalidStateException.class, () -> new PickPileAction("Alice", 1).execute(p1));

        try {
            controller.onActionReceived(new ReturnPileAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

    }

    @Test
    void endBuildTest(){
        try {
            controller.onActionReceived(new EndBuildingAction("Alice", 1));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        BuildState b = (BuildState) game.getGameState();
        assertSame(BuildPlayerState.READY, b.getPlayerState().get("Alice"));
        System.out.println(p1.getPosition());
    }

    //@Test
    void endBuildTest2(){
        try {
            controller.onActionReceived(new DrawFaceDownAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        System.out.println(p1.getHeldTile());
        try {
            controller.onActionReceived(new PlaceAction("Alice", 2,4));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        try {
            controller.onActionReceived(new DrawFaceDownAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        System.out.println(p1.getHeldTile());
        try {
            controller.onActionReceived(new PlaceAction("Alice", 3,4));
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        try {
            controller.onActionReceived(new EndBuildingAction("Alice", 1));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        BuildState b = (BuildState) game.getGameState();
        assertEquals(BuildPlayerState.FIXING, b.getPlayerState().get("Alice"));
    }

    @Test
    void fixShipTest(){
        do{
            try {
                controller.onActionReceived(new ReturnTileAction("Alice"));
            } catch (InvalidActionException | InvalidStateException ignored) {      }

            try {
                controller.onActionReceived(new DrawFaceDownAction("Alice"));
            } catch (InvalidActionException | InvalidStateException ignored) {      }
        }
        while (p1.getHeldTile().getConnection(Direction.LEFT) == Connection.SINGLE
                || p1.getHeldTile().getConnection(Direction.LEFT) == Connection.DOUBLE
                || p1.getHeldTile().getConnection(Direction.LEFT) == Connection.UNIVERSAL);
        try {
            controller.onActionReceived(new PlaceAction("Alice", 2,4));
        } catch (InvalidActionException | InvalidStateException ignored) {        }


        try {
            controller.onActionReceived(new EndBuildingAction("Alice", 1));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        BuildState b = (BuildState) game.getGameState();
        assertEquals(BuildPlayerState.FIXING, b.getPlayerState().get("Alice"));
        List<Coordinates> coordsList = new ArrayList<>();
        coordsList.add(new Coordinates(2,4));

        try {
            controller.onActionReceived(new FixShipAction("Alice", coordsList));
        } catch (InvalidActionException | InvalidStateException ignored) {        }
        assertEquals(BuildPlayerState.READY, b.getPlayerState().get("Alice"));

    }

    @Test
    void chooseTileFromBufferTest(){
        try {
            controller.onActionReceived(new DrawFaceDownAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        int idx = game.getPlayer("Alice").getHeldTile().getId();

        try {
            controller.onActionReceived(new PlaceInBufferAction("Alice"));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        try {
            controller.onActionReceived(new ChooseTileFromBufferAction("Alice", 0));
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertEquals(idx, game.getPlayer("Alice").getHeldTile().getId());
    }

    @Test
    void StartTimerTest(){

    }

    @Test
    void playerStateTest(){
        BuildState state = (BuildState) game.getGameState();

        // all players start in building
        assertEquals(BuildPlayerState.BUILDING, state.getPlayerState().get("Alice"));
        assertEquals(BuildPlayerState.BUILDING, state.getPlayerState().get("Bob"));
        assertEquals(BuildPlayerState.BUILDING, state.getPlayerState().get("Charlie"));
    }

    @Test
    void renderBuildStateTest() throws InterruptedException, InvalidStateException {
        BuildState state = (BuildState) game.getGameState();
        System.out.println(state.render("Alice"));
        Thread.sleep(5000);
        state.drawFaceDown(p1);
        TuiDrawer.clear();
        System.out.println(state.render("Alice"));
        Thread.sleep(5000);
        state.returnTile(p1);
        System.out.println(state.render("Alice"));
        Thread.sleep(5000);
        state.drawFaceDown(p1);
        TuiDrawer.clear();
        System.out.println(state.render("Alice"));
        Thread.sleep(5000);
        state.returnTile(p1);
        System.out.println(state.render("Alice"));
        Thread.sleep(5000);
        state.pickPile(p1, 1);
        System.out.println(state.render("Alice"));
    }

    @AfterEach
    void tearDown() {
    }
}