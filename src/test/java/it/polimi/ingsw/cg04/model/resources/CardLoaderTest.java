package it.polimi.ingsw.cg04.model.resources;

import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.enumerations.Meteor;
import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.utils.CardLoader;
import it.polimi.ingsw.cg04.model.utils.TileLoader;
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
    
    @Test
    void LoadAllFromJson() {
        // Carica il JSON di test
        List<Integer> cards1 = new ArrayList<>();
        List<Integer> cards2 = new ArrayList<>();
        Map<Integer, AdventureCard> cards = CardLoader.loadCardsFromJson("src/test/java/it/polimi/ingsw/cg04/model/resources/AdventureCardsFile.json", cards1, cards2);

        assertNotNull(cards);
        assertNotNull(cards1);
        assertNotNull(cards2);
        assertFalse(cards.isEmpty());
        assertFalse(cards1.isEmpty());
        assertFalse(cards2.isEmpty());

        assertInstanceOf(AbandonedShip.class, cards.get(1));
        assertInstanceOf(AbandonedStation.class, cards.get(2));
        assertInstanceOf(Epidemic.class, cards.get(3));
        assertInstanceOf(MeteorsRain.class, cards.get(4));
        assertInstanceOf(OpenSpace.class, cards.get(5));
        assertInstanceOf(Pirates.class, cards.get(6));
        assertInstanceOf(Planets.class, cards.get(7));
        assertInstanceOf(Slavers.class, cards.get(8));
        assertInstanceOf(Smugglers.class, cards.get(9));
        assertInstanceOf(Stardust.class, cards.get(10));
        assertInstanceOf(WarZone.class, cards.get(11));
    }

}
