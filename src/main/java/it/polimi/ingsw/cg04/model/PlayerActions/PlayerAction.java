package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

public interface PlayerAction {
    void execute(Player player);
    boolean checkAction(Player player);
    String getPlayerNickname();
    String getType();
}
