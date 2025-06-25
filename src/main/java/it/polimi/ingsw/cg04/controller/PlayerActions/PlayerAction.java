package it.polimi.ingsw.cg04.controller.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerAction implements Action {
    protected List<String> logs;
    private final String nickname;

    protected PlayerAction(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public abstract void execute(Player player) throws InvalidStateException;

    @Override
    public abstract boolean checkAction(Player player) throws InvalidActionException;

    @Override
    public String getPlayerNickname() {
        return nickname;
    }

    @Override
    public void dispatchTo(GamesController controller) throws InvalidActionException, InvalidStateException {
        controller.onActionReceived(this);
    }

    /**
     * Updates the logs of the action with the provided list of new log entries.
     *
     * @param newLogs the list of new log entries to be added to the action's logs.
     */
    public void addLogs(List<String> newLogs) {
        this.logs = new ArrayList<>(newLogs);
    }

    /**
     * Retrieves the current list of logs associated with this action.
     *
     * @return a list of log messages representing events or actions
     *         related to the execution of this action.
     */
    public List<String> getLogs() {
        return logs;
    }
}
