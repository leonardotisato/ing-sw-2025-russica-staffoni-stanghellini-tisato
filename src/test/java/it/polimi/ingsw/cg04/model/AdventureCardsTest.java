package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.GameStates.*;
import it.polimi.ingsw.cg04.model.PlayerActions.*;
import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AdventureCardsTest {

    private GamesController controller;
    private Game game;
    private Player p, p2, p3;
    private Shipyard shipyard;
//    Tile centerTile33;
//    Tile housingTile35;
//    Tile propulsorTile71;
//    Tile batteryTile16;
//    Tile storageTile68;
//    Tile laserTile101;

    @BeforeEach
    void setUp() {
        shipyard = new Shipyard();

        controller = new GamesController();
        game = new Game(1, "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json", "src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json");
        game.addPlayer("Filippo", PlayerColor.BLUE);
        game.addPlayer("Martin", PlayerColor.RED);
        game.addPlayer("Joseph", PlayerColor.GREEN);
        controller.addGame(game);
        p = game.getPlayer("Filippo");
        p2 = game.getPlayer("Martin");
        p3 = game.getPlayer("Joseph");
        Tile centerTile33 = game.getTilesDeckMap().get(33);
        Tile housingTile35 = game.getTilesDeckMap().get(35);
        housingTile35.rotate90dx();
        Tile propulsorTile71 = game.getTilesDeckMap().get(71);
        Tile batteryTile16 = game.getTilesDeckMap().get(16);
        Tile storageTile68 = game.getTilesDeckMap().get(68);
        Tile laserTile101 = game.getTilesDeckMap().get(101);
        laserTile101.rotate90dx();
        Tile laserTile119 = game.getTilesDeckMap().get(119);
        laserTile119.rotate90sx();
        Tile laserTile131 = game.getTilesDeckMap().get(131);
        Map<BoxType, Integer> boxes = new HashMap<>();
        boxes.put(BoxType.BLUE, 1);
        boxes.put(BoxType.RED, 1);
        boxes.put(BoxType.YELLOW, 0);
        boxes.put(BoxType.GREEN, 0);
        p.getShip().placeTile(centerTile33, 2, 2);
        p.getShip().placeTile(housingTile35, 3, 2);
        p.getShip().placeTile(propulsorTile71, 3, 1);
        p.getShip().placeTile(batteryTile16, 2, 1);
        p.getShip().placeTile(storageTile68, 2, 3);
        p.getShip().placeTile(laserTile119, 1, 1);
        p.getShip().placeTile(laserTile131, 1, 2);
        p.getShip().placeTile(laserTile101, 1, 3);
//        p.getShip().addCrew(CrewType.HUMAN, 2, 2);
//        p.getShip().addCrew(CrewType.HUMAN, 3, 2);
        p.getShip().setBoxes(boxes, 2, 3);
        p2.setShip(shipyard.createShip3());
        p3.setShip(shipyard.createShip4());
        game.getBoard().occupyCell(12, p);
        game.getBoard().occupyCell(10, p2);
        game.getBoard().occupyCell(8, p3);
    }

    @Test
    void testOpenSpacePattern(){
        game.setCurrentAdventureCard(game.getCardById(5));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        List<Integer> usedBatteries1 = new ArrayList<>();
        List<Integer> usedBatteries2 = new ArrayList<>();
        List<Integer> usedBatteries3 = new ArrayList<>();
        List<Coordinates> coordinates1 = new ArrayList<>();
        List<Coordinates> coordinates2 = new ArrayList<>();
        List<Coordinates> coordinates3 = new ArrayList<>();
//        usedBatteries1.add(1);
//        coordinates1.add(new Coordinates(2, 1));
        usedBatteries2.add(1);
        coordinates2.add(new Coordinates(1, 2));
        PlayerAction action = new ChoosePropulsorAction(p.getName(),coordinates1, usedBatteries1);
        PlayerAction action2 = new ChoosePropulsorAction(p2.getName() ,coordinates2, usedBatteries2);
        PlayerAction action3 = new ChoosePropulsorAction(p3.getName() ,coordinates3, usedBatteries3);
        controller.onActionReceived(action);
        assertEquals(13, p.getCurrentCell());
        assertEquals(3, p.getShip().getNumBatteries());
        assertEquals(3, p.getShip().getTile(2,1).getNumBatteries());
        assertInstanceOf(OpenSpaceState.class, game.getGameState());
        assertThrows(RuntimeException.class, () -> controller.onActionReceived(action3));
        controller.onActionReceived(action2);
        assertEquals(14, p2.getCurrentCell());
        assertEquals(5, p2.getShip().getNumBatteries());
        assertEquals(2, p2.getShip().getTile(1,2).getNumBatteries());
        assertInstanceOf(OpenSpaceState.class, game.getGameState());
        controller.onActionReceived(action3);
        assertEquals(8, p3.getCurrentCell());
        assertEquals(2, p3.getShip().getNumBatteries());
        assertInstanceOf(FlightState.class, game.getGameState());
        assertNull(game.getCurrentAdventureCard());
        assertThrows(RuntimeException.class, () ->  controller.onActionReceived(action));
    }

    @Test
    void testAbandonedShipPattern(){
        game.setCurrentAdventureCard(game.getCardById(37));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        List<Integer> numCrewMembersLost1 = new ArrayList<>();
        List<Coordinates> coordinates1 = new ArrayList<>();
        PlayerAction action1 = new RemoveCrewAction(p.getName(),coordinates1, numCrewMembersLost1);
        PlayerAction action2 = new RemoveCrewAction(p2.getName(),new ArrayList<>(), new ArrayList<>());
        PlayerAction action3 = new RemoveCrewAction(p3.getName(),new ArrayList<>(), new ArrayList<>());
        assertThrows(RuntimeException.class, () -> controller.onActionReceived(action2));
        controller.onActionReceived(action1);
        assertInstanceOf(AbandonedShipState.class, game.getGameState());
        assertEquals(0, p.getNumCredits());
        assertEquals(4, p.getShip().getNumCrew());
        assertEquals(2, p.getShip().getTile(2,2).getNumCrew());
        assertEquals(2, p.getShip().getTile(3,2).getNumCrew());
        assertEquals(12, p.getCurrentCell());
        controller.onActionReceived(action2);
        assertEquals(0, p2.getNumCredits());
        assertEquals(4, p2.getShip().getNumCrew());
        assertEquals(2, p2.getShip().getTile(2,4).getNumCrew());
        assertEquals(2, p2.getShip().getTile(2,3).getNumCrew());
        assertEquals(10, p2.getCurrentCell());
        controller.onActionReceived(action3);
        assertInstanceOf(FlightState.class, game.getGameState());
        assertNull(game.getCurrentAdventureCard());
        assertThrows(RuntimeException.class, () ->  controller.onActionReceived(action1));
    }

    @Test
    void testAbandonedStationPattern(){
        game.setCurrentAdventureCard(game.getCardById(39));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        List<Map<BoxType,Integer>> newBoxes1 = new ArrayList<>();
        List<Map<BoxType,Integer>> newBoxes2 = new ArrayList<>();
        newBoxes2.add(new HashMap<>(Map.of(BoxType.RED, 1, BoxType.GREEN, 0, BoxType.YELLOW, 1, BoxType.BLUE, 0)));
        List<Coordinates> coordinates1 = new ArrayList<>();
        List<Coordinates> coordinates2 = new ArrayList<>();
        coordinates2.add(new Coordinates(2, 1));
        PlayerAction action1 = new HandleBoxesAction(coordinates1, newBoxes1, game);
        PlayerAction action2 = new HandleBoxesAction(coordinates2, newBoxes2, game);
        game.getCurrentAdventureCard().setMembersNeeded(4);
        game.getGameState().handleAction(p, action1);
        assertInstanceOf(AbandonedStationState.class, game.getGameState());
        game.getGameState().handleAction(p2, action2);
        assertEquals(Map.of(BoxType.RED, 1, BoxType.GREEN, 0, BoxType.YELLOW, 1, BoxType.BLUE, 0), p2.getShip().getBoxes());
        assertEquals(Map.of(BoxType.RED, 1, BoxType.GREEN, 0, BoxType.YELLOW, 1, BoxType.BLUE, 0), p2.getShip().getTile(2,1).getBoxes());
        assertEquals(9, p2.getCurrentCell());
        assertInstanceOf(FlightState.class, game.getGameState());
    }

    @Test
    void testPlanets(){
        List<Map<BoxType,Integer>> newBoxes = new ArrayList<>();
        newBoxes.add(new HashMap<>(Map.of(BoxType.RED, 2, BoxType.GREEN, 0, BoxType.YELLOW, 0, BoxType.BLUE, 0)));
        List<List<Integer>> coordinates = new ArrayList<>();
        coordinates.add(List.of(2, 3));
        game.setCurrentAdventureCard(game.getCardById(32));
        Planets card = (Planets) game.getCurrentAdventureCard();
        card.createListIsOccupied();
        p.choosePlanet(1);
        card.solveEffect(p, coordinates, newBoxes);
        assertEquals(Map.of(BoxType.RED, 2, BoxType.GREEN, 0, BoxType.YELLOW, 0, BoxType.BLUE, 0), p.getShip().getBoxes());
        assertEquals(Map.of(BoxType.RED, 2, BoxType.GREEN, 0, BoxType.YELLOW, 0, BoxType.BLUE, 0), p.getShip().getTile(2,3).getBoxes());
        assertThrows(RuntimeException.class, () -> p.choosePlanet(1));
    }

    @Test
    void PlanetsPattern(){
        game.setCurrentAdventureCard(game.getCardById(13));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        PlanetsState gameState = (PlanetsState)game.getGameState();
        Integer planetIdx1 =  0;
        Integer planetIdx2 = null;
        Integer planetIdx3 = 2;
        List<Map<BoxType,Integer>> boxes1 = new ArrayList<>();
        List<Map<BoxType,Integer>> boxes2 = new ArrayList<>();
        boxes1.add(new HashMap<>(Map.of(BoxType.YELLOW, 0, BoxType.GREEN, 0, BoxType.BLUE, 0, BoxType.RED, 0)));
        boxes2.add(new HashMap<>(Map.of(BoxType.YELLOW, 1, BoxType.GREEN, 0, BoxType.BLUE, 0, BoxType.RED, 0)));
        List<Coordinates> coordinates1 = new ArrayList<>();
        List<Coordinates> coordinates2 = new ArrayList<>();
        coordinates1.add(new Coordinates(2, 3));
        coordinates2.add(new Coordinates(3, 6));
        PlayerAction action = new PlanetsAction(planetIdx1, coordinates1, boxes1, game);
        PlayerAction action2 = new PlanetsAction(null, null, null, game);
        PlayerAction action3 = new PlanetsAction(planetIdx3, coordinates2, boxes2, game);
        assertTrue(action.checkAction(p));
        gameState.handleAction(p, action);
        assertFalse(action.checkAction(p3));
        assertTrue(action2.checkAction(p2));
        gameState.handleAction(p2, action2);
        assertTrue(action3.checkAction(p3));
        gameState.handleAction(p3, action3);
        assertEquals(6, p3.getCurrentCell());
        assertEquals(9, p.getCurrentCell());
        assertInstanceOf(FlightState.class, game.getGameState());
        assertNull(game.getCurrentAdventureCard());
    }
}
