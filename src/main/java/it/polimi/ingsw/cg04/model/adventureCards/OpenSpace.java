package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.OpenSpaceState;

public class OpenSpace extends AdventureCard {

    public OpenSpace() {
        super();
    }


    @Override
    public AdventureCardState createState(Game game) {
        return new OpenSpaceState(game);
    }
}
