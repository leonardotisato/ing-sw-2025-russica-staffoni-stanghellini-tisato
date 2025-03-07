package it.polimi.ingsw.cg04.model.advetureCards;

import java.util.HashMap;
import java.util.Map;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

public class AbandonedStation extends AdventureCard {

    private int membersNeeded;
    private Map<BoxType,Integer> obtainedResources;

    public AbandonedStation(int cardLevel, int daysLost, int membersNeeded){
        super(cardLevel, daysLost);
        obtainedResources = new HashMap<>();
        this.membersNeeded = membersNeeded;
    }

    public void solveEffect(){
        // add effect
    }

    public int getMembersNeeded(){
        return membersNeeded;
    }

    public Map<BoxType,Integer> getObtainedResources(){
        return obtainedResources;
    }
}
