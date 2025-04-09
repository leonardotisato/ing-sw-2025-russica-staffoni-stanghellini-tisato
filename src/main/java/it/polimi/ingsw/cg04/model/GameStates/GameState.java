package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

public abstract class GameState {
    public void handleAction(Player player, PlayerAction action){
        return;
    }

    public void placeTile(Player player, int x, int y) {
        throw new IllegalArgumentException("cant place tile in this game state");
    }

    public void placeInBuffer(Player player) {
        throw new IllegalArgumentException("cant place tile in buffer in this game state");
    }
}
