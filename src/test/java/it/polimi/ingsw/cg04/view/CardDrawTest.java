package it.polimi.ingsw.cg04.view;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.adventureCards.Planets;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardDrawTest {
    private Game game;
    @BeforeEach
    void setUp() {
        game = new Game(2, 4, 0, "Alice", PlayerColor.RED);
    }
    @Test
    public void drawOpenSpaceCard() {
        AdventureCard card = game.getCardById(5);
        System.out.println(card.draw());
    }
    @Test
    public void drawEpidemicCard() {
        AdventureCard card = game.getCardById(25);
        System.out.println(card.draw());
    }
    @Test
    public void drawAbandonedShipCard() {
        AdventureCard card = game.getCardById(37);
        System.out.println(card.draw());
    }
    @Test
    public void drawAbandonedStationCard() {
        AdventureCard card = game.getCardById(39);
        System.out.println(card.draw());
    }
    @Test
    public void drawMeteorsRainCard() {
        AdventureCard card = game.getCardById(9);
        System.out.println(card.draw());
    }

    @Test
    public void drawPiratesCard() {
        AdventureCard card = game.getCardById(3);
        System.out.println(card.draw());
    }

    @Test
    public void drawPlanetsCard() {
        AdventureCard card = game.getCardById(13);
        System.out.println(card.draw());
    }

    @Test
    public void drawSlaversCard() {
        AdventureCard card = game.getCardById(1);
        System.out.println(card.draw());
    }

    @Test
    public void drawSmugglersCard() {
        AdventureCard card = game.getCardById(2);
        System.out.println(card.draw());
    }

    @Test
    public void drawStardustCard() {
        AdventureCard card = game.getCardById(4);
        System.out.println(card.draw());
    }

    @Test
    public void drawWarzoneCard() {
        AdventureCard card = game.getCardById(36);
        System.out.println(card.draw());
    }

    @Test
    public void drawAllPlanetsCard() {
        for (Integer i : game.getAdventureCardsMap().keySet()) {
            AdventureCard card = game.getCardById(i);
            if (card.getType().equals("Planets")) {
                System.out.println(card.draw());
            }
        }
    }


}
