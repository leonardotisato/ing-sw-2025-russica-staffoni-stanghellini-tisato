package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.ChoosePropuslsorAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

public class OpenSpaceState implements GameState {
    public void handleAction(Player player, PlayerAction action) {
        if(!(action instanceof ChoosePropuslsorAction)){
            throw new IllegalArgumentException("Illegal action");
        }
        action.execute(player);
    }
}
