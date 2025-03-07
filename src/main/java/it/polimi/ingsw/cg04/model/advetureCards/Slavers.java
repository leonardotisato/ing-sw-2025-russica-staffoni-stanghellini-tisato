package it.polimi.ingsw.cg04.model.advetureCards;

public class Slavers extends AdventureCard{

    private int firePower;
    private int membersLost;
    private int creditsEarned;

    public Slavers(int cardLevel, int daysLost, int firePower, int membersLost, int creditsEarned){
        super(cardLevel, daysLost);
        this.firePower = firePower;
        this.membersLost = membersLost;
        this.creditsEarned = creditsEarned;
    }

    public void solveEffect(){
        // add effect
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
}
