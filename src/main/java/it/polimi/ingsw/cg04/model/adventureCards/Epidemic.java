package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.EpidemicState;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;

public class Epidemic extends AdventureCard {

    public Epidemic() {
        super();
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new EpidemicState(game);
    }

    /**
     * Draws the content of the "Epidemic" adventure card
     * to the provided StringBuilder in a formatted textual representation.
     *
     * @param sb the StringBuilder to which the formatted content will be appended
     */
    @Override
    void drawContent(StringBuilder sb) {
        sb.append("│ ").append(centerText("There is an epidemic!", WIDTH - 4)).append(" │").append("\n");
    }
}
