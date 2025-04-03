package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.OpenSpaceState;

public class Slavers extends AdventureCard{
    @Expose
    private int firePower;
    @Expose
    private int membersLost;
    @Expose
    private int creditsEarned;

    public Slavers(){
        super();
    }

    public int getFirePower() {
        return firePower;
    }
    public void setFirePower(int firePower) {
        this.firePower = firePower;
    }

    public int getMembersLost() {
        return membersLost;
    }
    public void setMembersLost(int membersLost) {
        this.membersLost = membersLost;
    }

    public int getCreditsEarned() {
        return creditsEarned;
    }
    public void setCreditsEarned(int creditsEarned) {
        this.creditsEarned = creditsEarned;
    }

    public void solveEffect(Game game){
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new SlaversState(game.getSortedPlayers(), this);
    }
}
