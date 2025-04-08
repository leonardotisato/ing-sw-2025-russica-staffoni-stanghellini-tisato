package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;

public class BuildState extends GameState {
    public void handleAction(Player player, PlayerAction action) {
        action.execute(player);
    }

    public void placeTile(Player player, int x, int y) {
        player.placeTile(x, y);
    }
}
