package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

public class LobbyState implements GameState {
    public void handleAction(Player player, PlayerAction action) {
        action.execute(player);
    }
}
