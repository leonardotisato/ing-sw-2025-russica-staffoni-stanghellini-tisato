package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.*;
import it.polimi.ingsw.cg04.model.Shipyard;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildStateTest {

    private GamesController controller;
    private Game game;
    private Player p1, p2, p3;
    private final Shipyard ship = new Shipyard();

    @BeforeEach
    void setUp() {
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
        controller.onActionReceived(new ChooseTileAction("Alice", game.getTileById(21)));
        assertEquals(game.getTileById(21), p1.getHeldTile());
        controller.onActionReceived(new PlaceAction("Alice", 3,2));
        assertNull(p1.getHeldTile());
    }

    @Test
    void placeInBufferTest(){
        controller.onActionReceived(new ChooseTileAction("Alice", game.getTileById(21)));
        assertEquals(game.getTileById(21), p1.getHeldTile());
        controller.onActionReceived(new PlaceInBufferAction("Alice"));
        assertTrue(p1.getShip().getTilesBuffer().contains(game.getTileById(21)));
        assertEquals(1, p1.getShip().getTilesBuffer().size());

        controller.onActionReceived(new ChooseTileAction("Alice", game.getTileById(22)));
        assertEquals(game.getTileById(22), p1.getHeldTile());
        controller.onActionReceived(new PlaceInBufferAction("Alice"));
        assertTrue(p1.getShip().getTilesBuffer().contains(game.getTileById(22)));
        assertEquals(2, p1.getShip().getTilesBuffer().size());

        controller.onActionReceived(new ChooseTileAction("Alice", game.getTileById(23)));
        assertEquals(game.getTileById(23), p1.getHeldTile());
        assertThrows(RuntimeException.class, () -> controller.onActionReceived(new PlaceInBufferAction("Alice")));
        assertFalse(p1.getShip().getTilesBuffer().contains(game.getTileById(23)));
        assertEquals(2, p1.getShip().getTilesBuffer().size());
        assertEquals(game.getTileById(23), p1.getHeldTile());
    }

    @Test
    void chooseTileTest(){
        controller.onActionReceived(new ChooseTileAction("Alice", game.getTileById(22)));
        assertThrows(RuntimeException.class, () -> controller.onActionReceived(new ChooseTileAction("Alice", game.getTileById(21))));
        controller.onActionReceived(new PlaceAction("Alice", 3, 2));
        controller.onActionReceived(new ChooseTileAction("Alice", game.getTileById(21)));
    }

    @Test
    void showFaceUpTest(){
        controller.onActionReceived(new ShowFaceUpAction("Alice"));
        assertThrows(IllegalArgumentException.class, () -> controller.onActionReceived(new ShowFaceUpAction("Alice")));
        controller.onActionReceived(new CloseFaceUpTilesAction("Alice"));
        controller.onActionReceived(new ShowFaceUpAction("Alice"));
    }

    @Test
    void closeFaceUpTest(){

    }

    @Test
    void drawFaceDownTest(){
        assertNull(p1.getHeldTile());
        controller.onActionReceived(new DrawFaceDownAction("Alice"));
        assertNotNull(p1.getHeldTile());
        assertThrows(RuntimeException.class, () -> controller.onActionReceived(new DrawFaceDownAction("Alice")));
    }

    @Test
    void returnTileTest(){

    }

    @Test
    void pickPileTest(){
        assertThrows(IllegalArgumentException.class, () -> controller.onActionReceived(new ReturnPileAction("Alice")));
        assertThrows(IllegalArgumentException.class, () -> controller.onActionReceived(new ReturnPileAction("Alice")));
        assertThrows(IllegalArgumentException.class, () -> controller.onActionReceived(new ReturnPileAction("Alice")));
        controller.onActionReceived(new PickPileAction("Alice", 2));
        assertThrows(IllegalArgumentException.class, () -> controller.onActionReceived(new PickPileAction("Alice", 1)));
    }

    @Test
    void returnPileTest(){
        assertThrows(IllegalArgumentException.class, () -> controller.onActionReceived(new ReturnPileAction("Alice")));
        controller.onActionReceived(new PickPileAction("Alice", 1));
        assertThrows(IllegalArgumentException.class, () -> controller.onActionReceived(new PickPileAction("Alice", 1)));
        controller.onActionReceived(new ReturnPileAction("Alice"));
    }

    @Test
    void endBuildTest(){
        controller.onActionReceived(new EndBuildingAction("Alice", 1));
        BuildState b = (BuildState) game.getGameState();
        assertSame(BuildPlayerState.READY, b.getPlayerState().get("Alice"));
        System.out.println(p1.getPosition());
    }

    @Test
    void endBuildTest2(){
        controller.onActionReceived(new DrawFaceDownAction("Alice"));
        controller.onActionReceived(new PlaceAction("Alice", 1,1));
        controller.onActionReceived(new EndBuildingAction("Alice", 1));
        BuildState b = (BuildState) game.getGameState();
        assertEquals(BuildPlayerState.FIXING, b.getPlayerState().get("Alice"));
    }

    @Test
    void fixShipTest(){
        controller.onActionReceived(new DrawFaceDownAction("Alice"));
        controller.onActionReceived(new PlaceAction("Alice", 1,1));
        controller.onActionReceived(new EndBuildingAction("Alice", 1));
        BuildState b = (BuildState) game.getGameState();
        assertEquals(BuildPlayerState.FIXING, b.getPlayerState().get("Alice"));
        List<Coordinates> coordsList = new ArrayList<>();
        coordsList.add(new Coordinates(1,1));
        controller.onActionReceived(new FixShipAction("Alice", coordsList));
        assertEquals(BuildPlayerState.READY, b.getPlayerState().get("Alice"));

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

    @AfterEach
    void tearDown() {
    }
}