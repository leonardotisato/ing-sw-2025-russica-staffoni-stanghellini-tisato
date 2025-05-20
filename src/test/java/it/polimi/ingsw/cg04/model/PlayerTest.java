package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Game game1;
    Player player1;

    @BeforeEach
    void setUp() {
        game1 = new Game(2);
        player1 = new Player("Alice", PlayerColor.RED, game1);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getName() {
        assertEquals("Alice", player1.getName());
    }

    @Test
    void getColor() {
        assertEquals(PlayerColor.RED, player1.getColor());
    }

    @Test
    void getShip() {
        assertNotNull(player1.getShip());
        assertEquals(2, player1.getShip().getLevel());
        assertEquals(PlayerColor.RED, player1.getShip().getColor());
    }

    @Test
    void getPosition() {
    }

    @Test
    void move() {
    }

    @Test
    void getLoops() {
    }

    @Test
    void getCurrentCell() {
    }

    @Test
    void getNumCredits() {
        assertEquals(0, player1.getNumCredits());
    }

    @Test
    void updateCredits() {
        player1.updateCredits(4);
        assertEquals(4, player1.getNumCredits());
        player1.updateCredits(5);
        assertEquals(9, player1.getNumCredits());
        player1.updateCredits(-7);
        assertEquals(2, player1.getNumCredits());
        player1.updateCredits(-6);
        assertEquals(0, player1.getNumCredits());
    }

    @Test
    void chooseFaceUpTile() {

    }

    @Test
    void pickFaceDownTile() {

    }

    @Test
    void returnTile() {
    }

    @Test
    void bookTile() {
    }

    @Test
    void placeTile() {
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
    void addCrewByType() {
    }

    @Test
    void useBattery() {
    }
}