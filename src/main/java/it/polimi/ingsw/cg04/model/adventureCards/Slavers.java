package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.SlaversState;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;

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

    public Integer getFirePower() {
        return firePower;
    }
    public void setFirePower(int firePower) {
        this.firePower = firePower;
    }

    public Integer getLostMembers() {
        return membersLost;
    }
    public void setMembersLost(int membersLost) {
        this.membersLost = membersLost;
    }

    public Integer getEarnedCredits() {
        return creditsEarned;
    }
    public void setCreditsEarned(int creditsEarned) {
        this.creditsEarned = creditsEarned;
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new SlaversState(game);
    }

    @Override
    void drawContent(StringBuilder sb) {
        sb.append("│ ").append(centerText("Required fire power: " + this.firePower, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Lost crew: " + this.membersLost, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Earned credits: " + this.creditsEarned, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("lost flight days: " + this.getDaysLost(), WIDTH - 4)).append(" │").append("\n");
    }
}
