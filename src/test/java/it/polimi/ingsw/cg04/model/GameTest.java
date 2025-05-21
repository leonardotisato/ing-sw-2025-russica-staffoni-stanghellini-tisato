package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.ExGameState;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game gameLev2;
    Shipyard shipyard = new Shipyard();

    @BeforeEach
    void setUp() {
        gameLev2 = new Game(2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addPlayer() {
        assertEquals(0, gameLev2.getNumPlayers());

        gameLev2.addPlayer("Alice", PlayerColor.RED);
        assertNotNull(gameLev2.getPlayers());
        assertEquals(1, gameLev2.getNumPlayers());
        assertEquals("Alice", gameLev2.getPlayers().getFirst().getName());

        // try to add player with same name or same color
        assertThrows(IllegalArgumentException.class, () -> gameLev2.addPlayer("Bob", PlayerColor.RED));
        assertThrows(IllegalArgumentException.class, () -> gameLev2.addPlayer("Alice", PlayerColor.BLUE));

        // add other players
        gameLev2.addPlayer("Bob", PlayerColor.BLUE);
        gameLev2.addPlayer("Charlie", PlayerColor.GREEN);
        gameLev2.addPlayer("Dave", PlayerColor.YELLOW);

        assertEquals(4, gameLev2.getNumPlayers());

        // check info retrieval of player in players list
        Player dave = gameLev2.getPlayer("Dave");
        assertEquals("Dave", dave.getName());
        assertEquals(PlayerColor.YELLOW, dave.getColor());

        // try to add one more player, exceeding maximum
        assertThrows(RuntimeException.class, () -> gameLev2.addPlayer("Please add me too!", PlayerColor.RED));

        assert(gameLev2.getNumPlayers() == 4);
    }

    @Test
    void removePlayer() {
        // try to remove non-existing player
        assertThrows(IllegalArgumentException.class, () -> gameLev2.removePlayer("Alice"));

        gameLev2.addPlayer("Alice", PlayerColor.RED);
        assertThrows(IllegalArgumentException.class, () -> gameLev2.removePlayer("Bob"));

        gameLev2.addPlayer("Bob", PlayerColor.BLUE);
        gameLev2.addPlayer("Charlie", PlayerColor.GREEN);
        gameLev2.addPlayer("Dave", PlayerColor.YELLOW);

        assertEquals(4, gameLev2.getNumPlayers());
        assertThrows(IllegalArgumentException.class, () -> gameLev2.removePlayer(null));

        gameLev2.removePlayer("Alice");
        assertEquals(3, gameLev2.getNumPlayers());

        gameLev2.removePlayer("Bob");
        assertEquals(2, gameLev2.getNumPlayers());

        gameLev2.removePlayer("Charlie");
        assertEquals(1, gameLev2.getNumPlayers());

        gameLev2.removePlayer("Dave");
        assertEquals(0, gameLev2.getNumPlayers());

    }

    @Test
    void createAdventureDeck() {
        // todo: check this method was tested in cardLoader
    }

    @Test
    void getPlayers() {
        Player alice = gameLev2.addPlayer("Alice", PlayerColor.RED);
        Player bob = gameLev2.addPlayer("Bob", PlayerColor.BLUE);

        assertEquals(alice, gameLev2.getPlayer("Alice"));
        assertEquals(bob, gameLev2.getPlayer("Bob"));

    }

    @Test
    void getSortedPlayers() {
        Player alice = gameLev2.addPlayer("Alice", PlayerColor.RED);
        Player bob = gameLev2.addPlayer("Bob", PlayerColor.BLUE);
        Player charlie = gameLev2.addPlayer("Charlie", PlayerColor.GREEN);
        Player dave = gameLev2.addPlayer("Dave", PlayerColor.YELLOW);
        
        assertEquals(4, gameLev2.getNumPlayers());
        

        FlightBoard fb = gameLev2.getBoard();
        System.out.println(fb);

        alice.move(fb.getStartingPosition(1));
        bob.move(fb.getStartingPosition(2));
        charlie.move(fb.getStartingPosition(3));
        dave.move(fb.getStartingPosition(4));

        System.out.println(fb);

        assertEquals(4, gameLev2.getSortedPlayers().size());
        assertEquals(alice, gameLev2.getSortedPlayers().get(0));
        assertEquals(bob, gameLev2.getSortedPlayers().get(1));
        assertEquals(charlie, gameLev2.getSortedPlayers().get(2));
        assertEquals(dave, gameLev2.getSortedPlayers().get(3));

        dave.move(1);
        System.out.println(fb);
        assertEquals(4, gameLev2.getSortedPlayers().size());
        assertEquals(alice, gameLev2.getSortedPlayers().get(0));
        assertEquals(bob, gameLev2.getSortedPlayers().get(1));
        assertEquals(dave, gameLev2.getSortedPlayers().get(2));
        assertEquals(charlie, gameLev2.getSortedPlayers().get(3));

        charlie.move(10);
        System.out.println(fb);
        assertEquals(4, gameLev2.getSortedPlayers().size());
        assertEquals(charlie, gameLev2.getSortedPlayers().get(0));
        assertEquals(alice, gameLev2.getSortedPlayers().get(1));
        assertEquals(bob, gameLev2.getSortedPlayers().get(2));
        assertEquals(dave, gameLev2.getSortedPlayers().get(3));

        // charlie completes a loop, make sure he is still first
        charlie.move(10);
        System.out.println(fb);
        assertEquals(4, gameLev2.getSortedPlayers().size());
        assertEquals(charlie, gameLev2.getSortedPlayers().get(0));
        assertEquals(alice, gameLev2.getSortedPlayers().get(1));
        assertEquals(bob, gameLev2.getSortedPlayers().get(2));
        assertEquals(dave, gameLev2.getSortedPlayers().get(3));

    }

    @Test
    void getNumPlayers() {
        assertEquals(0, gameLev2.getNumPlayers());
        gameLev2.addPlayer("Alice", PlayerColor.RED);
        assertEquals(1, gameLev2.getNumPlayers());
        gameLev2.addPlayer("Bob", PlayerColor.BLUE);
        assertEquals(2, gameLev2.getNumPlayers());
        gameLev2.addPlayer("Charlie", PlayerColor.GREEN);
        assertEquals(3, gameLev2.getNumPlayers());
        gameLev2.addPlayer("Dave", PlayerColor.YELLOW);
        assertEquals(4, gameLev2.getNumPlayers());

        gameLev2.removePlayer("Alice");
        assertEquals(3, gameLev2.getNumPlayers());
        gameLev2.removePlayer("Bob");
        assertEquals(2, gameLev2.getNumPlayers());
        gameLev2.removePlayer("Charlie");
        assertEquals(1, gameLev2.getNumPlayers());
        gameLev2.removePlayer("Dave");
        assertEquals(0, gameLev2.getNumPlayers());
    }

    @Test
    void getBoard() {
        assertNotNull(gameLev2.getBoard());
        assertInstanceOf(FlightBoard.class, gameLev2.getBoard());
    }

    @Test
    void getPlayer() {

        // getPlayer(name)
        assertThrows(RuntimeException.class, () -> gameLev2.getPlayer("Alice"));

        Player alice = gameLev2.addPlayer("Alice", PlayerColor.RED);
        assertThrows(RuntimeException.class, () -> gameLev2.getPlayer("Bob"));
        assertEquals(alice, gameLev2.getPlayer("Alice"));

        gameLev2.removePlayer("Alice");
        assertThrows(RuntimeException.class, () -> gameLev2.getPlayer("Alice"));

        // getPlayer(ranking)
        alice = gameLev2.addPlayer("Alice", PlayerColor.RED);
        Player bob = gameLev2.addPlayer("Bob", PlayerColor.BLUE);
        Player charlie = gameLev2.addPlayer("Charlie", PlayerColor.GREEN);
        Player dave = gameLev2.addPlayer("Dave", PlayerColor.YELLOW);

        assertEquals(4, gameLev2.getNumPlayers());


        FlightBoard fb = gameLev2.getBoard();
        System.out.println(fb);

        alice.move(fb.getStartingPosition(1));
        bob.move(fb.getStartingPosition(2));
        charlie.move(fb.getStartingPosition(3));
        dave.move(fb.getStartingPosition(4));

        System.out.println(fb);

        assertEquals(4, gameLev2.getSortedPlayers().size());
        assertEquals(alice, gameLev2.getPlayer(0));
        assertEquals(bob, gameLev2.getPlayer(1));
        assertEquals(charlie, gameLev2.getPlayer(2));
        assertEquals(dave, gameLev2.getPlayer(3));

        dave.move(1);
        System.out.println(fb);
        assertEquals(4, gameLev2.getSortedPlayers().size());
        assertEquals(alice, gameLev2.getPlayer(0));
        assertEquals(bob, gameLev2.getPlayer(1));
        assertEquals(dave, gameLev2.getPlayer(2));
        assertEquals(charlie, gameLev2.getPlayer(3));

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
    void startBuildPhase() {

        Player alice = gameLev2.addPlayer("Alice", PlayerColor.RED);
        Player bob = gameLev2.addPlayer("Bob", PlayerColor.BLUE);
        Player charlie = gameLev2.addPlayer("Charlie", PlayerColor.GREEN);
        Player dave = gameLev2.addPlayer("Dave", PlayerColor.YELLOW);

        //gameLev2.startBuildPhase();

        //assertEquals(ExGameState.BUILDING, gameLev2.getGameState());
        assertEquals(4, gameLev2.getNumPlayers());
//        assertEquals(ExPlayerState.BUILDING, alice.getState());
//        assertEquals(ExPlayerState.BUILDING, bob.getState());
//        assertEquals(ExPlayerState.BUILDING, charlie.getState());
//        assertEquals(ExPlayerState.BUILDING, dave.getState());
    }

    @Test
    void startFlightPhase() {
        Player alice = gameLev2.addPlayer("Alice", PlayerColor.RED);
        Player bob = gameLev2.addPlayer("Bob", PlayerColor.BLUE);
        Player charlie = gameLev2.addPlayer("Charlie", PlayerColor.GREEN);
        Player dave = gameLev2.addPlayer("Dave", PlayerColor.YELLOW);

       // gameLev2.startFlightPhase();

        //assertEquals(ExGameState.FLIGHT, gameLev2.getGameState());
        assertEquals(4, gameLev2.getNumPlayers());
//        assertEquals(ExPlayerState.FLIGHT, alice.getState());
//        assertEquals(ExPlayerState.FLIGHT, bob.getState());
//        assertEquals(ExPlayerState.FLIGHT, charlie.getState());
//        assertEquals(ExPlayerState.FLIGHT, dave.getState());
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
        assertEquals(2, gameLev2.getLevel());
    }

    @Test
    void testDeepCopy() {
        Game game = new Game(2, 4, 0, "Alice", PlayerColor.BLUE);
        game.addPlayer("Bob", PlayerColor.RED);
        game.addPlayer("Charlie", PlayerColor.GREEN);
        game.addPlayer("Dave", PlayerColor.YELLOW);

        game.getPlayer("Alice").move(4);

        game.getPlayer("Bob").setShip(shipyard.createShip5());

        Game copy = game.deepCopy();
        assertEquals(PlayerColor.BLUE, copy.getPlayer("Alice").getColor());
        assertEquals(PlayerColor.RED, copy.getPlayer("Bob").getColor());
        assertEquals(1, copy.getPlayer("Alice").getRanking());
        System.out.println(copy.getBoard());
        System.out.println(copy.getPlayer("Bob").getShip().draw());
    }
}