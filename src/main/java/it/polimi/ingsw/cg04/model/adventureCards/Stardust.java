package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.OpenSpaceState;
import it.polimi.ingsw.cg04.model.GameStates.StardustState;

public class Stardust extends AdventureCard {

    public Stardust() {
        super();
    }

    // todo: testing
    public void solveEffect(Game game) {
        // partendo dall'ultimo in classifica lo sposto di tante posizioni quanti i connettori esposti
        for(int i = game.getPlayers().size() - 1; i>= 0; i--) {
            game.getPlayer(i).move(-(game.getPlayer(i).getShip().getNumExposedConnectors()));
        }
    }

    @Override
    public AdventureCardState createState(Game game) {
        return new StardustState(game);
    }
}