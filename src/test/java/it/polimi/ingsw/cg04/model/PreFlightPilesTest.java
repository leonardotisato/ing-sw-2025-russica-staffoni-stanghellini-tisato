package it.polimi.ingsw.cg04.model;

import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PreFlightPilesTest {
    Game game;

    @BeforeEach
    void setUp() {
        game = new Game(2, 3, 1, "Piero", PlayerColor.RED);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createAdventureCardsDeck() {
        System.out.println("PreFlightPiles: ");
        for (int i = 0; i < game.getPreFlightPiles().size(); i++) {
            System.out.println("Pile " + i);
            for (int j = 0; j < game.getPreFlightPiles().get(i).size(); j++) {
                int id = game.getPreFlightPiles().get(i).get(j);
                System.out.println("[ID, Level, Name] [" + id + ", " + game.getAdventureCardsMap().get(id).getCardLevel() + ", " + game.getAdventureCardsMap().get(id).getType() + "]");
            }

        }

        System.out.println("AdventureCardsDeck: ");
        for (int i = 0; i < game.getAdventureCardsDeck().size(); i++) {
            int id = game.getAdventureCardsDeck().get(i);
            System.out.println("[ID, Level, Name] [" + id + ", " + game.getAdventureCardsMap().get(id).getCardLevel() + ", " + game.getAdventureCardsMap().get(id).getType() + "]");
        }
    }
}