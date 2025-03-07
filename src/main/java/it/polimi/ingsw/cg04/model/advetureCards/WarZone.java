package it.polimi.ingsw.cg04.model.advetureCards;

public class WarZone extends AdventureCard {

    private int[][] critPenalty;

    public WarZone(int cardLevel, int daysLost, int[][] critPenalty) {
        super(cardLevel, daysLost);
        this.critPenalty = critPenalty;
    }

    public void solveEffect(){
        // add effect
    }

    public int[][] getCritPenalty(){
        return critPenalty;
    }
}
