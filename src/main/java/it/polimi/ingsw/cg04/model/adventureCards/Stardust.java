package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.StardustState;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;

public class Stardust extends AdventureCard {

    public Stardust() {
        super();
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new StardustState(game);
    }

    /**
     * Draws the content of the Stardust adventure card into the provided StringBuilder.
     *
     * @param sb the StringBuilder instance where the formatted card content will be appended
     */
    @Override
    void drawContent(StringBuilder sb) {
        sb.append("│ ").append(centerText("There is a stardust!", WIDTH - 4)).append(" │").append("\n");
    }
}