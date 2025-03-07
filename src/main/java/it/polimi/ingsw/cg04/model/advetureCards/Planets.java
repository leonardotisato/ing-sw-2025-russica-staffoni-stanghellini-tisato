package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.util.List;
import java.util.Map;

public class Planets extends AdventureCard{
    private List<Map<BoxType, Integer>> planetReward;

    public void solveEffect(){
        // add effect
    }

    public List<Map<BoxType, Integer>> getPlanetReward(){
        return planetReward;
    }
}
