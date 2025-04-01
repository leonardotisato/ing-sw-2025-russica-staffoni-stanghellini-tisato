package it.polimi.ingsw.cg04.model.PlayerStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

public interface PlayerState {
    public void handleAction(Player player, PlayerAction action);
}
