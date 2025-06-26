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

    /**
     *
     * @return the number of lost crew members as an Integer
     */
    @Override
    public Integer getLostMembers() {
        return lostMembers;
    }

    public void setLostMembers(int lostMembers) {
        this.lostMembers = lostMembers;
    }

    /**
     *
     * @return the earned credits as an Integer
     */
    @Override
    public Integer getEarnedCredits() {
        return earnedCredits;
    }

    public void setEarnedCredits(int earnedCredits) {
        this.earnedCredits = earnedCredits;
    }

    /**
     * Creates the state associated with the adventure card.
     *
     * @param game the game instance that provides the context for the adventure card state
     * @return the newly created instance of {@code AbandonedShipState} which represents the state of the current adventure card
     */
    @Override
    public AdventureCardState createState(Game game) {
        return new AbandonedShipState(game);
    }

    /**
     * Draws the content of the {@code AbandonedShip} card
     *
     * @param sb the {@code StringBuilder} to which the content will be appended
     */
    @Override
    void drawContent(StringBuilder sb) {
        sb.append("│ ").append(centerText("Lost crew: " + this.lostMembers, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("Earned credits: " + this.earnedCredits, WIDTH - 4)).append(" │").append("\n");
        sb.append("│ ").append(centerText("lost flight days: " + this.getDaysLost(), WIDTH - 4)).append(" │").append("\n");
    }
}
