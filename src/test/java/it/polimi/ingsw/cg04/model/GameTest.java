package it.polimi.ingsw.cg04.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void removePlayer() {
    }

    @Test
    void setBoard() {
    }

    @Test
    void createAdventureDeck() {
    }

    @Test
    void getPlayers() {
    }

    @Test
    void getSortedPlayers() {
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