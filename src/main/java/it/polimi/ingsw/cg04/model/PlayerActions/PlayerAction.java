package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;

public interface PlayerAction{
    void execute(Player player);
    boolean checkAction(Player player) throws InvalidActionException;
    String getPlayerNickname();
}
