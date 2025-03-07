package it.polimi.ingsw.cg04.model.advetureCards;

abstract class AdventureCard {

    private int cardLevel;
    private int daysLost;

    public AdventureCard(int cardLevel, int daysLost){
        this.cardLevel = cardLevel;
        this.daysLost = daysLost;
    }

    public abstract void solveEffect();

    public int getCardLevel() {
        return cardLevel;
    }

    public int getDaysLost() {
        return daysLost;
    }
}
