package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.OpenSpaceState;
import it.polimi.ingsw.cg04.model.Player;

import java.util.List;

public class OpenSpace extends AdventureCard {

    public OpenSpace() {
        super();
    }


    @Override
    public AdventureCardState createState(Game game) {
        return new OpenSpaceState(game);
    }
}
