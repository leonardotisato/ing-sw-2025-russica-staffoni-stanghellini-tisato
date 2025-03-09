package it.polimi.ingsw.cg04.model.advetureCards;

public class Slavers extends AdventureCard{

    private final int firePower;
    private final int membersLost;
    private final int creditsEarned;

    public Slavers(int cardLevel, int daysLost, int firePower, int membersLost, int creditsEarned){
        super(cardLevel, daysLost);
        this.firePower = firePower;
        this.membersLost = membersLost;
        this.creditsEarned = creditsEarned;
    }

    public int getFirePower() {
        return firePower;
    }

    public int getMembersLost() {
        return membersLost;
    }

    public int getCreditsEarned() {
        return creditsEarned;
    }

    public void solveEffect(){
        // add effect
    }
}
