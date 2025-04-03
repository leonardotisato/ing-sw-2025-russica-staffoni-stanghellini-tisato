package it.polimi.ingsw.cg04.model.resources;

import it.polimi.ingsw.cg04.model.adventureCards.*;
import it.polimi.ingsw.cg04.model.utils.CardLoader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CardLoaderTest {

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
