package it.polimi.ingsw.cg04.model.advetureCards;

import java.util.HashMap;
import java.util.Map;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

public class AbandonedStation extends AdventureCard {

    private final int membersNeeded;
    private final Map<BoxType, Integer> obtainedResources;

    public AbandonedStation(int cardLevel, int daysLost, int membersNeeded) {
        super(cardLevel, daysLost);
        obtainedResources = new HashMap<>();
        this.membersNeeded = membersNeeded;
    }

    public int getMembersNeeded() {
        return membersNeeded;
    }

    public Map<BoxType, Integer> getObtainedResources() {
        return obtainedResources;
    }

    public int getObtainedResourcesByType(BoxType boxType) {
        return obtainedResources.get(boxType);
    }

    public void solveEffect(Game game) {
        // add effect
    }
}