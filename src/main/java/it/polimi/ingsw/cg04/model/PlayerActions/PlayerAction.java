package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;

import java.io.Serializable;

public interface PlayerAction extends Serializable {
    void execute(Player player);
    boolean checkAction(Player player) throws InvalidActionException;
    String getPlayerNickname();
}
