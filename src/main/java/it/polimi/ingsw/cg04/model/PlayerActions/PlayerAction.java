package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

public interface PlayerAction {
    public void execute(Player player);
    boolean checkAction(Player player);
}
