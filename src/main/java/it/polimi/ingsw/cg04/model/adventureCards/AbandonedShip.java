package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;

public class AbandonedShip extends AdventureCard {
    @Expose
    private int lostMembers;
    @Expose
    private int earnedCredits;

    public AbandonedShip() {
        super();
    }

    public int getLostMembers() { return lostMembers; }
    public void setLostMembers(int lostMembers) {
        this.lostMembers = lostMembers;
    }

    public int getEarnedCredits() { return earnedCredits; }
    public void setEarnedCredits(int earnedCredits) {
        this.earnedCredits = earnedCredits;
    }

    public void solveEffect(Game game) {

        /*
        *
        * for each player
        *   if player.wants_to_solve == true
        *       player.add_credits
        *       player.lose_crew
        *       player.lose_days
        *       break
        *
        */

    }
}
