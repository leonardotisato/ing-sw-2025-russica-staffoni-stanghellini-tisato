package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import java.io.Serializable;

public interface Action extends Serializable {
    void execute(Player player) throws InvalidStateException;
    boolean checkAction(Player player) throws InvalidActionException;
    String getPlayerNickname();
    void dispatchTo(GamesController controller) throws InvalidActionException, InvalidStateException;
}
