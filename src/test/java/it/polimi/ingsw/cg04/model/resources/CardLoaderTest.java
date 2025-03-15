package it.polimi.ingsw.cg04.model.resources;

import it.polimi.ingsw.cg04.model.adventureCards.AbandonedShip;
import it.polimi.ingsw.cg04.model.adventureCards.AbandonedStation;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.adventureCards.MeteorsRain;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Meteor;
import it.polimi.ingsw.cg04.model.utils.CardLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CardLoaderTest {

    @Test
    void testAbandonedStationFromJson() {
        // Carica il JSON di test
        List<Integer> cards1 = new ArrayList<>();
        List<Integer> cards2 = new ArrayList<>();
        Map<Integer, AdventureCard> cards = CardLoader.loadCardsFromJson("src/test/java/it/polimi/ingsw/cg04/model/resources/AdventureCardsFile.json", cards1, cards2);

        assertNotNull(cards);
        assertFalse(cards.isEmpty());

        AdventureCard card = cards.get(2);
        AbandonedStation abandonedStation = (AbandonedStation) card;
        assertNotNull(card);
        assertInstanceOf(AbandonedStation.class, card);
        assertEquals(1, abandonedStation.getCardLevel());
        assertEquals(30, abandonedStation.getDaysLost());
        assertEquals(1, abandonedStation.getMembersNeeded());
        assertTrue(abandonedStation.getObtainedResources().containsKey(BoxType.RED));
        assertTrue(abandonedStation.getObtainedResources().containsKey(BoxType.YELLOW));
    }

    @Test
    void testAbandonedShipFromJson() {
        // Carica il JSON di test
        List<Integer> cards1 = new ArrayList<>();
        List<Integer> cards2 = new ArrayList<>();
        Map<Integer, AdventureCard> cards = CardLoader.loadCardsFromJson("src/test/java/it/polimi/ingsw/cg04/model/resources/AdventureCardsFile.json", cards1, cards2);

        assertNotNull(cards);
        assertFalse(cards.isEmpty());

        AdventureCard card = cards.get(1);
        AbandonedShip abandonedShip = (AbandonedShip) card;
        assertNotNull(card);
        assertInstanceOf(AbandonedShip.class, card);
        assertEquals(1, abandonedShip.getCardLevel());
        assertEquals(50, abandonedShip.getDaysLost());
        assertEquals(2, abandonedShip.getLostMembers());
        assertEquals(1, abandonedShip.getEarnedCredits());

    }

    @Test
    void testMeteorsRainFromJson() {
        // Carica il JSON di test
        List<Integer> cards1 = new ArrayList<>();
        List<Integer> cards2 = new ArrayList<>();
        Map<Integer, AdventureCard> cards = CardLoader.loadCardsFromJson("src/test/java/it/polimi/ingsw/cg04/model/resources/AdventureCardsFile.json", cards1, cards2);

        assertNotNull(cards);
        assertFalse(cards.isEmpty());

        AdventureCard card = cards.get(3);
        MeteorsRain meteorsRain = (MeteorsRain) card;
        assertNotNull(card);
        assertInstanceOf(MeteorsRain.class, card);
        assertEquals(1, meteorsRain.getCardLevel());
        assertEquals(60, meteorsRain.getDaysLost());
        assertEquals(List.of(Direction.UP, Direction.DOWN), meteorsRain.getDirections());
        assertEquals(List.of(Meteor.HEAVYMETEOR, Meteor.HEAVYMETEOR), meteorsRain.getMeteors());

    }


}
