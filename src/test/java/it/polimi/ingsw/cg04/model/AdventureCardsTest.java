package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.PlayerActions.ChoosePropuslsorAction;
import it.polimi.ingsw.cg04.model.PlayerActions.HandleBoxesAction;
import it.polimi.ingsw.cg04.model.PlayerActions.HandleCrewAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.GameStates.AbandonedShipState;
import it.polimi.ingsw.cg04.model.GameStates.AbandonedStationState;
import it.polimi.ingsw.cg04.model.GameStates.OpenSpaceState;
import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AdventureCardsTest {

    private Game game;
    private Player p;
//    Tile centerTile33;
//    Tile housingTile35;
//    Tile propulsorTile71;
//    Tile batteryTile16;
//    Tile storageTile68;
//    Tile laserTile101;

    @BeforeEach
    void setUp() {
        game = new Game(1, "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json", "src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json");
        game.addPlayer("Filippo", PlayerColor.BLUE);
        p = game.getPlayer("Filippo");
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
        game.getBoard().occupyCell(4, p);
    }
    
    @Test
    void testAbandonedShip() {
        List<Integer> numCrewMembersLost = new ArrayList<>();
        numCrewMembersLost.add(2);
        numCrewMembersLost.add(2);
        List<List<Integer>> coordinates = new ArrayList<>();
        coordinates.add(List.of(2, 2));
        coordinates.add(List.of(3, 2));
        game.setCurrentAdventureCard(game.getCardById(37));
        AbandonedShip card = (AbandonedShip) game.getCurrentAdventureCard();
        card.solveEffect(p, numCrewMembersLost, coordinates);
        assertEquals(6, p.getNumCredits());
        assertEquals(0, p.getShip().getNumCrew());
        assertEquals(0, p.getShip().getTile(2,2).getNumCrew());
        assertEquals(0, p.getShip().getTile(3,2).getNumCrew());
        assertEquals(3, p.getCurrentCell());
        assertThrows(RuntimeException.class, () -> card.solveEffect(p, List.of(1), List.of(List.of(2,1))));
    }

    @Test
    void testAbandonedStation() {
        List<Map<BoxType,Integer>> newBoxes = new ArrayList<>();
        newBoxes.add(new HashMap<>(Map.of(BoxType.RED, 1, BoxType.GREEN, 0, BoxType.YELLOW, 1, BoxType.BLUE, 0)));
        List<List<Integer>> coordinates = new ArrayList<>();
        coordinates.add(List.of(2, 3));
        game.setCurrentAdventureCard(game.getCardById(39));
        AbandonedStation card = (AbandonedStation) game.getCurrentAdventureCard();
        card.setMembersNeeded(4);
        card.solveEffect(p, coordinates, newBoxes);
        assertEquals(Map.of(BoxType.RED, 1, BoxType.GREEN, 0, BoxType.YELLOW, 1, BoxType.BLUE, 0), p.getShip().getBoxes());
        assertEquals(Map.of(BoxType.RED, 1, BoxType.GREEN, 0, BoxType.YELLOW, 1, BoxType.BLUE, 0), p.getShip().getTile(2,3).getBoxes());
        card.setMembersNeeded(7);
        assertThrows(RuntimeException.class, () -> card.solveEffect(p, coordinates, newBoxes));
    }

    @Test
    void testEpidemic() {;
        game.setCurrentAdventureCard(game.getCardById(25));
        Epidemic card = (Epidemic) game.getCurrentAdventureCard();
        card.solveEffect(p);
        assertEquals(2, p.getShip().getNumCrew());
        assertEquals(1, p.getShip().getTile(2,2).getNumCrew());
        assertEquals(1, p.getShip().getTile(3,2).getNumCrew());
    }

    @Test
    void testOpenSpace(){
        List<Integer> usedBatteries = new ArrayList<>();
        List<List<Integer>> coordinates = new ArrayList<>();
        game.setCurrentAdventureCard(game.getCardById(26));
        OpenSpace card = (OpenSpace) game.getCurrentAdventureCard();
        card.solveEffect(p, coordinates, usedBatteries);
        assertEquals(5, p.getCurrentCell());
        usedBatteries.add(1);
        coordinates.add(List.of(2, 1));
        card.solveEffect(p, coordinates, usedBatteries);
        assertEquals(8, p.getCurrentCell());
        assertEquals(2, p.getShip().getNumBatteries());
        assertEquals(2, p.getShip().getTile(2,1).getNumBatteries());
    }

    @Test
    void testOpenSpacePattern(){
        p.setCurrState(new OpenSpaceState());
        List<Integer> usedBatteries = new ArrayList<>();
        List<List<Integer>> coordinates = new ArrayList<>();
        usedBatteries.add(1);
        coordinates.add(List.of(2, 1));
        PlayerAction action = new ChoosePropuslsorAction(coordinates, usedBatteries);
        p.handleAction(action);
        assertEquals(7, p.getCurrentCell());
        assertEquals(2, p.getShip().getNumBatteries());
        assertEquals(2, p.getShip().getTile(2,1).getNumBatteries());
        assertEquals(0, p.getActivity());

    }

    @Test
    void testAbandonedShipPattern(){
        p.setCurrState(new AbandonedShipState());
        p.getGame().setCurrentAdventureCard(game.getCardById(37));
        List<Integer> numCrewMembersLost = new ArrayList<>();
        numCrewMembersLost.add(2);
        numCrewMembersLost.add(2);
        List<List<Integer>> coordinates = new ArrayList<>();
        coordinates.add(List.of(2, 2));
        coordinates.add(List.of(3, 2));
        PlayerAction action = new HandleCrewAction(coordinates, numCrewMembersLost);
        p.handleAction(action);
        assertEquals(6, p.getNumCredits());
        assertEquals(0, p.getShip().getNumCrew());
        assertEquals(0, p.getShip().getTile(2,2).getNumCrew());
        assertEquals(0, p.getShip().getTile(3,2).getNumCrew());
        assertEquals(3, p.getCurrentCell());
        assertEquals(0, p.getActivity());
        assertEquals(ExPlayerState.FLIGHT, p.getState());
    }

    @Test
    void testAbandonedStationPattern(){
        p.setCurrState(new AbandonedStationState());
        p.getGame().setCurrentAdventureCard(game.getCardById(39));
        List<Map<BoxType,Integer>> newBoxes = new ArrayList<>();
        newBoxes.add(new HashMap<>(Map.of(BoxType.RED, 1, BoxType.GREEN, 0, BoxType.YELLOW, 1, BoxType.BLUE, 0)));
        List<List<Integer>> coordinates = new ArrayList<>();
        coordinates.add(List.of(2, 3));
        PlayerAction action = new HandleBoxesAction(coordinates, newBoxes);
        p.getGame().getCurrentAdventureCard().setMembersNeeded(4);
        p.handleAction(action);
        assertEquals(Map.of(BoxType.RED, 1, BoxType.GREEN, 0, BoxType.YELLOW, 1, BoxType.BLUE, 0), p.getShip().getBoxes());
        assertEquals(Map.of(BoxType.RED, 1, BoxType.GREEN, 0, BoxType.YELLOW, 1, BoxType.BLUE, 0), p.getShip().getTile(2,3).getBoxes());
        assertEquals(0, p.getActivity());
        assertEquals(ExPlayerState.FLIGHT, p.getState());
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
}
