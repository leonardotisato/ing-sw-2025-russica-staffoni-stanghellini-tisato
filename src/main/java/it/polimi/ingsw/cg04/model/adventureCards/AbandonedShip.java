package it.polimi.ingsw.cg04.model.adventureCards;

import com.google.gson.annotations.Expose;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AbandonedShipState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;

public class AbandonedShip extends AdventureCard {
    @Expose
    private int lostMembers;
    @Expose
    private int earnedCredits;

    public AbandonedShip() {
        super();
    }

    public Integer getLostMembers() { return lostMembers; }
    public void setLostMembers(int lostMembers) {
        this.lostMembers = lostMembers;
    }

    public Integer getEarnedCredits() { return earnedCredits; }
    public void setEarnedCredits(int earnedCredits) {
        this.earnedCredits = earnedCredits;
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new AbandonedShipState(game);
    }

    @Override
    void drawContent(StringBuilder sb) {
        sb.append("│ ").append(centerText("Lost crew: " + this.lostMembers, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Earned credits: " + this.earnedCredits, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("lost flight days: " + this.getDaysLost(), WIDTH - 4)).append(" │").append("\n");
    }
}
