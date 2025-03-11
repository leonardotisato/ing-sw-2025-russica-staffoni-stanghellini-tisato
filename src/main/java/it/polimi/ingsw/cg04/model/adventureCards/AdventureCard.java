package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;

public abstract class AdventureCard {

    private final int cardLevel;
    private final int daysLost;

    public AdventureCard(int cardLevel, int daysLost){
        this.cardLevel = cardLevel;
        this.daysLost = daysLost;
    }

    public abstract void solveEffect(Game game);

    public int getCardLevel() {
        return cardLevel;
    }

    public int getDaysLost() {
        return daysLost;
    }
}
