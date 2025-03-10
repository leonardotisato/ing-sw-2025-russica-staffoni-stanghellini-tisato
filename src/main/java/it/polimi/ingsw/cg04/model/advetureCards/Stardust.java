package it.polimi.ingsw.cg04.model.advetureCards;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;

public class Stardust extends AdventureCard {

    public Stardust(int cardLevel, int daysLost) {
        super(cardLevel, daysLost);
    }

    // todo: testing
    public void solveEffect(Game game) {
        // partendo dall'ultimo in classifica lo sposto di tante posizioni quanti i connettori esposti
        for(int i = game.getPlayers().size() - 1; i>= 0; i--) {
            game.getPlayer(i).move(-(game.getPlayer(i).getShip().getNumExposedConnectors()));
        }
    }
}