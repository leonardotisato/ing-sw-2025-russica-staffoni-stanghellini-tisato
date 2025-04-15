package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.EpidemicState;

public class Epidemic extends AdventureCard {

    public Epidemic() {
        super();
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new EpidemicState(game);
    }
}
