package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Planets extends AdventureCard{
    private List<Map<BoxType, Integer>> planetReward;

    public Planets(int cardLevel, int daysLost) {
        super(cardLevel, daysLost);
        planetReward = new ArrayList<>();
    }
    public void solveEffect(){
        // add effect
    }

    public List<Map<BoxType, Integer>> getPlanetReward(){
        return planetReward;
    }
}
