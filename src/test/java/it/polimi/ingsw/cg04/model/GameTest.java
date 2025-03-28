package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game1;

    @BeforeEach
    void setUp() {
        game1 = new Game(2, "src/main/java/it/polimi/ingsw/cg04/resources/AdventureCardsFile.json", "src/main/java/it/polimi/ingsw/cg04/resources/TilesFile.json");

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void setNumPlayers() {
    }

    @Test
    void addPlayer() {
        assertEquals(0, game1.getNumPlayers());

        game1.addPlayer("Alice", PlayerColor.RED);
        assertNotNull(game1.getPlayers());
        assertEquals(1, game1.getNumPlayers());
        assertEquals("Alice", game1.getPlayers().getFirst().getName());

        // try to add player with same name or same color
        assertThrows(IllegalArgumentException.class, () -> game1.addPlayer("Bob", PlayerColor.RED));
        assertThrows(IllegalArgumentException.class, () -> game1.addPlayer("Alice", PlayerColor.BLUE));

        // add other players
        game1.addPlayer("Bob", PlayerColor.BLUE);
        game1.addPlayer("Charlie", PlayerColor.GREEN);
        game1.addPlayer("Dave", PlayerColor.YELLOW);

        assertEquals(4, game1.getNumPlayers());

        // check info retrieval of player in players list
        Player dave = game1.getPlayer("Dave");
        assertEquals("Dave", dave.getName());
        assertEquals(PlayerColor.YELLOW, dave.getColor());

        // try to add one more player, exceeding maximum
        assertThrows(RuntimeException.class, () -> game1.addPlayer("Please add me too!", PlayerColor.RED));

        assert(game1.getNumPlayers() == 4);
    }

    @Test
    void removePlayer() {
        // try to remove non-existing player
        assertThrows(IllegalArgumentException.class, () -> game1.removePlayer("Alice"));

        game1.addPlayer("Alice", PlayerColor.RED);
        assertThrows(IllegalArgumentException.class, () -> game1.removePlayer("Bob"));

        game1.addPlayer("Bob", PlayerColor.BLUE);
        game1.addPlayer("Charlie", PlayerColor.GREEN);
        game1.addPlayer("Dave", PlayerColor.YELLOW);

        assertEquals(4, game1.getNumPlayers());
        assertThrows(IllegalArgumentException.class, () ->game1.removePlayer(null));

        game1.removePlayer("Alice");
        assertEquals(3, game1.getNumPlayers());

        game1.removePlayer("Bob");
        assertEquals(2, game1.getNumPlayers());

        game1.removePlayer("Charlie");
        assertEquals(1, game1.getNumPlayers());

        game1.removePlayer("Dave");
        assertEquals(0, game1.getNumPlayers());

    }

    @Test
    void setBoard() {
    }

    @Test
    void createAdventureDeck() {
    }

    @Test
    void getPlayers() {
        Player alice = game1.addPlayer("Alice", PlayerColor.RED);
        Player bob = game1.addPlayer("Bob", PlayerColor.BLUE);

        assertEquals(alice, game1.getPlayer("Alice"));

    }

    @Test
    void getSortedPlayers() {
        Player alice = game1.addPlayer("Alice", PlayerColor.RED);
        Player bob = game1.addPlayer("Bob", PlayerColor.BLUE);
        Player charlie = game1.addPlayer("Charlie", PlayerColor.GREEN);
        Player dave = game1.addPlayer("Dave", PlayerColor.YELLOW);
        
        assertEquals(4, game1.getNumPlayers());
        

        FlightBoard fb = game1.getBoard();
        System.out.println(fb);

        alice.move(fb.getStartingPosition(1));
        bob.move(fb.getStartingPosition(2));
        charlie.move(fb.getStartingPosition(3));
        dave.move(fb.getStartingPosition(4));

        System.out.println(fb);




    }

    @Test
    void getNumPlayers() {
    }

    @Test
    void getBoard() {
    }

    @Test
    void getPlayer() {
    }

    @Test
    void testGetPlayer() {
    }

    @Test
    void getCardById() {
    }

    @Test
    void getTileById() {
    }

    @Test
    void getFaceDownTiles() {
    }

    @Test
    void getFaceUpTiles() {
    }

    @Test
    void rollDices() {
    }

    @Test
    void startBuildPhase() {
    }

    @Test
    void startFLIGHT() {
    }

    @Test
    void checkShips() {
    }

    @Test
    void calculateBestShip() {
    }

    @Test
    void giveEndCredits() {
    }

    @Test
    void handleEndGame() {
    }

    @Test
    void movePlayer() {
    }

    @Test
    void placeTile() {
    }

    @Test
    void chooseFaceUpTile() {
    }

    @Test
    void pickFaceDownTile() {
    }

    @Test
    void updateCredits() {
    }

    @Test
    void getNextAdventureCard() {
    }

    @Test
    void buildPiles() {
    }

    @Test
    void bookTile() {
    }

    @Test
    void returnTile() {
    }

    @Test
    void chooseBookedTile() {
    }

    @Test
    void showFaceUpTiles() {
    }

    @Test
    void showPile() {
    }

    @Test
    void returnPile() {
    }

    @Test
    void loadResource() {
    }

    @Test
    void removeResource() {
    }

    @Test
    void removeCrew() {
    }

    @Test
    void useBattery() {
    }

    @Test
    void setPlayerState() {
    }

    @Test
    void setGameState() {
    }

    @Test
    void beginGame() {
    }

    @Test
    void endGame() {
    }

    @Test
    void getLevel() {
        assertEquals(2, game1.getLevel());
    }
}