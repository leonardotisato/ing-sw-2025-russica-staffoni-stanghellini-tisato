package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.FlightBoard;
import it.polimi.ingsw.cg04.model.Game;

public class AbandonedShip extends AdventureCard {
    private final int lostMembers;
    private final int earnedCredits;

    public AbandonedShip(int cardLevel, int daysLost, int lostMembers, int earnedCredits) {
        super(cardLevel, daysLost);
        this.lostMembers = lostMembers;
        this.earnedCredits = earnedCredits;
    }

    public int getLostMembers() { return lostMembers; }

    public int getEarnedCredits() { return earnedCredits; }

    public void solveEffect(Game game) {    }
}
