package it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.PlayerActions.Action;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;

public abstract class InitAction implements Action {

    public abstract void execute(GamesController controller);

    public abstract boolean checkAction(GamesController controller) throws InvalidActionException;

    public abstract String getPlayerNickname();

    public void dispatchTo(GamesController controller) throws InvalidActionException {
        controller.onInitActionReceived(this);
    }
}
