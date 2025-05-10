package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

import java.io.Serializable;
import java.util.List;

public abstract class PlayerAction implements Action {
    protected List<String> logs;
    public abstract void execute(Player player) throws InvalidStateException;
    public abstract boolean checkAction(Player player) throws InvalidActionException;
    public abstract String getPlayerNickname();
    public void dispatchTo(GamesController controller) throws InvalidActionException, InvalidStateException {
        controller.onActionReceived(this);
    }
    public void addLogs(List<String> newLogs) {
        this.logs.addAll(newLogs);
    }
}
