package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.HandleBoxesAction;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

public class AbandonedStationState implements GameState {
    public void handleAction(Player player, PlayerAction action) {
        if(!(action instanceof HandleBoxesAction)){
            throw new IllegalArgumentException("Illegal action");
        }
        action.execute(player);
    }
}
