package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.PlayerActions.HandleCrewAction;

public class AbandonedShipState implements GameState {
    public void handleAction(Player player, PlayerAction action) {
        if(!(action instanceof HandleCrewAction)){
            throw new IllegalArgumentException("Illegal action");
        }
        action.execute(player);
    }
}
