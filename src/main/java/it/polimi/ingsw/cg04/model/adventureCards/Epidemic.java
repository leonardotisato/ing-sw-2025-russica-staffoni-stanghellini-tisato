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

    @Override
    void drawContent(StringBuilder sb) {
        sb.append("│ ").append(centerText("There is an epidemic!", WIDTH - 4)).append(" │").append("\n");
    }
}
