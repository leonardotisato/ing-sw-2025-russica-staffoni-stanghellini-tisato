package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.util.HashMap;
import java.util.Map;

public class Smugglers extends AdventureCard {
    private int firePower;
    private int lostGoods;
    private Map<BoxType, Integer> reward;

    public Smugglers(int firePower, int lostGoods) {
        this.firePower = firePower;
        this.lostGoods = lostGoods;
        this.reward = new HashMap<BoxType, Integer>();
    }

    public void solveEffect() { }
}
