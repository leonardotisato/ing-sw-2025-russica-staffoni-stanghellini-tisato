package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;

import java.util.HashMap;
import java.util.Map;

public class Smugglers extends AdventureCard {
    private final int firePower;
    private final int lostGoods;
    private final Map<BoxType, Integer> rewards;

    public Smugglers(int cardLevel, int daysLost, int firePower, int lostGoods) {
        super(cardLevel, daysLost);
        this.firePower = firePower;
        this.lostGoods = lostGoods;
        this.rewards = new HashMap<BoxType, Integer>();
    }

    public int getFirePower() { return firePower; }

    public int getLostGoods() { return lostGoods; }

    public Map<BoxType, Integer> getReward() { return rewards; }

    public void addReward(BoxType boxType, int quantity) {
        rewards.put(boxType, quantity);
    }

    public int getRewardQuantity(BoxType boxType) {
        return rewards.get(boxType);
    }

    public void solveEffect(Game game) { }
}
