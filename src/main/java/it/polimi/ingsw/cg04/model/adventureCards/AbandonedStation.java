package it.polimi.ingsw.cg04.model.adventureCards;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

public class AbandonedStation extends AdventureCard {
    @Expose private int membersNeeded;
    @Expose private Map<BoxType, Integer> obtainedResources;


    public AbandonedStation() {
        super();
        this.obtainedResources = new HashMap<>();
    }

    public int getMembersNeeded() {
        return membersNeeded;
    }
    public void setMembersNeeded(int membersNeeded) {
        this.membersNeeded = membersNeeded;
    }
    public Map<BoxType, Integer> getObtainedResources() {
        return obtainedResources;
    }
    public void setObtainedResources(Map<BoxType, Integer> obtainedResources) {
        this.obtainedResources = obtainedResources;
    }

    public int getObtainedResourcesByType(BoxType boxType) {
        return obtainedResources.get(boxType);
    }

    public void solveEffect(Game game) {
        // add effect
    }
}