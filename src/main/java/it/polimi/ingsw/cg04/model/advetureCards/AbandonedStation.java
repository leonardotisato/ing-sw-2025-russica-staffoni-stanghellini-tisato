package it.polimi.ingsw.cg04.model.advetureCards;

import java.util.Map;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

public class AbandonedStation extends AdventureCard {

    private int membersNeeded;
    private Map<BoxType,Integer> obtainedResources;

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
