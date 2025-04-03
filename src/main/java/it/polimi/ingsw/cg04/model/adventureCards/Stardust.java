package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.OpenSpaceState;
import it.polimi.ingsw.cg04.model.GameStates.StardustState;

public class Stardust extends AdventureCard {

    public Stardust() {
        super();
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new StardustState(game);
    }
}