package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

public abstract class GameState {
    public void handleAction(Player player, PlayerAction action){
        return;
    }
}
