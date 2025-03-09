package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.Game;

public class WarZone extends AdventureCard {

    private final int[][] critPenalty;

    public WarZone(int cardLevel, int daysLost, int[][] critPenalty) {
        super(cardLevel, daysLost);
        this.critPenalty = critPenalty;
    }

    public int[][] getCritPenalty(){
        return critPenalty;
    }

    public void solveEffect(Game game) {
        // add effect
    }
}
