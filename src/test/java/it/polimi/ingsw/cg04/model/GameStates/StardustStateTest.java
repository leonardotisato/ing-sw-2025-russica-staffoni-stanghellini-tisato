package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.HandleBoxesAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.PlayerActions.StardustAction;
import it.polimi.ingsw.cg04.model.adventureCards.AbandonedStation;
import it.polimi.ingsw.cg04.model.adventureCards.Stardust;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.TileLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StardustStateTest {
    private Game game;
    private List<Player> players;
    private Integer currPlayerIdx;

    @BeforeEach
    void setUp() {
        game = new Game(1, "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json", "src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json");
        game.addPlayer("Leonardo", PlayerColor.GREEN);
        game.addPlayer("Giovanni", PlayerColor.BLUE);

        players = game.getPlayers();
        currPlayerIdx = players.size() - 1;
        game.getBoard().occupyCell(4, players.get(0));
        game.getBoard().occupyCell(12, players.get(1));

        Tile centerTile33 = game.getTilesDeckMap().get(33);
        Tile housingTile35 = game.getTilesDeckMap().get(35);
        housingTile35.rotate90dx();
        Tile propulsorTile71 = game.getTilesDeckMap().get(71);
        Tile batteryTile16 = game.getTilesDeckMap().get(16);players.get(0).getShip().placeTile(centerTile33, 2, 2);
        Tile storageTile68 = game.getTilesDeckMap().get(68);players.get(0).getShip().placeTile(housingTile35, 3, 2);
        Tile laserTile101 = game.getTilesDeckMap().get(101);players.get(0).getShip().placeTile(propulsorTile71, 3, 1);
        laserTile101.rotate90dx();
        Tile laserTile119 = game.getTilesDeckMap().get(119);
        laserTile119.rotate90sx();
        Tile laserTile131 = game.getTilesDeckMap().get(131);
        Map<BoxType, Integer> boxes = new HashMap<>();
        boxes.put(BoxType.BLUE, 1);
        boxes.put(BoxType.RED, 1);
        boxes.put(BoxType.YELLOW, 0);
        boxes.put(BoxType.GREEN, 0);

        players.get(0).getShip().placeTile(batteryTile16, 2, 1);
        players.get(0).getShip().placeTile(storageTile68, 2, 3);
        players.get(0).getShip().placeTile(laserTile119, 1, 1);
        players.get(0).getShip().placeTile(laserTile131, 1, 2);
        players.get(0).getShip().placeTile(laserTile101, 1, 3);

        ArrayList<Integer> faceDownTiles = new ArrayList<>();
        Map<Integer, Tile> tiles = TileLoader.loadTilesFromJson("src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json", faceDownTiles);

        Tile storageTile18 = tiles.get(18);
        Tile structuralTile53 = tiles.get(53);
        Tile propulsorTile76 = tiles.get(76);
        Tile propulsorTile94 = tiles.get(94);
        Tile laserTile106 = tiles.get(106);
        Tile storageTile19 = tiles.get(19);
        Tile laserTile125 = tiles.get(125);
        Tile shieldTile150 = tiles.get(150);
        Tile shieldTile151 = tiles.get(151);
        Tile batteryTile5 = tiles.get(5);
        Tile storageTile26 = tiles.get(26);
        Tile laserTile134 = tiles.get(134); // double
        Tile laserTile121 = tiles.get(121); // single
        Tile laserTile122 = tiles.get(122); // single
        Tile laserTile123 = tiles.get(123); // single
        Tile structuralTile58 = tiles.get(58);
        Tile propulsorTile97 = tiles.get(97); // double
        Tile housingTile46 = tiles.get(46);
        Tile housingTile47 = tiles.get(47);
        Tile housingTile48 = tiles.get(48);
        Tile alienSupportTile141 = tiles.get(141);
        Tile alienSupportTile142 = tiles.get(142);
        Tile alienSupportTile143 = tiles.get(143);
        Tile housingTile33 = tiles.get(33); // central


        players.get(1).getShip().placeTile(housingTile33, 2, 2);
        players.get(1).getShip().placeTile(structuralTile53, 4, 3);
        players.get(1).getShip().placeTile(propulsorTile76, 3, 0);
        players.get(1).getShip().placeTile(propulsorTile94, 3, 1);
        players.get(1).getShip().placeTile(housingTile46, 3, 3);
        players.get(1).getShip().placeTile(alienSupportTile141, 3, 4);
        players.get(1).getShip().placeTile(laserTile106, 2, 0);
        players.get(1).getShip().placeTile(storageTile19, 2, 1);
        players.get(1).getShip().placeTile(laserTile125, 2, 3);
        players.get(1).getShip().placeTile(storageTile18, 1, 2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void triggerNextState() {
    }

    @Test
    void testStarDustTest() {
        game.setCurrentAdventureCard(game.getCardById(24));
        game.setGameState(game.getCurrentAdventureCard().createState(game));

        PlayerAction action1 = new StardustAction(game);

        int expPlayer1 = players.get(0).getShip().getNumExposedConnectors();

        game.getGameState().handleAction(players.get(0), action1);
        assertEquals(4, players.get(0).getCurrentCell());
        assertInstanceOf(StardustState.class, game.getGameState());

        PlayerAction action2 = new StardustAction(game);

        int expPlayer2 = players.get(1).getShip().getNumExposedConnectors();

        game.getGameState().handleAction(players.get(1), action2);
        assertEquals(2, players.get(1).getCurrentCell());
        assertInstanceOf(StardustState.class, game.getGameState());

    }
}