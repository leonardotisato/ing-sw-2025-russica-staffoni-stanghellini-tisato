package it.polimi.ingsw.cg04.model.advetureCards;

public class AbandonedShip extends AdventureCard {
    private int lostMembers;
    private int earnedCredits;

    public AbandonedShip(int lostMembers, int earnedCredits) {
        this.lostMembers = lostMembers;
        this.earnedCredits = earnedCredits;
    }

    public void solveEffect() {

    }
}
