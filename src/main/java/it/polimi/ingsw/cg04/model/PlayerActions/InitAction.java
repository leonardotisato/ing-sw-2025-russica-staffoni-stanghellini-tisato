package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;

public interface InitAction extends PlayerAction {

    void execute(GamesController controller);

    boolean checkAction(GamesController controller) throws InvalidActionException;
}
