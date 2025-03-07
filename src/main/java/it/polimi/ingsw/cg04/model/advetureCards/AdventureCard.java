package it.polimi.ingsw.cg04.model.advetureCards;

abstract class AdventureCard {

    private int cardLevel;
    private int daysLost;

    abstract void solveEffect();

    public int getCardLevel() {
        return cardLevel;
    }

    public int getDaysLost() {
        return daysLost;
    }
}
