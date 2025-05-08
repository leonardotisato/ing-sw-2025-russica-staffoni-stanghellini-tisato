package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.OpenSpaceState;

import static it.polimi.ingsw.cg04.model.utils.TuiDrawer.centerText;

public class OpenSpace extends AdventureCard {

    public OpenSpace() {
        super();
    }


    @Override
    public AdventureCardState createState(Game game) {
        return new OpenSpaceState(game);
    }

    @Override
    void drawContent(StringBuilder sb){
        sb.append("│ ").append(centerText("Time to fly!", WIDTH - 4)).append(" │").append("\n");
    }
}
