package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.GameStates.*;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AbandonedShipState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AbandonedStationState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.OpenSpaceState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.PlanetsState;
import it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions.SetNicknameAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.PlayerActions.AdventureCardActions.*;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
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

        usedBatteries2.add(1);
        coordinates2.add(new Coordinates(1, 2));

        PlayerAction action = new ChoosePropulsorAction(p.getName(),coordinates1, usedBatteries1);
        PlayerAction action2 = new ChoosePropulsorAction(p2.getName() ,coordinates2, usedBatteries2);
        PlayerAction action3 = new ChoosePropulsorAction(p3.getName() ,coordinates3, usedBatteries3);

        try {
            controller.onActionReceived(action);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertEquals(13, p.getCurrentCell());
        assertEquals(3, p.getShip().getNumBatteries());
        assertEquals(3, p.getShip().getTile(2,1).getNumBatteries());
        assertInstanceOf(OpenSpaceState.class, game.getGameState());
        assertThrows(InvalidStateException.class, () -> action3.execute(p3));


        try {
            controller.onActionReceived(action2);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertEquals(14, p2.getCurrentCell());
        assertEquals(5, p2.getShip().getNumBatteries());
        assertEquals(2, p2.getShip().getTile(1,2).getNumBatteries());
        assertInstanceOf(OpenSpaceState.class, game.getGameState());

        try {
            controller.onActionReceived(action3);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertEquals(8, p3.getCurrentCell());
        assertEquals(2, p3.getShip().getNumBatteries());

        assertInstanceOf(FlightState.class, game.getGameState());
        assertNull(game.getCurrentAdventureCard());
        assertThrows(InvalidStateException.class, () ->  action.execute(p));
    }

    @Test
    void testAbandonedShipPattern(){
        game.setCurrentAdventureCard(game.getCardById(37));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        List<Integer> numCrewMembersLost1 = new ArrayList<>();
        List<Coordinates> coordinates1 = new ArrayList<>();

        PlayerAction action1 = new RemoveCrewAction(p.getName(),null, null);
        PlayerAction action2 = new RemoveCrewAction(p2.getName(),null, null);
        PlayerAction action3 = new RemoveCrewAction(p3.getName(),null, null);

        assertThrows(InvalidStateException.class, () -> action2.execute(p2));

        try {
            controller.onActionReceived(action1);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertInstanceOf(AbandonedShipState.class, game.getGameState());
        assertEquals(0, p.getNumCredits());
        assertEquals(4, p.getShip().getNumCrew());
        assertEquals(2, p.getShip().getTile(2,2).getNumCrew());
        assertEquals(2, p.getShip().getTile(3,2).getNumCrew());
        assertEquals(12, p.getCurrentCell());

        try {
            controller.onActionReceived(action2);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertEquals(0, p2.getNumCredits());
        assertEquals(4, p2.getShip().getNumCrew());
        assertEquals(2, p2.getShip().getTile(2,4).getNumCrew());
        assertEquals(2, p2.getShip().getTile(2,3).getNumCrew());
        assertEquals(10, p2.getCurrentCell());

        try {
            controller.onActionReceived(action3);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertInstanceOf(FlightState.class, game.getGameState());
        assertNull(game.getCurrentAdventureCard());
        assertThrows(InvalidStateException.class, () ->  action1.execute(p));
    }

    @Test
    public void renderAbandonedShipState(){
        game.setCurrentAdventureCard(game.getCardById(37));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        GameState state = game.getGameState();
        System.out.println(state.render("Filippo"));
    }

    @Test
    void testAbandonedStationPattern(){
        game.setCurrentAdventureCard(game.getCardById(39));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        List<Map<BoxType,Integer>> newBoxes1 = new ArrayList<>();

        List<Coordinates> storageTilesCoordinates2 = p2.getShip().getTilesMap().get("StorageTile");
        List<Map<BoxType,Integer>> newBoxes2 = new ArrayList<>();
        newBoxes2.add(new HashMap<>(Map.of(BoxType.RED, 1, BoxType.GREEN, 0, BoxType.YELLOW, 1, BoxType.BLUE, 0)));
        while (newBoxes2.size() < storageTilesCoordinates2.size()) {
            newBoxes2.add(new HashMap<>(Map.of(BoxType.RED, 0, BoxType.GREEN, 0, BoxType.YELLOW, 0, BoxType.BLUE, 0)));
        }
        List<Coordinates> coordinates1 = new ArrayList<>();

        PlayerAction action1 = new HandleBoxesAction(p.getName(), coordinates1, newBoxes2);
        PlayerAction action2 = new HandleBoxesAction(p2.getName(), storageTilesCoordinates2, newBoxes2);

        game.getCurrentAdventureCard().setMembersNeeded(3);
        assertThrows(InvalidActionException.class, () ->  action1.checkAction(p));
        assertInstanceOf(AbandonedStationState.class, game.getGameState());
        PlayerAction action3 = new HandleBoxesAction(p.getName(), null, null);

        try {
            controller.onActionReceived(action3);
        } catch (InvalidActionException | InvalidStateException ignored) {        }
        try {
            controller.onActionReceived(action2);
        } catch (InvalidActionException | InvalidStateException ignored) {        }

        assertEquals(Map.of(BoxType.RED, 1, BoxType.GREEN, 0, BoxType.YELLOW, 1, BoxType.BLUE, 0), p2.getShip().getBoxes());
        assertEquals(9, p2.getCurrentCell());
        assertInstanceOf(FlightState.class, game.getGameState());
        assertNull(game.getCurrentAdventureCard());
    }

    @Test
    public void renderAbandonedStationState(){
        game.setCurrentAdventureCard(game.getCardById(39));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        GameState state = game.getGameState();
        System.out.println(state.render("Filippo"));
    }

    @Test
    void PlanetsPattern(){
        game.setCurrentAdventureCard(game.getCardById(13));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        PlanetsState gameState = (PlanetsState)game.getGameState();
        Integer planetIdx1 =  0;
        Integer planetIdx2 = null;
        Integer planetIdx3 = 2;

        List<Coordinates> storageTilesCoordinates1 = p.getShip().getTilesMap().get("StorageTile");
        List<Coordinates> storageTilesCoordinates2 = p2.getShip().getTilesMap().get("StorageTile");
        List<Coordinates> storageTilesCoordinates3 = p3.getShip().getTilesMap().get("StorageTile");

        List<Map<BoxType,Integer>> boxes1 = new ArrayList<>();

        while (boxes1.size() < storageTilesCoordinates1.size()) {
            boxes1.add(new HashMap<>(Map.of(BoxType.RED, 0, BoxType.GREEN, 0, BoxType.YELLOW, 0, BoxType.BLUE, 0)));
        }

        List<Map<BoxType,Integer>> boxes3 = new ArrayList<>();

        boxes3.add(new HashMap<>(Map.of(BoxType.YELLOW, 1, BoxType.GREEN, 0, BoxType.BLUE, 0, BoxType.RED, 0)));
        while (boxes3.size() < storageTilesCoordinates3.size()) {
            boxes3.add(new HashMap<>(Map.of(BoxType.RED, 0, BoxType.GREEN, 0, BoxType.YELLOW, 0, BoxType.BLUE, 0)));
        }

        PlayerAction action = new PlanetsAction(p.getName(), planetIdx1, storageTilesCoordinates1, boxes1);
        PlayerAction action2 = new PlanetsAction(p2.getName(), null, null, null);
        PlayerAction action3 = new PlanetsAction(p3.getName(),planetIdx3, storageTilesCoordinates3, boxes3);
        try {
            controller.onActionReceived(action);
        } catch (InvalidActionException | InvalidStateException ignored) {        }
        assertThrows(InvalidStateException.class, () -> action3.execute(p3));
        try {
            controller.onActionReceived(action2);
        } catch (InvalidActionException | InvalidStateException ignored) {        }
        try {
            controller.onActionReceived(action3);
        } catch (InvalidActionException | InvalidStateException ignored) {        }
        assertEquals(6, p3.getCurrentCell());
        assertEquals(9, p.getCurrentCell());
        assertInstanceOf(FlightState.class, game.getGameState());
        assertNull(game.getCurrentAdventureCard());
    }

    @Test
    void EpidemicPattern(){
        game.setCurrentAdventureCard(game.getCardById(25));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        List<Coordinates> coordinates1 = new ArrayList<>();
        List<Coordinates> coordinates2 = new ArrayList<>();
        PlayerAction action = new EpidemicAction(p.getName());
        PlayerAction action2 = new EpidemicAction(p2.getName());
        PlayerAction action3 = new EpidemicAction(p3.getName());
        try {
            controller.onActionReceived(action);
        } catch (InvalidActionException | InvalidStateException ignored) {        }
        assertThrows(InvalidStateException.class, () -> action.execute(p));
        try {
            controller.onActionReceived(action2);
        } catch (InvalidActionException | InvalidStateException ignored) {        }
        try {
            controller.onActionReceived(action3);
        } catch (InvalidActionException | InvalidStateException ignored) {        }
        assertEquals(2, p.getShip().getNumCrew());
        assertEquals(2, p.getShip().getNumCrew());
        assertEquals(2, p.getShip().getNumCrew());
        assertInstanceOf(FlightState.class, game.getGameState());
        assertNull(game.getCurrentAdventureCard());
    }

    @Test
    public void renderEpidemicState(){
        game.setCurrentAdventureCard(game.getCardById(25));
        game.setGameState(game.getCurrentAdventureCard().createState(game));
        GameState state = game.getGameState();
        System.out.println(state.render("Martin"));
    }

}
